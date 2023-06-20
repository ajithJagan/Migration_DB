package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.notifications.repository.OwnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.repository.SeekerNotificationsRepository;
import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillStatusRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.SkillOwnerServiceImpl;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.*;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.Task;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillSeekerServiceImpl.class)
class SkillSeekerServiceImplTest {
    static String msaReleased = "MSA Released";
    static String msaInProgress = "MSA in Progress";
    static String poReleased = "PO Released";
    static String poInProgress = "PO in Progress";
    static String sowReleased = "SOW Released";
    static String sowInProgress = "SOW in Progress";
    static String active = "Active";
    @Autowired
    SkillSeekerServiceImpl skillSeekerService;


    @MockBean
    SkillOwnerServiceImpl skillOwnerService;
    @MockBean
    OwnerSkillDomainEntity ownerSkillDomainEntity;
    @MockBean
    SeekerAccessRepository seekerAccessRepository;
    @MockBean
    SeekerModuleRepository seekerModuleRepository;

    @MockBean
    PoRepository poRepository;

    @MockBean
    OwnerSkillStatusRepository ownerSkillStatusRepository;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    @MockBean
    SubRolesRepository subRolesRepository;
    @MockBean
    WorkForceStrength workForceStrength;
    @MockBean
    SeekerNotificationsRepository seekerNotificationsRepository;
    @MockBean
    OwnerNotificationsRepository ownerNotificationsRepository;
    @MockBean
    SeekerModulesEntity seekerModulesEntity;
    @MockBean
    RegistrationRepository registrationRepository;
    @MockBean
    ModelMapper modelMapper;
    @MockBean
    RegistrationService registrationService;

    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;
    @MockBean
    SkillSeekerMsaRepository skillSeekerMsaRepository;

    @MockBean
    SkillOwnerRepository skillOwnerRepository;
    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;
    @MockBean
    RequirementPhaseRepository requirementPhaseRepository;
    @MockBean
    SkillSeekerTaskRepository skillSeekerTaskRepository;
    @MockBean
    SkillSeekerProjectRepository skillSeekerProjectRepository;
    @MockBean
    SkillSeekerTaskService skillSeekerTaskService;

    @MockBean
    TemplateRepository templateRepository;
    @MockBean
    OwnerSkillDomainRepository ownerSkillDomainRepository;

@MockBean
    StatementOfWorkService  statementOfWorkService;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;
    SkillSeeker skillSeeker = new SkillSeeker();
    List<SkillSeeker> skillSeekerList = new ArrayList<>();
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    List<SkillSeekerEntity> skillSeekerEntityList = new ArrayList<>();
    List<SeekerModulesEntity> seekerModulesEntityList = new ArrayList<>();
    Date date;
    SubRoles subRoles = new SubRoles();
    List<SubRoles> subRolesList = new ArrayList<>();
    SubRole subRole = new SubRole();
    List<SeekerAccessEntity> seekerAccessEntityList = new ArrayList<>();
    Registration registration = new Registration();
    SeekerAccessEntity seekerAccessEntity = new SeekerAccessEntity();
    List<SeekerAccess> seekerAccessDtoList = new ArrayList<>();
    RegistrationEntity registrationEntity = new RegistrationEntity();
    List<SeekerAccessEntity> seekerAccessEntities = new ArrayList<>();
    SeekerRoleListing seekerRoleListing = new SeekerRoleListing();
    List<SeekerRoleListing> seekerRoleListings = new ArrayList<>();
    List<Integer> integers = new ArrayList<>();
    Integer integer = 1;
    Roles roles = new Roles();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    Contracts contracts = new Contracts();
    SkillSeekerMSAEntity skillSeekerMSAEntity = new SkillSeekerMSAEntity();
    PoEntity poEntity = new PoEntity();
    StatementOfWorkEntity statementOfWorkEntity = new StatementOfWorkEntity();
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    Job job = new Job();
    ContractDetails contractDetails = new ContractDetails();
    List<ContractDetails> contractDetailsList = new ArrayList<>();

    StatementOfWorkEntity statementOfWork = new StatementOfWorkEntity();
    SkillSeekerMSAEntity skillSeekerMsa = new SkillSeekerMSAEntity();
    PoEntity purchaseOrder = new PoEntity();

    SelectionPhase selectionPhase = new SelectionPhase();

    RequirementPhases requirementPhases = new RequirementPhases();
    List<RequirementPhases> requirementPhasesList = new ArrayList<>();

