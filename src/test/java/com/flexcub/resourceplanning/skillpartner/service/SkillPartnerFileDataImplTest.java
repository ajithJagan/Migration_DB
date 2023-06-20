package com.flexcub.resourceplanning.skillpartner.service;

import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillowner.repository.ExcelFileRepository;
import com.flexcub.resourceplanning.skillpartner.service.impl.SkillPartnerFileDataImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SkillPartnerFileDataImpl.class)
class SkillPartnerFileDataImplTest {

    @Autowired
    SkillPartnerFileDataImpl skillPartnerFileData;

    @MockBean
    ExcelFileRepository excelFileRepository;

    @MockBean
    RegistrationService registrationService;

    @MockBean
    SkillPartnerService skillPartnerService;

    FileDB fileDB = new FileDB();


    @BeforeEach
    void setup() throws IOException {
        fileDB.setSkillPartnerId("1");
        fileDB.setId(1);
        fileDB.setName("sample.xlsx");
        fileDB.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileDB.setData(new byte[1]);
        fileDB.setSynced(false);

        File myFile = new File("src/test/resources/Sample_Excel_Sheet.xlsx");
        fileDB.setData(Files.readAllBytes(myFile.toPath()));
    }

//    @Test
//    void readSkillPartnerExcelFileTest() {
//        Mockito.when(excelFileRepository.findById(1)).thenReturn(Optional.of(fileDB));
//        assertEquals("File synced successfully", skillPartnerFileData.readSkillPartnerExcelFile(1));
//    }
}
