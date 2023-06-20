package com.flexcub.resourceplanning.invoice.service;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.invoice.dto.*;
import com.flexcub.resourceplanning.invoice.entity.*;
import com.flexcub.resourceplanning.invoice.repository.*;
import com.flexcub.resourceplanning.invoice.service.impl.InvoiceServiceImpl;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = InvoiceServiceImpl.class)
class InvoiceServiceTest {
    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;
    @MockBean
    PoRepository poRepository;
    @MockBean
    OwnerTimeSheetRepository timeSheetRepository;

    @MockBean
    ModelMapper modelMapper;
    @MockBean
    InvoiceRepository invoiceRepository;

    @MockBean
    InvoiceStatusRepository invoiceStatusRepository;

    @MockBean
    InvoiceDataRepository invoiceDataRepository;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @MockBean
    SkillOwnerRepository skillOwnerRepository;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;

    @MockBean
    SkillSeekerProjectRepository skillSeekerProjectRepository;

    @MockBean
    AdminInvoiceRepository adminInvoiceRepository;

    @Autowired
    InvoiceService invoiceService;

    @MockBean
    SeekerInvoiceRepository seekerInvoiceRepository;

    @MockBean
    AdminInvoiceDataRepository adminInvoiceDataRepository;

    @MockBean
    NotificationService notificationService;

    List<WorkEfforts> workEffortsList = new ArrayList<>();
    List<OwnerTimeSheetEntity> ownerTimeSheetEntities = new ArrayList<>();
    SelectionPhase selectionPhase = new SelectionPhase();
    List<InvoiceData> invoiceDataList = new ArrayList<>();
    Invoice invoice = new Invoice();

    List<Invoice> invoiceList = new ArrayList<>();
    InvoiceRequest invoiceRequest = new InvoiceRequest();
    InvoiceData invoiceData = new InvoiceData();
    PartnerInvoiceResponse partnerInvoiceResponse = new PartnerInvoiceResponse();
    List<PartnerInvoiceResponse> partnerInvoiceResponseList = new ArrayList<>();
    InvoiceStatus invoiceStatus = new InvoiceStatus();
    List<InvoiceStatus> statusList = new ArrayList<>();
    WorkEfforts efforts = new WorkEfforts();

    InvoiceResponse invoiceResponse = new InvoiceResponse();
    InvoiceListingData invoiceListingData = new InvoiceListingData();

    List<InvoiceListingData> invoiceListingDataList = new ArrayList<>();

    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    PoEntity poEntity=new PoEntity();
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    OwnerTimeSheetEntity ownerTimeSheetEntity = new OwnerTimeSheetEntity();
    Job job = new Job();
    AdminInvoiceData adminInvoiceData = new AdminInvoiceData();
    List<AdminInvoiceData> adminInvoiceDataList = new ArrayList<>();
    InvoiceAdmin invoiceAdmin = new InvoiceAdmin();
    SeekerInvoiceStatus seekerInvoiceStatus = new SeekerInvoiceStatus();
    List<AdminInvoiceRequest> adminInvoiceRequests = new ArrayList<>();

    AdminInvoiceRequest adminInvoiceRequest = new AdminInvoiceRequest();
    InvoiceResponse response = new InvoiceResponse();
    List<InvoiceResponse> invoiceResponses = new ArrayList<>();
    ClientInvoiceDetails clientInvoiceDetails = new ClientInvoiceDetails();
    List<ClientInvoiceDetails> clientInvoiceDetailsList = new ArrayList<>();
    InvoiceDetails invoiceDetails = new InvoiceDetails();
    List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
    List<InvoiceAdmin> invoiceAdmins = new ArrayList<>();
    AdminInvoiceSeeker adminInvoiceSeeker = new AdminInvoiceSeeker();
    List<AdminInvoiceSeeker> adminInvoiceSeekers = new ArrayList<>();
    AdminInvoiceResponseData invoiceResponseData = new AdminInvoiceResponseData();

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    InvoiceUpdate invoiceUpdate = new InvoiceUpdate();
    InvoiceUpdateData invoiceUpdateData = new InvoiceUpdateData();
    List<InvoiceUpdateData> updateData = new ArrayList<>();
    InvoiceDetailResponse detailResponse = new InvoiceDetailResponse();

