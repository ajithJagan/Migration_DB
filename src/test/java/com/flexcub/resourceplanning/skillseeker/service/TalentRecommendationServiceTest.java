package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.job.dto.ScheduleInterviewDto;
import com.flexcub.resourceplanning.job.entity.FeedbackRate;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerResumeAndImageRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillSetService;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.dto.RecommendedCandidates;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.TalentRecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TalentRecommendationServiceImpl.class)
class TalentRecommendationServiceTest {

    @Autowired
    TalentRecommendationServiceImpl talentRecommendationService;

    @MockBean
    SkillOwnerResumeAndImageRepository skillOwnerResumeAndImageRepository;

    @MockBean
    OwnerSkillSetService ownerSkillSetService;

    @MockBean
    LocationMatchForJobService locationMatchForJobService;

    @MockBean
    SkillOwnerRepository skillOwnerRepository;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @MockBean
    JobRepository jobRepository;

    @MockBean
    PoRepository poRepository;

    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;

    @MockBean
    OwnerSkillSetRepository ownerSkillSetRepository;

    @MockBean
    StatementOfWorkRepository statementOfWorkRepository;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    ModelMapperConfiguration modelMapperConfiguration;

    List<RecommendedCandidates> recommendedCandidatesList = new ArrayList<>();


    List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();
    OwnerSkillSetEntity ownerSkillSet = new OwnerSkillSetEntity();

    List<OwnerSkillSetEntity> ownerSkillSetEntities = new ArrayList<>();
    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();
    Job job = new Job();
    SelectionPhase selectionPhase = new SelectionPhase();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    RequirementPhase requirementPhaseNew = new RequirementPhase();

    SkillPartnerEntity skillPartnerEntity=new SkillPartnerEntity();

    FeedbackRate feedbackRate=new FeedbackRate();
    List<RequirementPhase> requirementPhases = new ArrayList<>();
    ScheduleInterviewDto scheduleInterviewDto = new ScheduleInterviewDto();


    ContractStatus contractStatus=new ContractStatus();

    @BeforeEach
    void beforeTest() {
        RecommendedCandidates recommendedCandidatesDto = new RecommendedCandidates();
        recommendedCandidatesDto.setJobId("FJB-00001");
        recommendedCandidatesDto.setSkillOwnerId(1);
        recommendedCandidatesList.add(recommendedCandidatesDto);
        skillOwnerEntities.add(getSkillOwner());
        ownerSkillSetEntities.add(ownerSkillSet);
        setJob();
        setSkillSet();
    }

    private void setSkillSet() {
        ownerSkillSet.setExperience("10+");
        ownerSkillSet.setOwnerSkillSetId(1);
        ownerSkillSet.setSkillOwnerEntityId(1);
        ownerSkillSet.setOwnerSkillLevelEntity(ownerSkillLevelEntity);


    }

