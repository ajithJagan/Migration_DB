package com.flexcub.resourceplanning.skillseeker.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.contracts.service.impl.ContractServiceImpl;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillDomainService;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.skillseeker.service.impl.StatementOfWorkServiceImpl;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = StatementOfWorkServiceImpl.class)
class StatementOfWorkServiceTest {

    @Autowired
    StatementOfWorkServiceImpl statementOfWorkService;
    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;
    @MockBean
    SkillOwnerRepository skillOwnerRepository;
    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    @MockBean
    ObjectMapper objectMapper;
    @MockBean
    OwnerSkillDomainRepository ownerSkillDomainRepository;
    @MockBean
    PoRepository poRepository;
    @MockBean
    NotificationService notificationService;
    @MockBean
    ContractStatusRepository contractStatusRepository;
    @MockBean
    ContractFileRepository contractFileRepository;
    @MockBean
    JobRepository jobRepository;
    @MockBean
    SkillSeekerProjectRepository seekerProjectRepository;
    @MockBean
    PoService poService;
    @MockBean
    SkillOwnerService skillOwnerService;
    @MockBean
    ContractServiceImpl contractServiceImpl;
    @MockBean
    JobService jobService;
    @MockBean
    OwnerSkillDomainService ownerSkillDomainService;
    @MockBean
    SkillSeekerProjectService skillSeekerProjectService;
    StatementOfWorkEntity statementOfWorkEntity = new StatementOfWorkEntity();
    List<StatementOfWorkGetDetails> statementOfWorkGetDetailsList = new ArrayList<>();
    StatementOfWorkGetDetails statementOfWorkGetDetails = new StatementOfWorkGetDetails();
    List<StatementOfWorkEntity> statementOfWorkEntities = new ArrayList<>();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    SkillSeekerProjectEntity skillSeekerProject = new SkillSeekerProjectEntity();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    OwnerSkillRoles ownerSkillRoles = new OwnerSkillRoles();
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    Job job = new Job();
    FileDB fileDB = new FileDB();
    MultipartFile file;
    List<RequirementPhase> requirementPhaseList = new ArrayList<>();
    RequirementPhase requirementPhase = new RequirementPhase();
    SelectionPhase selectionPhase = new SelectionPhase();
    List<MultipartFile> multipartFileList = new ArrayList<>();
    List<SelectionPhase> selectionPhases = new ArrayList<>();
    OwnerSkillStatusEntity ownerSkillStatus = new OwnerSkillStatusEntity();
    SowRequest sowRequest = new SowRequest();
    SowUpdateRequest sowUpdateRequest = new SowUpdateRequest();
    StatementOfWork statementOfWorkDto = new StatementOfWork();
    SowStatusDto sowStatusDto = new SowStatusDto();
    SeekerPurchaseOrder seekerProductOwner = new SeekerPurchaseOrder();
    ContractFiles contractFiles = new ContractFiles();
    ContractStatus contractStatus = new ContractStatus();
    List<SeekerPurchaseOrder> seekerProductOwners = new ArrayList<>();
    TemplateTable templateTable = new TemplateTable();
    List<TemplateTable> templateTableList = new ArrayList<>();
    @MockBean
    private TemplateRepository templateRepository;

