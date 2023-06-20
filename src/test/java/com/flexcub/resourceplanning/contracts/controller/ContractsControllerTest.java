package com.flexcub.resourceplanning.contracts.controller;

import com.flexcub.resourceplanning.contracts.dto.MsaFilesResponse;
import com.flexcub.resourceplanning.contracts.dto.PartnerContractDetails;
import com.flexcub.resourceplanning.contracts.dto.PoResponse;
import com.flexcub.resourceplanning.contracts.service.ContractService;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import com.flexcub.resourceplanning.contracts.dto.SeekerMSADetails;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ContractController.class)
class ContractsControllerTest {

    @MockBean
    ContractService contractService;
    @Autowired
    ContractController contractController;
    PurchaseOrder purchaseOrder = new PurchaseOrder();

    PoResponse poResponse = new PoResponse();

    @MockBean
    SkillSeekerService skillSeekerService;

    MsaFilesResponse response = new MsaFilesResponse();
    PartnerContractDetails partnerMsaDetails = new PartnerContractDetails();
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();

    SeekerMSADetails seekerMSADetails = new SeekerMSADetails();
    List<SeekerMSADetails> seekerMSADetailsList = new ArrayList<>();
    List<PartnerContractDetails> partnerMsaDetailsList = new ArrayList<>();
    ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @BeforeEach
    void setDetails() {
        response.setFileId(1);
        response.setMsaStatus("Created");


        partnerMsaDetails.setSkillPartnerId(1);
        partnerMsaDetails.setBusinessName("Qbrainx");
        partnerMsaDetails.setContractStartDate(LocalDate.of(2023, 04, 22));
        partnerMsaDetails.setContractExpiryDate(LocalDate.of(2024, 01, 01));
        partnerMsaDetails.setStatus("Initiated");
        partnerMsaDetailsList.add(partnerMsaDetails);

    }

    @Test
    void createSeekerMsaTest() throws IOException {
        when(contractService.createSeekerMsa(Mockito.any(), Mockito.any())).thenReturn(response);
        assertEquals(200, contractController.createSeekerMSA(Mockito.any(), Mockito.any()).getStatusCodeValue());

    }

    @Test
    void createPartnerMsaTest() throws IOException {
        when(contractService.createPartnerMsa(Mockito.any(), Mockito.any())).thenReturn(response);
        assertEquals(200, contractController.createPartnerMSA(Mockito.any(), Mockito.any()).getStatusCodeValue());

    }

    @Test
    void getPartnerMsaDetailsInSuperAdminTest() {
        when(contractService.getPartnerMsaDetailsInSuperAdmin()).thenReturn(partnerMsaDetailsList);
        assertEquals(200, contractController.getPartnerContractDetailsInSuperAdmin(1,1).getStatusCodeValue());
    }

    @Test
    void downloadSkillPartnerMsaAgreementTest() throws Exception {
        Mockito.when(contractService.downloadSkillPartnerMsaAgreement(Mockito.anyInt())).thenReturn(resourceResponseEntity);
        assertEquals(200, contractController.downloadSkillPartnerMsaAgreement(Mockito.anyInt()).getStatusCodeValue());

    }

    @Test
    void updatePartnerMsaStatus() throws IOException {
        when(contractService.updatePartnerMsaStatus(1, 8)).thenReturn(response);
        assertEquals(200, contractController.updatePartner(1, 8).getStatusCodeValue());

    }

//    @Test
//    void createPOTest() throws IOException {
//        when(contractService.createPO(Mockito.any(), Mockito.any())).thenReturn(poResponse);
//        assertEquals(200, contractController.createPO(Mockito.any(), Mockito.any()).getStatusCodeValue());
//    }

//    @Test
//    void updatePOByAdminTest() throws IOException {
//        when(contractService.createPO(Mockito.any(), Mockito.any())).thenReturn(poResponse);
//        assertEquals(200, contractController.updatePOByAdmin(Mockito.any(), Mockito.any()).getStatusCodeValue());
//    }

//    @Test
//    void updatePoStatusTest() {
//        when(contractService.updateStatus(Mockito.anyInt(), Mockito.anyInt())).thenReturn(purchaseOrder);
//        assertEquals(200, contractController.updatePoStatus(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
//    }

    @Test
    void updateSeekerMsaStatus() throws IOException {
        when(contractService.updateSeekerMsaStatus(1,8)).thenReturn(response);
        assertEquals(200, contractController.updateSeeker(1,8).getStatusCodeValue());

    }
    @Test
    void getSeekerMsaDetailsInSuperAdminTest() {
        when(contractService.getSeekerMsaDetailsInSuperAdmin()).thenReturn(seekerMSADetailsList);
        assertEquals(200, contractController.getPartnerContractDetailsInSuperAdmin(1,1).getStatusCodeValue());
    }
    @Test
    void downloadSkillSeekerMsaAgreementTest() throws Exception {
        Mockito.when(skillSeekerService.getSkillSeekerEntity(seekerMSADetails.getSkillSeekerId())).thenReturn(Optional.ofNullable(skillSeeker));
        Mockito.when(contractService.downloadSeekerMsa(Mockito.anyInt())).thenReturn(resourceResponseEntity);
        assertEquals(200, contractController.downloadSeekerMsa(Mockito.anyInt()).getStatusCodeValue());

    }
    @Test
    void getContractByPartnerIdTest() throws Exception {
        Mockito.when(contractService.getContractByPartnerId(Mockito.anyInt())).thenReturn(partnerMsaDetailsList);
        assertEquals(200, contractController.getContractByPartnerId(Mockito.anyInt()).getStatusCodeValue());
    }

}
