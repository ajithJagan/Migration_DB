//package com.flexcub.resourceplanning.skillseeker.service;
//
//
//import com.flexcub.resourceplanning.job.entity.Job;
//import com.flexcub.resourceplanning.job.entity.SelectionPhase;
//import com.flexcub.resourceplanning.job.repository.JobRepository;
//import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
//import com.flexcub.resourceplanning.notifications.service.impl.NotificationServiceImpl;
//import com.flexcub.resourceplanning.skillowner.entity.FileDB;
//import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
//import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
//import com.flexcub.resourceplanning.skillseeker.dto.MsaStatusDto;
//import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerMsaDto;
//import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
//import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
//import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
//import com.flexcub.resourceplanning.skillseeker.service.impl.PoServiceImpl;
//import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerMSAServiceImpl;
//import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdminMsa;
//import com.flexcub.resourceplanning.template.entity.TemplateTable;
//import com.flexcub.resourceplanning.template.repository.TemplateRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = SkillSeekerMSAServiceImpl.class)
//class SkillSeekerMSAServiceTest {
//
//    @Autowired
//    SkillSeekerMSAServiceImpl skillSeekerMSAService;
//
//    @MockBean
//    PoServiceImpl poService;
//
//    @MockBean
//    TemplateRepository templateRepository;
//
//    @MockBean
//    SkillSeekerMsaRepository skillSeekerMSARepository;
//
//    @MockBean
//    JobRepository jobRepository;
//
//    @MockBean
//    ModelMapper modelMapper;
//
//    @MockBean
//    SkillSeekerProjectRepository skillSeekerProjectRepository;
//    @MockBean
//    ContractStatusRepository msaStatusRepository;
//    @MockBean
//    SelectionPhaseRepository selectionPhaseRepository;
//    @MockBean
//    NotificationServiceImpl notificationService;
//    SkillSeekerMSAEntity skillSeekerMSA = new SkillSeekerMSAEntity();
//    List<SkillSeekerMSAEntity> skillSeekerMSAList = new ArrayList<>();
//    SkillSeekerProjectEntity skillSeekerProject = new SkillSeekerProjectEntity();
//    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
//    SeekerAdminMsa seekerAdminMsaDto = new SeekerAdminMsa();
//    SkillSeekerMsaDto skillSeekerMsaDto = new SkillSeekerMsaDto();
//    List<SeekerAdminMsa> seekerAdminMsaDtoList = new ArrayList<>();
//    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
//    Optional<ContractStatus> msaStatus = Optional.of(new ContractStatus());
//    Job job = new Job();
//    List<MultipartFile> file = new ArrayList<>();
//
//    FileDB fileDB = new FileDB();
//
//    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
//
//    MsaStatusDto msaStatusDto = new MsaStatusDto();
//
//    TemplateTable templateTable = new TemplateTable();
//
//
//    @BeforeEach
//    public void setup() throws IOException {
//
//
//        job.setJobId("FJB-00001");
//        msaStatus.get().setId(3);
//        msaStatus.get().setStatus("Active");
//        skillSeekerMSA.setSkillSeekerId(1);
//        skillSeekerMSA.setSkillSeekerProject(skillSeekerProject);
//        skillSeekerMSA.setData(new byte[35204]);
//        skillSeekerMSA.setId(1);
//        skillSeekerMSA.setName("new.docx");
//        skillSeekerMSA.setDateSigned(LocalDate.parse("2022 - 12 - 23"));
//        skillSeekerMSA.setExpiryDate(new Date(2022 - 12 - 30));
//        skillSeekerMSA.setSize(55L);
//        skillSeekerMSA.setMimeType("application/pdf");
//        skillSeekerMSA.setMsaCreated(true);
//        skillSeekerMSA.setMsaStatus(msaStatus.get());
//        skillSeekerMSA.setJobId(job);
//        skillSeekerMSA.setSkillSeekerProject(skillSeekerProject);
//        skillOwnerEntity.setSkillOwnerEntityId(1);
//        skillSeekerMSA.setSkillOwnerEntity(skillOwnerEntity);
//
//        ownerSkillDomainEntity.setDomainValues("Banking");
//        ownerSkillDomainEntity.setDomainId(1);
//
//
//        skillSeeker.setId(1);
//
//        skillSeekerProject.setSkillSeeker(skillSeeker);
//        skillSeekerProject.setId(1);
//        skillSeekerProject.setPrimaryContactEmail("kevinranig@gmail.com");
//        skillSeekerProject.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
//
//        seekerAdminMsaDto.setSkillSeekerId(1);
//        seekerAdminMsaDto.setId(1);
//        seekerAdminMsaDto.setSkillSeekerProjectName("flexcub");
//        seekerAdminMsaDto.setSkillSeekerProjectDept("Development");
//        seekerAdminMsaDto.setDateSigned(LocalDate.parse("2022 - 1 - 23"));
//
//        seekerAdminMsaDto.setExpiryDate(new Date(2024 - 7 - 23));
//
//        fileDB.setSkillPartnerId("1");
//        fileDB.setId(1);
//        fileDB.setName("application.pdf");
//        fileDB.setType("application/pdf");
//        fileDB.setData(new byte[1]);
//        fileDB.setSynced(false);
//
//        msaStatusDto.setMsaStatusId(3);
//
//
//        MultipartFile multipartFile = new MockMultipartFile(fileDB.getName(), fileDB.getName(), "application/pdf", fileDB.getName().getBytes());
//
//        file.add(multipartFile);
//
//        skillSeekerMsaDto.setId(1);
//
//        skillSeekerMSAList.add(skillSeekerMSA);
//        seekerAdminMsaDtoList.add(seekerAdminMsaDto);
//
//        templateTable.setId(1L);
//        templateTable.setSize(4L);
//        templateTable.setTemplateName("vendor.docx");
//        templateTable.setTemplateMimeType("vendor/docx");
//        templateTable.setData(new byte[3]);
//
//
//    }
//
//
//    @Test
//    void downloadAgreementTest() {
//        when(skillSeekerMSARepository.findById(anyInt())).thenReturn(Optional.of(skillSeekerMSA));
//
//        assertNotNull(skillSeekerMSAService.downloadAgreement(1));
//
//    }
//
//
//    @Test
//    void getMsaDetailsTest() {
//
//        when(skillSeekerMSARepository.findAll()).thenReturn(skillSeekerMSAList);
//        assertEquals(1, skillSeekerMSAService.getMsaDetails().size());
//    }
//
//    @Test
//    void getSkillSeekerMsaTemplateDownloadTest() throws IOException {
//        when(templateRepository.findByMSAFile()).thenReturn(Optional.of(templateTable));
//        assertEquals(200, skillSeekerMSAService.getSkillSeekerMsaTemplateDownload().getStatusCodeValue());
//    }
//
//    @Test
//    void getMsaDetailsBySeekerTest() {
//        when(skillSeekerMSARepository.findBySkillSeekerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerMSAList));
//        assertEquals(1, skillSeekerMSAService.getMsaDetailsBySeeker(1).size());
//    }
//
//    @Test
//    void addDocumentsTest() {
//
//        Optional<SelectionPhase> selectionPhase = Optional.of(new SelectionPhase());
//        when(skillSeekerMSARepository.save(Mockito.any())).thenReturn(skillSeekerMSA);
//        when(jobRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(job));
//        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(selectionPhase);
//        when(msaStatusRepository.findById(Mockito.any())).thenReturn(msaStatus);
//        assertEquals(skillSeekerMsaDto.getId(), skillSeekerMSAService.addDocuments(file, skillSeekerMSA.getSkillSeekerId(), skillSeekerProject.getId(), job.getJobId(), skillSeekerMSA.getSkillOwnerEntity().getSkillOwnerEntityId()).getId());
//    }
//
//    @Test
//    void updateMsaStatus() {
//        when(skillSeekerMSARepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerMSA));
//        when(msaStatusRepository.findById(Mockito.anyInt())).thenReturn(msaStatus);
//        assertEquals(msaStatusDto.getMsaStatusId(), skillSeekerMSAService.updateMsaStatus(1, msaStatusDto.getMsaStatusId()).getMsaStatusId());
//    }
//
//    @Test
//    void getMsaStatus() {
//        ContractStatus status = new ContractStatus();
//        List<ContractStatus> msaStatus1 = new ArrayList<>();
//        msaStatus1.add(status);
//        when(msaStatusRepository.findAll()).thenReturn(msaStatus1);
//        assertEquals(1, skillSeekerMSAService.getMsaStatus().size());
//    }
//
//}