    List<SelectionPhase> selectionPhases = new ArrayList<>();
    List<AdminInvoice> adminInvoices=new ArrayList<>();
    AdminInvoice adminInvoice=new AdminInvoice();


    @BeforeEach
    void setInvoiceDetails() {

        job.setJobId("FJB-00001");
        job.setSkillSeeker(skillSeeker);
        job.setJobTitle("aaaa");
        selectionPhase.setJob(job);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setJobId(job);
        poEntity.setRateCard(150);
        poEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        poEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        poEntity.setContractEndDate(LocalDate.now());
        poEntity.setContractStartDate(LocalDate.now());

        poEntity.setPoId(new ContractFiles());
        poEntity.setRole("Java");
//        poEntity.setStartDate(new java.util.Date());
//        poEntity.setEndDate(new java.util.Date());

        poEntity.setSkillOwnerEntity(skillOwnerEntity);


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
        skillPartnerEntity.setServiceFeePercentage(20.0);


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
        skillSeeker.setOwnerSkillDomainEntity(ownerSkillDomainEntity);

        selectionPhase.setMailSent(true);

        selectionPhase.setSelectionId(1);
        selectionPhase.setAccepted(true);
        selectionPhase.setRate(50);
        selectionPhase.setJob(job);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
//        selectionPhase.setSkillOwnerEntity(invoiceData.getOwnerId());
        selectionPhases.add(selectionPhase);

        skillPartnerEntity.setSkillPartnerId(1);
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);

        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
        ownerTimeSheetEntity.setSkillOwnerEntity(skillOwnerEntity);
        ownerTimeSheetEntity.setEndDate(new Date(2022 - 12 - 01));
        ownerTimeSheetEntity.setStartDate(new Date(2022 - 12 - 05));
        ownerTimeSheetEntity.setSkillSeekerEntity(skillSeeker);
        ownerTimeSheetEntity.setSkillSeekerProjectEntity(skillSeekerProjectEntity);

        ownerTimeSheetEntity.setHours("4,4,4,4");
        ownerTimeSheetEntity.setTimeSheetId(1);
        efforts.setSkillOwnerEntityId(1);
        efforts.setOwnerName("Priya");
        efforts.setDesignation("Developer");
        efforts.setSkillSeekerEntityId(ownerTimeSheetEntity.getSkillSeekerEntity().getId());
        efforts.setClientName(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getSkillSeeker().getSkillSeekerName());
        efforts.setSkillSeekerProjectEntityId(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getId());
        efforts.setProjectName("Flexcub");
        efforts.setTotalHours(55);
        efforts.setRate(150);
        efforts.setServiceFeesAmount(230.50);
        efforts.setAmount(invoiceData.getAmount());
        efforts.setActualAmount(100.0);
        efforts.setTimesheetStartDate(LocalDate.now());
        workEffortsList.add(efforts);
        ownerTimeSheetEntities.add(ownerTimeSheetEntity);

        invoice.setId("IN-001");
        invoice.setInvoiceStatus(invoiceStatus);
        invoice.setSkillPartner(skillPartnerEntity);
        invoice.setTimesheetId(ownerTimeSheetEntity.getTimeSheetId());
        invoice.setDueDate(LocalDate.of(2022, 12, 10));
        invoice.setInvoiceDate(LocalDate.of(2022, 12, 10));
        invoice.setInvoiceData(invoiceDataList);
        invoice.setWeekStartDate(new Date(2022 - 12 - 05));
        invoice.setTimesheetId(1);



        invoiceResponse.setInvoiceId(invoice.getId());
        invoiceResponse.setTotalAmount(invoiceData.getAmount());
        invoiceResponse.setInvoiceStatus(invoiceStatus);


        invoiceListingData.setInvoiceId(invoice.getId());
        invoiceListingData.setInvoiceDate(invoice.getInvoiceDate());
        invoiceListingData.setSoCount(invoice.getInvoiceData().size());
        invoiceListingData.setStatus(invoice.getInvoiceStatus());
        invoiceListingData.setTo("abd");
        invoiceListingDataList.add(invoiceListingData);
        invoiceList.add(invoice);


        invoiceStatus.setId(1);
        invoiceStatus.setStatus("submitted");

