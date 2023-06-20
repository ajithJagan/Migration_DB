package com.flexcub.resourceplanning.invoice.controller;

import com.flexcub.resourceplanning.invoice.dto.*;
import com.flexcub.resourceplanning.invoice.entity.Invoice;
import com.flexcub.resourceplanning.invoice.entity.InvoiceData;
import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import com.flexcub.resourceplanning.invoice.service.InvoiceService;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = InvoiceController.class)
class InvoiceControllerTest {
    @MockBean
    InvoiceService invoiceService;
    @Autowired
    InvoiceController invoiceController;


    List<WorkEfforts> workEffortsList = new ArrayList<>();
    List<OwnerTimeSheetEntity> ownerTimeSheetEntities = new ArrayList<>();

    List<InvoiceData> invoiceDataList = new ArrayList<>();
    SelectionPhase selectionPhase = new SelectionPhase();
    InvoiceRequest invoiceRequest = new InvoiceRequest();
    InvoiceData invoiceData = new InvoiceData();

    InvoiceListingData invoiceListingData = new InvoiceListingData();
    List<InvoiceListingData> invoiceListingDataList = new ArrayList<>();
    PartnerInvoiceResponse partnerInvoiceResponse = new PartnerInvoiceResponse();
    List<PartnerInvoiceResponse> partnerInvoiceResponseList = new ArrayList<>();
    InvoiceStatus invoiceStatus = new InvoiceStatus();

    List<InvoiceStatus> statusList = new ArrayList<>();
    Invoice invoice = new Invoice();
    WorkEfforts efforts = new WorkEfforts();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    InvoiceDetailResponse invoiceDetailResponse = new InvoiceDetailResponse();
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    OwnerTimeSheetEntity ownerTimeSheetEntity = new OwnerTimeSheetEntity();

    InvoiceResponse invoiceResponse = new InvoiceResponse();
    Job job = new Job();
    PoEntity poEntity = new PoEntity();

    List<InvoiceDetails> invoiceDetails = new ArrayList<>();

    List<AdminInvoice> adminInvoices = new ArrayList<>();

    List<InvoiceResponse> invoiceResponses = new ArrayList<>();

    List<AdminInvoiceRequest> adminInvoiceRequests = new ArrayList<>();

    SeekerInvoiceStatus seekerInvoiceStatus = new SeekerInvoiceStatus();

    List<ClientInvoiceDetails> clientInvoiceDetails = new ArrayList<>();

    List<AdminInvoiceSeeker> adminInvoiceSeekers = new ArrayList<>();
    InvoiceUpdate invoiceUpdate = new InvoiceUpdate();
    InvoiceUpdateData invoiceUpdateData = new InvoiceUpdateData();
    List<InvoiceUpdateData> updateData = new ArrayList<>();


    @BeforeEach
    void setInvoiceDetails() {

        job.setJobId("FJB-00001");
        selectionPhase.setJob(job);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setJobId(job);

        skillPartnerEntity.setSkillPartnerId(1);
        skillPartnerEntity.setPhone("8825773502");
        skillPartnerEntity.setPrimaryContactFullName("kevin");
        skillPartnerEntity.setTaxIdBusinessLicense("Tam987");
        skillPartnerEntity.setBusinessEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setBusinessName("");
        skillPartnerEntity.setState("TamilNadu");
        skillPartnerEntity.setPrimaryContactEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setAddressLine1("123 - kev Street");
        skillPartnerEntity.setAddressLine2("134 - manu Street");
        skillPartnerEntity.setExcelId("1");
        skillPartnerEntity.setSecondaryContactEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setZipcode("638656");

        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setDOB(new Date(1998 - 01 - 23).toLocalDate());
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(45);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(true);
        skillSeeker.setId(1);
        skillSeeker.setSkillSeekerName("goodHeaven");
        skillSeekerProjectEntity.setId(1);
        skillSeekerProjectEntity.setSkillSeeker(skillSeeker);
        skillSeeker.setSkillSeekerName("Priya");

        selectionPhase.setRate(50);


        skillPartnerEntity.setSkillPartnerId(1);
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);

        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
        ownerTimeSheetEntity.setSkillOwnerEntity(skillOwnerEntity);
        ownerTimeSheetEntity.setEndDate(new Date(2022 - 12 - 01));
        ownerTimeSheetEntity.setStartDate(new Date(2022 - 12 - 05));
        ownerTimeSheetEntity.setSkillSeekerEntity(skillSeeker);
        ownerTimeSheetEntity.setSkillSeekerProjectEntity(skillSeekerProjectEntity);

