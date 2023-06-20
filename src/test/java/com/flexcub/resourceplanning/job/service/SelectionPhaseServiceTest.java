package com.flexcub.resourceplanning.job.service;

import com.flexcub.resourceplanning.job.dto.*;
import com.flexcub.resourceplanning.job.entity.*;
import com.flexcub.resourceplanning.job.repository.*;
import com.flexcub.resourceplanning.job.service.impl.SelectionPhaseServiceImpl;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import com.flexcub.resourceplanning.notifications.entity.OwnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.repository.ContentRepository;
import com.flexcub.resourceplanning.notifications.repository.OwnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.FlexcubEmailService;
import com.flexcub.resourceplanning.skillowner.entity.*;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerSlotsRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SelectionPhaseServiceImpl.class)
class SelectionPhaseServiceTest {

    @Autowired
    SelectionPhaseServiceImpl selectionPhaseService;

    @MockBean
    FlexcubEmailService flexcubEmailService;

    @MockBean
    NotificationService notificationService;

    @MockBean
    SkillOwnerService skillOwnerService;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    JobWorkFlowComponentRepository jobWorkFlowComponentRepository;

    @MockBean
    SkillOwnerSlotsRepository skillOwnerSlotsRepository;

    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;

    @MockBean
    OwnerNotificationsRepository ownerNotificationsRepository;

    @MockBean
    FeedbackRepository feedbackRepository;
    @MockBean
    JobRepository jobRepository;

    @MockBean
    ContentRepository contentRepository;

    @MockBean
    RequirementPhaseRepository requirementPhaseRepository;

    @MockBean
    OwnerSkillSetRepository ownerSkillSetRepository;

    @MockBean
    WorkFlowRepository workFlowRepository;

    @MockBean
    RegistrationRepository registrationRepository;

    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;

    @MockBean
    SkillSeekerMsaRepository skillSeekerMsaRepository;

    @MockBean
    PoRepository repository;

    @MockBean
    SkillOwnerRepository skillOwnerRepository;

    @Value("${spring.mvc.format.time}")
    private String timeType;


    SelectionPhase selectionPhase = new SelectionPhase();

    FeedbackRate feedbackRate = new FeedbackRate();

    SelectionPhaseResponse selectionPhaseResponse = new SelectionPhaseResponse();
    List<SelectionPhase> selectionPhaseList = new ArrayList<>();
    SelectionPhaseDto selectionPhaseDto = new SelectionPhaseDto();
    List<SelectionPhaseDto> selectionPhaseDtos = new ArrayList<>();
    StageStatusDto stageStatusDto = new StageStatusDto();
    JobWorkFlowComponent jobWorkFlowComponent = new JobWorkFlowComponent();
    Notification notificationDto = new Notification();
    ContentEntity content = new ContentEntity();
    RejectCandidateDto rejectCandidateDto = new RejectCandidateDto();
    Job job = new Job();
    Job job1 = new Job();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    RequirementPhase requirementPhaseNew = new RequirementPhase();
    List<RequirementPhase> requirementPhases = new ArrayList<>();
    ScheduleInterviewDto scheduleInterviewDto = new ScheduleInterviewDto();
    InsertRequirementPhaseDto insertRequirementPhaseDto = new InsertRequirementPhaseDto();
    StageStatusDto statusDto = new StageStatusDto();
    NewSlotRequestBySeekerDto newSlotRequestBySeekerDto = new NewSlotRequestBySeekerDto();
    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity;
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    OwnerSkillYearOfExperience ownerSkillYearOfExperience;
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    RequirementPhaseDetailsDto requirementPhaseDetailsDto = new RequirementPhaseDetailsDto();
    SlotConfirmByOwnerDto slotConfirmByOwnerDto = new SlotConfirmByOwnerDto();
    AcceptRejectDto acceptRejectDto = new AcceptRejectDto();
    ContentEntity contentEntity = new ContentEntity();
    JobWorkFlowComponent workFlowComponentJob = new JobWorkFlowComponent();
    FlowLockedDto flowLockedDto = new FlowLockedDto();
    List<WorkFlowComponent> workFlowComponentList = new ArrayList<>();
    SlotConfirmBySeekerDto slotConfirmBySeekerDto = new SlotConfirmBySeekerDto();
    WorkFlowComponent workFlowComponent = new WorkFlowComponent();
    SkillOwnerSlotsEntity skillOwnerSlotsEntity = new SkillOwnerSlotsEntity();

