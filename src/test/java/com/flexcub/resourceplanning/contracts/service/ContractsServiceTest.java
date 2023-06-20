package com.flexcub.resourceplanning.contracts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexcub.resourceplanning.config.PartnerContractPagination;
import com.flexcub.resourceplanning.config.SeekerContractPagination;
import com.flexcub.resourceplanning.contracts.dto.*;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.contracts.service.impl.ContractServiceImpl;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.entity.*;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.*;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerProjectService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ContractServiceImpl.class)
class ContractsServiceTest {
    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    @MockBean
    SkillPartnerRepository skillPartnerRepository;
    @MockBean
    TemplateRepository templateRepository;
    @MockBean
    ObjectMapper objectMapper;

    @MockBean
    ContractFileRepository contractFileRepository;
    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;
    @MockBean
    ContractStatusRepository contractStatusRepository;
    @MockBean
    RegistrationRepository registrationRepository;
    @MockBean
    JobRepository jobRepository;
    @Autowired
    ContractServiceImpl contractService;
    @MockBean
    NotificationService notificationService;
    @MockBean
    PartnerContractPagination partnerContractPagination;
    @MockBean
    SeekerContractPagination seekerContractPagination;

    @MockBean
    SkillOwnerService skillOwnerService;

    @MockBean
    StatementOfWorkService statementOfWorkService;

    @MockBean
    SkillSeekerProjectService skillSeekerProjectService;

    @MockBean
    SkillPartnerService partnerService;

    @MockBean
    JobService jobService;
    @MockBean
    PoService poService;

    @MockBean
    SkillSeekerService skillSeekerService;

    @MockBean
    SelectionPhaseService selectionPhaseService;

    @MockBean
    RequirementPhaseRepository requirementPhaseRepository;

    @MockBean
    SkillSeekerProjectRepository seekerProjectRepository;

    @MockBean
    PoRepository poRepository;


    UpdatePORequest updatePORequest = new UpdatePORequest();
    ContractStatus contractStatus = new ContractStatus();

    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();

    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    PurchaseOrder purchaseOrder = new PurchaseOrder();

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    PoRequest poRequest = new PoRequest();


    PoResponse poResponse = new PoResponse();
    PoEntity poEntity = new PoEntity();
    Job job = new Job();
    Notification notification = new Notification();
    ContentEntity content = new ContentEntity();
    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();

    SkillSeekerEntity skillSeeker1 = new SkillSeekerEntity();
    List<SkillSeekerEntity> skillSeekerEntities = new ArrayList<>();

    SkillPartnerEntity skillPartner = new SkillPartnerEntity();
    StatementOfWorkEntity statementOfWorkEntity = new StatementOfWorkEntity();

    //    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
//    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
//    SkillPartnerEntity skillPartner = new SkillPartnerEntity();
    List<SkillPartnerEntity> partnerMsaDetailsList = new ArrayList<>();
    SkillPartnerEntity skillPartner1 = new SkillPartnerEntity();
    SkillPartnerEntity skillPartner2 = new SkillPartnerEntity();
    SkillPartnerEntity skillPartner3 = new SkillPartnerEntity();
    SkillPartnerEntity skillPartner4 = new SkillPartnerEntity();
    SeekerMsaFileRequest seekerMsaFileRequest = new SeekerMsaFileRequest();
    PartnerMsaFileRequest partnerMsaFileRequest = new PartnerMsaFileRequest();
    ContractFiles contractFiles = new ContractFiles();
    FileDB fileDB = new FileDB();
    List<MultipartFile> multipartFileList = new ArrayList<>();

    MsaFilesResponse msaFilesResponse = new MsaFilesResponse();

    OwnerSkillStatusEntity ownerSkillStatus = new OwnerSkillStatusEntity();

    List<RequirementPhase> requirementPhaseList = new ArrayList<>();

    RequirementPhase requirementPhase = new RequirementPhase();

    SelectionPhase selectionPhase = new SelectionPhase();

    List<SelectionPhase> selectionPhases = new ArrayList<>();

    List<SkillOwnerPortfolio> skillOwnerPortfolios = new ArrayList<>();

    OwnerSkillRoles ownerSkillRoles = new OwnerSkillRoles();

    Optional<ContractStatus> sowStatus = Optional.of(new ContractStatus());
    SeekerMSADetails msaDetails = new SeekerMSADetails();
    List<SeekerMSADetails> seekerMSADetails = new ArrayList<>();