    LocalDate localDate;

    RequirementPhase requirementPhase = new RequirementPhase();

    List<RequirementPhase> requirementPhasesLists = new ArrayList<>();

    ContractStatus contractStatus = new ContractStatus();

    ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
    TaskList taskList = new TaskList();
    List<TaskList> taskLists = new ArrayList<>();
    Task task = new Task();
    List<Task> list = new ArrayList<>();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    List<SkillSeekerProjectEntity> skillSeekerProjectEntities = new ArrayList<>();
    SkillSeekerTaskEntity skillSeekerTask = new SkillSeekerTaskEntity();
    List<SkillSeekerTaskEntity> skillSeekerTaskEntityList = new ArrayList<>();

    OnBoarding onBoarding = new OnBoarding();

    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();


    @BeforeEach
    public void setup() {

        integers.add(integer);
        subRoles.setId(1);
        subRole.setRoleId(1);
        subRole.setModuleId(integers);
        subRoles.setSubRoleDescription("Basic Level");


        skillSeekerEntityList.add(skillSeekerEntity);
        skillSeekerList.add(skillSeeker);


        ownerSkillDomainEntity.setDomainId(1);
        ownerSkillDomainEntity.setDomainValues("Scala");

        skillSeeker.setId(1);
        skillSeeker.setSkillSeekerName("Ajith");
        skillSeeker.setEmail("ajith.j@qbrainx.com");
        skillSeeker.setPrimaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeeker.setSecondaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeeker.setCity("Salem");
        skillSeeker.setPhone("9087654321");
        skillSeeker.setAddressLine1("sangagiri");
        skillSeeker.setAddressLine2("salem");
        skillSeeker.setStatus("Ok");
        skillSeeker.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeeker.setState("TamilNadu");
        skillSeeker.setSubRoles(subRoles);
        skillSeeker.setZipCode(76542);
        skillSeeker.setTaxIdBusinessLicense("23444");
        skillSeeker.setAddedByAdmin(true);
        skillSeeker.setActive(true);
        skillSeeker.setPrimaryContactFullName("Ajith");
        skillSeeker.setSecondaryContactFullName("Ajith");
        skillSeeker.setPrimaryContactPhone("909877554433");
        skillSeeker.setSecondaryContactPhone("1234567890");
        skillSeeker.setModuleAccess(seekerModulesEntityList);
        skillSeeker.setRegistrationAccess(true);
        skillSeeker.setRegistrationAccess(registrationEntity.isAccountStatus());


        skillSeekerEntity.setId(1);
        skillSeekerEntity.setSkillSeekerName("Ajith");
        skillSeekerEntity.setEmail("ajith.j@qbrainx.com");
        skillSeekerEntity.setPrimaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeekerEntity.setSecondaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeekerEntity.setCity("Salem");
        skillSeekerEntity.setEndDate(date);
        skillSeekerEntity.setPhone("9087654321");
        skillSeekerEntity.setAddressLine1("sangagiri");
        skillSeekerEntity.setAddressLine2("salem");
        skillSeekerEntity.setStatus("Ok");
        skillSeekerEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerEntity.setState("Tamilnadu");
        skillSeekerEntity.setStartDate(new java.sql.Date(2022 - 12 - 23));
        skillSeekerEntity.setEndDate(new java.sql.Date(2023 - 12 - 30));
        skillSeekerEntity.setSubRoles(subRoles);
        skillSeekerEntity.setZipCode(76542);
        skillSeekerEntity.setTaxIdBusinessLicense("23444");
        skillSeekerEntity.setAddedByAdmin(true);
        skillSeekerEntity.setIsActive(true);
        skillSeekerEntity.setPrimaryContactFullName("Ajith");
        skillSeekerEntity.setSecondaryContactFullName("Ajith");
        skillSeekerEntity.setPrimaryContactPhone("909877554433");
        skillSeekerEntity.setSecondaryContactPhone("1234567890");
        skillSeekerEntity.setId(registration.getId());
        skillSeekerEntity.setRegistrationAccess(registrationEntity.isAccountStatus());
        skillSeekerEntity.setRegistrationAccess(true);


        registration.setId(1);
        registration.setCity("Salem");
        registration.setState("Tamilnadu");
        registration.setEmailId("ajith.j@qbrainx.com");
        registration.setAccountStatus(true);
        registration.setFirstName("Aji");
        registration.setSubRoles(1);
        registration.setPassword("1234567");
        registration.setBusinessName("Ajith");
        registration.setLastName("kumar");
        registration.setWorkForceStrength(workForceStrength);
        registration.setModulesAccess(seekerModulesEntityList);
        registration.setMailStatus("Ok");


        roles.setRoleDescription("Max");
        roles.setRolesId(123333L);


        workForceStrength.setId(1);
        workForceStrength.setRange("0 to 49");

        seekerModulesEntity.setId(1);
        seekerModulesEntity.setSeekerModule("Admin");

        seekerModulesEntityList.add(seekerModulesEntity);


        registrationEntity.setId(1);
        registrationEntity.setCity("Salem");
        registrationEntity.setState("Tamilnadu");
        registrationEntity.setEmailId("ajith.j@qbrainx.com");
        registrationEntity.setFirstName("Aji");
        registrationEntity.setPassword("1234567");
        registrationEntity.setBusinessName("Ajith");
        registrationEntity.setLastName("kumar");
        registrationEntity.setWorkForceStrength(workForceStrength);
        registrationEntity.setMailStatus("Ok");
        registrationEntity.setRoles(roles);
        registrationEntity.setDomainId(1);
        registrationEntity.setExcelId("excel");
        registrationEntity.setTechnologyIds("1223");
        registrationEntity.setToken("adeqdqecwcwecewcfqeed321y8e31298dgoq");
        registrationEntity.setTrial(false);
        registrationEntity.setCreatedAt(LocalDate.now());
        registrationEntity.setTrialExpiredOn(LocalDate.now().plusDays(7));
        registrationEntity.setAccountStatus(true);


        registrationEntity.setEmailId(skillSeeker.getEmail());
        registrationEntity.setContactPhone(skillSeeker.getPhone());
        registrationEntity.setState(skillSeeker.getState());
        registrationEntity.setFirstName(skillSeeker.getPrimaryContactFullName());
        roles.setRolesId(1L);
        registrationEntity.setRoles(roles);

        seekerAccessEntity.setSeekerModulesEntity(seekerModulesEntity);
        seekerAccessEntity.setSubRoles(subRoles);
        seekerAccessEntity.setTaxIdBusinessLicense("Po-123");
        seekerAccessEntities.add(seekerAccessEntity);

        seekerModulesEntity.setSeekerModule("Hr-Manager");

        seekerRoleListing.setRoleId("2");
        seekerRoleListing.setAccessList(seekerModulesEntityList);
        subRoles.setId(2);
        subRoles.setSubRoleDescription("HrManager");

        seekerRoleListings.add(seekerRoleListing);

        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setPrimaryEmail("Sukumar.m@qbrainx.com");
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);

