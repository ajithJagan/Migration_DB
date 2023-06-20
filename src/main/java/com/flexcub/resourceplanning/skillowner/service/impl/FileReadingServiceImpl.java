package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResp;
import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillowner.repository.ExcelFileRepository;
import com.flexcub.resourceplanning.skillowner.service.FileReadingService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.XLSX_SPREADSHEET;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.FILE_NOT_FOUND;

@Service
public class FileReadingServiceImpl implements FileReadingService {
    Logger logger = LoggerFactory.getLogger(FileReadingServiceImpl.class);
    FileResp fileID = new FileResp();
    @Autowired
    private ExcelFileRepository excelFileRepository;
    @Autowired
    private SkillOwnerFileDataServiceImpl skillOwnerFileDataService;

    @Value("${flexcub.defaultTemplateName}")
    private String defaultTemplateName;

    @Override
    @Transactional
    public FileResp checkFileTypeAndUpload(List<MultipartFile> excelDataFileList) {
        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(CSV, TEXT_CSV);
        fileTypeList.put(XLS, APPLICATION_VND_MS_EXCEL);
        fileTypeList.put(XLSX, XLSX_SPREADSHEET);
        if (!excelDataFileList.isEmpty()) {
            for (MultipartFile excelDataFile : excelDataFileList) {
                if (fileTypeList.containsValue(excelDataFile.getContentType())) {
                    logger.info("FileReadingServiceImpl || checkFileTypeAndUpload || File Type verified !!");
                    try {
                        String uploadResponse = uploadExcelFile(excelDataFile);
                        if (!"File uploaded".equalsIgnoreCase(uploadResponse)) {
                            throw new ServiceException();
                        }
                        logger.info("FileReadingServiceImpl || checkFileTypeAndUpload || Uploaded the file successfully: {} // ->", excelDataFile.getOriginalFilename());
                    } catch (ServiceException e) {
                        throw new ServiceException(WRONG_TEMPLATE.getErrorCode(), WRONG_TEMPLATE.getErrorDesc());
                    } catch (Exception e) {
                        logger.info("FileReadingServiceImpl || checkFileTypeAndUpload || Could not upload the file: {} // ->", excelDataFile.getOriginalFilename());
                        throw new ServiceException(EXPECTATION_FAILED.getErrorCode(), EXPECTATION_FAILED.getErrorDesc());
                    }
                } else {
                    logger.info("FileReadingServiceImpl || checkFileTypeAndUpload || Wrong File Format ");
                    throw new ServiceException(WRONG_FILE_FORMAT.getErrorCode(), WRONG_FILE_FORMAT.getErrorDesc());
                }
            }
            return fileID;
        } else {
            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        Optional<FileDB> fileDb = excelFileRepository.findByName(defaultTemplateName);
        ByteArrayResource resource = null;
        if (fileDb.isPresent()) {
            resource = new ByteArrayResource(fileDb.get().getData());
        } else {
            throw new IOException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = Sample_template.xlsx");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @Transactional
    private String uploadExcelFile(MultipartFile file) throws IOException {
        logger.info("SkillOwnerFileDataImpl || uploadExcelFile || Calling Repository to add file in DataBase");
        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(CSV, TEXT_CSV);
        fileTypeList.put(XLS, APPLICATION_VND_MS_EXCEL);
        fileTypeList.put(XLSX, XLSX_SPREADSHEET);
        boolean isTemplateCorrect = false;
        XSSFWorkbook workbook = null;
        if (file.getContentType().equalsIgnoreCase(fileTypeList.get(XLS)) || file.getContentType().equalsIgnoreCase(fileTypeList.get(XLSX))) {
            InputStream excelInputStream = new ByteArrayInputStream(file.getBytes());
            workbook = new XSSFWorkbook(excelInputStream);
            if (workbook.getNumberOfSheets() != 0) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    if (workbook.getSheetName(i).equals("Skill Owner(Candidate)")) {
                        XSSFSheet worksheet = workbook.getSheetAt(0);
                        Row row = worksheet.getRow(0);
                        isTemplateCorrect = skillOwnerFileDataService.checkExcelTemplate(row);
                    }
                    if (workbook.getSheetName(i).equals("Skill Partner(Employer)")) {
                        XSSFSheet worksheet = workbook.getSheetAt(1);
                        Row row = worksheet.getRow(0);
                        isTemplateCorrect = skillOwnerFileDataService.checkSkillPartnerExcelTemplate(row);
                    }
                    if (workbook.getSheetName(i).equals("Skill Seeker(Employer)")) {
                        XSSFSheet worksheet = workbook.getSheetAt(2);
                        Row row = worksheet.getRow(0);
                        isTemplateCorrect = skillOwnerFileDataService.checkSkillSeekerExcelTemplate(row);
                    }
                }
            }
            if (isTemplateCorrect) {
                XSSFSheet worksheet = workbook.getSheetAt(0);
                Row row = worksheet.getRow(0);
                isTemplateCorrect = skillOwnerFileDataService.checkExcelTemplate(row);
            } else {
                throw new ServiceException(WRONG_TEMPLATE.getErrorCode(), WRONG_TEMPLATE.getErrorDesc());
            }

        } else {
            isTemplateCorrect = skillOwnerFileDataService.checkCsvTemplate(file);

        }
        if (isTemplateCorrect) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())) + "_" + LocalDateTime.now();
            FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), false);
            FileDB fileDB1 = excelFileRepository.save(fileDB);
            fileID.setId(fileDB1.getId());
            return "File uploaded";
        }
        throw new ServiceException(WRONG_TEMPLATE.getErrorCode(), WRONG_TEMPLATE.getErrorDesc());
    }

}

