//package com.flexcub.resourceplanning.skillowner.service;
//
//import com.flexcub.resourceplanning.job.entity.Job;
//import com.flexcub.resourceplanning.registration.service.FlexcubEmailService;
//import com.flexcub.resourceplanning.skillowner.dto.Efforts;
//import com.flexcub.resourceplanning.skillowner.dto.OwnerTimeSheet;
//import com.flexcub.resourceplanning.skillowner.dto.TimeSheet;
//import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
//import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
//import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
//import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
//import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
//import com.flexcub.resourceplanning.skillowner.repository.TimesheetDocumentRepository;
//import com.flexcub.resourceplanning.skillowner.service.impl.OwnerTimeSheetServiceImpl;
//import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
//import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
//import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
//import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerTaskServiceImpl;
//import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = OwnerTimeSheetServiceImpl.class)
//class OwnerTimeSheetServiceImplTest {
//    @Autowired
//    OwnerTimeSheetServiceImpl ownerTimeSheetService;
//    @MockBean
//    FlexcubEmailService flexcubEmailService;
//    @MockBean
//    SkillSeekerTaskServiceImpl skillSeekerTaskService;
//    @MockBean
//    OwnerTimeSheetRepository ownerTimeSheetRepository;
//    @MockBean
//    TimesheetDocumentRepository timesheetDocumentRepository;
//    @MockBean
//    SkillOwnerRepository skillOwnerRepository;
//    @MockBean
//    SkillSeekerProjectRepository skillSeekerProjectRepository;
//    @MockBean
//    ModelMapper modelMapper;
//    @MockBean
//    PoRepository poRepository;
//
//    OwnerTimeSheet ownerTimeSheet = new OwnerTimeSheet();
//    List<OwnerTimeSheet> ownerTimeSheetList = new ArrayList<>();
//    TimeSheetResponse timeSheetResponse = new TimeSheetResponse();
//    List<TimeSheetResponse> timeSheetResponseList = new ArrayList<>();
//    SkillSeekerProject skillSeekerProjectTest = new SkillSeekerProject();
//    List<SkillSeekerProject> skillSeekerProjectTests = new ArrayList<>();
//    TimeSheet timeSheet = new TimeSheet();
//    List<TimeSheet> timeSheetList = new ArrayList<>();
//    OwnerTimeSheetEntity ownerTimeSheetEntity = new OwnerTimeSheetEntity();
//    List<OwnerTimeSheetEntity> ownerTimeSheetEntityList = new ArrayList<>();
//    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
//    List<SkillSeekerProjectEntity> skillSeekerProjectEntities = new ArrayList<>();
//    PoEntity poEntity = new PoEntity();
//    List<PoEntity> poEntities = new ArrayList<>();
//    Efforts efforts = new Efforts();
//    List<Efforts> effortsList = new ArrayList<>();
//    SkillSeekerTaskEntity skillSeekerTask = new SkillSeekerTaskEntity();
//    Job job = new Job();
//    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
//
//    @BeforeEach
//    public void setup() {
//
//        ownerTimeSheetEntity.setTimeSheetId(1);
//        ownerTimeSheetEntity.setSkillOwnerEntity(skillOwnerEntity);
//        ownerTimeSheetEntity.setSkillSeekerProjectEntity(ownerTimeSheetEntity.getSkillSeekerProjectEntity());
//        ownerTimeSheetEntity.setSkillSeekerTaskEntity(ownerTimeSheetEntity.getSkillSeekerTaskEntity());
//        ownerTimeSheetEntity.setHours("8Hours");
//        ownerTimeSheetEntity.setTimesheetStatus("Approved");
//        ownerTimeSheetEntity.setStartDate(new Date(2023, 1, 1));
//        ownerTimeSheetEntity.setEndDate(new Date(2023, 2, 1));
//        ownerTimeSheetEntity.setInvoiceGenerated(false);
//
//        ownerTimeSheetEntityList.add(ownerTimeSheetEntity);
//
//        ownerTimeSheet.setTimeSheetId(2);
//        ownerTimeSheet.setSkillOwnerEntityId(5);
//        ownerTimeSheet.setSkillSeekerEntityId(3);
//        ownerTimeSheet.setSkillSeekerProjectEntityId(1);
//        ownerTimeSheet.setEfforts(effortsList);
//        ownerTimeSheet.setStartDate(new Date(2023, 1, 1));
//        ownerTimeSheet.setEndDate(new Date(2023, 2, 1));
//        ownerTimeSheet.setApproved(true);
//        ownerTimeSheet.setHours("8Hours");
//        ownerTimeSheet.setTotalHours("8Hours");
//
//        ownerTimeSheetList.add(ownerTimeSheet);
//
//        timeSheetResponse.setTimeSheetId(1);
//        timeSheetResponse.setSkillOwnerEntityId(1);
//        timeSheetResponse.setSkillSeekerEntityId(1);
//        timeSheetResponse.setSkillSeekerProjectEntityId(1);
//        timeSheetResponse.setSkillSeekerTaskEntityId(1);
//        timeSheetResponse.setTitle("Flexcub Project");
//        timeSheetResponse.setTaskTitle("SkillOwner Details");
//        timeSheetResponse.setTaskDescription("To add the SkillOwner Details");
//        timeSheetResponse.setStartDate(new Date(2023, 1, 1));
//        timeSheetResponse.setEndDate(new Date(2023, 2, 1));
//        timeSheetResponse.setApproved(true);
//        timeSheetResponse.setHours("8Hours");
//
//        timeSheetResponseList.add(timeSheetResponse);
//
//        skillSeekerProjectTest.setId(1);
//        skillSeekerProjectTest.setTitle("Hema");
//        skillSeekerProjectTest.setPrimaryContactEmail("hemamalini.a@qbrainx.com");
//        skillSeekerProjectTest.setPrimaryContactPhone("9090909090");
//        skillSeekerProjectTest.setSummary("good morning");
//        skillSeekerProjectTest.setSecondaryContactEmail("hemamalini1114@gmail.com");
//        skillSeekerProjectTest.setSecondaryContactPhone("8523697562");
//        skillSeekerProjectTest.setOwnerSkillDomainEntity(skillSeekerProjectTest.getOwnerSkillDomainEntity());
//        skillSeekerProjectTest.setTaskData(skillSeekerProjectTest.getTaskData());
//        skillSeekerProjectTest.setSkillSeekerTechnologyData(skillSeekerProjectTest.getSkillSeekerTechnologyData());
//        skillSeekerProjectTest.setSkillSeeker(skillSeekerProjectEntity.getSkillSeeker());
//        skillSeekerProjectTest.setStartDate(new Date(2023, 1, 1));
//        skillSeekerProjectTest.setEndDate(new Date(2023, 1, 1));
//
//        skillSeekerProjectTests.add(skillSeekerProjectTest);
//
//        timeSheet.setSkillSeekerProjectEntityId(1);
//        timeSheet.setSkillSeekerTaskEntityId(1);
//        timeSheet.setHours("5Hours,6,8");
//        timeSheet.setTitle("Flexcub");
//        timeSheet.setApproved(false);
//        timeSheet.setStartDate(new Date(2023, 1, 1));
//        timeSheet.setEndDate(new Date(2023, 2, 1));
//        timeSheet.setTaskDescription("to add the skill owner project details");
//        timeSheet.setTaskTitle("Skill owner Implementation");
//        timeSheet.setTotalHours(8);
//
//
//        timeSheetList.add(timeSheet);
//
//        poEntity.setId(1);
//        poEntity.setName("Hema");
//        poEntity.setSize(265);
//        poEntity.setSkillOwnerEntity(skillOwnerEntity);
//        poEntity.setDateOfRelease(new java.util.Date());
//        poEntity.setExpiryDate(new java.util.Date());
//        poEntity.setJobId(job);
//        poEntity.setData(new byte[2]);
//        poEntity.setMimeType("doc");
//        poEntity.setOwnerSkillDomainEntity(poEntity.getOwnerSkillDomainEntity());
//        poEntity.setPoStatus(poEntity.getPoStatus());
//        poEntity.setSkillSeekerId(2);
//        poEntity.setRole("Java Developer");
//        poEntity.setSkillSeekerProject(skillSeekerProjectEntity);
//
//
//        poEntities.add(poEntity);
//
//        efforts.setHours("8Hours");
//        efforts.setSkillSeekerTaskEntity(skillSeekerTask);
//
//        effortsList.add(efforts);
//
//        skillSeekerTask.setId(1);
//        skillSeekerTask.setTaskTitle("Task1");
//        skillSeekerTask.setTaskDescription("Task Work Flow");
//        skillSeekerTask.setSkillSeekerProject(skillSeekerTask.getSkillSeekerProject());
//        skillSeekerTask.setSkillSeekerId(1);
//
//        job.setJobId("FJB-00001");
//        job.setJobTitle("Java Developer");
//        job.setJobLocation("Texas");
//        job.setOwnerSkillTechnologiesEntity(job.getOwnerSkillTechnologiesEntity());
//        job.setJobDescription("Document");
//        job.setOwnerSkillDomainEntity(job.getOwnerSkillDomainEntity());
//        job.setSeekerProject(job.getSeekerProject());
//        job.setProject("Mobile Development");
//        job.setOwnerSkillYearOfExperience(job.getOwnerSkillYearOfExperience());
//        job.setNumberOfPositions(10);
//        job.setOriginalNumberOfPosition(10);
//        job.setRemote(50);
//        job.setTravel(50);
//        job.setBaseRate(100);
//        job.setMaxRate(400);
//        job.setFederalSecurityClearance(false);
//        job.setScreeningQuestions(false);
//        job.setStatus("HR");
//        job.setHiringPriority(job.getHiringPriority());
//        job.setCoreTechnology("Java");
//        job.setSkillSeeker(job.getSkillSeeker());
//        job.setTaxIdBusinessLicense("EIN: 38-1440200");
//        job.setExpiryDate(new Date(2023, 1, 1));
//        job.setCustomTech("React");
//
//        skillOwnerEntity.setSkillOwnerEntityId(1);
//        skillOwnerEntity.setDOB(new Date(2023, 19, 01));
//        skillOwnerEntity.setCity("Arab");
//        skillOwnerEntity.setAddress("No.11/A, North Street");
//        skillOwnerEntity.setAccountStatus(false);
//        skillOwnerEntity.setImageAvailable(true);
//        skillOwnerEntity.setEndDate(new Date(2024, 19, 01));
//        skillOwnerEntity.setResumeAvailable(true);
//        skillOwnerEntity.setAboutMe("Dedication");
//        skillOwnerEntity.setFirstName("Vicky");
//        skillOwnerEntity.setLastName("R");
//        skillOwnerEntity.setPrimaryEmail("vignesh.r@qbrainx.com");
//        skillOwnerEntity.setAlternateEmail("vignesh.r@qbrainx.com");
//        skillOwnerEntity.setPhoneNumber("9090909090");
//        skillOwnerEntity.setAlternatePhoneNumber("9090909090");
//        skillOwnerEntity.setOwnerSkillStatusEntity(skillOwnerEntity.getOwnerSkillStatusEntity());
//        skillOwnerEntity.setVisaStatus(skillOwnerEntity.getVisaStatus());
//        skillOwnerEntity.setState("Alabama");
//        skillOwnerEntity.setLinkedIn("https://www.linkedin.com/feed/");
//        skillOwnerEntity.setRateCard(100);
//        skillOwnerEntity.setAccountStatus(false);
//        skillOwnerEntity.setState("Active");
//        skillOwnerEntity.setMaritalStatus(skillOwnerEntity.getMaritalStatus());
//        skillOwnerEntity.setGender(skillOwnerEntity.getGender());
//        skillOwnerEntity.setAboutMe("Available");
//        skillOwnerEntity.setSsn("12-2222-212");
//        skillOwnerEntity.setPermanentAddress("1/2, brooklyn street,texas");
//        skillOwnerEntity.setPortfolioUrl(skillOwnerEntity.getPortfolioUrl());
//        skillOwnerEntity.setExpYears(2);
//        skillOwnerEntity.setExpMonths(10);
////        skillOwnerEntity.setUsAuthorization(false);
////        skillOwnerEntity.setUSC(false);
////        skillOwnerEntity.setStatusVisa("H1B");
//        skillOwnerEntity.setRateCard(100);
//        skillOwnerEntity.setFederalSecurityClearance(false);
//        skillOwnerEntity.setStartDate(new Date(2024, 19, 01));
//        skillOwnerEntity.setEndDate(new Date(2024, 19, 01));
//        skillOwnerEntity.setOnBoardingDate(new Date(2024, 19, 01));
//        skillOwnerEntity.setResumeAvailable(false);
//        skillOwnerEntity.setImageAvailable(false);
//
//        skillSeekerProjectEntity.setId(1);
//        skillSeekerProjectEntity.setSkillSeekerTechnologyData(skillSeekerProjectEntity.getSkillSeekerTechnologyData());
//        skillSeekerProjectEntity.setTitle("Flexcub");
//        skillSeekerProjectEntity.setOwnerSkillDomainEntity(skillSeekerProjectEntity.getOwnerSkillDomainEntity());
//        skillSeekerProjectEntity.setSkillSeeker(skillSeekerProjectEntity.getSkillSeeker());
//        skillSeekerProjectEntity.setSummary("Implementing the Task");
//        skillSeekerProjectEntity.setStartDate(new Date(2023, 1, 12));
//        skillSeekerProjectEntity.setEndDate(new Date(2023, 1, 12));
//        skillSeekerProjectEntity.setPrimaryContactPhone("9087654321");
//        skillSeekerProjectEntity.setPrimaryContactEmail("hemamalini.a@qbrainx.com");
//        skillSeekerProjectEntity.setSecondaryContactEmail("vignesh.r@qbrainx.com");
//        skillSeekerProjectEntity.setSecondaryContactPhone("9090909090");
//
//        skillSeekerProjectEntities.add(skillSeekerProjectEntity);
//
//
//    }
//
//    @Test
//    void insertTimeSheetTest() {
//        Mockito.when(ownerTimeSheetRepository.findByStartDateAndSkillOwnerEntity(ownerTimeSheetEntity.getStartDate(), 1)).thenReturn(Optional.ofNullable(ownerTimeSheetEntityList));
//        when(modelMapper.map(ownerTimeSheetEntity, TimeSheetResponse.class)).thenReturn(timeSheetResponse);
//        assertEquals(1, timeSheetResponseList.size());
//        assertEquals(timeSheetResponse, timeSheetResponseList.get(0));
//    }
//
//    @Test
//    void updateTimeSheetTest() {
//        Mockito.when(ownerTimeSheetRepository.findByTimeSheetId(Mockito.anyInt())).thenReturn(Optional.ofNullable(ownerTimeSheetEntity));
//        Mockito.when(ownerTimeSheetRepository.save(ownerTimeSheetEntity)).thenReturn(ownerTimeSheetEntity);
//        Mockito.when(modelMapper.map(ownerTimeSheetEntity, TimeSheetResponse.class)).thenReturn(timeSheetResponse);
//        assertEquals(timeSheetResponse, ownerTimeSheetService.updateTimeSheet(timeSheetResponse));
//
//    }
//
//    @Test
//    void deleteTimeSheetTest() {
//        ownerTimeSheetRepository.deleteById(timeSheetResponse.getTimeSheetId());
//        Mockito.verify(ownerTimeSheetRepository, times(1)).deleteById(1);
//
//    }
//
//    @Test
//    void getTimeSheetTest() {
//        Mockito.when(ownerTimeSheetRepository.findByStartDateAndSkillOwnerEntity(ownerTimeSheetEntity.getStartDate(), 1)).thenReturn(Optional.ofNullable(ownerTimeSheetEntityList));
//        when(modelMapper.map(ownerTimeSheetEntity, TimeSheetResponse.class)).thenReturn(timeSheetResponse);
//        assertEquals(1, timeSheetResponseList.size());
//    }
//
//    @Test
//    void getProjectDetailsTest() {
//        Mockito.when(poRepository.findListBySkillOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(poEntities));
//        Mockito.when(modelMapper.map(ownerTimeSheetEntity, SkillSeekerProject.class)).thenReturn(skillSeekerProjectTest);
//        Mockito.when(modelMapper.map(skillSeekerProjectEntity, SkillSeekerProject.class)).thenReturn(skillSeekerProjectTest);
//        assertEquals(1, ownerTimeSheetService.getProjectDetails(1).size());
//
//    }
//
//    @Test
//    void getOwnerTimeSheetDetailsTest() {
//        Mockito.when(ownerTimeSheetRepository.findByOwnerId(1)).thenReturn(Optional.ofNullable(ownerTimeSheetEntityList)); // Use ofNullable is unnecessary here
//        Mockito.when(modelMapper.map(ownerTimeSheetEntity, TimeSheet.class)).thenReturn(timeSheet);
//        assertEquals(1, timeSheetList.size());
//    }
//}