        contracts.setStatus(msaInProgress);
        contracts.setLocation("New York");
        contracts.setName("sukumar");
        contracts.setExpiresOn(new Date());
        contracts.setOwnerContactNumber("9884104947");
        contracts.setProcessedOn(LocalDate.parse("2023-05-02"));
        contracts.setOwnerMailId("Sukumar.m@qbrainx.com");

        skillSeekerMSAEntity.setSkillOwnerEntity(skillOwnerEntity);

        poEntity.setId(1);
        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setSkillSeekerId(1);
        poEntity.setJobId(job);
        poEntity.setPoStatus(contractStatus);
        poEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        poEntity.setContractStartDate(LocalDate.now().minusDays(7));
        poEntity.setContractEndDate(new java.sql.Date(2025, 1, 12).toLocalDate());
        poEntity.setOnBoarding(new java.util.Date(2023, 1, 13));
//        poEntity.setStartDate(new java.util.Date(2023, 1, 12));
//        poEntity.setEndDate(new java.util.Date(2025, 1, 12));
//        poEntity.setDeletedAt(new java.sql.Date(2025, 1, 12).toLocalDate().atStartOfDay());
        poEntity.setDeletedAt(null);
        contracts.setPosition(poEntity.getJobId().getJobTitle());



        job.setJobId("FJB-00001");
        job.setJobTitle("Java Developer");

        skillPartnerEntity.setSkillPartnerId(1);
        skillPartnerEntity.setBusinessName("White Black PVT LTD");

        contracts.setSeekerName(skillPartnerEntity.getBusinessName());
        contractStatus.setId(8);
        contractStatus.setStatus("Approved");