        partnerInvoiceResponse.setSkillSeekerId(skillSeeker.getId());
        partnerInvoiceResponse.setAmount(20);
        partnerInvoiceResponse.setTotalHours(invoiceData.getTotalHours());
        partnerInvoiceResponse.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        partnerInvoiceResponse.setSkillSeekerProjectId(skillSeekerProjectEntity.getId());
        partnerInvoiceResponseList.add(partnerInvoiceResponse);

        invoiceData.setAmount(20);
        invoiceData.setOwnerId(skillOwnerEntity);
        invoiceData.setSkillSeeker(skillSeeker);
        invoiceData.setInvoiceStatus(invoiceStatus);
        invoiceData.setTotalHours(partnerInvoiceResponse.getTotalHours());
        invoiceData.setSkillSeekerProjectId(skillSeekerProjectEntity);
        invoiceData.setId(1);
        invoiceData.setActualAmount(100.0);
        invoiceDataList.add(invoiceData);
        statusList.add(invoiceStatus);

        invoiceRequest.setPartnerInvoiceResponseList(partnerInvoiceResponseList);
        invoiceRequest.setDueDate(new java.util.Date(2022 - 12 - 20));
        invoiceRequest.setStartDate(new java.util.Date(2022 - 12 - 25));
        invoiceRequest.setEndDate(new java.util.Date(2022 - 12 - 30));
        invoiceRequest.setSkillPartnerId(invoice.getSkillPartner().getSkillPartnerId());

        adminInvoiceData.setOwnerId(skillOwnerEntity);
        adminInvoiceData.setSkillSeekerProjectId(skillSeekerProjectEntity);
        adminInvoiceData.setSkillSeeker(skillSeeker);
        adminInvoiceDataList.add(adminInvoiceData);

        invoiceAdmin.setId("IN-001");
        invoiceAdmin.setSkillPartnerEntity(skillPartnerEntity);
        invoiceAdmin.setDueDate(new Date(2023,3,3).toLocalDate());
        invoiceAdmin.setTimesheetId(invoice.getTimesheetId());

        seekerInvoiceStatus.setAdminInvoiceId("IN-001");
        seekerInvoiceStatus.setStatusId(1);
        seekerInvoiceStatus.setComment("good");


        invoiceResponses.add(response);

        adminInvoiceRequest.setInvoiceId(1);
        adminInvoiceRequests.add(adminInvoiceRequest);

        clientInvoiceDetailsList.add(clientInvoiceDetails);

        invoiceDetailsList.add(invoiceDetails);

        invoiceAdmin.setInvoiceData(adminInvoiceDataList);
        invoiceAdmin.setInvoiceStatus(invoiceStatus);
        invoiceAdmins.add(invoiceAdmin);

        adminInvoiceSeekers.add(adminInvoiceSeeker);
        invoiceResponseData.setPosition(skillSeeker.getOwnerSkillDomainEntity().getDomainValues());

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