        ownerTimeSheetEntity.setHours("4,4,4,4");
        efforts.setSkillOwnerEntityId(1);
        efforts.setOwnerName("Priya");
        efforts.setDesignation("Developer");
        efforts.setSkillSeekerEntityId(ownerTimeSheetEntity.getSkillSeekerEntity().getId());
        efforts.setClientName(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getSkillSeeker().getSkillSeekerName());
        efforts.setSkillSeekerProjectEntityId(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getId());
        efforts.setProjectName("Flexcub");
        efforts.setTotalHours(25);
        efforts.setAmount(selectionPhase.getRate());
        workEffortsList.add(efforts);
        ownerTimeSheetEntities.add(ownerTimeSheetEntity);

        invoiceDetailResponse.setInvoiceData(workEffortsList);
        invoiceDetailResponse.setStatus(invoiceStatus);
        invoiceDetailResponse.setSkillPartnerId(1);
        invoiceDetailResponse.setEndDate(new Date(2022 - 12 - 01));
        invoiceDetailResponse.setStartDate(new Date(2022 - 12 - 01));
        invoiceDetailResponse.setDueDate(new Date(2022 - 12 - 01).toLocalDate());


        invoice.setId("IN-02");
        invoice.setInvoiceData(invoice.getInvoiceData());
        invoice.setInvoiceStatus(invoiceStatus);
        invoice.setSkillPartner(skillPartnerEntity);
        invoice.setDueDate(LocalDate.of(2022, 12, 10));
        invoice.setInvoiceDate(LocalDate.of(2022, 12, 10));

        invoiceData.setInvoiceStatus(invoiceStatus);
        invoiceData.setId(1);
        invoiceData.setAmount(3000);
        invoiceData.setOwnerId(skillOwnerEntity);
        invoiceData.setSkillSeekerProjectId(skillSeekerProjectEntity);
        invoiceData.setSkillSeeker(skillSeeker);
        invoiceData.setTotalHours(100);