    SkillSeekerEntity getSkillSeekerEntity = new SkillSeekerEntity();

    HiringPriority hiringPriority = new HiringPriority();

    SelectionPhaseDto selectionPhaseDtoNew = new SelectionPhaseDto();
    RescheduleDto rescheduleDto = new RescheduleDto();
    OwnerNotificationsEntity ownerNotificationsEntity = new OwnerNotificationsEntity();

    @BeforeEach
    void setSelectionPhaseTest() {
        job.setJobId("FJB-00002");
        job.setJobTitle("Default");
        job.setJobTitle("Java Developer");
        job.setJobLocation("US");
        job.setJobDescription("Developer");
        job.setProject("flex");
        job.setNumberOfPositions(2);
        job.setRemote(56);
        job.setTravel(67);
        job.setBaseRate(56);
        job.setMaxRate(67);
        job.setFederalSecurityClearance(true);
        job.setScreeningQuestions(true);
        job.setStatus("Draft");
        job.setHiringPriority(hiringPriority);
        job.setCoreTechnology("Java");
        skillSeekerEntity.setId(2);
        getSkillSeekerEntity.setId(1);
        job.setSkillSeeker(getSkillSeekerEntity);
        job.setOwnerSkillYearOfExperience(ownerSkillYearOfExperience);
        job.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        job.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        job1.setSkillSeeker(skillSeekerEntity);

        selectionPhase.setJob(job);
        selectionPhase.setSelectionId(1);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setNewSlotRequested(false);
        selectionPhase.setSlotConfirmed(true);
        selectionPhase.setShowSelectionBar(true);
        selectionPhase.setAccepted(true);
        selectionPhase.setInterviewAccepted(true);
        selectionPhase.setCurrentStage(2);
        selectionPhase.setRequirementPhase(requirementPhases);
        selectionPhase.setShowTicksValues(true);
        selectionPhase.setAccepted(true);
        selectionPhase.setInterviewAccepted(true);
        selectionPhase.setJoinedOn(LocalDate.now());
        selectionPhase.setRejectedOn(LocalDate.now());


        requirementPhases.add(requirementPhaseNew);
        selectionPhase.setRequirementPhase(requirementPhases);

        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setFirstName("Soundarya");
        skillOwnerEntity.setLastName("Ram");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setLinkedIn("linkedin");
        skillOwnerEntity.setPrimaryEmail("soundaryaramachandran97@gmail.com");
        skillOwnerEntity.setRateCard(45);
        skillOwnerEntity.setState("TamilNadu");
        skillOwnerEntity.setAccountStatus(true);
        skillOwnerEntity.setExpMonths(1);
        skillOwnerEntity.setExpYears(1);


        rejectCandidateDto.setJobId(job.getJobId());
        rejectCandidateDto.setSkillOwnerId(9);

        feedbackRate.setId(1);
        feedbackRate.setRate(10);

        requirementPhaseNew.setRequirementId(1);
        requirementPhaseNew.setRequirementPhaseName("InitialScreening");
        requirementPhaseNew.setSkillOwnerId(1);
        requirementPhaseNew.setJobId(selectionPhase.getJob().getJobId());
        requirementPhaseNew.setStage(1);
        requirementPhaseNew.setFeedback("60");
        requirementPhaseNew.setFeedback("Good");
        requirementPhaseNew.setInterviewedBy(scheduleInterviewDto.getInterviewedBy());
        requirementPhaseNew.setDateOfInterview(scheduleInterviewDto.getDateOfInterview());
        requirementPhaseNew.setTimeOfInterview(scheduleInterviewDto.getTimeOfInterview());
        requirementPhaseNew.setEndTimeOfInterview(scheduleInterviewDto.getEndTimeOfInterview());
        requirementPhaseNew.setStatus("Cleared");
        requirementPhaseNew.setCandidateRate(feedbackRate);
        requirementPhases.add(requirementPhaseNew);


        rejectCandidateDto.setStage(1);


//        scheduleInterviewDto.setJobId(job.getJobId());
//        scheduleInterviewDto.setSkillOwnerId(1);
//        scheduleInterviewDto.setStage(2);
//        scheduleInterviewDto.setInterviewedBy("Soundarya");
        scheduleInterviewDto.setDateOfInterview(LocalDate.now());
//        scheduleInterviewDto.setTimeOfInterview(LocalTime.now());
//        scheduleInterviewDto.setModeOfInterview("virtual");


        List<String> requirementPhaseList1 = new ArrayList<>();
        requirementPhaseList1.add("InitialScreening");
        insertRequirementPhaseDto.setRequirementPhases(requirementPhaseList1);

        requirementPhaseNew.setRequirementId(1);
        requirementPhaseNew.setRequirementPhaseName("JAVA");
        requirementPhaseNew.setStatus(null);
        requirementPhaseNew.setSkillOwnerId(1);
        requirementPhaseNew.setDateOfInterview(LocalDate.parse("2023-04-12"));
        requirementPhaseNew.setTimeOfInterview(LocalTime.of(5,00,00));
        requirementPhaseNew.setEndTimeOfInterview(LocalTime.of(9,00,00));
        requirementPhases.add(requirementPhaseNew);
        selectionPhase.setRequirementPhase(requirementPhases);


        insertRequirementPhaseDto.setJobId(job.getJobId());


        selectionPhaseResponse.setJobId(job.getJobId());
        selectionPhaseResponse.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        selectionPhaseResponse.setJobTitle("Developer");
        selectionPhaseResponse.setCurrentStage(1);
        selectionPhaseResponse.setSkillOwnerName("Soundarya");
        selectionPhaseResponse.setShowTicksValues(false);
        selectionPhaseResponse.setRequirementPhaseList(requirementPhases);
        selectionPhaseResponse.setShowSelectionBar(false);
        selectionPhaseResponse.setExperience("1");
        selectionPhaseResponse.setSelectionId(1);
        selectionPhaseResponse.setRate(100);
        selectionPhaseResponse.setRequirementPhaseList(selectionPhaseResponse.getRequirementPhaseList());
        selectionPhaseResponse.setAccepted(false);
        selectionPhaseResponse.setDateSlotsByOwner1(LocalDate.now());
        selectionPhaseResponse.setDateSlotsByOwner2(LocalDate.now());
        selectionPhaseResponse.setDateSlotsByOwner3(LocalDate.now());
        selectionPhaseResponse.setTimeSlotsByOwner1(LocalTime.now());
        selectionPhaseResponse.setTimeSlotsByOwner2(LocalTime.now());
        selectionPhaseResponse.setTimeSlotsByOwner3(LocalTime.now());
        selectionPhaseResponse.setNewSlotRequested(false);
        selectionPhaseResponse.setSlotConfirmed(false);
        selectionPhaseResponse.setImageAvailable(false);
        selectionPhaseResponse.setResumeAvailable(false);
        selectionPhaseResponse.setInterviewAccepted(false);
        selectionPhaseResponse.setMsaCreated(false);
        selectionPhaseResponse.setSowCreated(false);
        selectionPhaseResponse.setPoCreated(false);



        skillOwnerEntity.setExpYears(1);

        selectionPhaseList.add(selectionPhase);

        requirementPhaseDetailsDto.setJobId(job.getJobId());
        requirementPhaseDetailsDto.setSkillOwnerId(1);
        requirementPhaseDetailsDto.setStage(1);


        slotConfirmByOwnerDto.setJobId(selectionPhase.getJob().getJobId());
        slotConfirmByOwnerDto.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
        slotConfirmByOwnerDto.setSlotsConfirmedBySeeker(selectionPhase.getSlotConfirmed());
        slotConfirmByOwnerDto.setDateSlotsByOwner1(new Date(2023, 03, 30));
        slotConfirmByOwnerDto.setDateSlotsByOwner2(new Date(2023, 03, 31));
        slotConfirmByOwnerDto.setDateSlotsByOwner3(new Date(2023, 04, 03));
        slotConfirmByOwnerDto.setTimeSlotsByOwner1(LocalTime.of(01, 40, 00));
        slotConfirmByOwnerDto.setTimeSlotsByOwner2(LocalTime.of(01, 40, 00));
        slotConfirmByOwnerDto.setTimeSlotsByOwner3(LocalTime.of(01, 40, 00));
        slotConfirmByOwnerDto.setEndTimeSlotsByOwner1(LocalTime.of(02,00,00));
        slotConfirmByOwnerDto.setEndTimeSlotsByOwner2(LocalTime.of(03,00,00));
        slotConfirmByOwnerDto.setEndTimeSlotsByOwner3(LocalTime.of(04,00,00));


        acceptRejectDto.setJobId(job.getJobId());
        acceptRejectDto.setSkillOwnerEntityId(1);
        acceptRejectDto.setAccepted(true);

        contentEntity.setTitle("Interview Schedule Confirmation");
        contentEntity.setId(1);

        selectionPhaseDto.setJob(job);
        selectionPhaseDto.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhaseDto.setCurrentStage(1);


        selectionPhaseDtoNew.setSkillOwnerEntity(skillOwnerEntity);

        selectionPhaseDtos.add(selectionPhaseDto);

        workFlowComponentJob.setJobId(job.getJobId());
        workFlowComponentJob.setFlow("HR ROUND");
        workFlowComponentJob.setLocked(false);

        flowLockedDto.setFlow(Collections.singletonList("HR ROUND"));
        flowLockedDto.setLocked(true);
        flowLockedDto.setJobId(job.getJobId());

        statusDto.setJobId(job.getJobId());
        statusDto.setSkillOwnerEntityId(1);
        statusDto.setStage(1);
        statusDto.setStatus(null);

        newSlotRequestBySeekerDto.setJobId(selectionPhase.getJob().getJobId());
        newSlotRequestBySeekerDto.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());


