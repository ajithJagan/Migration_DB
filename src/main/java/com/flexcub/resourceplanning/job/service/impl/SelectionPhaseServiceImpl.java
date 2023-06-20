package com.flexcub.resourceplanning.job.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.dto.*;
import com.flexcub.resourceplanning.job.entity.*;
import com.flexcub.resourceplanning.job.repository.*;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.registration.entity.AccountVerificationEmailContext;
import com.flexcub.resourceplanning.registration.service.FlexcubEmailService;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerSlotsEntity;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerSlotsRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class SelectionPhaseServiceImpl implements SelectionPhaseService {


    private static final LocalTime START_TIME = LocalTime.of(9, 0); // Working hours start at 9 AM
    private static final LocalTime END_TIME = LocalTime.of(18, 0); // Working hours end at 6 PM
    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;
    @Autowired
    SkillOwnerSlotsRepository skillOwnerSlotsRepository;
    @Autowired
    RequirementPhaseRepository requirementPhaseRepository;
    @Autowired
    JobWorkFlowComponentRepository jobWorkFlowComponentRepository;
    @Autowired
    WorkFlowRepository workFlowRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;
    @Autowired
    SkillSeekerMsaRepository skillSeekerMsaRepository;
    @Autowired
    PoRepository poRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    Logger logger = LoggerFactory.getLogger(SelectionPhaseServiceImpl.class);
    @Autowired
    SkillOwnerService skillOwnerService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FlexcubEmailService emailService;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Value("${flexcub.baseURLHiringProcess}")
    private String baseURLHiringProcess;
    @Value("${flexcub.baseURLRejectionMail}")
    private String baseURLRejectionMail;
    @Value("${flexcub.baseURLScheduleMail}")
    private String baseURLScheduleMail;
    @Value("${flexcub.baseURLSelectedForRoundMail}")
    private String baseURLSelectedForRoundMail;
    @Value("${flexcub.baseURLNewSlotRequest}")
    private String baseURLNewSlotRequest;
    @Value("${job.hiring}")
    private String inHiring;
    @Value("${job.scheduled}")
    private String scheduled;
    @Value("${job.notCleared}")
    private String notCleared;
    @Value("${job.cleared}")
    private String cleared;
    @Value("${job.reScheduled}")
    private String ReScheduled;
    @Value("${schedule.interview}")
    private String modeOfInterview;
    @Value("${flexcub.baseURLReScheduleMail}")
    private String baseURLReScheduleMail;
    @Value("${spring.mvc.format.time}")
    private String timeType;

    @Value("${flexcub.baseurlLogin}")
    private String baseurlLogin;

    private static boolean isWithinWorkingHours(ScheduleInterviewDto scheduleInterviewDto) {
        return !scheduleInterviewDto.getTimeOfInterview().isBefore(START_TIME) && !scheduleInterviewDto.getEndTimeOfInterview().isAfter(END_TIME);
    }

    @Transactional
    @Override
    public List<SelectionPhase> getSelectionPhase() {
        List<SelectionPhase> selectionPhaseList;
        try {
            selectionPhaseList = selectionPhaseRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
        logger.info("SelectionPhaseServiceImpl || getSelectionPhase || Get all the SelectionPhase Details");
        return selectionPhaseList;
    }

    @Override
    @Transactional
    public List<SelectionPhase> insertMultipleData(List<SelectionPhaseDto> selectionPhases) {
        List<SelectionPhase> selectionPhaseList = new ArrayList<>();
        if (!selectionPhases.isEmpty()) {
            for (SelectionPhaseDto selectionPhase : selectionPhases) {
                Optional<Job> byJobId = jobRepository.findByJobId(selectionPhase.getJob().getJobId());

                if (byJobId.isPresent()) {

                    if (selectionPhaseRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()).isEmpty()) {
                        Optional<List<SelectionPhase>> phaseList = selectionPhaseRepository.findBySkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                        boolean exists = false;
                        for (SelectionPhase phaseL : phaseList.get()) {
                            if (byJobId.get().getSkillSeeker().getId() == phaseL.getJob().getSkillSeeker().getId()) {
                                exists = true;

                            }
                        }
                        if (!exists) {
                            if (phaseList.get().size() <= 2) {
                                SelectionPhase phase = new SelectionPhase();
                                modelMapper.map(selectionPhase, phase);
                                SkillOwnerEntity bySkillOwnerEntityId = skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                                phase.setRate(bySkillOwnerEntityId.getRateCard());

                                SelectionPhase phaseResponse = selectionPhaseRepository.save(phase);
                                selectionPhaseList.add(phaseResponse);
                                notificationService.shortlistBySeekerNotification(phaseResponse);
                            } else {
                                logger.info("Skill owner exceeded");
                                throw new ServiceException(USER_LIMIT_EXCEEDED.getErrorCode(), USER_LIMIT_EXCEEDED.getErrorDesc());
                            }
                        } else {
                            throw new ServiceException(USER_LIMIT_EXCEEDED.getErrorCode(), USER_LIMIT_EXCEEDED.getErrorDesc());
                        }
                    } else {
                        logger.info("User with skillOwnerId {} already exist", selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                        throw new ServiceException(EXISTING_CANDIDATE.getErrorCode(), "User with skillOwnerId already exist");
                    }
                    Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
                    job.ifPresent(value -> {
                        value.setStatus(inHiring);
                        jobRepository.save(value);
                    });
                } else {
                    logger.info("SelectionPhaseServiceImpl || insertMultipleData || Invalid Job {}", byJobId);
                    throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
                }

            }

            logger.info("SelectionPhaseServiceImpl || insertMultipleData || Inserting SelectionPhase");
            return selectionPhaseList;

        } else {
            throw new ServiceException(BODY_EMPTY.getErrorCode(), BODY_EMPTY.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public List<SelectionPhaseResponse> insertRequirementPhase(InsertRequirementPhaseDto insertRequirementPhase) {

        Optional<List<SelectionPhase>> selectionPhaseList = Optional.ofNullable(selectionPhaseRepository.findByJobJobId(insertRequirementPhase.getJobId()));
        Optional<JobWorkFlowComponent> workFlowComponent = jobWorkFlowComponentRepository.findByJobId(insertRequirementPhase.getJobId());
        try {
            if (selectionPhaseList.isPresent() && !selectionPhaseList.get().isEmpty()) {
                if (!workFlowComponent.isPresent()) {
                    JobWorkFlowComponent flowComponent = new JobWorkFlowComponent();
                    flowComponent.setFlow(listToString(insertRequirementPhase.getRequirementPhases()));
                    flowComponent.setJobId(insertRequirementPhase.getJobId());
                    flowComponent.setLocked(false);
                    jobWorkFlowComponentRepository.save(flowComponent);

                    return addDataToDb(insertRequirementPhase, selectionPhaseList.get());
                } else {
                    if (workFlowComponent.get().getLocked()) {
                        List<SelectionPhase> selectionPhases = new ArrayList<>();

                        for (int i = 0; i < selectionPhaseList.get().size(); i++) {
                            if ((!requirementPhaseRepository.ifRecordExistByOwnerId(selectionPhaseList.get().get(i).getSkillOwnerEntity().getSkillOwnerEntityId(), insertRequirementPhase.getJobId()))) {
                                selectionPhases.add(selectionPhaseList.get().get(i));
                            }
                        }
                        insertRequirementPhase.setRequirementPhases(Arrays.asList(workFlowComponent.get().getFlow().split(",")));
                        if (!selectionPhaseList.get().isEmpty()) {
                            return addDataToDb(insertRequirementPhase, selectionPhases);
                        } else {
                            throw new ServiceException();
                        }
                    } else {
                        Optional<List<RequirementPhase>> phase = requirementPhaseRepository.findByJobId(insertRequirementPhase.getJobId());
                        workFlowComponent.get().setFlow(listToString(insertRequirementPhase.getRequirementPhases()));
                        jobWorkFlowComponentRepository.save(workFlowComponent.get());
                        if (phase.isPresent()) {
                            requirementPhaseRepository.deleteByJobId(insertRequirementPhase.getJobId());
                        }
                        return addDataToDb(insertRequirementPhase, selectionPhaseList.get());
                    }
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(NO_CANDIDATES_SHORTLISTED.getErrorCode(), NO_CANDIDATES_SHORTLISTED.getErrorDesc());
        } catch (ServiceException e) {
            throw new ServiceException(FLOW_LOCKED.getErrorCode(), FLOW_LOCKED.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    /**
     * @param scheduleInterviewDto to schedule
     * @return responseEntity String
     */
    @Override
    @Transactional
    public StageStatusDto scheduleInterview(ScheduleInterviewDto scheduleInterviewDto, Boolean isAutoScheduled) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId());

            if (selectionPhase.isPresent() && (selectionPhase.get().getAccepted() && selectionPhase.get().getInterviewAccepted())
                    || selectionPhase.get().getAccepted() && !selectionPhase.get().getInterviewAccepted()) {
                SelectionPhase selectionPhase1 = selectionPhase.get();
                Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId(), scheduleInterviewDto.getStage());

                validTimeCheck(scheduleInterviewDto);

                scheduleInterviewCheck(scheduleInterviewDto.getDateOfInterview(), scheduleInterviewDto.getTimeOfInterview(), scheduleInterviewDto.getSkillOwnerId());
                if (requirementPhase.isPresent()
                        && (null == requirementPhase.get().getStatus() || requirementPhase.get().getStatus().equalsIgnoreCase(scheduled))) {
                    RequirementPhase requirementPhaseNew = requirementPhase.get();
                    requirementPhaseNew.setInterviewedBy(scheduleInterviewDto.getInterviewedBy());
                    requirementPhaseNew.setDateOfInterview(scheduleInterviewDto.getDateOfInterview());
                    requirementPhaseNew.setTimeOfInterview(scheduleInterviewDto.getTimeOfInterview());
                    requirementPhaseNew.setEndTimeOfInterview(scheduleInterviewDto.getEndTimeOfInterview());
                    requirementPhaseNew.setStatus(scheduled);
                    requirementPhaseNew.setModeOfInterview(scheduleInterviewDto.getModeOfInterview());

                    RequirementPhase phase = requirementPhaseRepository.save(requirementPhaseNew);

                    //Updating flow
                    Optional<JobWorkFlowComponent> flowComponent = jobWorkFlowComponentRepository.findByJobId(scheduleInterviewDto.getJobId());
                    if (flowComponent.isPresent() && !flowComponent.get().getLocked()) {
                        flowComponent.get().setLocked(true);
                        jobWorkFlowComponentRepository.save(flowComponent.get());
                    }
                    selectionPhase.get().setDateSlotsByOwner1(null);
                    selectionPhase.get().setDateSlotsByOwner2(null);
                    selectionPhase.get().setDateSlotsByOwner3(null);
                    selectionPhase.get().setTimeSlotsByOwner1(null);
                    selectionPhase.get().setEndTimeSlotsByOwner1(null);
                    selectionPhase.get().setTimeSlotsByOwner1(null);
                    selectionPhase.get().setTimeSlotsByOwner2(null);
                    selectionPhase.get().setEndTimeSlotsByOwner2(null);
                    selectionPhase.get().setTimeSlotsByOwner3(null);
                    selectionPhase.get().setEndTimeSlotsByOwner3(null);
                    selectionPhase.get().setSlotConfirmed(null);
                    selectionPhase.get().setInterviewAccepted(false);
                    selectionPhaseRepository.save(selectionPhase.get());

                    //Notification
                    if (!isAutoScheduled && phase.getStatus().equalsIgnoreCase(scheduled)) {
                        scheduleInterviewDto.setJobId(selectionPhase.get().getJob().getJobId());
                        Notification notificationDto = new Notification();
                        notificationDto.setDateOfInterview(phase.getDateOfInterview());
                        notificationDto.setTimeOfInterview(phase.getTimeOfInterview());
                        notificationDto.setStage(phase.getStage());
                        notificationDto.setRequirementPhaseName(phase.getRequirementPhaseName());
                        ContentEntity content = notificationService.scheduleInterviewNotification(scheduleInterviewDto, notificationDto);
                        logger.info("SelectionPhaseServiceImpl || Content || scheduleInterview {}", content.getTitle());
                    }
                    StageStatusDto statusDto = new StageStatusDto();
                    statusDto.setStatus(requirementPhaseNew.getStatus());
                    statusDto.setJobId(scheduleInterviewDto.getJobId());
                    statusDto.setSkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId());
                    statusDto.setStage(phase.getStage());
                    logger.info("SelectionPhaseServiceImpl || scheduleInterview || scheduling The Interview");

                    return statusDto;

                } else {
                    throw new ServiceException(INVALID_REQUIREMENT_PHASE.getErrorCode(), INVALID_REQUIREMENT_PHASE.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_JOB_SKILL_OWNER_COMBO.getErrorCode(), INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    public ScheduleInterviewDto validTimeCheck(ScheduleInterviewDto scheduleInterviewDto) {

        if (!isWithinWorkingHours(scheduleInterviewDto)) {
            throw new ServiceException(" Invalid time 9am to 6pm");
        }
        return modelMapper.map(scheduleInterviewDto, ScheduleInterviewDto.class);
    }

    private void scheduleInterviewCheck(LocalDate localDate, LocalTime localTime, int skillOwnerId) {
        try {
            List<RequirementPhase> byDateAndTime = (requirementPhaseRepository.findByDateAndTime(skillOwnerId));

            if (!localDate.isEqual(LocalDate.now()) && (!localDate.isBefore(LocalDate.now()))) {
                for (RequirementPhase requirementPhase : byDateAndTime) {
                    try {
                        if (null != requirementPhase.getDateOfInterview() && null != requirementPhase.getTimeOfInterview() && (requirementPhase.getDateOfInterview().isEqual(localDate) && requirementPhase.getTimeOfInterview().equals(localTime))) {
                            throw new ServiceException();
                        }
                    } catch (NullPointerException e) {
                        throw new NullPointerException();
                    } catch (ServiceException e) {
                        throw new ServiceException(SLOT_ALREADY_BOOKED.getErrorCode(), SLOT_ALREADY_BOOKED.getErrorDesc());
                    }
                }
            } else {
                throw new ServiceException(SLOT_ALREADY_BOOKED.getErrorCode(), SLOT_ALREADY_BOOKED.getErrorDesc());
            }
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public StageStatusDto acceptInterview(AcceptRejectDto acceptRejectDto) {
        try {
            Optional<SelectionPhase> phase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(
                    acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId());

            if (acceptRejectDto.getAccepted()) {
                if (phase.isPresent()) {
                    SelectionPhase selectionPhase = phase.get();
                    selectionPhase.setInterviewAccepted(true);
                    selectionPhaseRepository.save(selectionPhase);
                    StageStatusDto statusDto = createStageStatusDto(acceptRejectDto, selectionPhase);
                    logger.info("SelectionPhaseServiceImpl || reInitiateHiring || Re-Initiate Hiring for SkillOwner");
                        notificationService.acceptNotificationForSeekerAndOwner(acceptRejectDto, selectionPhase.getCurrentStage());
                    return statusDto;
                }
            } else {
                if (phase.isPresent()) {
                    SelectionPhase selectionPhase = phase.get();
                    notificationService.newSlotByOwnerNotification(acceptRejectDto, selectionPhase.getCurrentStage());

                    StageStatusDto statusDto = createStageStatusDto(acceptRejectDto, selectionPhase);
                    return statusDto;
                }
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
        return null;
    }

    private StageStatusDto createStageStatusDto(AcceptRejectDto acceptRejectDto, SelectionPhase selectionPhase) {
        StageStatusDto statusDto = new StageStatusDto();
        statusDto.setJobId(acceptRejectDto.getJobId());
        statusDto.setSkillOwnerEntityId(acceptRejectDto.getSkillOwnerEntityId());
        statusDto.setStatus(scheduled);
        statusDto.setStage(selectionPhase.getCurrentStage());
        statusDto.setAccepted(acceptRejectDto.getAccepted());
        return statusDto;
    }




    @Override
    @Transactional
    public StageStatusDto rejectCandidate(RejectCandidateDto rejectCandidateDto) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(rejectCandidateDto.getJobId(), rejectCandidateDto.getSkillOwnerId());
            if (selectionPhase.isPresent()) {
                SelectionPhase selectionPhase1 = selectionPhase.get();
                Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(rejectCandidateDto.getJobId(), rejectCandidateDto.getSkillOwnerId(), rejectCandidateDto.getStage());
                if (requirementPhase.isPresent()) {
                    RequirementPhase requirementPhaseNew = requirementPhase.get();
                    requirementPhaseNew.setStatus(notCleared);

//                     60 days cool down
                    Optional<SelectionPhase> selectionPhase2 = selectionPhaseRepository.findByJobIdAndSkillOwnerId(rejectCandidateDto.getJobId(), rejectCandidateDto.getSkillOwnerId());
                    LocalDate localDate = LocalDate.now();
                    if (requirementPhaseNew.getStatus().equals(notCleared)) {
                        selectionPhase2.get().setRejectedOn(localDate);
                        selectionPhaseRepository.saveAndFlush(selectionPhase2.get());
                    } else {
                        selectionPhaseRepository.saveAndFlush(selectionPhase2.get());
                    }


                    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                    emailContext.baseurlLogin(baseurlLogin);
                    emailContext.rejectionMail(emailContext, selectionPhase1);
                    try {
                        emailContext.baseURLRejectionMail(baseURLRejectionMail);
                        emailService.rejectionMail(emailContext);

                        logger.info("Rejection Mail sent");
                    } catch (MessagingException e) {
                        throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Rejection Mail not Sent");
                    }
                    requirementPhaseRepository.save(requirementPhaseNew);
                    notificationService.rejectedNotification(selectionPhase1);
                    StageStatusDto statusDto = new StageStatusDto();
                    statusDto.setStatus(requirementPhaseNew.getStatus());
                    statusDto.setJobId(rejectCandidateDto.getJobId());
                    statusDto.setSkillOwnerEntityId(rejectCandidateDto.getSkillOwnerId());
                    statusDto.setStage(rejectCandidateDto.getStage());
                    logger.info("SelectionPhaseServiceImpl || rejectCandidate || Rejecting The Candidate");
                    return statusDto;
                } else {
                    throw new ServiceException(INVALID_REQUIREMENT_PHASE.getErrorCode(), INVALID_REQUIREMENT_PHASE.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_JOB_SKILL_OWNER_COMBO.getErrorCode(), INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
            }
        } catch (ServiceException e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException(e.getErrorCode(), e.getErrorDesc());
        }
    }

    /**
     * @param jobId id of job
     * @return List of SelectionPhases
     */
    @Override
    @Transactional
    public List<SelectionPhaseResponse> getCandidatesByJobId(String jobId) {
        try {
            Optional<List<SelectionPhase>> selectionPhases = Optional.ofNullable(selectionPhaseRepository.findByJobJobId(jobId));
            List<SelectionPhaseResponse> selectionPhaseResponses = new ArrayList<>();
            if (!selectionPhases.get().isEmpty()) {
                List<SelectionPhase> selectionPhaseList = selectionPhases.get();
                selectionPhaseList.forEach(selectionPhase -> {
                    selectionPhase.setCurrentStage(currentStage(selectionPhase));
                    selectionPhaseRepository.saveAndFlush(selectionPhase);
                    String skillOwnerFullName = selectionPhase.getSkillOwnerEntity().getFirstName() + " " + selectionPhase.getSkillOwnerEntity().getLastName();
                    SelectionPhaseResponse selectionPhaseResponse = new SelectionPhaseResponse();
                    BeanUtils.copyProperties(selectionPhase, selectionPhaseResponse, NullPropertyName.getNullPropertyNames(selectionPhase));
                    selectionPhaseResponse.setJobId(selectionPhase.getJob().getJobId());
                    selectionPhaseResponse.setJobTitle(selectionPhase.getJob().getJobTitle());
                    selectionPhaseResponse.setSkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                    selectionPhaseResponse.setSkillOwnerName(skillOwnerFullName);
                    selectionPhaseResponse.setRate(selectionPhase.getRate());

                    Optional<SkillSeekerMSAEntity> msaEntity = Optional.ofNullable(skillSeekerMsaRepository.findByOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));
                    if (msaEntity.isPresent()) {
                        selectionPhaseResponse.setMsaCreated(true);
                    }

                    Optional<StatementOfWorkEntity> sowEntity = Optional.ofNullable(statementOfWorkRepository.findByOwner(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));
                    if (sowEntity.isPresent()) {
                        selectionPhaseResponse.setSowCreated(true);
                    }
                    Optional<PoEntity> poEntity = poRepository.findByDeleteAtNull(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                    if (poEntity.isPresent()) {
                        selectionPhaseResponse.setPoCreated(true);
                    }


                    //TODO - Admin page created mean's need to code poEntity condition and getPoCreated

                    List<RequirementPhase> requirementPhaseList = new ArrayList<>(selectionPhase.getRequirementPhase());

                    requirementPhaseList.sort(Comparator.comparingInt(RequirementPhase::getStage));
                    selectionPhaseResponse.setRequirementPhaseList(requirementPhaseList);

                    selectionPhaseResponse.setImageAvailable(selectionPhase.getSkillOwnerEntity().isImageAvailable());
                    selectionPhaseResponse.setResumeAvailable(selectionPhase.getSkillOwnerEntity().isResumeAvailable());
                    selectionPhaseResponse.setInterviewAccepted(selectionPhase.getInterviewAccepted());
                    selectionPhaseResponses.add(selectionPhaseResponse);
                });
                logger.info("SelectionPhaseServiceImpl || getCandidatesByJobId || Get CandidatesByJobId Details");
                return selectionPhaseResponses;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), "Invalid Job Id");
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data not saved");
        }

    }

    /**
     * @param jobId        id
     * @param skillOwnerId id
     * @return ResponseEntity as String
     */
    @Override
    @Transactional
    public StageStatusDto reInitiateHiring(String jobId, int skillOwnerId) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);

            Optional<List<SelectionPhase>> selectionPhases = selectionPhaseRepository.findBySkillOwnerId(skillOwnerId);

            selectionPhases.get().forEach(phase -> {
                if (phase.getAccepted() && null == phase.getRejectedOn()) {
                    throw new ServiceException(SKILL_OWNER_JOB.getErrorCode(), SKILL_OWNER_JOB.getErrorDesc());
                }
            });

            if (selectionPhase.isPresent()) {
                List<RequirementPhase> requirementPhaseList = selectionPhase.get().getRequirementPhase();
                requirementPhaseList.forEach(requirementPhase -> {
                    requirementPhase.setStatus(null);
                    requirementPhase.setInterviewedBy(null);
                    requirementPhase.setComments(null);
                    requirementPhase.setDateOfInterview(null);
                    requirementPhase.setTimeOfInterview(null);
                    requirementPhase.setFeedback(null);
                    requirementPhase.setCandidateRate(null);
                    requirementPhaseRepository.save(requirementPhase);
                });
                notificationService.reinitiateNotification(selectionPhase.get());
                selectionPhase.get().setInterviewAccepted(false);
                selectionPhase.get().setAccepted(false);
                selectionPhase.get().setRejectedOn(null);
                selectionPhaseRepository.save(selectionPhase.get());

                StageStatusDto statusDto = new StageStatusDto();
                statusDto.setJobId(jobId);
                statusDto.setSkillOwnerEntityId(skillOwnerId);
                logger.info("SelectionPhaseServiceImpl || reInitiateHiring || ReInitiating Hiring process");
                return statusDto;
            } else {
                throw new ServiceException(INVALID_JOB_SKILL_OWNER_COMBO.getErrorCode(), INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
            }
        } catch (Exception e) {

            throw new ServiceException(SKILL_OWNER_JOB.getErrorCode(), SKILL_OWNER_JOB.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public SelectionPhaseResponse candidateInterviewDetails(String jobId, int skillOwnerId) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
            SelectionPhaseResponse selectionPhaseResponse = new SelectionPhaseResponse();
            if (selectionPhase.isPresent()) {
                selectionPhase.get().setCurrentStage(currentStage(selectionPhase.get()));
                selectionPhaseRepository.saveAndFlush(selectionPhase.get());

                List<RequirementPhase> requirementPhaseList = new ArrayList<>(selectionPhase.get().getRequirementPhase());
                requirementPhaseList.sort(Comparator.comparingInt(RequirementPhase::getStage));
                selectionPhaseResponse.setRequirementPhaseList(requirementPhaseList);

                BeanUtils.copyProperties(selectionPhase.get(), selectionPhaseResponse, NullPropertyName.getNullPropertyNames(selectionPhase.get()));

                selectionPhaseResponse.setSkillOwnerId(selectionPhase.get().getSkillOwnerEntity().getSkillOwnerEntityId());
                selectionPhaseResponse.setSkillOwnerName(selectionPhase.get().getSkillOwnerEntity().getFirstName() + " " + selectionPhase.get().getSkillOwnerEntity().getLastName());
                selectionPhaseResponse.setJobId(selectionPhase.get().getJob().getJobId());
                selectionPhaseResponse.setJobTitle(selectionPhase.get().getJob().getJobTitle());
                selectionPhaseResponse.setCurrentStage(selectionPhase.get().getCurrentStage());
                selectionPhaseResponse.setShowSelectionBar(true);
                selectionPhaseResponse.setShowTicksValues(true);

                String years = selectionPhase.get().getSkillOwnerEntity().getExpYears() == 1 || selectionPhase.get().getSkillOwnerEntity().getExpYears() == 0 ? selectionPhase.get().getSkillOwnerEntity().getExpYears() + " Year, " : selectionPhase.get().getSkillOwnerEntity().getExpYears() + " Years, ";
                String months = selectionPhase.get().getSkillOwnerEntity().getExpMonths() == 1 || selectionPhase.get().getSkillOwnerEntity().getExpMonths() == 0 ? selectionPhase.get().getSkillOwnerEntity().getExpMonths() + " Month" : selectionPhase.get().getSkillOwnerEntity().getExpMonths() + " Months";
                selectionPhaseResponse.setExperience(years + months);

                logger.info("SelectionPhaseServiceImpl || candidateInterviewDetails || Showing Candidate Interview Details ");
                return selectionPhaseResponse;
            } else {
                throw new ServiceException(INVALID_JOB_SKILL_OWNER_COMBO.getErrorCode(), INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
            }

        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }
    }

    /**
     * @return list of workflow
     */
    @Override
    public List<WorkFlowComponent> getWorkflow() {
        try {
            logger.info("SelectionPhaseServiceImpl || getWorkflow || Get the workflow Details");
            return workFlowRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
    }

    /**
     * @param selectionPhase object
     * @return stage
     */
    @Override
    @Transactional
    public int currentStage(SelectionPhase selectionPhase) {
        try {
            List<RequirementPhase> requirementPhaseList = selectionPhase.getRequirementPhase();
            requirementPhaseList.sort(Comparator.comparingInt(RequirementPhase::getStage));
            for (RequirementPhase requirementPhase : requirementPhaseList) {
                if (null == requirementPhase.getStatus() || requirementPhase.getStatus().equalsIgnoreCase(notCleared) || requirementPhase.getStatus().equalsIgnoreCase(scheduled) || requirementPhase.getStatus().equalsIgnoreCase(ReScheduled)) {
                    return requirementPhase.getStage();
                }
            }
            return requirementPhaseList.size();
        } catch (Exception e) {
            logger.error("Error in finding current stage");
            return 1;
        }
        //default return
    }

    @Transactional
    @Override
    public void shortlistingMail(String jobId) {

        Optional<List<SelectionPhase>> selectionPhase = Optional.of(selectionPhaseRepository.findByJobJobId(jobId));
        List<SelectionPhase> selectionPhaseList = selectionPhase.get();
        if (!selectionPhase.get().isEmpty()) {
            selectionPhaseList.forEach(selectionPhases -> {
                if (!(selectionPhases.getMailSent())) {
                    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                    emailContext.baseurlLogin(baseurlLogin);
                    emailContext.hiringProcess(emailContext, selectionPhases);
                    try {
                        emailContext.baseURLHiringProcess(baseURLHiringProcess);
                        emailService.hiringProcesss(emailContext);
                        selectionPhases.setMailSent(true);
                        selectionPhaseRepository.save(selectionPhases);
                        logger.info("Hiring Mail sent");
                    } catch (Exception e) {
                        throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Hiring Mail not sent");
                    }
                }
            });
        } else {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    /**
     * @param jobId        id
     * @param skillOwnerId id
     * @param stage        id
     * @return phase details
     */
    @Override
    @Transactional
    public RequirementPhaseDetailsDto selectedForRound(String jobId, int skillOwnerId, int stage) {
        RequirementPhaseDetailsDto detailsDto = new RequirementPhaseDetailsDto();

        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
        Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(jobId, skillOwnerId, stage);
        Optional<List<RequirementPhase>> phaseList = requirementPhaseRepository.findByJobIdSkillOwnerId(jobId, skillOwnerId);
        Optional<SkillOwnerSlotsEntity> bySkillOwnerEntityId = Optional.ofNullable(skillOwnerService.findBySkillOwnerEntityId(skillOwnerId));

        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        String format = time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

        if (requirementPhase.isPresent() && selectionPhase.isPresent()) {
            if (!(requirementPhase.get().getEndTimeOfInterview().isBefore(LocalTime.parse(format)) && requirementPhase.get().getDateOfInterview().isEqual(LocalDate.now())) ||
                    (requirementPhase.get().getDateOfInterview().isBefore(LocalDate.now()))) {
                if (bySkillOwnerEntityId.get().getDateSlotsByOwner3() != null) {
                    selectionPhase.get().setCurrentStage(stage + 1);
                    requirementPhase.get().setStatus(cleared);

                    RequirementPhase phase = requirementPhaseRepository.save(requirementPhase.get());

                    SelectionPhase phase1 = selectionPhaseRepository.save(selectionPhase.get());
                    notificationService.qualifiedNotification(phase1, requirementPhase.get());

                    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                    ScheduleInterviewDto interviewDto = new ScheduleInterviewDto();
                    interviewDto.setModeOfInterview(requirementPhase.get().getModeOfInterview());
                    interviewDto.setDateOfInterview(requirementPhase.get().getDateOfInterview());
                    interviewDto.setTimeOfInterview(requirementPhase.get().getTimeOfInterview());
                    emailContext.baseurlLogin(baseurlLogin);
                    emailContext.selectedForRound(emailContext, selectionPhase.get(), interviewDto, requirementPhase.get());
                    try {

                        emailContext.baseURLSelectedForRoundMail(baseURLSelectedForRoundMail);
                        emailService.selectedForRoundMail(emailContext);
                        logger.info("Mail sent");

                    } catch (MessagingException e) {
                        throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Mail not Sent");
                    }

                    BeanUtils.copyProperties(phase, detailsDto, NullPropertyName.getNullPropertyNames(phase));
                    if (stage < phaseList.get().size()) {
                        autoSchedule(jobId, skillOwnerId, stage + 1);
                    }
                } else {
                    notificationService.newSlotBySeekerNotification(skillOwnerId);
                }
            } else {
                throw new ServiceException(DATE_TIME_CHECK.getErrorCode(), DATE_TIME_CHECK.getErrorDesc());
            }
        } else {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
        logger.info("SelectionPhaseServiceImpl || selectedForRound || Selected For Round");
        return detailsDto;
    }

    /**
     * @param jobId id
     * @return flow obj
     */
    @Override
    @Transactional
    public FlowLockedDto isLocked(String jobId) {
        Optional<JobWorkFlowComponent> workFlowComponent = jobWorkFlowComponentRepository.findByJobId(jobId);
        Optional<List<RequirementPhase>> requirementPhaseList = requirementPhaseRepository.findByJobId(jobId);
        FlowLockedDto flowLockedDto = new FlowLockedDto();
        if (workFlowComponent.isPresent()) {
            flowLockedDto.setJobId(jobId);
            flowLockedDto.setFlow(Arrays.asList(workFlowComponent.get().getFlow().split(",")));
            List<Integer> percentage = new ArrayList<>();
            AtomicInteger i = new AtomicInteger(0);
            requirementPhaseList.get().sort(Comparator.comparingInt(RequirementPhase::getRequirementId));
            requirementPhaseList.get().forEach(phase -> {
                if (i.get() < workFlowComponent.get().getFlow().split(",").length) {
                    i.getAndIncrement();
                }
            });
            flowLockedDto.setPercentageExpected(percentage);
            flowLockedDto.setLocked(workFlowComponent.get().getLocked());
            logger.info("SelectionPhaseServiceImpl || isLocked || Locking The Flow ");
            return flowLockedDto;
        } else {
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public SlotConfirmByOwnerDto getSlots(String jobId, int skillOwnerId) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
            SlotConfirmByOwnerDto slotConfirmByOwnerDto = new SlotConfirmByOwnerDto();
            if (selectionPhase.isPresent()) {
                slotConfirmByOwnerDto.setJobId(selectionPhase.get().getJob().getJobId());
                slotConfirmByOwnerDto.setSkillOwnerEntityId(selectionPhase.get().getSkillOwnerEntity().getSkillOwnerEntityId());
                slotConfirmByOwnerDto.setSlotsConfirmedBySeeker(selectionPhase.get().getSlotConfirmed());
                BeanUtils.copyProperties(selectionPhase.get(), slotConfirmByOwnerDto, NullPropertyName.getNullPropertyNames(selectionPhase.get()));
                logger.info("SelectionPhaseServiceImpl || getSelectionPhase || get the slots from selectionPhase ");
                return slotConfirmByOwnerDto;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public SlotConfirmByOwnerDto updateSlotBySkillOwner(SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(slotConfirmByOwnerDto.getJobId(), slotConfirmByOwnerDto.getSkillOwnerEntityId());
        try {
            if (selectionPhase.isPresent()) {
                LocalDate firstDate = slotConfirmByOwnerDto.getDateSlotsByOwner1().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate secondDate = slotConfirmByOwnerDto.getDateSlotsByOwner2().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate thirdDate = slotConfirmByOwnerDto.getDateSlotsByOwner3().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                scheduleInterviewCheck(firstDate, slotConfirmByOwnerDto.getTimeSlotsByOwner1(), slotConfirmByOwnerDto.getSkillOwnerEntityId());
                scheduleInterviewCheck(secondDate, slotConfirmByOwnerDto.getTimeSlotsByOwner2(), slotConfirmByOwnerDto.getSkillOwnerEntityId());
                scheduleInterviewCheck(thirdDate, slotConfirmByOwnerDto.getTimeSlotsByOwner3(), slotConfirmByOwnerDto.getSkillOwnerEntityId());

                slotConfirmByOwnerDto.setJobId(selectionPhase.get().getJob().getJobId());
                slotConfirmByOwnerDto.setSkillOwnerEntityId(selectionPhase.get().getSkillOwnerEntity().getSkillOwnerEntityId());
                slotConfirmByOwnerDto.setSlotsConfirmedBySeeker(selectionPhase.get().getSlotConfirmed());
                BeanUtils.copyProperties(slotConfirmByOwnerDto, selectionPhase.get(), NullPropertyName.getNullPropertyNames(slotConfirmByOwnerDto));
                selectionPhase.get().setNewSlotRequested(false);
                selectionPhase.get().setInterviewAccepted(false);
                selectionPhaseRepository.save(selectionPhase.get());

                notificationService.slotBySkillOwnerNotification(slotConfirmByOwnerDto);
                logger.info("SelectionPhaseServiceImpl || updateSlots || update the slots by skillOwner ");
                return slotConfirmByOwnerDto;

            } else {
                throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(SLOT_ALREADY_BOOKED.getErrorCode(), SLOT_ALREADY_BOOKED.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public SlotConfirmBySeekerDto slotConfirmedBySeeker(String jobId, int skillOwnerId) {
        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
        try {
            SlotConfirmBySeekerDto slotConfirmBySeekerDto = new SlotConfirmBySeekerDto();
            if (selectionPhase.isPresent()) {
                selectionPhase.get().setSlotConfirmed(true);
                selectionPhase.get().setNewSlotRequested(false);
                SelectionPhase selectionPhaseResponse = selectionPhaseRepository.save(selectionPhase.get());
                slotConfirmBySeekerDto.setSkillOwnerEntityId(selectionPhaseResponse.getSkillOwnerEntity().getSkillOwnerEntityId());
                slotConfirmBySeekerDto.setJobId(selectionPhaseResponse.getJob().getJobId());
                slotConfirmBySeekerDto.setSlotConfirmed(selectionPhaseResponse.getSlotConfirmed());

                notificationService.slotConfirmedBySeekerNotification(slotConfirmBySeekerDto);
                logger.info("SelectionPhaseServiceImpl || updateSlotConfirmedBySeeker || update the slots by skillSeeker ");

                return slotConfirmBySeekerDto;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public NewSlotRequestBySeekerDto updateNewSlotBySeeker(String jobId, int skillOwnerId) {
        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
        try {
            NewSlotRequestBySeekerDto newSlotRequestBySeekerDto = new NewSlotRequestBySeekerDto();
            if (selectionPhase.isPresent() && selectionPhase.get().getAccepted()) {
                SelectionPhase selectionPhase1 = selectionPhase.get();
                newSlotRequestBySeekerDto.setJobId(selectionPhase.get().getJob().getJobId());
                newSlotRequestBySeekerDto.setSkillOwnerEntityId(selectionPhase.get().getSkillOwnerEntity().getSkillOwnerEntityId());
                newSlotRequestBySeekerDto.setNewSlotRequested(true);
                if (null == selectionPhase.get().getNewSlotRequested() || !selectionPhase.get().getNewSlotRequested()) {
                    selectionPhase.get().setDateSlotsByOwner1(null);
                    selectionPhase.get().setDateSlotsByOwner2(null);
                    selectionPhase.get().setDateSlotsByOwner3(null);
                    selectionPhase.get().setTimeSlotsByOwner1(null);
                    selectionPhase.get().setTimeSlotsByOwner2(null);
                    selectionPhase.get().setTimeSlotsByOwner3(null);
                    selectionPhase.get().setSlotConfirmed(false);
                    selectionPhase.get().setNewSlotRequested(true);

                    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                    emailContext.baseurlLogin(baseurlLogin);
                    emailContext.newSlotRequest(emailContext, selectionPhase1);
                    try {
                        emailContext.baseURLNewSlotRequest(baseURLNewSlotRequest);
                        emailService.newSlotRequest(emailContext);
                        logger.info("Mail sent");
                    } catch (MessagingException e) {
                        throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Mail not Sent");
                    }

                    selectionPhaseRepository.save(selectionPhase.get());
                    if (selectionPhase1.getNewSlotRequested()) {
                        ContentEntity content = notificationService.newSlotBySeekerNotification(jobId, skillOwnerId, newSlotRequestBySeekerDto);
                        logger.info("SelectionPhaseServiceImpl || Content || updateNewSlotBySeeker {}", content.getTitle());
                    }
                }
                logger.info("SelectionPhaseServiceImpl || updateNewSlotBySeeker || update the newSlots by skillSeeker ");
                return newSlotRequestBySeekerDto;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public RequirementPhaseDetailsDto updateDetailsForParticularRound(RequirementPhaseDetailsDto
                                                                              requirementPhaseDetailsDto) {
        try {
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(requirementPhaseDetailsDto.getJobId(), requirementPhaseDetailsDto.getSkillOwnerId());
            if (selectionPhase.isPresent()) {
                Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(requirementPhaseDetailsDto.getJobId(), requirementPhaseDetailsDto.getSkillOwnerId(), requirementPhaseDetailsDto.getStage());
                if (requirementPhase.isPresent()) {
                    BeanUtils.copyProperties(requirementPhaseDetailsDto, requirementPhase.get(), NullPropertyName.getNullPropertyNames(requirementPhaseDetailsDto));
                    if (null != requirementPhaseDetailsDto.getFeedback()) {
                        requirementPhase.get().setFeedback(requirementPhaseDetailsDto.getFeedback());
                    }
                    requirementPhase.get().setCandidateRate(requirementPhaseDetailsDto.getCandidatePercentage());
                    RequirementPhase phase = requirementPhaseRepository.save(requirementPhase.get());
                    RequirementPhaseDetailsDto detailsDto = new RequirementPhaseDetailsDto();
                    BeanUtils.copyProperties(phase, detailsDto, NullPropertyName.getNullPropertyNames(phase));
                    detailsDto.setCandidatePercentage(requirementPhaseDetailsDto.getCandidatePercentage());
                    logger.info("SelectionPhaseServiceImpl || updateDetailsForParticularRound || update Details ForParticular Round {}", detailsDto);
                    return detailsDto;
                } else {
                    logger.error(INVALID_REQUIREMENT_PHASE.getErrorDesc());
                    throw new ServiceException(INVALID_REQUIREMENT_PHASE.getErrorCode(), INVALID_REQUIREMENT_PHASE.getErrorDesc());
                }
            } else {
                logger.error(INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
                throw new ServiceException(INVALID_JOB_SKILL_OWNER_COMBO.getErrorCode(), INVALID_JOB_SKILL_OWNER_COMBO.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public AcceptRejectDto acceptRejectBySkillOwner(AcceptRejectDto acceptRejectDto) {
        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId());
        try {
            if (!acceptRejectDto.getAccepted()) {
                notificationService.acceptBySkillOwnerNotification(acceptRejectDto);
                removeCurrentOwnerDetails(acceptRejectDto.getSkillOwnerEntityId(), acceptRejectDto.getJobId());
            }

            if (selectionPhase.isPresent() && acceptRejectDto.getAccepted()) {

                Optional<List<SelectionPhase>> selectionPhases = selectionPhaseRepository.findBySkillOwnerId(acceptRejectDto.getSkillOwnerEntityId());
                for (SelectionPhase phase : selectionPhases.get()) {
                    if (phase.getAccepted() && null == phase.getRejectedOn()) {
                        throw new ServiceException(SKILL_OWNER_JOB.getErrorCode(), SKILL_OWNER_JOB.getErrorDesc());
                    }
                }
                selectionPhase.get().setAccepted(acceptRejectDto.getAccepted());
                selectionPhase.get().setInterviewAccepted(true);
                selectionPhaseRepository.save(selectionPhase.get());

                notificationService.acceptBySkillOwnerNotification(acceptRejectDto);
                logger.info("SelectionPhaseServiceImpl || Content || updateNewSlotBySeeker {}");

                autoSchedule(acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId(), 1);
                logger.info("SelectionPhaseServiceImpl || updateNewSlotBySeeker || update the selectionPhase by acceptOrReject");
                return acceptRejectDto;
            }


        } catch (ServiceException e) {
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }
        return acceptRejectDto;
    }

    @Transactional
    public void removeCurrentOwnerDetails(int ownerId, String jobId) {

        Optional<List<RequirementPhase>> bySkillOwnerIdReqPhase = requirementPhaseRepository.findByJobIdSkillOwnerId(jobId, ownerId);
        if (bySkillOwnerIdReqPhase.isPresent()) {
            bySkillOwnerIdReqPhase.get().forEach(requirementPhase -> requirementPhaseRepository.delete(requirementPhase));
        }

        Optional<SelectionPhase> byJobIdAndSkillOwnerId = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, ownerId);
        if (byJobIdAndSkillOwnerId.isPresent()) {
            selectionPhaseRepository.delete(byJobIdAndSkillOwnerId.get());
        }

    }

    public String listToString(List<String> flow) {
        StringBuilder flowList = new StringBuilder();
        for (String s : flow) {
            flowList.append(s).append(",");
        }

        return flowList.substring(0, flowList.length() - 1);
    }

    @Override
    @Transactional
    public List<SelectionPhaseResponse> addDataToDb(InsertRequirementPhaseDto
                                                            insertRequirementPhase, List<SelectionPhase> selectionPhaseList) {
        List<SelectionPhaseResponse> selectionPhaseResponses = new ArrayList<>();
        selectionPhaseList.forEach(selectionPhase -> {
            List<RequirementPhase> requirementPhaseList = new ArrayList<>();
            AtomicInteger sequence = new AtomicInteger(1);
            AtomicInteger percentageSequence = new AtomicInteger(0);
            insertRequirementPhase.getRequirementPhases().forEach(phase -> {
                try {
                    RequirementPhase requirementPhase = new RequirementPhase();
                    requirementPhase.setStage(sequence.get());
                    percentageSequence.getAndIncrement();
                    sequence.getAndIncrement();

                    requirementPhase.setJobId(selectionPhase.getJob().getJobId());
                    requirementPhase.setSkillOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                    requirementPhase.setRequirementPhaseName(phase.toUpperCase());
                    requirementPhaseList.add(requirementPhaseRepository.save(requirementPhase));
                } catch (Exception e) {
                    logger.error("failed to insert requirement Phase for skillOwner id :{}", selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                }
            });
            selectionPhase.setRequirementPhase(requirementPhaseList);

            SelectionPhase selectionPhaseObj = selectionPhaseRepository.save(selectionPhase);

            String skillOwnerFullName = selectionPhaseObj.getSkillOwnerEntity().getFirstName() + " " + selectionPhaseObj.getSkillOwnerEntity().getLastName();
            SelectionPhaseResponse selectionPhaseResponse = new SelectionPhaseResponse();
            BeanUtils.copyProperties(selectionPhaseObj, selectionPhaseResponse, NullPropertyName.getNullPropertyNames(selectionPhaseObj));
            selectionPhaseResponse.setJobId(selectionPhaseObj.getJob().getJobId());
            selectionPhaseResponse.setJobTitle(selectionPhaseObj.getJob().getJobTitle());
            selectionPhaseResponse.setSkillOwnerId(selectionPhaseObj.getSkillOwnerEntity().getSkillOwnerEntityId());
            selectionPhaseResponse.setSkillOwnerName(skillOwnerFullName);
            selectionPhaseResponse.setRequirementPhaseList(selectionPhaseObj.getRequirementPhase());

            selectionPhaseResponses.add(selectionPhaseResponse);
        });
        logger.info("SelectionPhaseServiceImpl || insertMultipleData || Inserting SelectionPhase");
        return selectionPhaseResponses;
    }

    @Override
    @Transactional
    public boolean autoSchedule(String jobId, int skillOwnerId, int stage) {

        ScheduleInterviewDto scheduleInterviewDto = new ScheduleInterviewDto();
        scheduleInterviewDto.setSkillOwnerId(skillOwnerId);
        scheduleInterviewDto.setJobId(jobId);
        scheduleInterviewDto.setStage(stage);
        scheduleInterviewDto.setModeOfInterview(modeOfInterview);
        try {
            Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId(), scheduleInterviewDto.getStage());
            Optional<List<RequirementPhase>> requirementPhaseInterviewList = requirementPhaseRepository.findBySkillOwnerId(skillOwnerId);
            Optional<SkillOwnerSlotsEntity> ownerSlots = Optional.ofNullable(skillOwnerSlotsRepository.findBySkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId()));
            if (ownerSlots.isPresent() && requirementPhase.isPresent()) {
                class SlotWindow {
                    LocalDate date;
                    LocalTime startTime;
                    LocalTime endTime;
                }
                List<SlotWindow> dateTimeSlot = new ArrayList<>();
                List<LocalDate> toRemove = new ArrayList<>();

                //adding slots given by owner to a list in order to compare efficiently
                if (null != ownerSlots.get().getDateSlotsByOwner1()) {
                    SlotWindow slotWindowObj = new SlotWindow();
                    slotWindowObj.date = ownerSlots.get().getDateSlotsByOwner1();
                    slotWindowObj.startTime = ownerSlots.get().getTimeSlotsByOwner1();
                    slotWindowObj.endTime = ownerSlots.get().getEndTimeSlotsByOwner1();
                    dateTimeSlot.add(slotWindowObj);
                }
                if (null != ownerSlots.get().getDateSlotsByOwner2()) {
                    SlotWindow slotWindowObj = new SlotWindow();
                    slotWindowObj.date = ownerSlots.get().getDateSlotsByOwner2();
                    slotWindowObj.startTime = ownerSlots.get().getTimeSlotsByOwner2();
                    slotWindowObj.endTime = ownerSlots.get().getEndTimeSlotsByOwner2();
                    dateTimeSlot.add(slotWindowObj);
                }
                if (null != ownerSlots.get().getDateSlotsByOwner3()) {
                    SlotWindow slotWindowObj = new SlotWindow();
                    slotWindowObj.date = ownerSlots.get().getDateSlotsByOwner3();
                    slotWindowObj.startTime = ownerSlots.get().getTimeSlotsByOwner3();
                    slotWindowObj.endTime = ownerSlots.get().getEndTimeSlotsByOwner3();
                    dateTimeSlot.add(slotWindowObj);
                }

                //adding date and time of interview already scheduled in a list after filtering old ones
                List<SlotWindow> scheduledSlots = new ArrayList<>();
                if (!requirementPhaseInterviewList.isEmpty()) {
                    for (RequirementPhase alreadyScheduled : requirementPhaseInterviewList.get()) {
                        if (null != alreadyScheduled.getDateOfInterview() && !alreadyScheduled.getDateOfInterview().isBefore(LocalDate.now().plus(1, ChronoUnit.DAYS))) {
                            SlotWindow slotWindowObj = new SlotWindow();
                            slotWindowObj.date = alreadyScheduled.getDateOfInterview();
                            slotWindowObj.startTime = alreadyScheduled.getTimeOfInterview();
                            slotWindowObj.endTime = alreadyScheduled.getEndTimeOfInterview();
                            scheduledSlots.add(slotWindowObj);
                        }
                    }
                }
                SlotWindow finalWindow = new SlotWindow();
                boolean slotFound = false;
                Collections.sort(dateTimeSlot, Comparator.comparing(o -> o.date));
                for (SlotWindow slotWindow : dateTimeSlot) {
                    if (!slotFound) {
                        while (slotWindow.startTime.isBefore(slotWindow.endTime)) {
                            if (slotWindow.date.isBefore(LocalDate.now().plusDays(1))) {
                                toRemove.add(slotWindow.date);
                                break;
                            }
                            boolean isMatched = scheduledSlots.stream().anyMatch(obj -> {
                                logger.info(String.valueOf(slotWindow.startTime.isBefore(obj.endTime) && (slotWindow.startTime.isAfter(obj.startTime) || slotWindow.startTime.equals(obj.startTime))));
                                logger.info(String.valueOf(obj.date.isEqual(slotWindow.date)));
                                return ((slotWindow.startTime.isBefore(obj.endTime) && (slotWindow.startTime.isAfter(obj.startTime) || slotWindow.startTime.equals(obj.startTime))) && obj.date.isEqual(slotWindow.date));
                            });
                            if (!isMatched) {
                                finalWindow = slotWindow;
                                slotFound = true;
                                break;
                            }
                            slotWindow.startTime = slotWindow.startTime.plusHours(1);
                        }
                    }
                }
                for (LocalDate date : toRemove) {
                    if ((null != ownerSlots.get().getDateSlotsByOwner1() && ownerSlots.get().getDateSlotsByOwner1().equals(date))) {
                        ownerSlots.get().setDateSlotsByOwner1(null);
                    } else if (null != ownerSlots.get().getDateSlotsByOwner2() && ownerSlots.get().getDateSlotsByOwner2().equals(date)) {
                        ownerSlots.get().setDateSlotsByOwner2(null);
                    } else if (null != ownerSlots.get().getDateSlotsByOwner3() && ownerSlots.get().getDateSlotsByOwner3().equals(date)) {
                        ownerSlots.get().setDateSlotsByOwner3(null);
                    }
                }
                skillOwnerSlotsRepository.save(ownerSlots.get());
                if ((null == ownerSlots.get().getDateSlotsByOwner1() && null == ownerSlots.get().getDateSlotsByOwner2() && null == ownerSlots.get().getDateSlotsByOwner3()) || null == finalWindow.date) {
                    notificationService.newSlotBySeekerNotification(skillOwnerId);
                }
                if (null == finalWindow.date) {
                    throw new ServiceException();
                }
                if (!requirementPhase.get().isRescheduled()) {
                    scheduleInterviewDto.setDateOfInterview(finalWindow.date);
                    scheduleInterviewDto.setTimeOfInterview(finalWindow.startTime);
                    scheduleInterviewDto.setEndTimeOfInterview(finalWindow.startTime.plusHours(1));
                    scheduleInterview(scheduleInterviewDto, true);
                }

                Notification notificationDto = new Notification();
                notificationDto.setDateOfInterview(finalWindow.date);
                notificationDto.setTimeOfInterview(finalWindow.startTime);
                notificationDto.setStage(stage);
                notificationDto.setRequirementPhaseName(requirementPhase.get().getRequirementPhaseName());
                Optional<RequirementPhase> requirementPhase1 = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(jobId, skillOwnerId, stage);
                requirementPhase1.get().setDateOfInterview(finalWindow.date);
                requirementPhase1.get().setTimeOfInterview(finalWindow.startTime);
                requirementPhase1.get().setEndTimeOfInterview(finalWindow.endTime);
                requirementPhaseRepository.save(requirementPhase1.get());
                notificationService.autoScheduleInterviewNotification(scheduleInterviewDto, notificationDto);
                return true;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            logger.error("No common slots found for owner");    //Can't throw error as it will block the current transaction
            return false;
        }
    }

    @Scheduled(fixedRateString = "PT12H")
    void checkCommonSlot() {
        List<SkillOwnerSlotsEntity> bySkillOwnerEntityId1 = skillOwnerSlotsRepository.findAll();
        bySkillOwnerEntityId1.forEach(bySkillOwnerEntityId -> {

            if (bySkillOwnerEntityId.getDateSlotsByOwner1() == null || bySkillOwnerEntityId.getDateSlotsByOwner1().equals(LocalDate.now()) || bySkillOwnerEntityId.getDateSlotsByOwner1().isBefore(LocalDate.now())) {
                bySkillOwnerEntityId.setDateSlotsByOwner1(null);
                bySkillOwnerEntityId.setTimeSlotsByOwner1(null);
                bySkillOwnerEntityId.setEndTimeSlotsByOwner1(null);
            }
            if (bySkillOwnerEntityId.getDateSlotsByOwner2() == null || bySkillOwnerEntityId.getDateSlotsByOwner2().equals(LocalDate.now()) || bySkillOwnerEntityId.getDateSlotsByOwner2().isBefore(LocalDate.now())) {
                bySkillOwnerEntityId.setDateSlotsByOwner2(null);
                bySkillOwnerEntityId.setTimeSlotsByOwner2(null);
                bySkillOwnerEntityId.setEndTimeSlotsByOwner2(null);
            }
            if (bySkillOwnerEntityId.getDateSlotsByOwner3() == null || bySkillOwnerEntityId.getDateSlotsByOwner3().equals(LocalDate.now()) || bySkillOwnerEntityId.getDateSlotsByOwner3().isBefore(LocalDate.now())) {
                bySkillOwnerEntityId.setDateSlotsByOwner3(null);
                bySkillOwnerEntityId.setTimeSlotsByOwner3(null);
                bySkillOwnerEntityId.setEndTimeSlotsByOwner3(null);

            }
            if (null == bySkillOwnerEntityId.getDateSlotsByOwner1() && null == bySkillOwnerEntityId.getDateSlotsByOwner2() && null == bySkillOwnerEntityId.getDateSlotsByOwner3()) {
                notificationService.newSlotBySeekerNotification(bySkillOwnerEntityId.getSkillOwnerEntityId());
            }

            skillOwnerSlotsRepository.save(bySkillOwnerEntityId);

        });


    }

    /**
     * @param slotConfirmByOwnerDto data
     * @return List slots
     */
    @Override
    public SlotConfirmByOwnerDto updateCommonSlotByOwner(SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        if (weekendRestriction(slotConfirmByOwnerDto)) {
            try {
                Optional<SkillOwnerSlotsEntity> ownerSlots = Optional.ofNullable(skillOwnerSlotsRepository.findBySkillOwnerEntityId(slotConfirmByOwnerDto.getSkillOwnerEntityId()));
                if (ownerSlots.isPresent()) {
                    int id = ownerSlots.get().getId();
                    modelMapper.map(slotConfirmByOwnerDto, ownerSlots.get());
                    ownerSlots.get().setId(id);
                    ownerSlots.get().setDateSlotsByOwner1(slotConfirmByOwnerDto.getDateSlotsByOwner1().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    ownerSlots.get().setDateSlotsByOwner2(slotConfirmByOwnerDto.getDateSlotsByOwner2().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    ownerSlots.get().setDateSlotsByOwner3(slotConfirmByOwnerDto.getDateSlotsByOwner3().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    skillOwnerSlotsRepository.save(ownerSlots.get());
                    logger.info("SelectionPhaseServiceImpl || updateCommonSlotByOwner || Common Slots By Owner");
                    return slotConfirmByOwnerDto;
                } else {
                    SkillOwnerSlotsEntity ownerNewSlots = new SkillOwnerSlotsEntity();
                    modelMapper.map(slotConfirmByOwnerDto, ownerNewSlots);
                    ownerNewSlots.setSkillOwnerEntityId(slotConfirmByOwnerDto.getSkillOwnerEntityId());
                    ownerNewSlots.setDateSlotsByOwner1(slotConfirmByOwnerDto.getDateSlotsByOwner1().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    ownerNewSlots.setDateSlotsByOwner2(slotConfirmByOwnerDto.getDateSlotsByOwner2().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    ownerNewSlots.setDateSlotsByOwner3(slotConfirmByOwnerDto.getDateSlotsByOwner3().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    skillOwnerSlotsRepository.save(ownerNewSlots);
                    logger.info("SelectionPhaseServiceImpl || updateCommonSlotByOwner || Common Slots By Owner");
                    return slotConfirmByOwnerDto;
                }
            } catch (Exception e) {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Invalid data");
            }
        } else {
            throw new ServiceException(WEEKENDS_RESTRICTED.getErrorCode(), WEEKENDS_RESTRICTED.getErrorDesc());
        }
    }

    private Boolean weekendRestriction(SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        return (slotConfirmByOwnerDto.getDateSlotsByOwner1().getDay() != 6 && slotConfirmByOwnerDto.getDateSlotsByOwner1().getDay() != 0)
                && (slotConfirmByOwnerDto.getDateSlotsByOwner2().getDay() != 6 && slotConfirmByOwnerDto.getDateSlotsByOwner2().getDay() != 0)
                && (slotConfirmByOwnerDto.getDateSlotsByOwner3().getDay() != 6 && slotConfirmByOwnerDto.getDateSlotsByOwner3().getDay() != 0);
    }

    @Override
    @Transactional
    public List<RateApprovalDto> updateSkillOwnerRate(List<RateApprovalDto> rateApprovalDtoList) {

        List<RateApprovalDto> rateApprovalDtoLists = new ArrayList<>();
        RateApprovalDto rateApprovalDto = new RateApprovalDto();
        rateApprovalDtoList.stream().forEach(approvalDto -> {
            SelectionPhase selectionPhase;
            Optional<SelectionPhase> phase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(approvalDto.getJobId(), approvalDto.getSkillOwnerId());
            try {
                if (phase.isPresent()) {
                    phase.get().setRate(approvalDto.getRate());
                    modelMapper.map(rateApprovalDto, SelectionPhase.class);
                    selectionPhase = selectionPhaseRepository.save(phase.get());
                    rateApprovalDtoLists.add(modelMapper.map(selectionPhase, RateApprovalDto.class));
                } else {
                    throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());
                }
            } catch (ServiceException e) {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
            }

        });
        logger.info("SelectionPhaseServiceImpl ||  updateSkillOwnerRate || Fixing Rate for the particular Owner");
        return rateApprovalDtoLists;
    }

    @Override
    public List<FeedbackRate> getFeedback() {
        return feedbackRepository.findAll();
    }

    @Transactional
    @Override
    public RescheduleDto rescheduleRound(String jobId, int skillOwnerId, int stage) {
        RescheduleDto rescheduleDto = new RescheduleDto();
        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, skillOwnerId);
        Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(jobId, skillOwnerId, stage);
        Optional<List<RequirementPhase>> phaseList = requirementPhaseRepository.findByJobIdSkillOwnerId(jobId, skillOwnerId);
        if (requirementPhase.isPresent() && (requirementPhase.get().getDateOfInterview().isBefore(LocalDate.now()) || requirementPhase.get().getDateOfInterview().isEqual(LocalDate.now()) && selectionPhase.isPresent())) {
            selectionPhase.get().setCurrentStage(stage);
            requirementPhase.get().setRescheduled(true);
            RequirementPhase phase = requirementPhaseRepository.save(requirementPhase.get());
            SelectionPhase phase1 = selectionPhaseRepository.save(selectionPhase.get());

            if (stage <= phaseList.get().size()) {
                if (autoSchedule(jobId, skillOwnerId, stage)) {
                    requirementPhase.get().setStatus("ReScheduled");
                    requirementPhaseRepository.save(requirementPhase.get());
                    rescheduleDto.setJobId(jobId);
                    rescheduleDto.setCurrentStage(stage);
                    rescheduleDto.setSkillOwnerId(skillOwnerId);
                    rescheduleDto.setDateOfInterview(requirementPhase.get().getDateOfInterview());
                    rescheduleDto.setStartTime(requirementPhase.get().getTimeOfInterview());
                    rescheduleDto.setEndTime(requirementPhase.get().getEndTimeOfInterview());
                    rescheduleDto.setStatus("Rescheduled");
                    logger.info("SelectionPhaseServiceImpl || Rescheduled ForRound || Rescheduled For Round");

                    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                    RescheduleDto interviewDto = new RescheduleDto();
                    interviewDto.setDateOfInterview(requirementPhase.get().getDateOfInterview());
                    interviewDto.setStartTime(requirementPhase.get().getTimeOfInterview());
                    emailContext.baseurlLogin(baseurlLogin);
                    emailContext.rescheduleMail(emailContext, selectionPhase.get(), interviewDto);
                    try {
                        emailContext.baseURLReScheduleMail(baseURLReScheduleMail);
                        emailService.reScheduleMail(emailContext);
                        logger.info("Mail sent");

                    } catch (MessagingException e) {
                        throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Mail not Sent");
                    }

                } else {
                    throw new ServiceException(NEW_SLOT_REQUESTED.getErrorCode(), NEW_SLOT_REQUESTED.getErrorDesc());
                }
            }
        } else {
            throw new ServiceException(RESCHEDULE_RESTRICTED.getErrorCode(), RESCHEDULE_RESTRICTED.getErrorDesc());
        }
        return rescheduleDto;
    }

    public Boolean shortlistStatus(int skillOwnerEntityId) {
        Optional<List<SelectionPhase>> phasesOptional = selectionPhaseRepository.findBySkillOwnerId(skillOwnerEntityId);
        if (phasesOptional.isPresent()) {
            List<SelectionPhase> phases = phasesOptional.get();
            boolean acceptReject = false;
            for (SelectionPhase phase : phases) {
                Optional<SelectionPhase> byJobIdAndSkillOwnerId = selectionPhaseRepository.findByJobIdAndSkillOwnerId(phase.getJob().getJobId(), skillOwnerEntityId);
                if (byJobIdAndSkillOwnerId.get().getAccepted() && byJobIdAndSkillOwnerId.get().getRejectedOn() == null) {
                    acceptReject = true;
                    break;
                } else if (byJobIdAndSkillOwnerId.get().getAccepted() && byJobIdAndSkillOwnerId.get().getRejectedOn() != null) {
                    acceptReject = false;
                    break;
                }
            }
            return acceptReject;
        } else {
            return false;
        }
    }

    @Override

    public Optional<List<SelectionPhase>> getByJobJobId(String jobId) {
        return selectionPhaseRepository.findByJobId(jobId);
    }

    @Override
    @Transactional
    public List<SelectionPhase> getByJobId(String jobId) {
        List<SelectionPhase> selection = selectionPhaseRepository.findOwnerByJobId(jobId);
        return selection;


    }

}