    PartnerContractDetails partnerContractDetails = new PartnerContractDetails();
    List<PartnerContractDetails> partnerContractDetailsList = new ArrayList<>();

    List<StatementOfWorkEntity> getStatementOfWorkEntity = new ArrayList<>();


    byte[] bytes = {1, 2, 3};

//    @MockBean
//    PoServiceImpl poServiceimpl;


    @BeforeEach
    public void setup() throws IOException {


        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setDOB(LocalDate.ofEpochDay(1998 - 01 - 23));
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(67);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(true);
        skillOwnerEntity.setPortfolioUrl(skillOwnerPortfolios);
        skillOwnerEntity.setOwnerSkillStatusEntity(ownerSkillStatusEntity);

        skillSeekerProjectEntity.setId(1);
        ownerSkillDomainEntity.setDomainId(1);

        job.setJobId("FJB-00001");
        job.setNumberOfPositions(5);

        poResponse.setPoId(1);
        poResponse.setPoStatus("Submitted");

        poResponse.setPoId(1);
        poResponse.setPoStatus("Updated");

        content.setId(1);
        content.setTitle("Submitted");

        updatePORequest.setPoId(poEntity.getId());
        updatePORequest.setRateCard(180);
        updatePORequest.setContractStartDate(new java.sql.Date(2023 - 02 - 23).toLocalDate());
        updatePORequest.setContractEndDate(new java.sql.Date(2025 - 12 - 23).toLocalDate());


        poEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setId(1);
        poEntity.setSkillSeekerId(1);
        poEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        poEntity.setJobId(job);
        poEntity.setPoStatus(contractStatus);
        poEntity.setRole(job.getJobTitle());
        poEntity.setDateOfRelease(new java.sql.Date(2022 - 12 - 23));
        poEntity.setExpiryDate(new java.sql.Date(2023 - 12 - 30));


        skillOwnerEntity.setSkillOwnerEntityId(1);


        poRequest.setJobId(job.getJobId());
        poRequest.setSeekerId(1);
        poRequest.setOwnerId(3);
        poRequest.setContractStartDate(new java.sql.Date(2023 - 04 - 21).toLocalDate());
        poRequest.setContractEndDate(new java.sql.Date(2024 - 12 - 25).toLocalDate());

        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setId(1);
        statementOfWorkEntity.setSowStatus(sowStatus.get());
        statementOfWorkEntity.setId(1);
        statementOfWorkEntity.setDateOfRelease(LocalDate.parse("2022-12-23"));
        statementOfWorkEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setJobId(job);
        statementOfWorkEntity.setSkillPartnerEntity(skillPartner4);
        skillPartner4.setBusinessName("QBX");
        statementOfWorkEntity.setSkillSeekerId(1);

        getStatementOfWorkEntity.add(statementOfWorkEntity);


        fileDB.setId(1);
        fileDB.setName("application.pdf");
        fileDB.setType("application/pdf");
        fileDB.setData(new byte[1]);
        fileDB.setSynced(false);

        skillPartner.setSkillPartnerId(1);
        skillPartner.setMsaStartDate(LocalDate.parse("2023-05-12"));
        skillPartner.setMsaEndDate(LocalDate.parse("2023-10-12"));
        skillPartner.setBusinessName("QBX");
        skillPartner.setMsaId(contractFiles);
        skillPartner.setMsaStatusId(contractStatus);
        contractStatus.setId(14);

        partnerMsaDetailsList.add(skillPartner);


        ownerSkillDomainEntity.setDomainId(1);
        skillSeekerEntity.setId(1);
        skillSeekerProjectEntity.setId(1);
        ownerSkillRoles.setRolesDescription("Developer");

        requirementPhase.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        requirementPhase.setJobId(job.getJobId());
        requirementPhaseList.add(requirementPhase);

        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setJob(job);
        selectionPhases.add(selectionPhase);
        ownerSkillStatus.setSkillOwnerStatusId(4);
        ownerSkillStatus.setStatusDescription("InHiring");


        skillSeeker.setId(1);
        skillSeeker.setMsaId(null);
        skillSeeker.setMsaStartDate(null);
        skillSeeker.setMsaEndDate(null);
        skillSeeker.setMsaStatusId(null);


        skillPartner2.setSkillPartnerId(1);
        skillPartner2.setMsaId(null);
        skillPartner2.setMsaStartDate(null);
        skillPartner2.setMsaEndDate(null);
        skillPartner2.setMsaStatusId(null);

//        contractStatus.setId(4);
//        contractStatus.setStatus("Sent");
//
//        contractStatus.setId(7);
//        contractStatus.setStatus("Submitted");
//
//        contractStatus.setId(12);
//        contractStatus.setStatus("Updated");
//
//        contractStatus.setId(1);
//        contractStatus.setStatus("Initiated");


        purchaseOrder.setId(poEntity.getId());
        purchaseOrder.setStatus(poEntity.getPoStatus().getStatus());

        seekerMsaFileRequest.setSeekerId(1);
        seekerMsaFileRequest.setContractStartDate(LocalDate.parse("2023-01-12"));
        seekerMsaFileRequest.setContractExpiryDate(LocalDate.parse("2023-04-12"));

        partnerMsaFileRequest.setPartnerId(1);
        partnerMsaFileRequest.setMsaContractStartDate(LocalDate.parse("2023-06-12"));
        partnerMsaFileRequest.setMsaContractExpiryDate(LocalDate.parse("2023-05-10"));

        requirementPhase.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        requirementPhase.setJobId(job.getJobId());
        requirementPhaseList.add(requirementPhase);

        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setJob(job);
        selectionPhases.add(selectionPhase);
        ownerSkillStatus.setSkillOwnerStatusId(4);
        ownerSkillStatus.setStatusDescription("InHiring");

        msaFilesResponse.setFileId(1);
        msaFilesResponse.setMsaStatus("Created");

        contractFiles.setId(1);
        contractFiles.setFileName("application.pdf");
        contractFiles.setMimeType("application/pdf");
        contractFiles.setData(new byte[1]);
        contractFiles.setSize(55667);

        fileDB.setSkillPartnerId("1");
        fileDB.setId(1);
        fileDB.setName("application.pdf");
        fileDB.setType("application/pdf");
        fileDB.setData(new byte[1]);
        fileDB.setSynced(false);
        MultipartFile multipartFile = new MockMultipartFile(fileDB.getName(), fileDB.getName(), "application/pdf", fileDB.getName().getBytes());
        MockMultipartFile kmlfile = new MockMultipartFile("data", "filename.kml", "text/plain", "some kml".getBytes());
        multipartFileList.add(multipartFile);
        skillPartner1.setSkillPartnerId(1);
        skillPartner1.setMsaStartDate(LocalDate.parse("2022-02-09"));
        skillPartner1.setMsaEndDate(LocalDate.parse("2023-05-09"));
        skillPartner1.setBusinessName("QBX");
        skillPartner1.setMsaId(contractFiles);
        skillPartner1.setMsaStatusId(contractStatus);
        contractStatus.setId(14);
        contractStatus.setStatus("Sent");
        partnerMsaDetailsList.add(skillPartner1);


        skillSeeker.setId(1);
        skillSeeker.setMsaStartDate(LocalDate.parse("2023-01-12"));
        skillSeeker.setMsaEndDate(LocalDate.parse("2023-01-12"));
        skillSeeker.setSkillSeekerName("QBX");
        skillSeeker.setMsaId(contractFiles);
        skillSeeker.setMsaStatusId(contractStatus);
        contractStatus.setId(14);
        skillSeekerEntities.add(skillSeeker);

        skillSeeker1.setId(1);
        skillSeeker1.setMsaStartDate(LocalDate.parse("2023-01-12"));
        skillSeeker1.setMsaEndDate(LocalDate.parse("2023-01-12"));
        skillSeeker1.setSkillSeekerName("QBX");
        skillSeeker1.setMsaId(contractFiles);
        skillSeeker1.setMsaStatusId(contractStatus);

        msaDetails.setContractId(4);
        msaDetails.setSkillSeekerId(1);

        seekerMSADetails.add(msaDetails);

        skillPartner3.setSkillPartnerId(1);
        skillPartner3.setMsaStartDate(LocalDate.parse("2022-02-09"));
        skillPartner3.setMsaEndDate(LocalDate.parse("2023-05-09"));
        skillPartner3.setBusinessName("QBX");
        skillPartner3.setMsaId(contractFiles);
        skillPartner3.setMsaStatusId(contractStatus);
        contractStatus.setId(14);
        contractStatus.setStatus("MSA - Approved");
        partnerMsaDetailsList.add(skillPartner1);

        partnerContractDetails.setSkillPartnerId(1);
        partnerContractDetails.setContractExpiryDate(LocalDate.parse("2022-02-09"));
        partnerContractDetails.setContractExpiryDate(LocalDate.parse("2023-05-09"));
        partnerContractDetails.setEmail("hemamalini.a@qbrainx.com");
        partnerContractDetails.setNoOfResource("10");
        partnerContractDetails.setDepartment("Backend Developer");
        partnerContractDetails.setPhone("9000080000");
        partnerContractDetails.setBusinessName("QBX");
        partnerContractDetails.setJobId("FJB-00323");
        partnerContractDetails.setStatus("Approved");
        partnerContractDetails.setStatusId(14);
        partnerContractDetails.setStatusId(15);
        partnerContractDetails.setProject("Developing Management");

        partnerContractDetailsList.add(partnerContractDetails);

        skillPartner4.setSkillPartnerId(1);
        skillPartner4.setMsaStartDate(LocalDate.parse("2022-02-09"));
        skillPartner4.setMsaEndDate(LocalDate.parse("2023-05-09"));
        skillPartner4.setBusinessName("QBX");
        skillPartner4.setMsaId(contractFiles);
        skillPartner4.setMsaStatusId(contractStatus);
        contractStatus.setId(14);
        contractStatus.setStatus("MSA - Initiated");


    }