        partnerInvoiceResponse.setSkillSeekerId(invoiceData.getSkillSeeker().getId());
        partnerInvoiceResponse.setAmount(invoiceData.getAmount());
        partnerInvoiceResponse.setTotalHours(invoiceData.getTotalHours());
        partnerInvoiceResponse.setSkillOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId());
        partnerInvoiceResponse.setSkillSeekerProjectId(invoiceData.getSkillSeekerProjectId().getId());
        partnerInvoiceResponseList.add(partnerInvoiceResponse);


        invoiceRequest.setPartnerInvoiceResponseList(partnerInvoiceResponseList);
        invoiceRequest.setDueDate(new java.util.Date(2022 - 12 - 20));
        invoiceRequest.setStartDate(new java.util.Date(2022 - 12 - 25));
        invoiceRequest.setEndDate(new java.util.Date(2022 - 12 - 30));
        invoiceRequest.setSkillPartnerId(invoice.getSkillPartner().getSkillPartnerId());
        invoiceStatus.setId(1);
        invoiceStatus.setStatus("submitted");
        statusList.add(invoiceStatus);
        invoiceResponse.setInvoiceId(invoice.getId());
        invoiceResponse.setInvoiceStatus(invoiceStatus);

        invoiceUpdate.setInvoiceId("IN-001");
        invoiceUpdate.setInvoiceDate(new Date(2023, 01, 31).toLocalDate());
        invoiceUpdate.setInvoiceUpdateList(updateData);

        invoiceUpdateData.setSkillOwnerEntityId(5);
        invoiceUpdateData.setAmount(200);
        invoiceUpdateData.setTotalHours(8);
        invoiceUpdateData.setFirstName("Hemamalini");
        invoiceUpdateData.setTitle("CDS-90");
        invoiceUpdateData.setSkillSeekerEntityId(3);
        invoiceUpdateData.setSkillSeekerName("Anand");
        invoiceUpdateData.setOwnerDesignation("DEveloper");
        invoiceUpdateData.setSkillSeekerProjectEntityId(1);

        updateData.add(invoiceUpdateData);

    }

    @Test
    void saveInvoiceDetailsByPartner() {
        when(invoiceService.saveInvoiceByPartner(invoiceRequest)).thenReturn((invoiceResponse));
        assertEquals(200, invoiceController.saveInvoiceDetailsByPartner(invoiceRequest).getStatusCodeValue());
        assertEquals(invoiceController.saveInvoiceDetailsByPartner(invoiceRequest).getBody(), invoiceResponse);

    }

    @Test
    void getOwnersByPartnersTest() {
        when((invoiceService.getOwnerByPartner(Mockito.anyInt(), Mockito.any(), Mockito.any()))).thenReturn(workEffortsList);
        assertEquals(200, invoiceController.getOwnersByPartners(Mockito.anyInt(), Mockito.any(), Mockito.any()).getStatusCodeValue());
    }

    @Test
    void getInvoicesTest() {
        when(invoiceService.getInvoices(Mockito.anyInt())).thenReturn(invoiceListingDataList);
        assertEquals(200, invoiceController.getInvoices(1).getStatusCodeValue());
    }

    @Test
    void getInvoiceByInvoiceId() {

        when(invoiceService.getInvoice("IN-002", true)).thenReturn(invoiceDetailResponse);
        assertEquals(200, invoiceController.getInvoiceByInvoiceId("IN-002", true).getStatusCodeValue());

    }

    @Test
    void updateInvoiceStatus() {
        Mockito.when(invoiceService.updatePartnerInvoiceStatus("IN-002", 1, null)).thenReturn(invoiceResponse);
        assertEquals(200, invoiceController.updateInvoiceStatus("IN-002", 1, null).getStatusCodeValue());

    }

    @Test
    void getInvoiceStatusTest() {
        Mockito.when(invoiceService.getInvoiceStatus()).thenReturn(statusList);
        assertEquals(200, invoiceController.getInvoiceStatus().getStatusCodeValue());
    }

    @Test
    void updateAdminInvoiceStatusTest() {
        Mockito.when(invoiceService.updateAdminInvoiceStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(invoiceResponse);
        assertEquals(200, invoiceController.updateSeekerInvoiceStatus(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()).getStatusCodeValue());
    }

    @Test
    void getAllInvoiceDetailsTest() {
        Mockito.when(invoiceService.getAllInvoiceDetails()).thenReturn(invoiceDetails);
        assertEquals(200, invoiceController.getAllInvoiceDetails().getStatusCodeValue());
    }

    @Test
    void getAdminInvoiceDataTest() {
        Mockito.when(invoiceService.getAdminInvoiceData(Mockito.anyInt(), Mockito.anyInt())).thenReturn(adminInvoices);
        assertEquals(200, invoiceController.getAdminInvoiceData(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void getPartnerInvoiceBySeekerTest() {
        Mockito.when(invoiceService.getAdminInvoiceData(Mockito.anyInt(), Mockito.anyInt())).thenReturn(adminInvoices);
        assertEquals(200, invoiceController.getPartnerInvoiceBySeeker(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void saveInvoiceDetailsByAdminTest() {
        Mockito.when(invoiceService.saveInvoiceDetailsByAdmin(Mockito.any())).thenReturn(invoiceResponses);
        assertEquals(200, invoiceController.saveInvoiceDetailsByAdmin(Mockito.any()).getStatusCodeValue());
    }

    @Test
    void updateSeekerInvoiceStatusTest() {
        Mockito.when(invoiceService.updateSeekerInvoiceStatus(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(seekerInvoiceStatus);
        assertEquals(200, invoiceController.updateSeekerInvoiceStatus(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()).getStatusCodeValue());
    }

    @Test
    void invoiceClientDetailsTest() {
        Mockito.when(invoiceService.invoiceClientDetails()).thenReturn(clientInvoiceDetails);
        assertEquals(200, invoiceController.invoiceClientDetails().getStatusCodeValue());
    }

    @Test
    void getAllInvoiceDetailAdminTest() {
        Mockito.when(invoiceService.getAllInvoiceDetailAdmin()).thenReturn(invoiceDetails);
        assertEquals(200, invoiceController.getAllInvoiceDetailAdmin().getStatusCodeValue());
    }

    @Test
    void getAdminInvoiceBySeekerTest() {
        Mockito.when(invoiceService.getAdminInvoiceBySeeker(Mockito.anyInt())).thenReturn(invoiceDetails);
        assertEquals(200, invoiceController.getAdminInvoiceBySeeker(Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void updatePartnerInvoiceTest() {
        Mockito.when(invoiceService.updateInvoiceDetailsByPartner(invoiceUpdate)).thenReturn(invoiceUpdate);
        assertEquals(200, invoiceController.updateInvoiceDetailsByPartner(invoiceUpdate).getStatusCodeValue());

    }

    @Test
    void updateInvoiceDetailsByAdminTest() {
        Mockito.when(invoiceService.updateInvoiceDetailsByAdmin(invoiceUpdate)).thenReturn(invoiceUpdate);
        assertEquals(200, invoiceController.updateInvoiceDetailsByAdmin(invoiceUpdate).getStatusCodeValue());

    }


}