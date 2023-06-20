package com.flexcub.resourceplanning.skillowner.controller;


import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillowner.repository.ExcelFileRepository;
import com.flexcub.resourceplanning.skillowner.dto.FileResp;
import com.flexcub.resourceplanning.skillowner.service.FileReadingService;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerFileDataService;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerFileDataService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerFileDataService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/v1/file-reading")
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class FileReadingController {
    Logger logger = LoggerFactory.getLogger(FileReadingController.class);

    @Autowired
    private FileReadingService fileReadingService;
    @Autowired
    private SkillOwnerFileDataService skillOwnerFileDataService;
    @Autowired
    private SkillPartnerFileDataService skillPartnerFileDataService;

    @Autowired
    ExcelFileRepository excelFileRepository;
    @Autowired
    private SkillSeekerFileDataService skillSeekerFileDataService;

    /**
     * @param
     * @return Response Entity
     * @throws IOException
     */
    @PostMapping(value = "/uploadExcel", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileResp> uploadExcel(@RequestParam("file") List<MultipartFile> excelDataFileList, @RequestParam("id") String id) {
        logger.info("FileReadingController|| uploadExcel || /upload Excel called");
        return new ResponseEntity<>(skillOwnerFileDataService.checkFileTypeAndUpload(excelDataFileList, id), HttpStatus.OK);
    }

    @PostMapping(value = "/uploadRegistrationDataFile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileResp> uploadDataFile(@RequestParam("file") List<MultipartFile> excelDataFileList) {
        logger.info("FileReadingController|| uploadExcel || /upload Excel called");
        return new ResponseEntity<>(fileReadingService.checkFileTypeAndUpload(excelDataFileList), HttpStatus.OK);
    }

    /**
     * @param fileId
     * @return responseEntity
     * @throws IOException
     */
    //TODO : to be added in scheduler
    @GetMapping(path = "/syncSkillOwnerFile/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setSkillOwnerDataInDb(@PathVariable int fileId, boolean devInsert) {
        logger.info("FileReadingController|| setDataInDb || /syncExcel called");
        try {
            return new ResponseEntity<>(skillOwnerFileDataService.readExcelFile(fileId, devInsert), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sync Failure");
        }
    }

    @GetMapping(path = "/syncSkillPartnerFile/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setSkillPartnerDataInDb(@PathVariable int fileId) {
        logger.info("FileReadingController|| setDataInDb || /syncExcel called");
        try {
            return new ResponseEntity<>(skillPartnerFileDataService.readSkillPartnerExcelFile(fileId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sync Failure");
        }
    }

    @GetMapping(path = "/syncSkillSeekerFile/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setSkillSeekerDataInDb(@PathVariable int fileId) {
        logger.info("FileReadingController|| setDataInDb || /syncExcel called");
        try {
            return new ResponseEntity<>(skillSeekerFileDataService.readSkillSeekerFile(fileId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sync Failure");
        }
    }

    @GetMapping("/download")
    @Transactional
    public ResponseEntity<byte[]> downloadExcel(int id) {
        Optional<FileDB> fileDB = excelFileRepository.findById(id);
        byte[] excelData =fileDB.get().getData();
                HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "filename.xlsx");
        headers.setContentLength(excelData.length);

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }


    @GetMapping(value = "/downloadTemplate", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        logger.info("FileReadingController|| downloadTemplate || /downloadTemplate Excel called");
        return fileReadingService.downloadTemplate();
    }
}