        scheduleInterviewDto.setJobId(selectionPhase.getJob().getJobId());
        scheduleInterviewDto.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        scheduleInterviewDto.setInterviewedBy("Soundarya");
        scheduleInterviewDto.setStage(1);
//        scheduleInterviewDto.setTimeOfInterview(LocalTime.now());
        scheduleInterviewDto.setModeOfInterview("Remote");
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2023, 05, 05));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(9, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(11, 2, 22));
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2023, 12, 11));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(9, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(11, 2, 22));
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2023, 12, 12));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(9, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(11, 2, 22));

        slotConfirmBySeekerDto.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
        slotConfirmBySeekerDto.setJobId(selectionPhase.getJob().getJobId());
        slotConfirmBySeekerDto.setSlotConfirmed(selectionPhase.getSlotConfirmed());

        scheduleInterviewDto.setJobId(job.getJobId());


        stageStatusDto.setJobId(job.getJobId());
        stageStatusDto.setSkillOwnerEntityId(skillOwnerEntity.getSkillOwnerEntityId());
        stageStatusDto.setStage(1);
        stageStatusDto.setStatus("scheduled");

        jobWorkFlowComponent.setJobId(job.getJobId());
        jobWorkFlowComponent.setFlow("Initial Screening");
        jobWorkFlowComponent.setLocked(false);

        notificationDto.setTitle("Interview Schedule Confirmation");
        notificationDto.setContent("Interview Schedule Confirm");
        content.setTitle("Interview Schedule Confirmation");
        content.setId(1);

        selectionPhaseResponse.setExperience(skillOwnerEntity.getExpYears().toString());

        rescheduleDto.setJobId("FJB-00002");
        rescheduleDto.setSkillOwnerId(1);
        rescheduleDto.setDateOfInterview(LocalDate.of(2023, 04, 17));
        rescheduleDto.setStartTime(LocalTime.of(01, 00, 00));
        rescheduleDto.setEndTime(LocalTime.of(02, 00, 00));
        rescheduleDto.setStatus("Rescheduled");
        rescheduleDto.setCurrentStage(2);
        skillOwnerSlotsEntity.setDateSlotsByOwner1(LocalDate.of(2024, 05, 2));
        skillOwnerSlotsEntity.setDateSlotsByOwner2(LocalDate.of(2024, 05, 10));
        skillOwnerSlotsEntity.setDateSlotsByOwner3(LocalDate.of(2024, 05, 11));
        skillOwnerSlotsEntity.setEndTimeSlotsByOwner1(LocalTime.of(12, 00, 00));
        skillOwnerSlotsEntity.setDateSlotsByOwner1(LocalDate.of(2024, 05, 22));
        skillOwnerSlotsEntity.setDateSlotsByOwner2(LocalDate.of(2024, 05, 23));
        skillOwnerSlotsEntity.setDateSlotsByOwner3(LocalDate.of(2024, 05, 24));
        skillOwnerSlotsEntity.setEndTimeSlotsByOwner1(LocalTime.of(02, 00, 00));
        skillOwnerSlotsEntity.setEndTimeSlotsByOwner2(LocalTime.of(02, 00, 00));
        skillOwnerSlotsEntity.setEndTimeSlotsByOwner3(LocalTime.of(02, 00, 00));
        skillOwnerSlotsEntity.setTimeSlotsByOwner1(LocalTime.of(01, 00, 00));
        skillOwnerSlotsEntity.setTimeSlotsByOwner2(LocalTime.of(01, 00, 00));
        skillOwnerSlotsEntity.setTimeSlotsByOwner3(LocalTime.of(01, 00, 00));
        skillOwnerSlotsEntity.setId(1);
        skillOwnerSlotsEntity.setSkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId());


        ownerNotificationsEntity.setId(1);
        ownerNotificationsEntity.setContentId(4);
        ownerNotificationsEntity.setTitle("");
        ownerNotificationsEntity.setDate(new Date());
        ownerNotificationsEntity.setContent("New SLot Request");
        ownerNotificationsEntity.setStage(1);
        ownerNotificationsEntity.setSkillOwnerEntityId(1);
        ownerNotificationsEntity.setMarkAsRead(false);
        ownerNotificationsEntity.setDateOfInterview(LocalDate.of(2023, 04, 11));
        ownerNotificationsEntity.setTimeOfInterview(LocalTime.of(01, 00, 00));

    }

    @Test
    void getSelectionPhaseTest() {
        when(selectionPhaseRepository.findAll()).thenReturn(Collections.singletonList(selectionPhase));
        assertEquals(selectionPhaseService.getSelectionPhase(), Collections.singletonList(selectionPhase));
    }


    @Test
    void insertMultipleDataTest() {
        when(jobRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(job1));
        when(selectionPhaseRepository.findBySkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhaseList));
        when(skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(skillOwnerEntity);
        when(jobRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(job1));
        assertEquals(1, selectionPhaseService.insertMultipleData(selectionPhaseDtos).size());
    }

    @Test
    void insertRequirementPhaseTest() {
        when(selectionPhaseRepository.findByJobJobId(Mockito.anyString())).thenReturn(selectionPhaseList);
        when(jobWorkFlowComponentRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(workFlowComponentJob));
        when(requirementPhaseRepository.findByJobIdSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(requirementPhases));
        when(selectionPhaseRepository.save(Mockito.any())).thenReturn(selectionPhase);
        assertEquals(1, selectionPhaseService.insertRequirementPhase(insertRequirementPhaseDto).size());

    }

    @Test
    void rejectCandidateTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(rejectCandidateDto.getJobId(), rejectCandidateDto.getSkillOwnerId())).thenReturn(Optional.of(selectionPhase));
        when(requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(rejectCandidateDto.getJobId(), rejectCandidateDto.getSkillOwnerId(), rejectCandidateDto.getStage())).thenReturn(Optional.ofNullable(requirementPhaseNew));
        assertEquals(stageStatusDto.getStage(), selectionPhaseService.rejectCandidate(rejectCandidateDto).getStage());

    }

    @Test
    void updateNewSlotBySeekerTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.any(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(notificationService.newSlotBySeekerNotification(Mockito.any(), Mockito.anyInt(), Mockito.any())).thenReturn(contentEntity);
        assertEquals(newSlotRequestBySeekerDto.getJobId(), selectionPhaseService.updateNewSlotBySeeker(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()).getJobId());
    }

    @Test
    void scheduleInterviewTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId())).thenReturn(Optional.of(selectionPhase));
        when(requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId(), scheduleInterviewDto.getStage())).thenReturn(Optional.of(requirementPhaseNew));
        when(requirementPhaseRepository.findByDateAndTime(scheduleInterviewDto.getSkillOwnerId())).thenReturn(requirementPhases);
        when(selectionPhaseService.validTimeCheck(scheduleInterviewDto)).thenReturn(scheduleInterviewDto);
        when(requirementPhaseRepository.save(requirementPhaseNew)).thenReturn(requirementPhaseNew);
        when(jobWorkFlowComponentRepository.findByJobId(scheduleInterviewDto.getJobId())).thenReturn(Optional.of(workFlowComponentJob));
        when(notificationService.scheduleInterviewNotification(Mockito.any(), Mockito.any())).thenReturn(contentEntity);
        assertThat(selectionPhaseService.scheduleInterview(scheduleInterviewDto, false).getJobId()).isEqualTo(statusDto.getJobId());
    }

    @Test
    void getCandidatesByJobIdTest() {
        when(selectionPhaseRepository.findByJobJobId(selectionPhase.getJob().getJobId())).thenReturn(selectionPhaseList);
        assertEquals(1, selectionPhaseService.getCandidatesByJobId(selectionPhaseResponse.getJobId()).size());
    }

    @Test
    void candidateInterviewDetailsTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhase));
        assertThat(selectionPhaseService.candidateInterviewDetails(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()).getJobId()).isEqualTo(job.getJobId());
        assertEquals(selectionPhaseResponse.getJobId(), selectionPhaseService.candidateInterviewDetails(job.getJobId(), skillOwnerEntity.getSkillOwnerEntityId()).getJobId());
    }


    @Test
    void reInitiateHiringTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhase));
        when(selectionPhaseRepository.findBySkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhaseList));
        assertThat(selectionPhaseService.reInitiateHiring(statusDto.getJobId(), statusDto.getSkillOwnerEntityId()));
    }

    @Test
    void updateDetailsForParticularRoundTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.ofNullable(requirementPhaseNew));
        when(requirementPhaseRepository.save(requirementPhaseNew)).thenReturn(requirementPhaseNew);
        assertEquals(requirementPhaseDetailsDto.getJobId(), selectionPhaseService.updateDetailsForParticularRound(requirementPhaseDetailsDto).getJobId());
    }

    @Test
    void getWorkflow() {
        when(workFlowRepository.findAll()).thenReturn(workFlowComponentList);
        workFlowComponentList.add(workFlowComponent);
        assertEquals(1, Collections.singletonList(workFlowComponentList).size());
    }

    @Test
    void getSlotsTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        assertEquals(slotConfirmByOwnerDto.getSlotsConfirmedBySeeker(), selectionPhaseService.getSlots(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()).getSlotsConfirmedBySeeker());
    }

    @Test
    void updateSlotBySkillOwnerTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(notificationService.slotBySkillOwnerNotification(slotConfirmByOwnerDto)).thenReturn(contentEntity);
        assertEquals(slotConfirmByOwnerDto.getJobId(), selectionPhaseService.updateSlotBySkillOwner(slotConfirmByOwnerDto).getJobId());
    }

    @Test
    void slotConfirmedBySeeker() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(selectionPhaseRepository.save(selectionPhase)).thenReturn(selectionPhase);
        assertEquals(slotConfirmBySeekerDto.getSlotConfirmed(), selectionPhaseService.slotConfirmedBySeeker(Mockito.anyString(), Mockito.anyInt()).getSlotConfirmed());
    }

    @Test
    void acceptRejectBySkillOwnerTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(selectionPhaseRepository.findBySkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhaseList));
        when(notificationService.acceptBySkillOwnerNotification(Mockito.any())).thenReturn(contentEntity);
        assertEquals(acceptRejectDto.getJobId(), selectionPhaseService.acceptRejectBySkillOwner(acceptRejectDto).getJobId());
    }


    @Test
    void selectedForRoundTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.of(requirementPhaseNew));
        when(requirementPhaseRepository.findByJobIdSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(requirementPhases));
        when(skillOwnerService.findBySkillOwnerEntityId(Mockito.anyInt())).thenReturn(skillOwnerSlotsEntity);
        when(requirementPhaseRepository.save(Mockito.any())).thenReturn(requirementPhaseNew);
        when(selectionPhaseRepository.save(Mockito.any())).thenReturn(selectionPhase);
        assertEquals(requirementPhaseDetailsDto.getJobId(), selectionPhaseService.selectedForRound(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId(), selectionPhase.getCurrentStage()).getJobId());
    }

    @Test
    void isLockedTest() {
        when(jobWorkFlowComponentRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(workFlowComponentJob));
        when(requirementPhaseRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(requirementPhases));
        assertEquals(flowLockedDto.getJobId(), selectionPhaseService.isLocked(selectionPhase.getJob().getJobId()).getJobId());
    }

    @Test
    void acceptInterviewTest() {
        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId())).thenReturn(Optional.of(selectionPhase));
        when(selectionPhaseRepository.save(Mockito.any())).thenReturn(selectionPhase);
        assertEquals(stageStatusDto.getJobId(), selectionPhaseService.acceptInterview(acceptRejectDto).getJobId());
    }

    @Test
    void updateCommonSlotByOwnerTest() {
        when(skillOwnerSlotsRepository.findBySkillOwnerEntityId(Mockito.anyInt())).thenReturn(skillOwnerSlotsEntity);
        assertEquals(slotConfirmByOwnerDto.getJobId(), selectionPhaseService.updateCommonSlotByOwner(slotConfirmByOwnerDto).getJobId());
    }

    @Test
    void rescheduleRoundTest() {

        when(selectionPhaseRepository.findByJobIdAndSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(selectionPhase));
        when(requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage("FJB-00002", 1, 1)).thenReturn(Optional.of(requirementPhaseNew));
        when(requirementPhaseRepository.findByJobIdSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(requirementPhases));
        when(requirementPhaseRepository.findBySkillOwnerId(Mockito.anyInt())).thenReturn(Optional.of(requirementPhases));
        when(requirementPhaseRepository.save(requirementPhaseNew)).thenReturn(requirementPhaseNew);
        when(selectionPhaseRepository.save(selectionPhase)).thenReturn(selectionPhase);
        when(skillOwnerSlotsRepository.findBySkillOwnerEntityId(1)).thenReturn(skillOwnerSlotsEntity);
        when(skillOwnerSlotsRepository.save(skillOwnerSlotsEntity)).thenReturn(skillOwnerSlotsEntity);
        when(requirementPhaseRepository.findByJobIdSkillOwnerId(Mockito.anyString(), Mockito.anyInt())).thenReturn(Optional.of(requirementPhases));
        when(requirementPhaseRepository.save(requirementPhaseNew)).thenReturn(requirementPhaseNew);
        when(contentRepository.findByCommonSlot()).thenReturn(content);
        when(skillOwnerRepository.findById(1)).thenReturn(Optional.of(skillOwnerEntity));
        when(ownerNotificationsRepository.save(ownerNotificationsEntity)).thenReturn(ownerNotificationsEntity);
        when(notificationService.autoScheduleInterviewNotification(Mockito.any(), Mockito.any())).thenReturn(contentEntity);
        assertEquals(rescheduleDto.getJobId(), selectionPhaseService.rescheduleRound("FJB-00002", 1, 1).getJobId());
    }

}


