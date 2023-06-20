package com.flexcub.resourceplanning.job.controller;

import com.flexcub.resourceplanning.job.dto.*;
import com.flexcub.resourceplanning.job.entity.*;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SelectionPhaseController.class)
class SelectionPhaseControllerTest {

    SelectionPhaseResponse selectionPhaseResponse = new SelectionPhaseResponse();
    List<SelectionPhase> selectionPhases = new ArrayList<>();
    SelectionPhase selectionPhase = new SelectionPhase();
    SelectionPhaseDto selectionPhaseDto = new SelectionPhaseDto();
    List<SelectionPhaseDto> selectionPhaseDtos = new ArrayList<>();
    Job job = new Job();
    RejectCandidateDto rejectCandidateDto = new RejectCandidateDto();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    InsertRequirementPhaseDto insertRequirementPhaseDto = new InsertRequirementPhaseDto();
    ScheduleInterviewDto scheduleInterviewDto = new ScheduleInterviewDto();
    StageStatusDto statusDto = new StageStatusDto();
    List<RequirementPhase> requirementPhases = new ArrayList<>();
    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity;
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    OwnerSkillYearOfExperience ownerSkillYearOfExperience;
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();

    SlotConfirmByOwnerDto slotConfirmByOwnerDto = new SlotConfirmByOwnerDto();

    AcceptRejectDto acceptRejectDto = new AcceptRejectDto();

    FlowLockedDto flowLockedDto = new FlowLockedDto();


    RequirementPhaseDetailsDto requirementPhaseDetailsDto = new RequirementPhaseDetailsDto();

    List<WorkFlowComponent> workFlowComponents = new ArrayList<>();


    NewSlotRequestBySeekerDto newSlotRequestBySeekerDto = new NewSlotRequestBySeekerDto();

    SlotConfirmBySeekerDto slotConfirmBySeekerDto = new SlotConfirmBySeekerDto();
    List<HiringPriority> hiringPriorities = new ArrayList<>();
    HiringPriority hiringPriority = new HiringPriority();
    RescheduleDto rescheduleDto = new RescheduleDto();
    @MockBean
    private SelectionPhaseService selectionPhaseService;
    @Autowired
    private SelectionPhaseController selectionPhaseController;

    @BeforeEach
    void setSelectionPhaseTest() {
        job.setJobId("FJB_001");
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
        job.setSkillSeeker(skillSeeker);
        job.setOwnerSkillYearOfExperience(ownerSkillYearOfExperience);
        job.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        job.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        selectionPhaseResponse.setJobId(job.getJobId());
        skillOwnerEntity.setSkillOwnerEntityId(1);
        selectionPhaseResponse.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());

        selectionPhase.setJob(job);
        selectionPhase.setSkillOwnerEntity(skillOwnerEntity);
        selectionPhase.setNewSlotRequested(null);
        selectionPhase.setSlotConfirmed(null);
        selectionPhase.setShowSelectionBar(true);
        selectionPhase.setAccepted(false);
        selectionPhase.setCurrentStage(1);
        selectionPhase.getDateSlotsByOwner1();
        selectionPhase.getDateSlotsByOwner2();
        selectionPhase.getDateSlotsByOwner3();
        selectionPhase.setShowTicksValues(true);


        selectionPhases.add(selectionPhase);

        insertRequirementPhaseDto.setJobId(job.getJobId());
        insertRequirementPhaseDto.setRequirementPhases(Arrays.asList("Pre Screening", "Offer Release"));

        RequirementPhase requirementPhase = new RequirementPhase();
        requirementPhase.setStage(1);
        requirementPhase.setRequirementId(1);
        requirementPhase.setRequirementPhaseName("Pre Screening");
        requirementPhases.add(requirementPhase);

        hiringPriorities.add(hiringPriority);