    @Test
    void createSeekerMsa() throws IOException {
        String msaDetails = "{\"seekerId\":1,\"contractStartDate\":\"2023-04-18T06:42:35.619Z\",\"contractExpiryDate\":\"2023-09-19T06:42:35.619Z\"}";
        when(contractFileRepository.save(any())).thenReturn(contractFiles);
        Mockito.when((objectMapper).readValue(msaDetails, SeekerMsaFileRequest.class)).thenReturn(seekerMsaFileRequest);
        when(skillSeekerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(skillSeeker));
        when(contractStatusRepository.findById(7)).thenReturn(Optional.of(contractStatus));
        when(skillSeekerService.getSkillSeekerEntity(skillSeeker.getId())).thenReturn(Optional.of(skillSeeker));
        when(contractService.getContractFiles(contractFiles.getId())).thenReturn(Optional.of(contractFiles));
//        when(contractService.updateContract(multipartFileList.get(0),contractFiles.getId())).thenReturn((contractFiles));
//        when(contractFileRepository.saveAndFlush(contractService.updateContract(multipartFileList.get(0),Mockito.anyInt())));
//        assertEquals(msaFilesResponse, contractService.createSeekerMsa(multipartFileList.get(0), msaDetails));
        Assertions.assertNotNull(contractService.createSeekerMsa(multipartFileList.get(0), msaDetails));
    }

