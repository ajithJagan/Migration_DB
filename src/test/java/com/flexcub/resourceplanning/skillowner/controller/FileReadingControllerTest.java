package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.FileResp;
import com.flexcub.resourceplanning.skillowner.repository.ExcelFileRepository;
import com.flexcub.resourceplanning.skillowner.service.FileReadingService;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerFileDataService;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerFileDataService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerFileDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FileReadingController.class)
class FileReadingControllerTest {

    List<MultipartFile> file;
    ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    @MockBean
    private FileReadingService fileReadingService;
    @MockBean
    private SkillPartnerFileDataService skillPartnerFileDataService;
    @Autowired
    private FileReadingController fileReadingController;

    @MockBean
    private SkillSeekerFileDataService skillSeekerFileDataService;
    @MockBean
    private SkillOwnerFileDataService skillOwnerFileDataService;

    @MockBean
    ExcelFileRepository excelFileRepository;
    FileResp fileId= new FileResp();

    @Test
    void uploadExcelTest() {
        Mockito.when(skillOwnerFileDataService.checkFileTypeAndUpload(Mockito.any(), Mockito.anyString())).thenReturn(fileId);
        assertEquals(200, fileReadingController.uploadExcel(file, "1").getStatusCodeValue());
    }


    @Test
    void setSkillOwnerDataInDbTest(){
        Mockito.when(skillOwnerFileDataService.readExcelFile(1,true)).thenReturn("File synced successfully");
        assertEquals(200, fileReadingController.setSkillOwnerDataInDb(1,true).getStatusCodeValue());

    }

    @Test
    void setSkillPartnerDataInDbTest() {
        Mockito.when(skillPartnerFileDataService.readSkillPartnerExcelFile(1)).thenReturn("File synced successfully");
        assertThat(fileReadingController.setSkillPartnerDataInDb(1).getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    void setSkillSeekerDataInDbTest() throws IOException {
        Mockito.when(skillSeekerFileDataService.readSkillSeekerFile(1)).thenReturn("File synced successfully");
        assertThat(fileReadingController.setSkillSeekerDataInDb(1).getStatusCodeValue()).isEqualTo(200);
    }
//    @Test
//    void downloadTemplateTest() throws IOException {
//
//        assertThat(fileReadingController.downloadExcel(1).getStatusCodeValue()).isEqualTo(200);
//    }
}