        detailResponse.setInvoiceData(workEffortsList);
        detailResponse.setInvoiceDate(new java.util.Date(2023, 1, 31));
        detailResponse.setComments("Good");
        detailResponse.setStatus(invoiceStatus);
        detailResponse.setDueDate(new Date(2023, 1, 31).toLocalDate());
        detailResponse.setEndDate(new java.util.Date(2023, 3, 31));
        detailResponse.setSkillPartnerId(1);
        poEntity.setSkillOwnerEntity(invoiceData.getOwnerId());
        poEntity.setJobId(job);
        adminInvoice.setInvoiceDataId(invoiceData.getId());
        adminInvoice.setInvoiceDataId(invoiceData.getAmount());
        adminInvoice.setStartDate(invoice.getWeekStartDate());
        adminInvoice.setOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId());
        adminInvoice.setOwnerName(invoiceData.getOwnerId().getFirstName());
        adminInvoice.setActualAmount(invoiceData.getActualAmount());
        adminInvoice.setAmount(invoiceData.getAmount());
        adminInvoice.setClient(invoiceData.getSkillSeeker().getSkillSeekerName());
        adminInvoice.setTotalHours(invoiceData.getTotalHours());
        adminInvoice.setInvoiceCreationDate(invoice.getInvoiceDate());
        adminInvoice.setInvoiceDueDate(invoice.getDueDate());
        adminInvoice.setTimesheetId(invoice.getTimesheetId());
        adminInvoice.setPosition(poEntity.getJobId().getJobTitle());
        adminInvoice.setRateCard(selectionPhase.getRate());
        adminInvoices.add(adminInvoice);

    }

    @Test
    void getOwnerByPartnerTest() {
        when(timeSheetRepository.findByPartnerIdAndIdStartDateEndDate(Mockito.anyInt(), Mockito.any(), Mockito.any())).thenReturn(ownerTimeSheetEntities);
        when(poRepository.findByOwnerId(ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(poEntity));
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(poEntity.getJobId().getJobId(), ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(selectionPhase));
        assertEquals(1, invoiceService.getOwnerByPartner(Mockito.anyInt(), Mockito.any(), Mockito.any()).size());
    }

    @Test
    void saveInvoiceByPartnerTest() {

        when(skillPartnerRepository.findById(invoiceRequest.getSkillPartnerId())).thenReturn(Optional.of(skillPartnerEntity));
        when(invoiceStatusRepository.findById(1)).thenReturn(Optional.ofNullable(invoiceStatus));
        when(skillOwnerRepository.findBySkillOwnerEntityId(1)).thenReturn(skillOwnerEntity);
        when(skillSeekerProjectRepository.findById(1)).thenReturn(Optional.of(skillSeekerProjectEntity));
        when(skillSeekerRepository.findById(1)).thenReturn(Optional.of(skillSeeker));
        when(invoiceRepository.saveAndFlush(any())).thenReturn(invoice);
        when(invoiceRepository.findById(String.valueOf(invoice))).thenReturn(Optional.of(invoice));
        assertEquals(invoice.getId(), invoiceService.saveInvoiceByPartner(invoiceRequest).getInvoiceId());

    }

    @Test
    void getInvoice() {
       when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.ofNullable(invoice));
       when(selectionPhaseRepository.findBySkillOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(selectionPhases));
       when(poRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntity));
       when(timeSheetRepository.findByTimeSheetId(invoice.getTimesheetId())).thenReturn(Optional.ofNullable(ownerTimeSheetEntity));
       when(adminInvoiceRepository.findByInvoiceId(invoice.getId())).thenReturn(Optional.ofNullable(invoiceAdmin));
       assertEquals(detailResponse.getStatus(),invoiceService.getInvoice("IN-001",true).getStatus());
    }