        rescheduleDto.setJobId("FJB-00001");
        rescheduleDto.setSkillOwnerId(5);
        rescheduleDto.setDateOfInterview(LocalDate.now());
        rescheduleDto.setStartTime(LocalTime.now());
        rescheduleDto.setEndTime(LocalTime.now());
        rescheduleDto.setStatus("Rescheduled");
        rescheduleDto.setCurrentStage(2);


    }

    @Test
    void insertSelectionPhasesTest() {

        Mockito.when(selectionPhaseService.insertMultipleData(selectionPhaseDtos)).thenReturn(selectionPhases);
        assertEquals(200, selectionPhaseController.insertSelectionPhases(selectionPhaseDtos).getStatusCodeValue());
    }

    @Test
    void insertSelectionPhaseNullTest() {
        Mockito.when(selectionPhaseService.insertMultipleData(selectionPhaseDtos)).thenReturn(null);
        assertNull(selectionPhaseController.insertSelectionPhases(selectionPhaseDtos).getBody());
    }

    @Test
    void insertRequirementPhasesTest() {

        Mockito.when(selectionPhaseService.insertRequirementPhase(insertRequirementPhaseDto)).thenReturn(Collections.singletonList(selectionPhaseResponse));
        assertEquals(200, selectionPhaseController.insertRequirementPhases(insertRequirementPhaseDto).getStatusCodeValue());
    }

    @Test
    void getCandidatesByJobIdTest() {
        Mockito.when(selectionPhaseService.getCandidatesByJobId(selectionPhaseResponse.getJobId())).thenReturn(Collections.singletonList(selectionPhaseResponse));
        assertEquals(200, selectionPhaseController.getCandidatesByJobId(selectionPhaseResponse.getJobId()).getStatusCodeValue());
    }

    @Test
    void scheduleInterviewTest() {
        Mockito.when(selectionPhaseService.scheduleInterview(scheduleInterviewDto, false)).thenReturn(statusDto);
        assertEquals(200, selectionPhaseController.scheduleInterview(scheduleInterviewDto).getStatusCodeValue());
    }

    @Test
    void rejectCandidateTest() {
        Mockito.when(selectionPhaseService.rejectCandidate(rejectCandidateDto)).thenReturn(statusDto);
        assertEquals(200, selectionPhaseController.rejectCandidate(rejectCandidateDto).getStatusCodeValue());
    }

    @Test
    void reInitiateHiringTest() {
        Mockito.when(selectionPhaseService.reInitiateHiring(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId())).thenReturn(statusDto);
        assertEquals(200, selectionPhaseController.reInitiateHiring(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId()).getStatusCodeValue());
    }

    @Test
    void candidateInterviewDetailsTest() {
        Mockito.when(selectionPhaseService.candidateInterviewDetails(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId())).thenReturn(selectionPhaseResponse);
        assertEquals(200, selectionPhaseController.candidateInterviewDetails(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId()).getStatusCodeValue());
    }

    @Test
    void updateDetailsForParticularRoundTest() {
        Mockito.when(selectionPhaseService.updateDetailsForParticularRound(requirementPhaseDetailsDto)).thenReturn(requirementPhaseDetailsDto);
        assertEquals(200, selectionPhaseController.updateDetailsForParticularRound(requirementPhaseDetailsDto).getStatusCodeValue());
    }

    @Test
    void getSelectionPhaseTest() {
        Mockito.when(selectionPhaseService.getSlots(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId())).thenReturn(slotConfirmByOwnerDto);
        assertEquals(200, selectionPhaseController.getSelectionPhase(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId()).getStatusCodeValue());
    }

    @Test
    void updateSlotBySkillOwnerTest() {
        Mockito.when(selectionPhaseService.updateSlotBySkillOwner(slotConfirmByOwnerDto)).thenReturn(slotConfirmByOwnerDto);
        assertEquals(200, selectionPhaseController.updateSlotBySkillOwner(slotConfirmByOwnerDto).getStatusCodeValue());
    }

    @Test
    void acceptRejectBySkillOwnerTest() {
        Mockito.when(selectionPhaseService.acceptRejectBySkillOwner(acceptRejectDto)).thenReturn(acceptRejectDto);
        assertEquals(200, selectionPhaseController.acceptRejectBySkillOwner(acceptRejectDto).getStatusCodeValue());
    }

    @Test
    void selectedForRoundTest() {
        Mockito.when(selectionPhaseService.selectedForRound(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(requirementPhaseDetailsDto);
        assertEquals(200, selectionPhaseController.selectedForRound(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void isLockedTest() {
        Mockito.when(selectionPhaseService.isLocked(Mockito.anyString())).thenReturn(flowLockedDto);
        assertEquals(200, selectionPhaseController.isLocked(Mockito.anyString()).getStatusCodeValue());
    }

    @Test
    void workFlowTest() {
        Mockito.when(selectionPhaseService.getWorkflow()).thenReturn(workFlowComponents);
        assertEquals(200, selectionPhaseController.workFlow().getStatusCodeValue());
    }

    @Test
    void updateSlotConfirmedBySeekerTest() {
        Mockito.when(selectionPhaseService.slotConfirmedBySeeker(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId())).thenReturn(slotConfirmBySeekerDto);
        assertEquals(200, selectionPhaseController.updateSlotConfirmedBySeeker(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId()).getStatusCodeValue());
    }

    @Test
    void updateNewSlotBySeekerTest() {
        Mockito.when(selectionPhaseService.updateNewSlotBySeeker(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId())).thenReturn(newSlotRequestBySeekerDto);
        assertEquals(200, selectionPhaseController.updateNewSlotBySeeker(selectionPhaseResponse.getJobId(), selectionPhaseResponse.getSkillOwnerId()).getStatusCodeValue());
    }

    @Test
    void insertUniversalSlotTest() {
        Mockito.when(selectionPhaseService.updateCommonSlotByOwner(slotConfirmByOwnerDto)).thenReturn(slotConfirmByOwnerDto);
        assertEquals(200, selectionPhaseController.insertUniversalSlot(slotConfirmByOwnerDto).getStatusCodeValue());
    }

    @Test
    void acceptInterviewTest() {
        Mockito.when(selectionPhaseService.acceptInterview(acceptRejectDto)).thenReturn(statusDto);
        assertEquals(200, selectionPhaseController.acceptInterview(acceptRejectDto).getStatusCodeValue());
    }

    @Test
    void rescheduleForRoundTest() {
        Mockito.when(selectionPhaseService.rescheduleRound(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(rescheduleDto);
        assertEquals(200, selectionPhaseController.rescheduleForRound(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());

    }


}