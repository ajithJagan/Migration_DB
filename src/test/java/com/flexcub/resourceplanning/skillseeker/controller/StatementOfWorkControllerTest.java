package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.SowStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWork;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWorkGetDetails;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;

import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StatementOfWorkController.class)
class StatementOfWorkControllerTest {

    @Autowired
    StatementOfWorkController statementOfWorkController;

    @MockBean
    StatementOfWorkService statementOfWorkService;

    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;

    @MockBean
    ContractFileRepository contractFileRepository;


    StatementOfWork statementOfWork = new StatementOfWork();

    StatementOfWorkEntity statementOfWorkEntity = new StatementOfWorkEntity();

    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();

    StatementOfWorkGetDetails statementOfWorkGetDetails = new StatementOfWorkGetDetails();

    ContractFiles contractFiles = new ContractFiles();
    SowStatusDto sowStatusDto = new SowStatusDto();
    List<StatementOfWorkGetDetails> statementOfWorkGetDetailsList = new ArrayList<>();

    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();


    @BeforeEach
    void setup() {
        skillSeekerEntity.setId(1);
        statementOfWorkGetDetails.setId(1);

        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProjectEntity);

        statementOfWorkEntity.setId(1);
        contractFiles.setMimeType("APPLICATION/PDF");
        contractFiles.setFileName("application.pdf");
        byte[] a = {1, 2, 3};
        contractFiles.setData(a);
        contractFiles.setId(1);

        sowStatusDto.setSowId(1);
        sowStatusDto.setSowStatusId(1);
    }

//    @Test
//    void uploadFileTest() throws Exception {
//
//        Mockito.when(statementOfWorkService.addDocument(Mockito.any(), Mockito.any())).thenReturn(statementOfWork);
//        assertEquals(200, statementOfWorkController.uploadFile(Mockito.any(), Mockito.anyString()).getStatusCodeValue());
//    }
//
//    @Test
//    void updateFile() throws Exception {
//        Mockito.when(statementOfWorkService.upDateSow(Mockito.any(), Mockito.any())).thenReturn(statementOfWork);
//        assertEquals(200, statementOfWorkController.updateFile(Mockito.any(), Mockito.anyString()).getStatusCodeValue());
//
//    }
//
//    @Test
//    void getSowDetailsTest() {
//        Mockito.when(statementOfWorkService.getSowDetails(skillSeekerEntity.getId())).thenReturn(statementOfWorkGetDetailsList);
//        assertEquals(200, statementOfWorkController.getSowDetails(skillSeekerEntity.getId()).getStatusCodeValue());
//    }
//
//    @Test
//    void getAllSowDetailsTest() {
//        Mockito.when(statementOfWorkService.getAllSowDetails()).thenReturn(statementOfWorkGetDetailsList);
//        assertEquals(200, statementOfWorkController.getAllSowDetails().getStatusCodeValue());
//    }
//
//    @Test
//    void updateSowStatusTest() {
//        Mockito.when(statementOfWorkService.updateSowStatus(Mockito.anyInt(), Mockito.anyInt())).thenReturn(sowStatusDto);
//        assertEquals(200, statementOfWorkController.updateSowStatus(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
//    }

    @Test
    void downloadAgreementSowTest() {
        Mockito.when(statementOfWorkService.downloadAgreementSOW(1)).thenReturn(statementOfWorkEntity);
        Mockito.when(statementOfWorkRepository.findById(1)).thenReturn(Optional.of(statementOfWorkEntity));
        Mockito.when(contractFileRepository.findById(1)).thenReturn(Optional.ofNullable(contractFiles));
        assertEquals(200, statementOfWorkController.downloadAgreementSow(1).getStatusCodeValue());
    }

    @Test
    void getSowTemplateTest() throws IOException {
        ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        Mockito.when(statementOfWorkService.templateDownload()).thenReturn(resourceResponseEntity);
        assertEquals(200, statementOfWorkController.getSowTemplate().getStatusCodeValue());
    }
}