//    @Test
//    void getInvoice() {
//        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
//        when(selectionPhaseRepository.findBySkillOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(selectionPhases));
////        when(adminInvoiceRepository.findByInvoiceId(Mockito.anyString())).thenReturn(Optional.of(invoiceAdmin));
////        when(selectionPhaseRepository.findBySkillOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(selectionPhases));
//        when(poRepository.findByOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(poEntity));
//        when(timeSheetRepository.findByTimeSheetId(invoice.getTimesheetId())).thenReturn(Optional.ofNullable(ownerTimeSheetEntity));
//        assertEquals(detailResponse.getStatus(), invoiceService.getInvoice("IN-001", true).getStatus());
////        assertEquals(1, invoiceRequest.getPartnerInvoiceResponseList().size());
//    }

    @Test
    void getInvoicesTest() {
        when(invoiceRepository.findBySkillPartnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceList));
        assertEquals(1, invoiceService.getInvoices(1).size());
    }

    @Test
    void updatePartnerInvoiceStatusTest() {
        Mockito.when(invoiceRepository.findById("IN-02")).thenReturn(Optional.ofNullable(invoice));
        Mockito.when(invoiceStatusRepository.findById(1)).thenReturn(Optional.ofNullable((invoiceStatus)));
        assertEquals(invoiceResponse.getInvoiceStatus(), invoiceService.updatePartnerInvoiceStatus("IN-02", 1, Mockito.anyString()).getInvoiceStatus());
    }

    @Test
    void getInvoiceStatus() {
        Mockito.when(invoiceStatusRepository.findAll()).thenReturn(statusList);
        assertEquals(1, invoiceService.getInvoiceStatus().size());
    }

    @Test
    void getAdminInvoiceDataTest() {
        Mockito.when(invoiceDataRepository.findByProjectIdAndSeekerId(Mockito.anyInt(), Mockito.anyInt())).thenReturn(invoiceDataList);
        Mockito.when(invoiceRepository.findAll()).thenReturn(invoiceList);
        Mockito.when(poRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntity));
        Mockito.when(selectionPhaseRepository.findBySkillOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(selectionPhases));
        assertEquals(adminInvoices.size(), invoiceService.getAdminInvoiceData(1, 1).size());

    }

    @Test
    void getAllInvoiceDetailsTest() {
        Mockito.when(invoiceRepository.findAll()).thenReturn(invoiceList);
        assertEquals(1, invoiceService.getAllInvoiceDetails().size());
    }

    @Test
    void getAllInvoiceOfSeekerTest() {
        Mockito.when(adminInvoiceDataRepository.findInvoiceBySeekerId(Mockito.anyInt())).thenReturn(adminInvoiceDataList);
        Mockito.when(invoiceRepository.findBySkillPartnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceList));
        assertEquals(1, invoiceService.getAllInvoiceOfSeeker(skillSeeker.getId()).size());
    }

    @Test
    void updateSeekerInvoiceStatusTest() {
        Mockito.when(adminInvoiceRepository.findById("IN-001")).thenReturn(Optional.of(invoiceAdmin));
        Mockito.when(invoiceStatusRepository.findById(1)).thenReturn(Optional.of(invoiceStatus));
        assertEquals(seekerInvoiceStatus.getStatusId(), invoiceService.updateSeekerInvoiceStatus("IN-001", 1, "good").getStatusId());
    }

    @Test
    void saveInvoiceDetailsByAdminTest() {
        Mockito.when(invoiceDataRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceData));
        Mockito.when(skillOwnerRepository.findBySkillOwnerEntityId(Mockito.anyInt())).thenReturn(skillOwnerEntity);
        Mockito.when(invoiceStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceStatus));
        Mockito.when(invoiceRepository.findBySkillPartnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceList));
        Mockito.when(skillSeekerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeeker));
        Mockito.when(skillSeekerProjectRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerProjectEntity));
        Mockito.when(adminInvoiceRepository.saveAndFlush(Mockito.any())).thenReturn(invoiceAdmin);
        assertEquals(1, invoiceService.saveInvoiceDetailsByAdmin(adminInvoiceRequests).size());
    }

    @Test
    void invoiceClientDetailsTest() {
        Mockito.when(invoiceDataRepository.findAll()).thenReturn(invoiceDataList);
        assertEquals(1, invoiceService.invoiceClientDetails().size());
    }

    @Test
    void getAllInvoiceDetailAdminTest() {
        Mockito.when(adminInvoiceRepository.findAll()).thenReturn(invoiceAdmins);
        assertEquals(1, invoiceService.getAllInvoiceDetailAdmin().size());
    }

    @Test
    void getAdminInvoiceBySeekerTest() {
        Mockito.when(adminInvoiceRepository.findAll()).thenReturn(invoiceAdmins);
        assertEquals(1, invoiceService.getAdminInvoiceBySeeker(1).size());
    }

    @Test
    void updateAdminInvoiceStatusTest() {
        Mockito.when(adminInvoiceRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(invoiceAdmin));
        Mockito.when(invoiceStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(invoiceStatus));
        assertEquals(invoiceResponse.getInvoiceId(), invoiceService.updateAdminInvoiceStatus(invoice.getId(), invoiceStatus.getId()).getInvoiceId());
    }

    @Test
    void updatePartnerInvoiceTest() {
        Mockito.when(invoiceRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(invoice));
        Mockito.when(invoiceRepository.save(invoice)).thenReturn(invoice);
        Mockito.when(modelMapper.map(invoiceUpdateData, InvoiceUpdateData.class)).thenReturn(invoiceUpdateData);
        Mockito.when(modelMapper.map(invoice, InvoiceUpdate.class)).thenReturn(invoiceUpdate);
        assertEquals(invoiceUpdate, invoiceService.updateInvoiceDetailsByPartner(invoiceUpdate));

    }

    @Test
    void updateInvoiceDetailsByAdminTest() {
        Mockito.when(adminInvoiceRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(invoiceAdmin));
        Mockito.when(adminInvoiceRepository.save(invoiceAdmin)).thenReturn(invoiceAdmin);
        Mockito.when(modelMapper.map(invoiceUpdateData, InvoiceUpdateData.class)).thenReturn(invoiceUpdateData);
        Mockito.when(modelMapper.map(invoiceAdmin, InvoiceUpdate.class)).thenReturn(invoiceUpdate);
        assertEquals(invoiceUpdate, invoiceService.updateInvoiceDetailsByAdmin(invoiceUpdate));


    }


}