    @BeforeEach
    void setup() {

        sowRequest.setOwnerId(1);
        sowRequest.setJobId("FJB-00001");
        sowRequest.setSeekerId(1);
        sowRequest.setSowStartDate(LocalDate.parse("2023-04-01"));
        sowRequest.setSowEndDate(LocalDate.parse("2023-10-30"));

        sowUpdateRequest.setSowId(1);
        sowUpdateRequest.setSowStartDate(LocalDate.parse("2023-04-30"));
        sowUpdateRequest.setSowEndDate(LocalDate.parse("2023-10-30"));

        skillOwnerEntity.setSkillOwnerEntityId(1);

        contractFiles.setId(1);
        contractFiles.setFileName("application.pdf");
        contractFiles.setMimeType("application/pdf");
        contractFiles.setData(new byte[1]);
        contractFiles.setSize(55667);

        contractStatus.setId(7);
        contractStatus.setStatus("Submitted");
        skillSeekerProject.setId(1);

        statementOfWorkDto.setId(contractStatus.getId());
        statementOfWorkDto.setStatus(contractStatus.getStatus());

        statementOfWorkEntity.setId(1);
        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProject);
        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setSowStatus(contractStatus);
//        statementOfWorkEntity.setSow(contractFiles);
        statementOfWorkEntity.setDateOfRelease(LocalDate.parse("2023-04-23"));
//        statementOfWorkEntity.setSowCreated(true);
        statementOfWorkEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setJobId(job);


        statementOfWorkEntity.setSkillSeekerId(1);


        job.setJobId("FJB_0001");
        job.setNumberOfPositions(2);
        job.setSeekerProject(skillSeekerProject);
        job.setOwnerSkillDomainEntity(ownerSkillDomainEntity);


        fileDB.setId(1);
        fileDB.setName("application.pdf");
        fileDB.setType("application/pdf");
        fileDB.setData(new byte[1]);
        fileDB.setSynced(false);

        MultipartFile multipartFile = new MockMultipartFile(fileDB.getName(), fileDB.getName(), "application/pdf", fileDB.getName().getBytes());
        multipartFileList.add(multipartFile);
        purchaseOrder.setId(1);

        ownerSkillDomainEntity.setDomainId(1);
        skillSeekerEntity.setId(1);
        skillSeekerProjectEntity.setId(1);
        ownerSkillRoles.setRolesDescription("Developer");

        requirementPhase.setJobId(job.getJobId());
        requirementPhaseList.add(requirementPhase);

        selectionPhase.setJob(job);
        selectionPhases.add(selectionPhase);
        ownerSkillStatus.setSkillOwnerStatusId(4);
        ownerSkillStatus.setStatusDescription("InHiring");

        seekerProductOwner.setId(statementOfWorkEntity.getId());
        seekerProductOwner.setSkillSeekerProjectName("Scala And Kafka");

        statementOfWorkEntities.add(statementOfWorkEntity);
        seekerProductOwners.add(seekerProductOwner);

        statementOfWorkGetDetails.setId(1);
        statementOfWorkGetDetails.setJobId("FJB-00001");
        statementOfWorkGetDetails.setDepartment("it");
        statementOfWorkGetDetails.setRole("developer");
        statementOfWorkGetDetails.setOwnerId(1);
        statementOfWorkGetDetails.setStatus("sow in process");
        statementOfWorkGetDetails.setPhone("1234567899");
        statementOfWorkGetDetails.setEmail("abcd@qbrainx.com");
        statementOfWorkGetDetails.setProject("abc");
        statementOfWorkGetDetails.setSkillOwnerName("abc");
        statementOfWorkGetDetailsList.add(statementOfWorkGetDetails);


        sowStatusDto.setSowStatusId(1);
        sowStatusDto.setSowId(1);
        templateTable.setId(1L);
        templateTable.setSize(4L);
        templateTable.setTemplateName("vendor");
        templateTable.setTemplateType("SOW_TEMPLATE");
        templateTable.setTemplateMimeType("vendor/pdf");
        templateTable.setData(new byte[3]);

        templateTableList.add(templateTable);
    }

