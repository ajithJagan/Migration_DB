//package com.flexcub.resourceplanning.skillseeker.controller;
//
//
//import com.flexcub.resourceplanning.job.entity.Job;
//import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
//import com.flexcub.resourceplanning.skillseeker.dto.MsaStatusDto;
//import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerMsaDto;
//import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
//import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerMSAService;
//import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdminMsa;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(classes = SeekerMSAController.class)
//class SeekerMSAControllerTest {
//
//    @MockBean
//    SkillSeekerMSAService skillSeekerMSAService;
//
//    @Autowired
//    SeekerMSAController seekerMSAController;
//
//    List<MultipartFile> multipartFile;
//
//    List<SeekerAdminMsa> seekerAdminMsa = new ArrayList<>();
//    SkillSeekerMSAEntity skillSeekerMSA = new SkillSeekerMSAEntity();
//
//    SkillSeekerMsaDto skillSeekerMsaDto = new SkillSeekerMsaDto();
//    MsaStatusDto msaStatusDto = new MsaStatusDto();
//    SkillSeekerProjectEntity skillSeekerProject = new SkillSeekerProjectEntity();
//    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
//
//    Job job = new Job();
//    ContractStatus msaStatus = new ContractStatus();
//
//    @BeforeEach
//    void beforeTest() {
//        skillSeekerProject.setId(1);
//        skillOwnerEntity.setSkillOwnerEntityId(1);
//        skillSeekerMSA.setSkillSeekerId(1);
//        skillSeekerMSA.setSkillSeekerProject(skillSeekerProject);
//        skillSeekerMSA.setData(new byte[35204]);
//        skillSeekerMSA.setId(1);
//        skillSeekerMSA.setName("new.doc");
//        skillSeekerMSA.setDateSigned(LocalDate.parse("2022 - 12 - 23"));
//        skillSeekerMSA.setExpiryDate(new Date(2022 - 12 - 30));
//        skillSeekerMSA.setSize(55L);
//        skillSeekerMSA.setMimeType("application/pdf");
//        skillSeekerMSA.setMsaCreated(true);
//        skillSeekerMSA.setMsaStatus(msaStatus);
//        skillSeekerMSA.setJobId(job);
//        skillSeekerMSA.setSkillSeekerProject(skillSeekerProject);
//        skillOwnerEntity.setSkillOwnerEntityId(1);
//        skillSeekerMSA.setSkillOwnerEntity(skillOwnerEntity);
//
//    }
//
//    @Test
//    void getMsaDetailsTest() {
//        Mockito.when((skillSeekerMSAService.getMsaDetails())).thenReturn(seekerAdminMsa);
//        assertEquals(200, seekerMSAController.getMsaDetails().getStatusCodeValue());
//    }
//
//    @Test
//    void uploadFileTest() throws Exception {
//        Mockito.when(skillSeekerMSAService.addDocuments(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(skillSeekerMsaDto);
//        assertEquals(200, seekerMSAController.uploadFile(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt()).getStatusCodeValue());
//    }
//
//    @Test
//    void downloadAgreementTest() throws IOException {
//        Mockito.when(skillSeekerMSAService.downloadAgreement(1)).thenReturn(Optional.of(skillSeekerMSA));
//        assertEquals(200, seekerMSAController.downloadAgreement(1).getStatusCodeValue());
//    }
//
//    @Test
//    void getSkillSeekerMsaTemplate() throws IOException {
//        ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);
//        Mockito.when(skillSeekerMSAService.getSkillSeekerMsaTemplateDownload()).thenReturn(resourceResponseEntity);
//        assertThat(seekerMSAController.getSkillSeekerMsaTemplate().getStatusCodeValue()).isEqualTo(200);
//    }
//
//    @Test
//    void getMsaDetailsBySeekerTest() {
//        Mockito.when(skillSeekerMSAService.getMsaDetailsBySeeker(Mockito.anyInt())).thenReturn(seekerAdminMsa);
//        assertEquals(200, seekerMSAController.getMsaDetailsBySeeker(Mockito.anyInt()).getStatusCodeValue());
//    }
//
//    @Test
//    void updateMsaStatus() {
//        Mockito.when(skillSeekerMSAService.updateMsaStatus(Mockito.anyInt(), Mockito.anyInt())).thenReturn(msaStatusDto);
//        assertEquals(200, seekerMSAController.updateMsaStatus(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
//    }
//
//    @Test
//    void getMsaStatus() {
//        List<ContractStatus> msaStatus1 = new ArrayList<>();
//
//        Mockito.when(skillSeekerMSAService.getMsaStatus()).thenReturn(msaStatus1);
//        assertEquals(200, seekerMSAController.getContractStatus().getStatusCodeValue());
//    }
//
//}
//