    @Test
    void createPartnerMsa() throws IOException {
        String msaDetails = "{\"partnerId\":1,\"msaContractStartDate\":\"2023-06-12T06:42:35.619Z\",\"msaContractExpiryDate\":\"2023-10-10T06:42:35.619Z\"}";
        when(contractFileRepository.save(any())).thenReturn(contractFiles);
        Mockito.when((objectMapper).readValue(msaDetails, PartnerMsaFileRequest.class)).thenReturn(partnerMsaFileRequest);
        when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartner1));
//        when(skillPartnerRepository.findById(14)).thenReturn(Optional.ofNullable(skillPartner1));
        when(contractStatusRepository.findById(14)).thenReturn(Optional.ofNullable(contractStatus));
        when(skillPartnerRepository.save(skillPartner)).thenReturn(skillPartner);
        assertEquals(msaFilesResponse.getFileId(), contractService.createPartnerMsa(multipartFileList.get(0), msaDetails).getFileId());
    }

    @Test
    void getPartnerMsaDetailsInSuperAdmin() {
        Mockito.when(skillPartnerRepository.findAll()).thenReturn(partnerMsaDetailsList);
        Assertions.assertEquals(3, contractService.getPartnerMsaDetailsInSuperAdmin().size());
    }


    @Test
    void downloadSkillPartnerMsaAgreement() throws Exception {
        when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartner));
        assertEquals(200, contractService.downloadSkillPartnerMsaAgreement(Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void updatePartnerMsa() {
        when(skillPartnerRepository.findById(1)).thenReturn(Optional.ofNullable(skillPartner3));
        when(contractStatusRepository.findById(17)).thenReturn(Optional.ofNullable(contractStatus));
        when(skillPartnerRepository.save(skillPartner3)).thenReturn(skillPartner3);
        assertEquals(msaFilesResponse.getFileId(), contractService.updatePartnerMsaStatus(1, 17).getFileId());


    }

//    @Test
//    void createPOTest() throws IOException {
//        String poDetails = "{\"seekerId\": 1,\"ownerId\": 3,\"projectId\": 12,\"domainId\": 3,\"role\": \"developer\",\"jobId\": \"FJB-00001\",\"contractStartDate\": \"2023-04-21\",\"contractEndDate\": \"2025-04-21\",\"ratePerHour\": 150}";
//        when(contractFileRepository.save(any())).thenReturn(contractFiles);
//        Mockito.when((objectMapper).readValue(poDetails, PoRequest.class)).thenReturn(poRequest);
//        when(skillOwnerService.getSkillOwnerEntity((Mockito.anyInt()))).thenReturn(Optional.ofNullable(skillOwnerEntity));
//        when(jobService.getJob(Mockito.anyString())).thenReturn(Optional.ofNullable(job));
//        when(skillSeekerProjectService.getById(Mockito.anyInt())).thenReturn(skillSeekerProjectEntity);
//        when(contractStatusRepository.findById(7)).thenReturn(Optional.ofNullable(contractStatus));
//        when(poService.save(poEntity)).thenReturn(poEntity);
//        when(notificationService.poStatusNotification(any(), any())).thenReturn(content);
//        when(jobService.getById(Mockito.anyString())).thenReturn(job);
//        when(statementOfWorkService.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(statementOfWorkEntity));
//        when(jobService.saveAndFlush(job)).thenReturn(job);
//        assertEquals(poResponse.getPoStatus(), contractService.createPO(multipartFileList.get(0), poDetails).getPoStatus());
//    }

//    @Test
//    void updatePOByAdminTest() throws IOException {
//        String updatePODetails = "{\"poId\": 1,\"contractStartDate\": \"2023 - 02 - 23\",\"contractEndDate\": \"2024 - 12 - 25\",\"rateCard\": 180}";
//        Mockito.when((objectMapper).readValue(updatePODetails, UpdatePORequest.class)).thenReturn(updatePORequest);
//        Mockito.when(poService.getById(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntity));
//        Mockito.when(contractFileRepository.save(Mockito.any(ContractFiles.class))).thenReturn(contractFiles);
//        Mockito.when(contractStatusRepository.findById(12)).thenReturn(Optional.ofNullable(contractStatus));
//        when(poService.saveAndFlush(poEntity)).thenReturn(poEntity);
//        assertEquals(poResponse.getPoStatus(), contractService.updatePOByAdmin(multipartFileList.get(0), updatePODetails).getPoStatus());
//    }


//    @Test
//    void updateStatusTest() {
//        when(poService.getById(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntity));
//        when(contractStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(contractStatus));
//        assertEquals(purchaseOrder.getId(), contractService.updateStatus(1, 1).getId());
//
//    }

    @Test
    void updateSeekerMsa() {
        when(skillSeekerRepository.findByMsaId(1)).thenReturn(Optional.ofNullable(skillSeeker1));
        when(contractStatusRepository.findById(8)).thenReturn(Optional.ofNullable(contractStatus));
        when(skillSeekerRepository.save(skillSeekerEntity)).thenReturn(skillSeeker1);
        assertEquals(msaFilesResponse.getFileId(), contractService.updateSeekerMsaStatus(1, 8).getFileId());

    }

    @Test
    void getSeekerMsaDetailsInSuperAdmin() {
        Mockito.when(skillSeekerService.getAllSeeker()).thenReturn(skillSeekerEntities);
        Mockito.when(skillSeekerRepository.findAll()).thenReturn(skillSeekerEntities);
        Assertions.assertEquals(1, contractService.getSeekerMsaDetailsInSuperAdmin().size());
    }

    @Test
    void downloadSkillSeekerMsaAgreement() throws Exception {
        when(skillSeekerService.getSkillSeekerEntity(skillSeeker.getId())).thenReturn(Optional.ofNullable(skillSeeker));
        assertEquals(200, contractService.downloadSeekerMsa(contractFiles.getId()).getStatusCodeValue());
    }

    @Test
    void getContractByPartnerId(){
        when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartner4));
        when(statementOfWorkRepository.findByPartner(Mockito.anyInt())).thenReturn(getStatementOfWorkEntity);
        assertEquals(2, contractService.getContractByPartnerId(Mockito.anyInt()).size());

    }
}