//    @Test
//    void addDocumentTest() throws IOException {
//        String sowDetails = "{\"ownerId\":3,\"seekerId\":1,\"domainId\":1,\"role\":1,\"projectID\":1, \"jobId\":\"FJB-00001\",\"sowStartDate\":\"2023-04-21\",\"sowEndDate\":\"2023-06-30\"}";
//        when((objectMapper).readValue(sowDetails, SowRequest.class)).thenReturn(sowRequest);
//        Mockito.when(skillOwnerService.getOwnerId(Mockito.anyInt())).thenReturn(skillOwnerEntity);
//        Mockito.when(contractFileRepository.save(Mockito.any())).thenReturn(contractFiles);
//        Mockito.when(jobService.findByJobId(sowRequest.getJobId())).thenReturn(job);
//        Mockito.when(skillSeekerProjectService.getById(job.getSeekerProject().getId())).thenReturn(skillSeekerProject);
//        Mockito.when(ownerSkillDomainService.getById(job.getOwnerSkillDomainEntity().getDomainId())).thenReturn(ownerSkillDomainEntity);
//        Mockito.when(skillOwnerService.getOwnerId(sowRequest.getOwnerId())).thenReturn(skillOwnerEntity);
//        Mockito.when(contractStatusRepository.findById(7)).thenReturn(Optional.ofNullable(contractStatus));
//        Mockito.when(statementOfWorkRepository.save(Mockito.any())).thenReturn(statementOfWorkEntity);
//        Mockito.when(jobService.findByJobId(job.getJobId())).thenReturn(job);
//        assertEquals(statementOfWorkEntity.getSowStatus().getStatus(), statementOfWorkService.addDocument(multipartFileList.get(0), sowDetails).getStatus());
//    }

//    @Test
//    void upDateSow() throws IOException {
//        String sowUpdateDetails = "{\"sowId\":1,\"sowStartDate\":\"2023-04-21\",\"sowEndDate\":\"2023-06-30\"}";
//        when((objectMapper).readValue(sowUpdateDetails, SowUpdateRequest.class)).thenReturn(sowUpdateRequest);
//        Mockito.when(statementOfWorkRepository.findById(1)).thenReturn(Optional.ofNullable(statementOfWorkEntity));
//        Mockito.when(contractFileRepository.save(Mockito.any())).thenReturn(contractFiles);
//        Mockito.when(contractStatusRepository.findById(12)).thenReturn(Optional.ofNullable(contractStatus));
//        Mockito.when(statementOfWorkRepository.save(statementOfWorkEntity)).thenReturn(statementOfWorkEntity);
//        assertEquals(statementOfWorkEntity.getId(), statementOfWorkService.upDateSow(multipartFileList.get(0), sowUpdateDetails).getId());
//
//    }

//    @Test
//    void getSowDetailsTest() {
//        Mockito.when(statementOfWorkRepository.findBySkillSeekerId(skillSeekerEntity.getId())).thenReturn(Optional.ofNullable(statementOfWorkEntities));
//        assertEquals(1, statementOfWorkService.getSowDetails(skillSeekerEntity.getId()).size());
//
//    }

//    @Test
//    void getAllSowDetailsTest() {
//        Mockito.when(statementOfWorkRepository.findAll()).thenReturn(statementOfWorkEntities);
//        assertEquals(1, statementOfWorkService.getAllSowDetails().size());
//        assertEquals(statementOfWorkGetDetails.getId(), statementOfWorkService.getAllSowDetails().size());
//    }

//    @Test
//    void updateStatusTest() {
//        Mockito.when(statementOfWorkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(statementOfWorkEntity));
//        Mockito.when(contractStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(contractStatus));
//        assertEquals(1, statementOfWorkService.updateSowStatus(1, 1).getSowId());
//
//    }

    @Test
    void downloadAgreementSOWTest() {
        Mockito.when(statementOfWorkRepository.findById(statementOfWorkEntity.getId())).thenReturn(Optional.ofNullable(statementOfWorkEntity));
        assertEquals(statementOfWorkEntity.getJobId(), statementOfWorkService.downloadAgreementSOW(statementOfWorkEntity.getId()).getJobId());
    }

    @Test
    void templateDownloadTest() {
        Mockito.when(templateRepository.findByTemplateFile("SOW_TEMPLATE")).thenReturn(templateTableList);
        assertEquals(200, statementOfWorkService.templateDownload().getStatusCodeValue());

    }
}