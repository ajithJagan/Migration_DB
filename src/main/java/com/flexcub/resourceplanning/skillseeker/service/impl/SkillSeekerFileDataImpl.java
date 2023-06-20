package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillowner.repository.ExcelFileRepository;
import com.flexcub.resourceplanning.skillpartner.service.impl.SkillPartnerFileDataImpl;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerFileDataService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerFileDataService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.XLSX;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Log4j2
public class SkillSeekerFileDataImpl implements SkillSeekerFileDataService {

    Logger logger = LoggerFactory.getLogger(SkillPartnerFileDataImpl.class);

    @Autowired
    ExcelFileRepository excelFileRepository;

    @Autowired
    RegistrationService registrationService;


    @Override
    @Transactional
    public String readSkillSeekerFile(int fileId) {
        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(CSV, TEXT_CSV);
        fileTypeList.put(XLS, APPLICATION_VND_MS_EXCEL);
        fileTypeList.put(XLSX, XLSX_SPREADSHEET);
        try {
            logger.info("SkillSeekerFileDataImpl || readExcelFile || Calling repo to check if file exists");
            Optional<FileDB> fileDb = excelFileRepository.findById(fileId);

            if (fileDb.isPresent() && !(fileDb.get().isSynced())) {
                InputStream excelInputStream = new ByteArrayInputStream(fileDb.get().getData());
                String fileName = fileDb.get().getName();
                if (fileDb.get().getType().equalsIgnoreCase(fileTypeList.get(CSV)) || fileDb.get().getType().equalsIgnoreCase(fileTypeList.get(TEXT_CSV))) {
                    importSeekerCsvFile(excelInputStream, fileName);
                    logger.info("File synced successfully ");
                    return "File synced successfully";
                } else if (fileDb.get().getType().equalsIgnoreCase(fileTypeList.get(XLS)) || fileDb.get().getType().equalsIgnoreCase(fileTypeList.get(XLSX))) {
                    importExcelFile(excelInputStream, fileName);
                    logger.info("FileReadingImpl || readExcelFile || File synced successfully ");
                    return "File synced successfully";
                } else {
                    logger.info("FileReadingImpl || readExcelFile || Wrong File Format ");
                    throw new ServiceException(WRONG_FILE_FORMAT.getErrorCode(), WRONG_FILE_FORMAT.getErrorDesc());
                }
            } else {
                logger.info("SkillSeekerFileDataImpl || readExcelFile || File not Found ");
                throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
            }
        } catch (Exception e) {
            logger.info("SkillSeekerFileDataImpl || readExcelFile || File synced failure");
            throw new ServiceException(FILE_NOT_SYNCED.getErrorCode(), FILE_NOT_SYNCED.getErrorDesc());
        }
    }


