package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.*;
import com.flexcub.resourceplanning.skillowner.repository.*;
import com.flexcub.resourceplanning.skillowner.service.impl.SkillOwnerFileDataServiceImpl;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Date;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillOwnerFileDataServiceImpl.class)
class SkillOwnerFileDataServiceImplTest {

    @Autowired
    SkillOwnerFileDataServiceImpl skillOwnerFileDataService;

    @MockBean
    OwnerSkillTechnologiesRepository ownerSkillTechnologiesRepository;

    @MockBean
    OwnerSkillRolesRepository ownerSkillRolesRepository;

    @MockBean
    ExcelFileRepository excelFileRepository;

    @MockBean
    VisaStatusRepository visaStatusRepository;

    @MockBean
    SkillOwnerService skillOwnerService;

    @MockBean
    OwnerSkillLevelRepository ownerSkillLevelRepository;

    @MockBean
    OwnerSkillSetRepository ownerSkillSetRepository;

    @MockBean
    OwnerSkillStatusRepository ownerSkillStatusRepository;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @MockBean
    RegistrationService registrationService;

    @MockBean
    ClientService clientService;
    @MockBean
    private SkillSeekerRepository skillSeekerRepository;
    @MockBean
    OwnerSkillDomainRepository ownerSkillDomainRepository;

    List<MultipartFile> file = new ArrayList<>();

    MultipartFile file1;


    InputStream excel;


    ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(HttpStatus.OK);


    HashMap<String, String> fileTypeList = new HashMap<>();


    FileDB fileDB = new FileDB();

    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();

    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    OwnerSkillTechnologiesEntity ownerSkillTechnology = new OwnerSkillTechnologiesEntity();

    OwnerSkillSetEntity ownerSkillSetEntity = new OwnerSkillSetEntity();
    OwnerSkillRolesEntity ownerSkillRole = new OwnerSkillRolesEntity();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();
    OwnerSkillLevelEntity ownerSkillLevel = new OwnerSkillLevelEntity();
    VisaEntity visaStatus = new VisaEntity();


    @BeforeEach
    public void setup() throws IOException {
        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        ownerSkillStatusEntity.setStatusDescription("Available");

        ownerSkillTechnology.setTechnologyId(1);
        ownerSkillRole.setRolesId(1);
        ownerSkillDomainEntity.setDomainId(1);
        visaStatus.setVisaId(1);
        skillPartnerEntity.setSkillPartnerId(1);
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("J");
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
        skillOwnerEntity.setDOB(new Date(1998 - 01 - 23).toLocalDate());
        skillOwnerEntity.setPrimaryEmail("ajith.j@qbrainx.com");
        skillOwnerEntity.setAlternateEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setOwnerSkillStatusEntity(ownerSkillStatusEntity);


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
//    void readExcelFileTest() throws IOException {
//        when(excelFileRepository.findById(1)).thenReturn(Optional.of(fileDB));
//        when(ownerSkillStatusRepository.findByStatusDescriptionIgnoreCase(anyString())).thenReturn(Optional.of(ownerSkillStatusEntity));
//        when(ownerSkillDomainRepository.findByDomainValuesIgnoreCase(anyString())).thenReturn(ownerSkillDomainEntity);
//        when(ownerSkillTechnologiesRepository.findByTechnologyValuesIgnoreCase(anyString())).thenReturn(ownerSkillTechnology);
//        when(ownerSkillRolesRepository.findByRolesDescriptionIgnoreCase(anyString())).thenReturn(ownerSkillRole);
//        when(visaStatusRepository.findByVisaStatusIgnoreCase(anyString())).thenReturn(visaStatus);
//        when(ownerSkillLevelRepository.findBySkillLevelDescriptionIgnoreCase(anyString())).thenReturn(ownerSkillLevel);
//        when(skillOwnerService.insertData(Mockito.anyList())).thenReturn(Collections.singletonList(skillOwnerEntity));
//        assertNotNull(skillOwnerFileDataService.readExcelFile(1,true));
//    }


}