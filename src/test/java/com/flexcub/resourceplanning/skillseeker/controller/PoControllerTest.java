package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.dto.SeekerPurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PoController.class)
class PoControllerTest {

    @Autowired
    PoController poController;
    @MockBean
    PoService poService;
    @MockBean
    PoRepository poRepository;
    @MockBean
    ByteArrayResource resource;
    List<SeekerPurchaseOrder> seekerPurchaseOrders = new ArrayList<>();
    PoEntity poEntity = new PoEntity();
    SkillSeekerProjectEntity seekerProject = new SkillSeekerProjectEntity();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    Job job = new Job();
    ContractStatus poStatus = new ContractStatus();
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    @BeforeEach
    void setup() {
        purchaseOrder.setId(1);
        purchaseOrder.setStatus("Completed");

        skillOwnerEntity.setSkillOwnerEntityId(1);

        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setSkillSeekerProject(seekerProject);
//        poEntity.setMimeType("APPLICATION/PDF");
//        poEntity.setName("application.pdf");
        byte[] a = {1, 2, 3};
//        poEntity.setData(a);
    }

    @Test
    void getPoDetailsTest() {
        when(poService.getPurchaseOrderDetails(Mockito.anyInt())).thenReturn(seekerPurchaseOrders);
        assertEquals(200, poController.getPoDetails(Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void getAllPoDetailsTest() {
        when(poService.getAllPurchaseOrderDetails()).thenReturn(seekerPurchaseOrders);
        assertEquals(200, poController.getAllPoDetails().getStatusCodeValue());
    }

//    @Test
//    void uploadFileTest() {
//        when(poService.addData(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(purchaseOrder);
//        assertEquals(200, poController.uploadFile(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()).getStatusCodeValue());
//    }

//    @Test
//    void updateFileTest() {
//        when(poService.addData(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(purchaseOrder);
//        assertEquals(200, poController.updateFile(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()).getStatusCodeValue());
//    }

//    @Test
//    void updatePoStatusTest() {
//        when(poService.updateStatus(Mockito.anyInt(), Mockito.anyInt())).thenReturn(purchaseOrder);
//        assertEquals(200, poController.updatePoStatus(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
//    }

    //    @Test
//    void downloadAgreementTest() throws IOException {
//        Mockito.when(poService.downloadAgreement(1)).thenReturn(Optional.of(poEntity));
//        Mockito.when(poRepository.findById(1)).thenReturn(Optional.of(poEntity));
//        assertEquals(200, poController.downloadAgreement(1).getStatusCodeValue());
//    }
    @Test
    void getProductOwnerTemplateTest() throws IOException {
        ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(poService.getPurchaseOrderTemplateDownload()).thenReturn(resourceResponseEntity);
        assertEquals(200, poController.getProductOwnerTemplate().getStatusCodeValue());
    }
}