        contractDetails.setNameOfOwner("vignesh");
        contractDetails.setPosition("Java Developer");
        contractDetails.setExperience("2Years,6 Months");
        contractDetails.setCurrentStage(4);
//        contractDetails.setListOfObjects(requirementPhasesList);
//        contractDetails.setContractWritingMSA("MSA Released");
//        contractDetails.setContractWritingSOW("SOW Released");
//        contractDetails.setContractWritingPO("PO in Progress");

        contractDetailsList.add(contractDetails);

        requirementPhases.setRequirementPhaseName("Walmart Business");
        requirementPhases.setStage(1);
        requirementPhases.setInterviewDate(LocalDate.now());
        requirementPhasesList.add(requirementPhases);

        selectionPhase.setSelectionId(1);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setRequirementPhase(requirementPhasesLists);

        job.setJobId("FJB-00001");

        skillSeekerMsa.setJobId(job);
        selectionPhase.setJob(job);

        purchaseOrder.setPoStatus(contractStatus);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillSeekerMsa.setSkillOwnerEntity(skillOwnerEntity);

        statementOfWork.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWork.setJobId(job);

        requirementPhase.setRequirementPhaseName(requirementPhases.getRequirementPhaseName());
        requirementPhase.setStage(requirementPhases.getStage());
//        requirementPhase.setTimeOfInterview(localDate.now());
        requirementPhasesLists.add(requirementPhase);

        projectTaskDetails.setSkillSeekerProjectAndTaskList(taskLists);

        taskList.setSkillSeekerTasks(list);
        taskList.setSkillSeekerProjectEntity(skillSeekerProjectEntity);

        taskLists.add(taskList);

        task.setTaskId(1);
        task.setTaskTitle("Flexcub");
        task.setTaskDescription("Implementation to the task");

        list.add(task);

        skillSeekerProjectEntity.setId(1);
        skillSeekerProjectEntity.setSkillSeekerTechnologyData(skillSeekerProjectEntity.getSkillSeekerTechnologyData());
        skillSeekerProjectEntity.setTitle("Flexcub");
        skillSeekerProjectEntity.setOwnerSkillDomainEntity(skillSeekerProjectEntity.getOwnerSkillDomainEntity());
        skillSeekerProjectEntity.setSkillSeeker(skillSeekerProjectEntity.getSkillSeeker());
        skillSeekerProjectEntity.setSummary("Implementing the Task");
        skillSeekerProjectEntity.setStartDate(new java.sql.Date(2023, 1, 12));
        skillSeekerProjectEntity.setEndDate(new java.sql.Date(2024, 1, 12));
        skillSeekerProjectEntity.setPrimaryContactPhone("9087654321");
        skillSeekerProjectEntity.setPrimaryContactEmail("hemamalini.a@qbrainx.com");

        skillSeekerProjectEntities.add(skillSeekerProjectEntity);

        skillSeekerTask.setSkillSeekerProject(skillSeekerProjectEntity);
        skillSeekerTask.setId(1);
        skillSeekerTask.setTaskDescription("Implementing the Task");
        skillSeekerTask.setTaskTitle("FLexcub");

        skillSeekerTaskEntityList.add(skillSeekerTask);

        statementOfWorkEntity.setSkillOwnerEntity(skillOwnerEntity);
        statementOfWorkEntity.setSkillSeekerId(1);
        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        statementOfWorkEntity.setId(1);
        statementOfWorkEntity.setSowStatus(contractStatus);
        statementOfWorkEntity.setRoles(job.getJobTitle());
        statementOfWorkEntity.setSowStartDate(new java.sql.Date(2023, 1, 12).toLocalDate());
        statementOfWorkEntity.setSowEndDate(new java.sql.Date(2024, 1, 12).toLocalDate());