    private void setJob() {
        job.setJobId("FJB-00001");
        job.setMaxRate(250);
        job.setBaseRate(100);
        job.setJobLocation("NY, NY");
        job.setJobTitle("Job Job");
        job.setJobDescription("JD");
        job.setScreeningQuestions(true);
        job.setExpiryDate(new Date(2023-05-17));
        OwnerSkillYearOfExperience yoe = new OwnerSkillYearOfExperience();
        yoe.setId(1);
        yoe.setExperience("1+");
        job.setOwnerSkillYearOfExperience(yoe);




        selectionPhase.setJob(job);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setNewSlotRequested(false);
        selectionPhase.setSlotConfirmed(true);
        selectionPhase.setShowSelectionBar(true);
        selectionPhase.setAccepted(true);
        selectionPhase.setInterviewAccepted(true);
        selectionPhase.setCurrentStage(1);
        selectionPhase.setRequirementPhase(requirementPhases);
        selectionPhase.setShowTicksValues(true);
        selectionPhase.setAccepted(true);
        selectionPhase.setInterviewAccepted(true);
        selectionPhase.setJoinedOn(LocalDate.now());
        selectionPhase.setRejectedOn(LocalDate.now());

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

        feedbackRate.setId(2);
        feedbackRate.setRate(10);

        requirementPhaseNew.setRequirementId(1);
        requirementPhaseNew.setRequirementPhaseName("InitialScreening");
        requirementPhaseNew.setSkillOwnerId(1);
        requirementPhaseNew.setJobId(selectionPhase.getJob().getJobId());
        requirementPhaseNew.setStage(1);
        requirementPhaseNew.setFeedback("Good");
        requirementPhaseNew.setInterviewedBy(scheduleInterviewDto.getInterviewedBy());
        requirementPhaseNew.setDateOfInterview(scheduleInterviewDto.getDateOfInterview());
        requirementPhaseNew.setStatus(null);
        requirementPhaseNew.setCandidateRate(feedbackRate);

        requirementPhases.add(requirementPhaseNew);

        scheduleInterviewDto.setJobId(selectionPhase.getJob().getJobId());
        scheduleInterviewDto.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        scheduleInterviewDto.setInterviewedBy("Soundarya");
        scheduleInterviewDto.setStage(1);
//        scheduleInterviewDto.setTimeOfInterview(LocalTime.now());
        scheduleInterviewDto.setModeOfInterview("Remote");
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2029, 11, 29));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(01, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(01, 2, 22));
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2029, 12, 30));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(01, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(01, 2, 22));
        scheduleInterviewDto.setDateOfInterview(LocalDate.of(2029, 12, 31));
        scheduleInterviewDto.setTimeOfInterview(LocalTime.of(01, 40, 00));
        scheduleInterviewDto.setEndTimeOfInterview(LocalTime.of(01, 2, 22));

    }

    private SkillOwnerEntity getSkillOwner() {
        SkillOwnerEntity skillOwner = new SkillOwnerEntity();
        skillOwner.setSkillOwnerEntityId(1);
        skillOwner.setFirstName("Rishabh");
        skillOwner.setRateCard(100);
        skillOwner.setState("NY");
        skillOwner.setCity("NY");
        skillOwner.setExpYears(12);
        skillOwner.setExpMonths(4);
        contractStatus.setId(8);
        skillOwner.setSkillPartnerEntity(skillPartnerEntity);
        skillPartnerEntity.setSkillPartnerId(1);
        skillPartnerEntity.setMsaStatusId(contractStatus);
        skillPartnerEntity.setMsaStartDate(LocalDate.of(2022,06,19));
        skillPartnerEntity.setMsaEndDate(LocalDate.of(2024,04,15));


        return skillOwner;
    }

    @Test
    void getTalentRecommendationServiceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Mockito.when(skillOwnerRepository.getAvailableTalents()).thenReturn(skillOwnerEntities);
        TalentRecommendationServiceImpl obj=new TalentRecommendationServiceImpl();
        Method privateMethod=TalentRecommendationServiceImpl.class.getDeclaredMethod("partnerRestrict", List.class, String.class);
        privateMethod.setAccessible(true);
        Mockito.when(jobRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.ofNullable(job));
        Mockito.when(ownerSkillSetService.skillSetPercentage(Mockito.anyString(), Mockito.anyList())).thenReturn(100);
        Mockito.when(ownerSkillSetService.skillSetPercentage(Mockito.anyString(), Mockito.anyList())).thenReturn(100);
        Mockito.when(selectionPhaseRepository.findByJobIdAndSkillOwnerId("FJB-00001", 1)).thenReturn(Optional.ofNullable(selectionPhase));
        Mockito.when(locationMatchForJobService.getLocationMatchPercentage(Mockito.anyString(), Mockito.anyString())).thenReturn(100);
        assertTrue(talentRecommendationService.getTalentRecommendation("FJB-00001").isEmpty());
    }
}