    private ResponseEntity<String> importSeekerCsvFile(InputStream excelInputStream, String fileName) throws IOException {
        Map<Integer, List<String>> failedMap = new HashMap<>();
        Map<Integer, List<String>> successMap = new HashMap<>();

        Map<String, Boolean> columnCheckMap = new HashMap<>();
        columnCheckMap.put("TaxID, Business License", true);
        columnCheckMap.put("Email", true);
        columnCheckMap.put("Phone", true);

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(excelInputStream, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        for (CSVRecord csvRecord : csvRecords) {

            boolean hasEmptyColumn = false;

            for (String columnName : columnCheckMap.keySet()) {
                String columnValue = csvRecord.get(columnName);

                if (columnCheckMap.get(columnName) && (columnValue == null || columnValue.isEmpty())) {
                    hasEmptyColumn = true;
                    break;
                }
            }


            if (null != csvRecord) {
                SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
                RegistrationEntity registration = new RegistrationEntity();
                Roles roles = new Roles();
                try {

                    try {
                        if (hasEmptyColumn) {
                            throw new ServiceException("null value found");
                        }
                    } catch (ServiceException e) {
                        csvStoreErrorFile(csvRecord);
                        logger.info(" null value found ");
                        continue;
                    }

                    registration.setExcelId(fileName + "_" + csvRecord.get(0));

                    if (csvRecord.get(1).isEmpty()) {
                        logger.info("BusinessName not found for: {}", skillSeekerEntity.getSkillSeekerName());
                        throw new IOException("No BusinessName");

                    }
                    registration.setBusinessName(csvRecord.get(1));

                    if (csvRecord.get(2).isEmpty()) {
                        logger.info("TaxIdBusinessLicense not found for: {}", skillSeekerEntity.getTaxIdBusinessLicense());
                        throw new IOException("No TaxIdBusinessLicense");
                    }
                    registration.setTaxIdBusinessLicense(csvRecord.get(2));

                    if (csvRecord.get(3).isEmpty()) {
                        logger.info("State not found for: {}", skillSeekerEntity.getState());
                        throw new IOException("No State");
                    }
                    registration.setState(csvRecord.get(3));

                    if (csvRecord.get(4).isEmpty()) {
                        logger.info("City not found for: {}", skillSeekerEntity.getCity());
                        throw new IOException("No City");
                    }
                    registration.setCity(csvRecord.get(4));

                    if (csvRecord.get(5).isEmpty()) {
                        logger.info("PrimaryContactEmail not found for: {}", skillSeekerEntity.getPrimaryContactEmail());
                        throw new IOException("No PrimaryContactEmail");
                    }
                    registration.setEmailId(csvRecord.get(5));

                    registration.setFirstName(csvRecord.get(6));

                    if (csvRecord.get(7).isEmpty()) {
                        logger.info("PrimaryContactPhone not found for: {}", skillSeekerEntity.getPrimaryContactPhone());
                        throw new IOException("No PrimaryContactPhone");
                    }
                    registration.setContactPhone(csvRecord.get(7));

                    if (null == csvRecord.get(8)) {
                        logger.info("Domain not found for: {}", skillSeekerEntity.getOwnerSkillDomainEntity());
                        throw new IOException("No domain found");
                    }
                    registration.setDomainId(Integer.parseInt(csvRecord.get(8)));
                    roles.setRolesId(1L);
                    registration.setRoles(roles);
                } catch (Exception e) {
                    e.printStackTrace();
                    csvStoreErrorFile(csvRecord);
                    logger.info("Error in setting object");
                    continue;
                }
                try {
                    logger.info("SkillSeekerFileDataImpl || importExcelFile || Saving data ");
                    try {
                        registrationService.insertDetails(registration);
                    } catch (ServiceException e) {
                        csvStoreErrorFile(csvRecord);
                        logger.info("Some thing went wrong while inserting seeker table");
                        continue;
                    }


                    List<String> successList = successMap.get(skillSeekerEntity.getId());

                    if (ObjectUtils.isEmpty(successList)) {
                        successList = new ArrayList<>();
                    }
                    successList.add(skillSeekerEntity.getPrimaryContactEmail());
                    successMap.put(skillSeekerEntity.getId(), successList);
                } catch (ServiceException serviceException) {
                    List<String> failedEntries = failedMap.get(skillSeekerEntity.getId());
                    if (failedEntries != null) {
                        failedEntries.add(skillSeekerEntity.getPrimaryContactEmail());
                    } else {
                        failedEntries = new ArrayList<>();
                        failedEntries.add(skillSeekerEntity.getPrimaryContactEmail());
                    }
                    failedMap.put(skillSeekerEntity.getId(), failedEntries);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("FileReadingImpl || importSeekerCsvFile || Error in saving data of name: {}", csvRecord.get(6));
                }
            }
        }
        registrationService.sendMailForFailedOwnerRegistrations(failedMap);
        return ResponseEntity.status(HttpStatus.OK).body("File Parsing Completed");
    }


    private static void csvStoreErrorFile(CSVRecord csvRecord) throws IOException {
        String failedData = "";
        for (int columnIndex = 0; columnIndex < csvRecord.size(); columnIndex++) {
            String cellValue = csvRecord.get(columnIndex);
            if (cellValue != null) {
                failedData += cellValue + "\t";
            } else {
                failedData += "\t";
            }
        }
        failedData += System.lineSeparator();

        failedData += System.lineSeparator();

        String fileNameData = "failed_seeker_csv_data.txt";
        FileWriter fileWriter = new FileWriter(fileNameData, true);
        fileWriter.write(failedData);
        fileWriter.close();
        System.out.println("Data stored in text file successfully: " + fileNameData);
    }


    public String importExcelFile(InputStream excel, String fileName) throws IOException {
        logger.info("SkillSeekerFileDataImpl || importExcelFile || Method start");
        XSSFWorkbook workbook = new XSSFWorkbook(excel);
        XSSFSheet worksheet = workbook.getSheetAt(2);
        Row row = worksheet.getRow(0);
        ArrayList<String> headerData = new ArrayList<>();

        //Storing header data to verify template
        for (Cell cell : row) {
            headerData.add(String.valueOf(cell));
        }
        String[] stringArray = headerData.toArray(new String[0]);
        String[] expectedHeaders = {"Skill Seeker Id", "Business Name", "TaxID, Business License", "State", "City",
                "Email", "Primary Contact - Full Name", "Phone", "Domain"};

        String[] newStringArray = Arrays.stream(stringArray).limit(expectedHeaders.length).toArray(String[]::new);

        if (Arrays.equals(newStringArray, expectedHeaders)) {
            for (int index = 1; index < worksheet.getPhysicalNumberOfRows(); index++) {
                XSSFRow rows = worksheet.getRow(index);

                Map<Integer, String> cellConditions = new HashMap<>();
                cellConditions.put(5, "Condition for Cell 5");
                cellConditions.put(7, "Condition for Cell 7");
                cellConditions.put(1, "Condition for Cell 1");
                cellConditions.put(2, "Condition for Cell 2");

                if (null != rows.getCell(0)) {
                    RegistrationEntity registration = new RegistrationEntity();
                    Roles roles = new Roles();
                    try {

                        for (Map.Entry<Integer, String> entry : cellConditions.entrySet()) {
                            int cellIndex = entry.getKey();
                            if (rows.getCell(cellIndex).getCellType() == CellType.BLANK) {
                                throw new ServerException("null found");
                            }
                        }
                        registration.setBusinessName(rows.getCell(1).getStringCellValue());
                        registration.setTaxIdBusinessLicense(rows.getCell(2).getStringCellValue());
                        registration.setState(rows.getCell(3).getStringCellValue());
                        registration.setCity(rows.getCell(4).getStringCellValue());
                        registration.setEmailId(rows.getCell(5).getStringCellValue());
                        registration.setFirstName(rows.getCell(6).getStringCellValue());
                        rows.getCell(7).setCellType(CellType.STRING);
                        registration.setContactPhone(rows.getCell(7).getStringCellValue());
                        registration.setDomainId((int) rows.getCell(8).getNumericCellValue());

                        roles.setRolesId(1L);
                        registration.setRoles(roles);

                    } catch (Exception e) {
                        e.printStackTrace();
                        //TODO : Add error to log file
                        errorFileStored(rows);
                        logger.info("row is skipped " + index);
                        logger.info("Error in setting object");
                        continue;
                    }

                    try {
                        logger.info("SkillSeekerFileDataImpl || importExcelFile || Saving data of row: {} of name: {}", index, rows.getCell(1).getStringCellValue());
                        try {
                            registrationService.insertDetails(registration);
                        } catch (ServiceException e) {
                            errorFileStored(rows);
                           logger.info("Some thing went wrong inserting in table");
                            continue;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        logger.error("SkillSeekerFileDataImpl || importExcelFile || Error in saving data of row: {} of name: {}", index, rows.getCell(1).getStringCellValue());
                        continue;
                    }
                }
            }
        } else {
            throw new ServiceException(WRONG_TEMPLATE.getErrorCode(), WRONG_TEMPLATE.getErrorDesc());
        }
        return "Sync Success";
    }

    private static void errorFileStored(XSSFRow rows) {
        String failedData = "";
        for (int columnIndex = 0; columnIndex < rows.getLastCellNum(); columnIndex++) {
            Cell cell = rows.getCell(columnIndex);
            if (cell != null) {
                if (cell.getCellType() == CellType.STRING) {
                    failedData += cell.getStringCellValue() + "\t";
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    failedData += cell.getNumericCellValue() + "\t";
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    failedData += cell.getBooleanCellValue() + "\t";
                }
            } else {
                failedData += "\t";
            }
        }
        failedData += System.lineSeparator();

        try {
            String fileNameData = "failed_seeker_excel_data.txt";
            FileWriter fileWriter = new FileWriter(fileNameData, true);
            fileWriter.write(failedData);
            fileWriter.close();
            System.out.println("Data stored in text file successfully: " + fileNameData);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
