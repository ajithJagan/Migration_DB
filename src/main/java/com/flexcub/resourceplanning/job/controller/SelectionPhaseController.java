package com.flexcub.resourceplanning.job.controller;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.dto.*;
import com.flexcub.resourceplanning.job.entity.FeedbackRate;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.entity.WorkFlowComponent;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/selectionPhase")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
public class SelectionPhaseController {

    @Autowired
    SelectionPhaseService selectionPhaseService;

    Logger logger = LoggerFactory.getLogger(SelectionPhaseController.class);

    @PostMapping(value = "/insertSelectionPhase", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SelectionPhase>> insertSelectionPhases(@RequestBody List<SelectionPhaseDto> selectionPhasesDto) {
        logger.info("SelectionPhaseController || insertSelectionPhases || Inserting SelectionPhase Details ");
        return new ResponseEntity<>(selectionPhaseService.insertMultipleData(selectionPhasesDto), HttpStatus.OK);
    }

    @PostMapping(value = "/insertRequirementPhases", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SelectionPhaseResponse>> insertRequirementPhases(@RequestBody InsertRequirementPhaseDto requirementPhases) {
        logger.info("SelectionPhaseController || insertRequirementPhases || Inserting RequirementPhases Details ");
        return new ResponseEntity<>(selectionPhaseService.insertRequirementPhase(requirementPhases), HttpStatus.OK);
    }


    @GetMapping(value = "/getCandidates", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SelectionPhaseResponse>> getCandidatesByJobId(@RequestParam String jobId) {
        logger.info("SelectionPhaseController || getCandidatesByJobId || Getting candidates list by job id ");
        return new ResponseEntity<>(selectionPhaseService.getCandidatesByJobId(jobId), HttpStatus.OK);
    }

    @PutMapping(value = "/scheduleInterview", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StageStatusDto> scheduleInterview(@RequestBody ScheduleInterviewDto scheduleInterviewDto) {
        logger.info("SelectionPhaseController || scheduleInterview || Scheduling interview for stage {}", scheduleInterviewDto.getStage());
        return new ResponseEntity<>(selectionPhaseService.scheduleInterview(scheduleInterviewDto, false), HttpStatus.OK);
    }

    @PutMapping(value = "/acceptInterview", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StageStatusDto> acceptInterview(@RequestBody AcceptRejectDto acceptRejectDto) {
        logger.info("SelectionPhaseController || acceptInterview || acceptInterview called");
        return new ResponseEntity<>(selectionPhaseService.acceptInterview(acceptRejectDto), HttpStatus.OK);
    }

    @PutMapping(value = "/rejectCandidate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StageStatusDto> rejectCandidate(@RequestBody RejectCandidateDto rejectCandidateDto) {
        logger.info("SelectionPhaseController || rejectCandidate || Rejecting candidate ");
        return new ResponseEntity<>(selectionPhaseService.rejectCandidate(rejectCandidateDto), HttpStatus.OK);
    }

    @PutMapping(value = "/reInitiateHiring", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StageStatusDto> reInitiateHiring(String jobId, int skillOwnerId) {
        logger.info("SelectionPhaseController || reInitiateHiring || RreInitiating Hiring ");
        return new ResponseEntity<>(selectionPhaseService.reInitiateHiring(jobId, skillOwnerId), HttpStatus.OK);
    }

    @GetMapping(value = "/candidateInterviewDetails", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SelectionPhaseResponse> candidateInterviewDetails(String jobId, int skillOwnerId) {
        logger.info("SelectionPhaseController || candidateInterviewDetails called");
        return new ResponseEntity<>(selectionPhaseService.candidateInterviewDetails(jobId, skillOwnerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getWorkFlow", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<WorkFlowComponent>> workFlow() {
        logger.info("SelectionPhaseController || workFlow called");
        return new ResponseEntity<>(selectionPhaseService.getWorkflow(), HttpStatus.OK);
    }

    @PostMapping(value = "/sendMail", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void shortlistingMail(@RequestParam String jobId) {
        try {
            selectionPhaseService.shortlistingMail(jobId);
        } catch (Exception e) {
            throw new ServiceException();
        }
    }

    @PutMapping(value = "updateDetailsForParticularRound", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RequirementPhaseDetailsDto> updateDetailsForParticularRound(@RequestBody RequirementPhaseDetailsDto requirementPhaseDetailsDto) {
        logger.info("SelectionPhaseController || updateDetailsForParticularRound || Updating the Details for Particular round ");
        return new ResponseEntity<>(selectionPhaseService.updateDetailsForParticularRound(requirementPhaseDetailsDto), HttpStatus.OK);
    }

    @GetMapping(value = "/getSlots", produces = {"application/json"})
    public ResponseEntity<SlotConfirmByOwnerDto> getSelectionPhase(String jobId, int skillOwnerId) {
        logger.info("SelectionPhaseController || getSelectionPhase || Getting the SlotDetails from SelectionPhase");
        return new ResponseEntity<>(selectionPhaseService.getSlots(jobId, skillOwnerId), HttpStatus.OK);
    }

    @PutMapping(value = "/slotsByOwner", produces = {"application/json"})
    public ResponseEntity<SlotConfirmByOwnerDto> updateSlotBySkillOwner(@RequestBody SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        logger.info("SelectionPhaseController || updateSlotBySkillOwner || Update The Slots By SkillOwner");
        return new ResponseEntity<>(selectionPhaseService.updateSlotBySkillOwner(slotConfirmByOwnerDto), HttpStatus.OK);
    }

    @PutMapping(value = "/slotConfirmationBySeeker", produces = {"application/json"})
    public ResponseEntity<SlotConfirmBySeekerDto> updateSlotConfirmedBySeeker(String jobId, int skillOwnerId) {
        logger.info("SelectionPhaseController || updateSlotConfirmedBySeeker || Confirm Slots By SkillSeeker");
        return new ResponseEntity<>(selectionPhaseService.slotConfirmedBySeeker(jobId, skillOwnerId), HttpStatus.OK);
    }

    @PutMapping(value = "/newSlotRequest", produces = {"application/json"})
    public ResponseEntity<NewSlotRequestBySeekerDto> updateNewSlotBySeeker(String jobId, int skillOwnerId) {
        logger.info("SelectionPhaseController || updateNewSlotBySeeker || Update NewSlots By SkillSeeker");
        return new ResponseEntity<>(selectionPhaseService.updateNewSlotBySeeker(jobId, skillOwnerId), HttpStatus.OK);
    }

    @PutMapping(value = "/acceptRejectBySkillOwner", produces = {"application/json"})
    public ResponseEntity<AcceptRejectDto> acceptRejectBySkillOwner(@RequestBody AcceptRejectDto acceptRejectDto) {
        logger.info("SelectionPhaseController || acceptRejectBySkillOwner || Accept Or Reject By SkillOwner");
        return new ResponseEntity<>(selectionPhaseService.acceptRejectBySkillOwner(acceptRejectDto), HttpStatus.OK);
    }

    @PutMapping(value = "/selectedForRound", produces = {"application/json"})
    public ResponseEntity<RequirementPhaseDetailsDto> selectedForRound(@RequestParam String jobId, int skillOwnerId, int stage) {
        logger.info("SelectionPhaseController || selectedForRound || Update selectedForRound By SkillSeeker");
        return new ResponseEntity<>(selectionPhaseService.selectedForRound(jobId, skillOwnerId, stage), HttpStatus.OK);

    }

    @GetMapping(value = "/isLocked", produces = {"application/json"})
    public ResponseEntity<FlowLockedDto> isLocked(String jobId) {
        logger.info("SelectionPhaseController || isLocked || Checking if locked");
        return new ResponseEntity<>(selectionPhaseService.isLocked(jobId), HttpStatus.OK);
    }

    @PostMapping(value = "/insertCommonSlotByOwner", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SlotConfirmByOwnerDto> insertUniversalSlot(@RequestBody SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        logger.info("SelectionPhaseController || insertUniversalSlot || Inserting Slots");
        return new ResponseEntity<>(selectionPhaseService.updateCommonSlotByOwner(slotConfirmByOwnerDto), HttpStatus.OK);
    }

    @PutMapping(value = "/rateForSkillOwner", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<RateApprovalDto>> skillOwnerRate(@RequestBody List<RateApprovalDto> rateApprovalDto) {
        logger.info("SelectionPhaseController || skillOwnerRate || Update rate for particular SkillOwner");
        return new ResponseEntity<>(selectionPhaseService.updateSkillOwnerRate(rateApprovalDto), HttpStatus.OK);
    }

    @GetMapping(value = "/getFeedback", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<FeedbackRate>> feedback() {
        logger.info("SelectionPhaseController || getCandidateFeedback || Getting candidates feedback");
        return new ResponseEntity<>(selectionPhaseService.getFeedback(), HttpStatus.OK);
    }

    @PutMapping(value = "/rescheduleForRound", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RescheduleDto> rescheduleForRound(@RequestParam String jobId, int skillOwnerId, int stage) {
        logger.info("SelectionPhaseController || reSchedule for Round || reScheduled the round");
        return new ResponseEntity<>(selectionPhaseService.rescheduleRound(jobId, skillOwnerId, stage), HttpStatus.OK);
    }

    @GetMapping(value = "/shortlistStatus", produces = {"application/json"})
    public ResponseEntity<Boolean> shortlistStatus(int skillOwnerEntityId) {
        logger.info("SkillOwnerController || shortlistStatusList || Getting the shortlistStatusList ");
        return new ResponseEntity<>(selectionPhaseService.shortlistStatus(skillOwnerEntityId), HttpStatus.OK);
    }


}