        onBoarding.setSeekerId(1);
        onBoarding.setSkillOwnerEntityId(3);
        onBoarding.setProjectId(1);
        onBoarding.setProjectName("mexus");
        onBoarding.setStartDate(new java.util.Date(2023, 1, 13));
        onBoarding.setEndDate(new java.util.Date(2025, 1, 12));
        onBoarding.setStatus("onboarded");

        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        ownerSkillStatusEntity.setStatusDescription("available");
        ownerSkillStatusEntity.setSkillOwnerStatusId(4);
        ownerSkillStatusEntity.setStatusDescription("hiring");
    }

    @Test
    void addClientDetails() throws MessagingException {
        Mockito.when(modelMapper.map(skillSeeker, SkillSeekerEntity.class)).thenReturn(skillSeekerEntity);
        Mockito.when(registrationService.insertDetails(Mockito.any())).thenReturn(registration);

        Mockito.when(skillSeekerRepository.findById(registration.getId())).thenReturn(Optional.ofNullable(skillSeekerEntity));

        skillSeekerRepository.save(skillSeekerEntity);
        Mockito.when(modelMapper.map(skillSeekerEntity, SkillSeeker.class)).thenReturn(skillSeeker);
        assertNotNull(skillSeekerService.addClientDetails(skillSeeker));
        assertEquals(skillSeeker.getId(), skillSeekerService.addClientDetails(skillSeeker).getId());
    }

    @Test
    void updateClientDetails() {
        Optional<SkillSeekerEntity> skillSeekerData = Optional.of(new SkillSeekerEntity());
        skillSeekerData.get().setId(1);
        Mockito.when(modelMapper.map(skillSeeker, SkillSeekerEntity.class)).thenReturn(skillSeekerEntity);
        Mockito.when(skillSeekerRepository.findById(anyInt())).thenReturn(skillSeekerData);
        Mockito.when(registrationRepository.findById(anyInt())).thenReturn(Optional.ofNullable(registrationEntity));
        Mockito.when(modelMapper.map(skillSeekerData, SkillSeeker.class)).thenReturn(skillSeeker);
        assertEquals(1, skillSeekerService.updateClientDetails(skillSeeker).getId());

    }


    @Test
    void deleteData() {
        Mockito.when(skillSeekerRepository.findById(1)).thenReturn(Optional.of(skillSeekerEntity));
        skillSeekerService.deleteData(1);
        Mockito.verify(skillSeekerRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void getSeekerData() {
        Mockito.when(skillSeekerRepository.findById(1)).thenReturn(Optional.of(skillSeekerEntity));
        Mockito.when(modelMapper.map(skillSeekerEntity, SkillSeeker.class)).thenReturn(skillSeeker);
        assertEquals("Ajith", skillSeeker.getSkillSeekerName());
        assertEquals(seekerModulesEntityList.size(), skillSeeker.getModuleAccess().size());

    }

    @Test
    void updateData() {

        Optional<SkillSeekerEntity> skillSeekerEntity1 = Optional.of(skillSeekerEntity);
        when(modelMapper.map(skillSeeker, SkillSeekerEntity.class)).thenReturn(skillSeekerEntity);
        when(skillSeekerRepository.findById(anyInt())).thenReturn(Optional.ofNullable(skillSeekerEntity));
        when(modelMapper.map(skillSeekerEntity1, SkillSeeker.class)).thenReturn(skillSeeker);
        assertEquals(skillSeeker.getId(), skillSeekerService.updateData(skillSeeker).getId());
    }

    @Test
    void addEntryToSkillSeeker() {
        when(skillSeekerRepository.findByTaxIdBusinessLicense(Mockito.anyString())).thenReturn((Optional.of(skillSeekerEntityList)));
        skillSeekerRepository.save(skillSeekerEntity);
        Mockito.verify(skillSeekerRepository, Mockito.times(1)).save(skillSeekerEntity);
    }

    @Test
    void downloadTemplate() throws FileNotFoundException {
        assertEquals(200, skillSeekerService.downloadTemplate().getStatusCodeValue());
    }

    @Test
    void addSeekerSubRoles() {
        Optional<SkillSeekerEntity> skillSeekerEntity1 = Optional.of(skillSeekerEntity);
        when(skillSeekerRepository.findById(anyInt())).thenReturn(Optional.of(skillSeekerEntity));
        when(subRolesRepository.findById(anyInt())).thenReturn(Optional.of(subRoles));
        when(skillSeekerRepository.save(Mockito.any())).thenReturn(skillSeekerEntity);
        when(modelMapper.map(skillSeekerEntity1, SkillSeeker.class)).thenReturn(skillSeeker);
        assertEquals(skillSeeker, skillSeekerService.addSeekerSubRoles(skillSeeker.getId(), subRoles.getId()));
    }

    @Test
    void getSkillSeeker() {
        when(skillSeekerRepository.findByTaxIdBusinessLicense(Mockito.anyString())).thenReturn(Optional.of(skillSeekerEntityList));
        when(modelMapper.map(skillSeekerEntity, SkillSeeker.class)).thenReturn(skillSeeker);
        when(skillSeekerRepository.findById(anyInt())).thenReturn(Optional.of(skillSeekerEntity));
        when(seekerAccessRepository.findByTaxIdAndSubRole(Mockito.anyString(), anyInt())).thenReturn(seekerAccessEntities);
        assertEquals(1, skillSeekerService.getSkillSeeker("23444").size());

    }


    @Test
    void addSubRoleTest() {
        when(subRolesRepository.findById(subRole.getRoleId())).thenReturn(Optional.of(subRoles));
        when(seekerModuleRepository.findById(anyInt())).thenReturn(Optional.of(seekerModulesEntity));
        when(seekerAccessRepository.save(seekerAccessEntity)).thenReturn(seekerAccessEntity);
        assertEquals(1, skillSeekerService.addSubRole(subRole).size());
    }


    @Test
    void getModulesTest() {
        when(seekerModuleRepository.findAll()).thenReturn(seekerModulesEntityList);
        assertEquals(1, skillSeekerService.getModules().size());
    }

    @Test
    void getRolesTest() {
        when(subRolesRepository.findAll()).thenReturn(subRolesList);
        assertEquals(subRolesList, skillSeekerService.getRoles());

    }

    @Test
    void getAccessDetailsTest() {
        when(skillSeekerRepository.findById(Mockito.any())).thenReturn(Optional.of(skillSeekerEntity));
        when(seekerAccessRepository.findByTaxIdAndSubRole(Mockito.anyString(), anyInt())).thenReturn(seekerAccessEntityList);
        assertEquals(seekerAccessEntityList, skillSeekerService.getAccessDetails(1));
    }

    @Test
    void getAccessByTaxIdTest() {
        Mockito.when(seekerAccessRepository.findByTaxId(seekerAccessEntity.getTaxIdBusinessLicense())).thenReturn(seekerAccessEntities);
        Mockito.when(seekerModuleRepository.findAll()).thenReturn(seekerModulesEntityList);
        assertEquals(0, skillSeekerService.getAccessByTaxId(seekerAccessEntity.getTaxIdBusinessLicense()).size());
    }

    @Test
    void getProjectTaskDetailsBySeekerTest() {
        Mockito.when(skillSeekerProjectRepository.findBySkillSeekerId(1)).thenReturn(Optional.of(skillSeekerProjectEntities));
        Mockito.when(skillSeekerTaskRepository.findBySkillSeekerProjectId(1)).thenReturn(Optional.of(skillSeekerTaskEntityList));
        assertEquals(2, skillSeekerService.getProjectTaskDetailsBySeeker(1).getSkillSeekerProjectAndTaskList().size());
    }

//    @Test
//    void getContractDetailsTest() {
//        when(skillSeekerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerEntity));
//        when(skillSeekerRepository.findByTaxIdBusinessLicense(Mockito.anyString())).thenReturn(skillSeekerEntity));
//        when(statementOfWorkRepository.findByOwner(Mockito.anyInt())).thenReturn(statementOfWorkEntity);
//        assertEquals(contracts.getOwnerMailId(), skillSeekerService.getContractDetails(skillOwnerEntity.getSkillOwnerEntityId()).get(0).getOwnerMailId());
//
//    }

//    @Test
//    void getListsOfContractDetailsTest() {
//        Mockito.when(selectionPhaseRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
//        Mockito.when(skillSeekerMsaRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(skillSeekerMsa));
//        Mockito.when(statementOfWorkRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(statementOfWork));
//        Mockito.when(repository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(purchaseOrder));
//        assertEquals(1, skillSeekerService.getListsOfContractDetails(1).size());
//    }

//    @Test
//    void onBoardingSkillOwnerTest() {
//        Mockito.when(poRepository.findByDeleteAtNull(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntity));
//        Mockito.when(statementOfWorkRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(statementOfWorkEntity));
//        Mockito.when(skillOwnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillOwnerEntity));
//        Mockito.when(ownerSkillStatusRepository.findById(4)).thenReturn(Optional.ofNullable(ownerSkillStatusEntity));
//        Mockito.when(ownerSkillStatusRepository.findById(1)).thenReturn(Optional.ofNullable(ownerSkillStatusEntity));
//        Mockito.when(skillOwnerRepository.save(skillOwnerEntity)).thenReturn(skillOwnerEntity);
////        Mockito.when(poRepository.save(poEntity)).thenReturn(poEntity);
//        Mockito.when(statementOfWorkRepository.save(statementOfWorkEntity)).thenReturn(statementOfWorkEntity);
//        assertEquals(1, skillSeekerService.onBoardingSkillOwner(onBoarding).getSkillOwnerEntityId());
//    }
}