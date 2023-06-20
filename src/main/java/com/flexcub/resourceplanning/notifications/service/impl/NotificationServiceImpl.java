package com.flexcub.resourceplanning.notifications.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.invoice.entity.Invoice;
import com.flexcub.resourceplanning.invoice.entity.InvoiceAdmin;
import com.flexcub.resourceplanning.invoice.repository.AdminInvoiceRepository;
import com.flexcub.resourceplanning.invoice.repository.SeekerInvoiceRepository;
import com.flexcub.resourceplanning.job.dto.*;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.entity.*;
import com.flexcub.resourceplanning.notifications.repository.*;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillStatusRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillYearOfExperienceRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillpartner.dto.HistoryOfJobs;
import com.flexcub.resourceplanning.skillpartner.dto.JobHistory;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerDetails;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.*;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Component
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    OwnerNotificationsRepository ownerNotificationsRepository;
    @Autowired
    SeekerInvoiceRepository seekerInvoiceRepository;

    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    SeekerNotificationsRepository seekerNotificationsRepository;
    @Autowired
    PartnerNotificationsRepository partnerNotificationsRepository;
    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    @Autowired
    SkillSeekerMsaRepository seekerMsaRepository;

    @Autowired
    SuperAdminNotificationRepositoy superAdminNotificationRepositoy;

    @Autowired
    SeekerModuleRepository seekerModuleRepository;
    @Autowired
    @Lazy
    SelectionPhaseService selectionPhaseService;
    @Autowired
    OwnerSkillYearOfExperienceRepository ownerSkillYearOfExperienceRepository;
    @Autowired
    SkillSeekerService skillSeekerService;

    @Autowired
    OwnerSkillStatusRepository ownerStatusRepository;


    @Autowired
    AdminInvoiceRepository adminInvoiceRepository;

    @Autowired
    ContractStatusRepository contractStatusRepository;
    @Autowired
    SkillPartnerRepository skillPartnerRepository;

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    @Autowired
    SkillSeekerRepository skillSeekerRepository;
    @Value("${formatter.dateType}")
    private String dateType;
    @Value("${formatter.timeType}")
    private String timeType;

    @Autowired
    private PoRepository poRepository;
    @Autowired
    private StatementOfWorkRepository statementOfWorkRepository;
    @Autowired
    private RequirementPhaseRepository requirementPhaseRepository;

    @Value("${project.default}")
    private String Default;


    @Override
    @Transactional
    public List<OwnerNotificationsEntity> getOwnerNotification(int id) {

        try {
            Optional<List<OwnerNotificationsEntity>> bySkillOwnerEntityId = ownerNotificationsRepository.findBySkillOwnerEntityId(id);
            if (bySkillOwnerEntityId.isPresent() && !bySkillOwnerEntityId.get().isEmpty()) {
                bySkillOwnerEntityId.get().sort(Comparator.comparing(OwnerNotificationsEntity::getId).reversed());
                return bySkillOwnerEntityId.get();
            }
            return Collections.emptyList();
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_ID_REQUEST.getErrorCode(), INVALID_ID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public List<PartnerNotificationsEntity> getPartnerNotification(int id) {

        try {
            Optional<List<PartnerNotificationsEntity>> bySkillPartnerEntityId = partnerNotificationsRepository.findBySkillPartnerEntityId(id);
            if (bySkillPartnerEntityId.isPresent() && !bySkillPartnerEntityId.get().isEmpty()) {
                bySkillPartnerEntityId.get().sort(Comparator.comparing(PartnerNotificationsEntity::getId).reversed());
                return bySkillPartnerEntityId.get();
            }
            return Collections.emptyList();
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public List<SeekerNotificationsEntity> getSeekerNotification(int id) {
        try {
            List<SeekerNotificationsEntity> seekerNotificationsEntities = new ArrayList<>();
            Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(id);
            if (skillSeeker.isPresent()) {
                Optional<List<SeekerNotificationsEntity>> bySkillSeekerEntityId = seekerNotificationsRepository.findByTaxIdBusinessLicense(skillSeeker.get().getTaxIdBusinessLicense());
                if (bySkillSeekerEntityId.isPresent() && !bySkillSeekerEntityId.get().isEmpty()) {

                    Map<Integer, List<Integer>> map = new HashMap<>();
                    map.put(1, List.of(2));
                    map.put(2, List.of(2));
                    map.put(3, List.of(2, 3));
                    map.put(4, List.of(2));
                    map.put(6, List.of(1, 2, 3, 4, 8));
                    map.put(10, List.of(2, 3, 8));

                    List<SeekerModulesEntity> accessEntities = skillSeekerService.getAccessDetails(id);
                    if (accessEntities.isEmpty()) {
                        return Collections.emptyList();
                    }
                    if (1 == skillSeeker.get().getSubRoles().getId()) {
                        return bySkillSeekerEntityId.get();
                    }
                    for (SeekerNotificationsEntity seekerNotifications : bySkillSeekerEntityId.get()) {
                        for (SeekerModulesEntity accessEntity : accessEntities) {
                            if (map.get(seekerNotifications.getContentId()).contains(accessEntity.getId())) {
                                seekerNotificationsEntities.add(seekerNotifications);
                                break;
                            }
                        }
                    }

                } else {
                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
                }
                logger.info("SkillSeekerServiceImpl || getSeekerNotification || get All Notification in seeker ");
                Collections.sort(seekerNotificationsEntities, new Comparator<SeekerNotificationsEntity>() {
                    public int compare(SeekerNotificationsEntity m1, SeekerNotificationsEntity m2) {
                        return m2.getDate().compareTo(m1.getDate());
                    }
                });
                return seekerNotificationsEntities;
            } else {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public ContentEntity scheduleInterviewNotification(ScheduleInterviewDto scheduleInterviewDto, Notification notificationDto) {
        try {
            ContentEntity content = contentRepository.findByScheduleInterview();
            Optional<SkillOwnerEntity> id = skillOwnerRepository.findById(scheduleInterviewDto.getSkillOwnerId());
            Optional<Job> job = jobRepository.findByJobId(scheduleInterviewDto.getJobId());
            if (job.isPresent()) {
                OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId());
                notifications.setJobId(scheduleInterviewDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setTitle(content.getTitle());
                notifications.setSeekerId(job.get().getSkillSeeker().getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                Optional<RequirementPhase> requirementPhase1 = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId(), scheduleInterviewDto.getStage());

                if (!requirementPhase1.get().isRescheduled()) {
                    notifications.setContent("Hi " + id.get().getFirstName() + " " + id.get().getLastName()
                            + ", You are scheduled for an interview for the role " + job.get().getJobTitle()
                            + " - " + "Round " + notificationDto.getStage() + " - "
                            + notificationDto.getRequirementPhaseName() + " with "
                            + job.get().getSkillSeeker().getSkillSeekerName() + " on "
                            + formatter.format(notificationDto.getDateOfInterview()) + " ,Time : "
                            + time.format(notificationDto.getTimeOfInterview()));
                } else if (requirementPhase1.get().isRescheduled()) {
                    notifications.setContent("Hi " + id.get().getFirstName() + " " + id.get().getLastName() + ", You are rescheduled for an interview for the role " + job.get().getJobTitle() + " - " + "Round " + notificationDto.getStage() + " - " + notificationDto.getRequirementPhaseName() + " with " + job.get().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(notificationDto.getDateOfInterview()) + " ,Time : " + time.format(notificationDto.getTimeOfInterview()));
                }
                notifications.setStage(notificationDto.getStage());
                notifications.setRequirementPhaseName(notificationDto.getRequirementPhaseName());
                notifications.setDateOfInterview(notificationDto.getDateOfInterview());
                notifications.setTimeOfInterview(notificationDto.getTimeOfInterview());
                notifications.setContentObj(content);
                ownerNotificationsRepository.save(notifications);
                if (!requirementPhase1.get().isRescheduled()) {
                    scheduleInterviewNotificationToPartner(scheduleInterviewDto, notificationDto);
                }
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public ContentEntity autoScheduleInterviewNotification(ScheduleInterviewDto scheduleInterviewDto, Notification notificationDto) {
        try {
            ContentEntity content = contentRepository.findByAutoScheduleInterview();
            Optional<Job> job = jobRepository.findByJobId(scheduleInterviewDto.getJobId());
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(job.get().getJobId(), scheduleInterviewDto.getSkillOwnerId());
            if (job.isPresent()) {
                OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId());
                notifications.setJobId(scheduleInterviewDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setTitle(content.getTitle());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);

                Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(scheduleInterviewDto.getJobId(), scheduleInterviewDto.getSkillOwnerId(), scheduleInterviewDto.getStage());
                if (!requirementPhase.get().isRescheduled()) {
                    selectionPhase.get().getRequirementPhase().stream().filter(f -> selectionPhase.get().getCurrentStage() == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared"))).map(f -> "Hi " + selectionPhase.get().getSkillOwnerEntity().getFirstName() + " " + selectionPhase.get().getSkillOwnerEntity().getLastName() + ", Your interview has been autoscheduled for " + f.getRequirementPhaseName() + " for the position of " + job.get().getJobTitle() + " with the client " + selectionPhase.get().getJob().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(notificationDto.getDateOfInterview()) + ",Time : " + time.format(notificationDto.getTimeOfInterview())).forEachOrdered(notifications::setContent);
                } else {
                    selectionPhase.get().getRequirementPhase().stream().filter(f -> selectionPhase.get().getCurrentStage() == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared"))).map(f -> "Hi " + selectionPhase.get().getSkillOwnerEntity().getFirstName() + " " + selectionPhase.get().getSkillOwnerEntity().getLastName() + ", Your interview has been rescheduled for " + f.getRequirementPhaseName() + " for the position of " + job.get().getJobTitle() + " with the client " + selectionPhase.get().getJob().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(notificationDto.getDateOfInterview()) + ",Time : " + time.format(notificationDto.getTimeOfInterview())).forEachOrdered(notifications::setContent);
                }
                notifications.setStage(notificationDto.getStage());
                notifications.setRequirementPhaseName(notificationDto.getRequirementPhaseName());
                notifications.setDateOfInterview(notificationDto.getDateOfInterview());
                notifications.setTimeOfInterview(notificationDto.getTimeOfInterview());
                notifications.setContentObj(content);
                ownerNotificationsRepository.save(notifications);
//                autoScheduleInterviewNotificationToPartner(scheduleInterviewDto, notificationDto);
                autoScheduleInterviewNotificationToSeeker(scheduleInterviewDto, notificationDto);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


    @Transactional
    public void autoScheduleInterviewNotificationToSeeker(ScheduleInterviewDto scheduleInterviewDto, Notification notificationDto) {
        try {
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId()));
            ContentEntity content = contentRepository.findByAutoScheduleInterview();
            Optional<Job> job = jobRepository.findByJobId(scheduleInterviewDto.getJobId());
            if (job.isPresent()) {
                SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillSeekerEntityId(job.get().getSkillSeeker().getId());
                notifications.setJobId(scheduleInterviewDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setTaxIdBusinessLicense(job.get().getTaxIdBusinessLicense());
                notifications.setOwnerId(scheduleInterviewDto.getSkillOwnerId());
                notifications.setTitle(content.getTitle());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                skillOwner.ifPresent(skillOwnerEntity -> notifications.setContent(job.get().getJobTitle() + " " + notificationDto.getRequirementPhaseName() + " " + "has been auto scheduled for candidate " + skillOwnerEntity.getFirstName() + " on " + formatter.format(notificationDto.getDateOfInterview()) + ",Time : " + time.format(notificationDto.getTimeOfInterview())));
                notifications.setStage(notificationDto.getStage());
                notifications.setContentObj(content);
                seekerNotificationsRepository.save(notifications);
                autoScheduleInterviewNotificationToPartner(scheduleInterviewDto, notificationDto);
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    public void autoScheduleInterviewNotificationToPartner(ScheduleInterviewDto scheduleInterviewDto, Notification notificationDto) {
        try {
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId()));
            ContentEntity content = contentRepository.findByAutoScheduleInterview();
            Optional<Job> job = jobRepository.findByJobId(scheduleInterviewDto.getJobId());
            if (job.isPresent()) {
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setTitle(content.getTitle());
                skillOwner.ifPresent(skillOwnerEntity -> notifications.setSkillPartnerEntityId(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId()));
                notifications.setJobId(scheduleInterviewDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                skillOwner.ifPresent(skillOwnerEntity -> notifications.setContent(job.get().getJobTitle() + " " + notificationDto.getRequirementPhaseName() + " " + "has been auto scheduled for candidate " + skillOwnerEntity.getFirstName() + "(" + scheduleInterviewDto.getSkillOwnerId() + ") on " + formatter.format(notificationDto.getDateOfInterview()) + ", Time : " + time.format(notificationDto.getTimeOfInterview())));
                notifications.setStage(notificationDto.getStage());
                notifications.setOwnerId(scheduleInterviewDto.getSkillOwnerId());
                notifications.setRequirementPhaseName(notificationDto.getRequirementPhaseName());
                notifications.setDateOfInterview(notificationDto.getDateOfInterview());
                notifications.setTimeOfInterview(notificationDto.getTimeOfInterview());
                notifications.setContentObj(content);
                partnerNotificationsRepository.save(notifications);
                scheduleInterviewNotificationToPartner(scheduleInterviewDto, notificationDto);
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    public ContentEntity scheduleInterviewNotificationToPartner(ScheduleInterviewDto scheduleInterviewDto, Notification notificationDto) {
        try {
            ContentEntity content = contentRepository.findByScheduleInterview();
            Optional<Job> job = jobRepository.findByJobId(scheduleInterviewDto.getJobId());
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(scheduleInterviewDto.getSkillOwnerId()));
            if (job.isPresent() && skillOwner.isPresent()) {
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(scheduleInterviewDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
//                notifications.setTitle(content.getTitle());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);

                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName()
                        + ", Your " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName()
                        + " is shortlisted for the " + job.get().getJobTitle() + " by the " + job.get().getSkillSeeker().getSkillSeekerName()
                        + " on " + formatter.format(notificationDto.getDateOfInterview()) + ", Time : "
                        + time.format(notificationDto.getTimeOfInterview()));

                notifications.setStage(notificationDto.getStage());
                notifications.setOwnerId(scheduleInterviewDto.getSkillOwnerId());
                notifications.setRequirementPhaseName(notificationDto.getRequirementPhaseName());
                notifications.setDateOfInterview(notificationDto.getDateOfInterview());
                notifications.setTimeOfInterview(notificationDto.getTimeOfInterview());
                notifications.setContentObj(content);
                partnerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


    @Override
    public ContentEntity slotBySkillOwnerNotification(SlotConfirmByOwnerDto slotConfirmByOwnerDto) {
        try {
            ContentEntity content = contentRepository.findByOwnerSlotId();
            SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
            Optional<Job> job = jobRepository.findByJobId(slotConfirmByOwnerDto.getJobId());
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(slotConfirmByOwnerDto.getSkillOwnerEntityId()));
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(job.get().getJobId(), skillOwner.get().getSkillOwnerEntityId());
            if (skillOwner.isPresent() && job.isPresent()) {
                notifications.setContentId(content.getId());
                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillSeekerEntityId(job.get().getSkillSeeker().getId());
                notifications.setJobId(slotConfirmByOwnerDto.getJobId());
                notifications.setDate(new Date());
                notifications.setOwnerId(slotConfirmByOwnerDto.getSkillOwnerEntityId());
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST


                selectionPhase.get().getRequirementPhase().stream().filter(f -> selectionPhase.get().getCurrentStage() == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared"))).map(f -> "Hi " + job.get().getSkillSeeker().getSkillSeekerName() + ", new available slots are added by the " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + "for the job" + job.get().getJobTitle() + " with job id " + job.get().getJobId() + " for the Round " + f.getStage() + ":" + f.getRequirementPhaseName() + ". Click below to accept the slots or request new ones  " + "on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).forEachOrdered(notifications::setContent);

//                notifications.setContent("Hi "+job.get().getSkillSeeker().getSkillSeekerName() + ", new available slots are added by the " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + "for the job" + job.get().getJobTitle() +
//                                " with job id " + job.get().getJobId() + " for the Round " + selectionPhase.get().getRequirementPhase().getStage() + ":" + f.getRequirementPhaseName() + ". Click below to accept the slots or request new ones  " +
//                                "on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

                notifications.setTaxIdBusinessLicense(job.get().getTaxIdBusinessLicense());
                notifications.setSkillOwnerId(slotConfirmByOwnerDto.getSkillOwnerEntityId());

                seekerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    public ContentEntity acceptBySkillOwnerNotification(AcceptRejectDto acceptRejectDto) {
        try {
            ContentEntity content = contentRepository.findByAccept();
            SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(acceptRejectDto.getSkillOwnerEntityId()));
            Optional<Job> job = jobRepository.findByJobId(acceptRejectDto.getJobId());
            if (skillOwner.isPresent() && job.isPresent()) {
                notifications.setContentId(content.getId());


                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillSeekerEntityId(job.get().getSkillSeeker().getId());
                notifications.setJobId(acceptRejectDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);
                notifications.setTaxIdBusinessLicense(job.get().getTaxIdBusinessLicense());
                notifications.setOwnerId(acceptRejectDto.getSkillOwnerEntityId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

                if (Boolean.TRUE.equals(acceptRejectDto.getAccepted())) {
                    notifications.setContent("Hi " + job.get().getSkillSeeker().getSkillSeekerName() + ", the candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " have accepted the invite for the interview of " + job.get().getJobId() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                }
                if (Boolean.FALSE.equals(acceptRejectDto.getAccepted())) {
                    notifications.setContent("Hi " + job.get().getSkillSeeker().getSkillSeekerName() + ", the candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " have rejected  the invite for the interview of " + job.get().getJobId() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                }
                notifications.setStage(0);
                notifications.setSkillOwnerId(acceptRejectDto.getSkillOwnerEntityId());
                seekerNotificationsRepository.save(notifications);
                acceptBySkillOwnerNotificationToPartner(acceptRejectDto);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

    }

    @Override
    public ContentEntity acceptInterviewBySkillOwnerNotification(String jobId, int ownerId) {
        try {
            ContentEntity content = contentRepository.findByAccept();
            SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(ownerId));
            Optional<Job> job = jobRepository.findByJobId(jobId);
            if (skillOwner.isPresent() && job.isPresent()) {
                notifications.setContentId(content.getId());

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillSeekerEntityId(job.get().getSkillSeeker().getId());
                notifications.setJobId(jobId);
                notifications.setOwnerId(ownerId);
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

                notifications.setContent("Hi " + job.get().getSkillSeeker().getSkillSeekerName() + ",  the candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + "has accepted the interview request for the " + job.get().getJobTitle() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

                notifications.setTaxIdBusinessLicense(job.get().getTaxIdBusinessLicense());
                notifications.setStage(0);
                notifications.setSkillOwnerId(ownerId);
                seekerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

    }

    public ContentEntity acceptBySkillOwnerNotificationToPartner(AcceptRejectDto acceptRejectDto) {
        try {
            ContentEntity content = contentRepository.findByAccept();
            PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(acceptRejectDto.getSkillOwnerEntityId()));
            Optional<Job> job = jobRepository.findByJobId(acceptRejectDto.getJobId());
            if (skillOwner.isPresent() && job.isPresent()) {

                Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId());

                if (selectionPhase.get().getAccepted() || !selectionPhase.get().getAccepted()) {
                    notifications.setContentId(content.getId());


                    notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                    notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                    notifications.setJobId(acceptRejectDto.getJobId());
                    notifications.setOwnerId(acceptRejectDto.getSkillOwnerEntityId());
                    notifications.setDate(new Date());
                    notifications.setMarkAsRead(false);
                    notifications.setContentObj(content);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                    DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                    LocalDateTime now = LocalDateTime.now();
                    TimeZone.setDefault(TimeZone.getTimeZone("EST")); //TODO - As of now, we're having in EST

                    if (Boolean.TRUE.equals(acceptRejectDto.getAccepted())) {
                        notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", the candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " has accepted their shortlisting for job: " + job.get().getJobTitle() + "(" + job.get().getJobId() + ")" + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                    }
                    if (Boolean.FALSE.equals(acceptRejectDto.getAccepted())) {
                        notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", the candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + "has rejected  their shortlisting for job: " + job.get().getJobTitle() + "(" + job.get().getJobId() + ")" + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                    }
                    notifications.setStage(0);
                    partnerNotificationsRepository.save(notifications);
                    return content;
                } else {
                    throw new NullPointerException();
                }

            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


    @Override
    public ContentEntity newSlotBySeekerNotification(String jobId, int skillOwnerId, NewSlotRequestBySeekerDto newSlotRequestBySeekerDto) {

        try {
            ContentEntity content = contentRepository.findByNewSlot();
            Optional<Job> job = jobRepository.findByJobId(jobId);
            Optional<SkillOwnerEntity> ownerId = skillOwnerRepository.findById(skillOwnerId);

            if (job.isPresent()) {
                OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
                notifications.setContentId(content.getId());

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle()
                        + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | "
                        + job.get().getJobTitle() + " | " + content.getTitle());

                notifications.setSkillOwnerEntityId(newSlotRequestBySeekerDto.getSkillOwnerEntityId());
                notifications.setJobId(newSlotRequestBySeekerDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

                Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(job.get().getJobId(), skillOwnerId);

                selectionPhase.get().getRequirementPhase().stream().filter(f -> selectionPhase.get().getCurrentStage()
                                == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared")))
                        .map(f -> "Hi " + ownerId.get().getFirstName() + " " + ownerId.get().getLastName() + ", "
                                + job.get().getSkillSeeker().getSkillSeekerName() + " has requested you to select new slots for the the round" + " "
                                + f.getStage() + ":" + f.getRequirementPhaseName() + " of the job " + job.get().getJobTitle() + "(" + job.get().getJobId()
                                + ")." + " " + "Click below to select new slots" + " on " + formatter.format(now) + ", Time : "
                                + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).forEachOrdered(notifications::setContent);

                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setContentObj(content);
                notifications.setStage(0);
                ownerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

    }

    @Override
    public ContentEntity newSlotBySeekerNotification(int skillOwnerId) {

//        try {
        ContentEntity content = contentRepository.findByCommonSlot();
        Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(skillOwnerId);
        if (skillOwner.isPresent()) {
            OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
            notifications.setContentId(content.getId());
            notifications.setTitle(content.getTitle());
            notifications.setSkillOwnerEntityId(skillOwnerId);
            notifications.setJobId(null);
            notifications.setDate(new Date());
            notifications.setMarkAsRead(false);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

            notifications.setContent("Hi " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + ", Please select new set of common slot for the jobs/interview rounds.  Click below to select new slots" + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            notifications.setRequirementPhaseName(null);
            notifications.setDateOfInterview(null);
            notifications.setContentObj(content);
            notifications.setStage(0);
            ownerNotificationsRepository.save(notifications);
            return content;
        } else {
            logger.info("Owner Not found");
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
        return null;
    }


    @Override
    public ContentEntity slotConfirmedBySeekerNotification(SlotConfirmBySeekerDto slotConfirmBySeekerDto) {

        try {
            ContentEntity content = contentRepository.findByConfirmBySeeker();
            Optional<Job> job = jobRepository.findByJobId(slotConfirmBySeekerDto.getJobId());
            Optional<SkillOwnerEntity> ownerId = skillOwnerRepository.findById(slotConfirmBySeekerDto.getSkillOwnerEntityId());
            Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(job.get().getJobId(), slotConfirmBySeekerDto.getSkillOwnerEntityId());
            if (job.isPresent()) {
                OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setSkillOwnerEntityId(slotConfirmBySeekerDto.getSkillOwnerEntityId());
                notifications.setJobId(slotConfirmBySeekerDto.getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST


                selectionPhase.get().getRequirementPhase().stream().filter(f -> selectionPhase.get().getCurrentStage() == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared"))).map(f -> "Hi " + ownerId.get().getFirstName() + " " + ownerId.get().getLastName() + ", slots are confirmed for the round " + f.getStage() + "-" + f.getRequirementPhaseName() + " for the job - " + job.get().getJobTitle() + " by the " + job.get().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).forEach(notifications::setContent);

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setContentObj(content);
                notifications.setStage(0);
                ownerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    public ContentEntity shortlistBySeekerNotification(SelectionPhase selectionPhase) {
        try {
            ContentEntity content = contentRepository.findByShortlist();
            Optional<SkillOwnerEntity> ownerId = skillOwnerRepository.findById(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            if (job.isPresent()) {

                selectionPhase.setJob(job.get());
                OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setJobId(selectionPhase.getJob().getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST")); //TODO - As of now, we're having in EST

                notifications.setContent("Hi " + ownerId.get().getFirstName() + " " + ownerId.get().getLastName() + ", you are shortlisted for the - " + job.get().getJobTitle() + ", by the " + job.get().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());

                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setContentObj(content);
                notifications.setStage(selectionPhase.getCurrentStage());
                ownerNotificationsRepository.save(notifications);
                shortlistBySeekerNotificationToPartner(selectionPhase);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    public ContentEntity shortlistBySeekerNotificationToPartner(SelectionPhase selectionPhase) {
        try {
            ContentEntity content = contentRepository.findByShortlist();
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));

            if (job.isPresent() && skillOwner.isPresent()) {
                selectionPhase.setJob(job.get());
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(selectionPhase.getJob().getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST")); //TODO - As of now, we're having in EST

                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", your candidate " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " shortlisted for the job " + selectionPhase.getJob().getJobTitle() + " by the " + job.get().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());

                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setContentObj(content);
                notifications.setStage(selectionPhase.getCurrentStage());
                partnerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    public ContentEntity qualifiedNotification(SelectionPhase selectionPhase, RequirementPhase requirementPhase) {
        try {
            ContentEntity content = contentRepository.findByQualified();
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
            notifications.setContentId(content.getId());
            notifications.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
            notifications.setJobId(selectionPhase.getJob().getJobId());
            notifications.setDate(new Date());
            notifications.setMarkAsRead(false);
            TimeZone.setDefault(TimeZone.getTimeZone("EST")); //TODO - As of now, we're having in EST


            //notifications.setContent("Hi " + selectionPhase.getSkillOwnerEntity().getFirstName() + " " + selectionPhase.getSkillOwnerEntity().getLastName() + ", you have cleared the round " + requirementPhase.getStage() + " - " + requirementPhase.getRequirementPhaseName() + "  and qualified for the next round " + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            notifications.setContent("Hi " + selectionPhase.getSkillOwnerEntity().getFirstName() + " " + selectionPhase.getSkillOwnerEntity().getLastName() + ", You have cleared the round " + requirementPhase.getStage() + " - " + requirementPhase.getRequirementPhaseName() + " for the position of " + job.get().getJobTitle() + " with the client " + selectionPhase.getJob().getSkillSeeker().getSkillSeekerName() + " which was held on " + requirementPhase.getDateOfInterview() + "  and  qualified for the next round");

            notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
            notifications.setRequirementPhaseName(requirementPhase.getRequirementPhaseName());
            notifications.setDateOfInterview(null);
            notifications.setContentObj(content);
            notifications.setStage(requirementPhase.getStage());
            ownerNotificationsRepository.save(notifications);
            qualifiedNotificationToPartner(selectionPhase, requirementPhase);
            return content;
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    public ContentEntity qualifiedNotificationToPartner(SelectionPhase selectionPhase, RequirementPhase requirementPhase) {
        try {
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));
            Optional<Job> jobId = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
//            Optional<SkillSeeker>skillSeeker=skillSeekerRepository.
            if (skillOwner.isPresent()) {
                ContentEntity content = contentRepository.findByQualified();
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(selectionPhase.getJob().getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST")); //TODO - As of now, we're having in EST


                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", Your " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " has cleared the round " + requirementPhase.getStage() + " " + requirementPhase.getRequirementPhaseName() + " and qualified for the next round for the  " + jobId.get().getJobTitle() + " by the " + jobId.get().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));


                notifications.setTitle(null == jobId.get().getSeekerProject() ? Default + " | " + jobId.get().getJobTitle() + " | " + content.getTitle() : jobId.get().getSeekerProject().getTitle() + " | " + jobId.get().getJobTitle() + " | " + content.getTitle());
                notifications.setOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setRequirementPhaseName(requirementPhase.getRequirementPhaseName());
                notifications.setDateOfInterview(null);
                notifications.setContentObj(content);
                notifications.setStage(requirementPhase.getStage());
                partnerNotificationsRepository.save(notifications);
                return content;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public ContentEntity rejectedNotification(SelectionPhase selectionPhase) {
        try {
            ContentEntity content = contentRepository.findByRejected();
            Optional<Job> jobId = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            Optional<SelectionPhase> selectionPhaseList = selectionPhaseRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
            OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
            notifications.setContentId(content.getId());
            notifications.setTitle(null == jobId.get().getSeekerProject() ? Default + " | " + jobId.get().getJobTitle() + " | " + content.getTitle() : jobId.get().getSeekerProject().getTitle() + " | " + jobId.get().getJobTitle() + " | " + content.getTitle());
            notifications.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
            notifications.setJobId(selectionPhase.getJob().getJobId());
            notifications.setDate(new Date());
            notifications.setMarkAsRead(false);
            notifications.setContentObj(content);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

            //TODO - check the iteration -- Done (Need to check )
            selectionPhaseList.get().getRequirementPhase().stream().filter(f -> selectionPhaseList.get().getCurrentStage() == f.getStage() && (null == f.getStatus() || !f.getStatus().equalsIgnoreCase("cleared"))).map(f -> "Hi " + selectionPhase.getSkillOwnerEntity().getFirstName() + " " + selectionPhase.getSkillOwnerEntity().getLastName() + ", you are rejected in the round " + f.getStage() + "-" + f.getRequirementPhaseName() + " and is disqualified for the job - " + selectionPhase.getJob().getJobTitle() + "by " + selectionPhase.getJob().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).forEachOrdered(notifications::setContent);

            notifications.setRequirementPhaseName(null);
            notifications.setDateOfInterview(null);
            notifications.setStage(selectionPhase.getCurrentStage());
            ownerNotificationsRepository.save(notifications);
            rejectedNotificationToPartner(selectionPhase);
            return content;
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }


    @Transactional
    @Override
    public ContentEntity ownerAvailabilityNotification(int ownerId) {
        Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(ownerId));
        if (skillOwner.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
            if (skillOwner.get().getOwnerSkillStatusEntity().getSkillOwnerStatusId() == 1) {
                ContentEntity content = contentRepository.findByCandidateActive();
                notifications.setContentId(content.getId());
                notifications.setTitle(content.getTitle());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(null);
                notifications.setDate(new Date());
                notifications.setOwnerId(skillOwner.get().getSkillOwnerEntityId());
                notifications.setDateOfInterview(null);
                notifications.setStage(0);
                notifications.setRequirementPhaseName(null);
                notifications.setTimeOfInterview(null);
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);
                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " is now active and  available for hiring" + " from " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                partnerNotificationsRepository.save(notifications);
                return content;
            } else {
                ContentEntity content = contentRepository.findByCandidateInActive();
                notifications.setContentId(content.getId());
                notifications.setTitle(content.getTitle());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(null);
                notifications.setDate(new Date());
                notifications.setOwnerId(skillOwner.get().getSkillOwnerEntityId());
                notifications.setDateOfInterview(null);
                notifications.setStage(0);
                notifications.setRequirementPhaseName(null);
                notifications.setTimeOfInterview(null);
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);
                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + " is now inactive and is not available for hiring. They will not be listed in the talent pool anymore" + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                partnerNotificationsRepository.save(notifications);
                return content;
            }
        }
        return null;
    }


    public void rejectedNotificationToPartner(SelectionPhase selectionPhase) {
        try {
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            if (skillOwner.isPresent()) {
                ContentEntity content = contentRepository.findByRejected();
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());

                notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());
                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(selectionPhase.getJob().getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setContentObj(content);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + " is rejected for Round " + selectionPhase.getCurrentStage() + " for " + selectionPhase.getJob().getJobTitle() + " by the " + selectionPhase.getJob().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setStage(selectionPhase.getCurrentStage());
                partnerNotificationsRepository.save(notifications);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public ContentEntity reinitiateNotification(SelectionPhase selectionPhase) {
        try {
            ContentEntity content = contentRepository.findByReinitiation();
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
            notifications.setContentId(content.getId());
            notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle() + " | " + content.getTitle() : job.get().getSeekerProject().getTitle() + " | " + job.get().getJobTitle() + " | " + content.getTitle());

            notifications.setSkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
            notifications.setJobId(selectionPhase.getJob().getJobId());
            notifications.setDate(new Date());
            notifications.setMarkAsRead(false);
            notifications.setContentObj(content);
            notifications.setSeekerId(job.get().getSkillSeeker().getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

            notifications.setContent("Hi " + selectionPhase.getSkillOwnerEntity().getFirstName() + " "
                    + selectionPhase.getSkillOwnerEntity().getLastName()
                    + ", you are reinitiated for the job - " + selectionPhase.getJob().getJobTitle()
                    + "( " + selectionPhase.getJob().getJobId() + " )" + ", from the" + selectionPhase.getJob().getSkillSeeker().getSkillSeekerName()
                    + ". Please accept the invite to proceed with the interview " + " on " + formatter.format(now) + ", Time : "
                    + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())

            );
            notifications.setRequirementPhaseName(null);
            notifications.setDateOfInterview(null);
            notifications.setStage(selectionPhase.getCurrentStage());
            ownerNotificationsRepository.save(notifications);
            reinitiateNotificationToPartner(selectionPhase);
            return content;
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(PARAMS_MISMATCH.getErrorCode(), PARAMS_MISMATCH.getErrorDesc());
        }
    }


    @Override
    public ContentEntity msaStatusNotification(SkillSeekerMSAEntity skillSeekerMSAEntity, Notification notificationDto) {

        try {
            Optional<SkillSeekerEntity> seeker = skillSeekerRepository.findById(skillSeekerMSAEntity.getSkillSeekerId());
            Optional<SkillOwnerEntity> byId = skillOwnerRepository.findById(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
            if (skillSeekerMSAEntity.getMsaStatus().getId() == 1) {
                ContentEntity content = contentRepository.findByInitiated();
                superAdminNotifications.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + " by " + " " + " " + seeker.get().getSkillSeekerName() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentId(content.getId());
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    superAdminNotifications.setProjectName(Default);
                } else {
                    superAdminNotifications.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA-" + content.getTitle());
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setSkillSeekerEntityId(skillSeekerMSAEntity.getSkillSeekerId());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setMsaId(skillSeekerMSAEntity.getId());
                superAdminNotifications.setMsaStatus(skillSeekerMSAEntity.getMsaStatus().getStatus());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            }

            if (skillSeekerMSAEntity.getMsaStatus().getId() == 3) {
                ContentEntity content = contentRepository.findByInProgress();
                superAdminNotifications.setContent(" MSA is in Status as" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + "which is generated by " + " " + seeker.get().getSkillSeekerName() + " " + "for" + " " + skillSeekerMSAEntity.getSkillOwnerEntity().getFirstName() + " " + "for the job" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentObj(content);

                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    superAdminNotifications.setProjectName(Default);
                } else {
                    superAdminNotifications.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }

                superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA-" + content.getTitle());
                superAdminNotifications.setSkillSeekerEntityId(skillSeekerMSAEntity.getSkillSeekerId());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setMsaId(skillSeekerMSAEntity.getId());
                superAdminNotifications.setMsaStatus(skillSeekerMSAEntity.getMsaStatus().getStatus());
                superAdminNotifications.setDate(new Date());

                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            }

            if (skillSeekerMSAEntity.getMsaStatus().getId() == 2) {
                ContentEntity content = contentRepository.findByInWriting();
                SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
                PartnerNotificationsEntity partnerNotificationsEntity = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    notifications.setProjectName(Default);
                } else {
                    notifications.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                notifications.setTitle(notifications.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA -" + content.getTitle());
                notifications.setDate(new Date());
                notifications.setContentObj(content);
                notifications.setSkillSeekerEntityId(skillSeekerMSAEntity.getSkillSeekerId());
                notifications.setSkillOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
                notifications.setTaxIdBusinessLicense(skillSeekerMSAEntity.getJobId().getTaxIdBusinessLicense());
                notifications.setMarkAsRead(false);
                notifications.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + "is in the Status" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

//                partnerNotificationsEntity.setContentId(content.getId());
//                partnerNotificationsEntity.setTitle(content.getTitle());
//                partnerNotificationsEntity.setContentObj(content);
//                partnerNotificationsEntity.setSkillPartnerEntityId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillPartnerEntity().getSkillPartnerId());
//                partnerNotificationsEntity.setOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
//                partnerNotificationsEntity.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
//                partnerNotificationsEntity.setMarkAsRead(false);
//
//                partnerNotificationsEntity.setDate(new Date());
//                partnerNotificationsEntity.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + "is in the Status" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus()+" "+" on "+" "+formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                seekerNotificationsRepository.save(notifications);

//                partnerNotificationsRepository.save(partnerNotificationsEntity);
                return content;

            }
            if (skillSeekerMSAEntity.getMsaStatus().getId() == 4) {
                ContentEntity content = contentRepository.findBySend();
                PartnerNotificationsEntity partnerNotificationsEntity = new PartnerNotificationsEntity();
                partnerNotificationsEntity.setContentId(content.getId());
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    partnerNotificationsEntity.setProjectName(Default);
                } else {
                    partnerNotificationsEntity.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                partnerNotificationsEntity.setTitle(partnerNotificationsEntity.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA - " + content.getTitle());
                partnerNotificationsEntity.setContentObj(content);
                partnerNotificationsEntity.setSkillPartnerEntityId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillPartnerEntity().getSkillPartnerId());
                partnerNotificationsEntity.setOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                partnerNotificationsEntity.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
                partnerNotificationsEntity.setMarkAsRead(false);
                partnerNotificationsEntity.setDate(new Date());
                partnerNotificationsEntity.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + "is in the Status" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

                partnerNotificationsRepository.save(partnerNotificationsEntity);
                return content;
            }
            if (skillSeekerMSAEntity.getMsaStatus().getId() == 5) {
                ContentEntity content = contentRepository.findByAccepted();
                superAdminNotifications.setContent(" MSA is in Status as" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + "which is generated by " + " " + seeker.get().getSkillSeekerName() + " " + "for" + " " + skillSeekerMSAEntity.getSkillOwnerEntity().getFirstName() + " " + "for the job" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentObj(content);
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    superAdminNotifications.setProjectName(Default);
                } else {
                    superAdminNotifications.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA-" + content.getTitle());
                superAdminNotifications.setSkillSeekerEntityId(skillSeekerMSAEntity.getSkillSeekerId());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setMsaId(skillSeekerMSAEntity.getId());
                superAdminNotifications.setMsaStatus(skillSeekerMSAEntity.getMsaStatus().getStatus());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            }

            if (skillSeekerMSAEntity.getMsaStatus().getId() == 6) {
                ContentEntity content = contentRepository.findByReleased();
                SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
                PartnerNotificationsEntity partnerNotificationsEntity = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());
                notifications.setDate(new Date());
                notifications.setContentObj(content);
                notifications.setSkillSeekerEntityId(skillSeekerMSAEntity.getSkillSeekerId());
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    notifications.setProjectName(Default);
                } else {
                    notifications.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                notifications.setTitle(notifications.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA -" + content.getTitle());
                notifications.setSkillOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
                notifications.setMarkAsRead(false);
                notifications.setTaxIdBusinessLicense(skillSeekerMSAEntity.getJobId().getSkillSeeker().getTaxIdBusinessLicense());
                notifications.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + "is in the Status" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                if (skillSeekerMSAEntity.getSkillSeekerProject() == null) {
                    partnerNotificationsEntity.setProjectName(Default);
                } else {
                    partnerNotificationsEntity.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                }
                partnerNotificationsEntity.setContentId(content.getId());
                partnerNotificationsEntity.setTitle(partnerNotificationsEntity.getProjectName() + " | " + skillSeekerMSAEntity.getJobId().getJobTitle() + " | " + "MSA -" + content.getTitle());
                partnerNotificationsEntity.setContentObj(content);
                partnerNotificationsEntity.setSkillPartnerEntityId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillPartnerEntity().getSkillPartnerId());
                partnerNotificationsEntity.setOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                partnerNotificationsEntity.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
                partnerNotificationsEntity.setMarkAsRead(false);
                partnerNotificationsEntity.setDate(new Date());
                partnerNotificationsEntity.setContent("MSA has been created for" + " " + byId.get().getFirstName() + " " + "for" + " " + skillSeekerMSAEntity.getJobId().getJobId() + " " + "is in the Status" + " " + skillSeekerMSAEntity.getMsaStatus().getStatus() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                partnerNotificationsRepository.save(partnerNotificationsEntity);
                seekerNotificationsRepository.save(notifications);
                return content;
            }


        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
        return null;
    }

    @Override
    public ContentEntity sowStatusNotification(StatementOfWorkEntity statementOfWorkEntity, Notification notificationDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        LocalDateTime now = LocalDateTime.now();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));

        SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();

        if (statementOfWorkEntity.getSowStatus().getId() == 21) {
            ContentEntity content = contentRepository.findByInReview();
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
            superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " | " + content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
            superAdminNotifications.setContent(content.getTitle());
            superAdminNotifications.setSowId(statementOfWorkEntity.getSowId().getId());
            superAdminNotifications.setSowStatus(statementOfWorkEntity.getSowStatus().getStatus());
            superAdminNotifications.setMarkAsRead(false);
            superAdminNotifications.setDate(new Date());
            superAdminNotifications.setSkillSeekerEntityId(statementOfWorkEntity.getSkillSeekerId());
            superAdminNotifications.setContent("SOW is  " + statementOfWorkEntity.getSowStatus().getStatus() + " at" + " " + statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + " for the project: " + " " + statementOfWorkEntity.getSkillSeekerProject().getTitle() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        } else if (statementOfWorkEntity.getSowStatus().getId() == 22) {
            ContentEntity content = contentRepository.findByApproved();
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
            superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " | " + content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
            superAdminNotifications.setContent(content.getTitle());
            superAdminNotifications.setSowId(statementOfWorkEntity.getSowId().getId());
            superAdminNotifications.setSowStatus(statementOfWorkEntity.getSowStatus().getStatus());
            superAdminNotifications.setMarkAsRead(false);
            superAdminNotifications.setDate(new Date());
            superAdminNotifications.setSkillSeekerEntityId(statementOfWorkEntity.getSkillSeekerId());
            superAdminNotifications.setContent("SOW has been " + statementOfWorkEntity.getSowStatus().getStatus() + " by" + " " + statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + " for the project: " + " " + statementOfWorkEntity.getSkillSeekerProject().getTitle() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }

        SeekerNotificationsEntity seekerNotificationsEntity = new SeekerNotificationsEntity();

        if (statementOfWorkEntity.getSowStatus().getId() == 20) {
            ContentEntity content = contentRepository.findBySend();
            seekerNotificationsEntity.setContent("SOW has been Send to" + " " + statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + " for the project: " + " " + statementOfWorkEntity.getSkillSeekerProject().getTitle() + " " + " by " + "Flexcub" + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            seekerNotificationsEntity.setContentId(content.getId());
            seekerNotificationsEntity.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
            seekerNotificationsEntity.setTitle(seekerNotificationsEntity.getProjectName() + " | " + "PO-" + content.getTitle());
            seekerNotificationsEntity.setContentObj(content);
            seekerNotificationsEntity.setDate(new Date());
            seekerNotificationsEntity.setSowId(statementOfWorkEntity.getSowId().getId());
            seekerNotificationsEntity.setSowStatus(statementOfWorkEntity.getSowStatus().getStatus());
            seekerNotificationsEntity.setSkillSeekerEntityId(statementOfWorkEntity.getSkillSeekerId());
            seekerNotificationsEntity.setTaxIdBusinessLicense(statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getTaxIdBusinessLicense());
            seekerNotificationsEntity.setMarkAsRead(false);
            seekerNotificationsRepository.save(seekerNotificationsEntity);
            return content;
        }
        return null;
    }


    @Override
    public ContentEntity poStatusNotification(PoEntity poEntity, Notification notificationDto) {

        try {
            SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));

            if (poEntity.getPoStatus().getId() == 26) {
                ContentEntity content = contentRepository.findByInReview();
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " | " + content.getTitle());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                superAdminNotifications.setContent(content.getTitle());
                superAdminNotifications.setPoId(poEntity.getPoId().getId());
                superAdminNotifications.setPoStatus(poEntity.getPoStatus().getStatus());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setSkillSeekerEntityId(poEntity.getSkillSeekerId());
                superAdminNotifications.setContent("PO is  " + poEntity.getPoStatus().getStatus() + " at" + " " + poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + " for the project: " + " " + poEntity.getSkillSeekerProject().getTitle() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            } else if (poEntity.getPoStatus().getId() == 27) {
                ContentEntity content = contentRepository.findByApproved();
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                superAdminNotifications.setTitle(superAdminNotifications.getProjectName() + " | " + poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " | " + content.getTitle());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                superAdminNotifications.setContent(content.getTitle());
                superAdminNotifications.setPoId(poEntity.getPoId().getId());
                superAdminNotifications.setPoStatus(poEntity.getPoStatus().getStatus());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setSkillSeekerEntityId(poEntity.getSkillSeekerId());
                superAdminNotifications.setContent("PO has been " + poEntity.getPoStatus().getStatus() + "by" + " " + poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + " for the project: " + " " + poEntity.getSkillSeekerProject().getTitle() + " " + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            }


            SeekerNotificationsEntity seekerNotificationsEntity = new SeekerNotificationsEntity();

            if (poEntity.getPoStatus().getId() == 4) {
                ContentEntity content = contentRepository.findBySend();
                seekerNotificationsEntity.setContent("PO has been Send to" + " " + poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName() + " " + "for the project: " + " " + poEntity.getSkillSeekerProject().getTitle() + " " + " by " + "SuperAdmin(FLEXCUB)" + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                seekerNotificationsEntity.setContentId(content.getId());
                seekerNotificationsEntity.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                seekerNotificationsEntity.setTitle(seekerNotificationsEntity.getProjectName() + " | " + "PO-" + content.getTitle());
                seekerNotificationsEntity.setContentObj(content);
                seekerNotificationsEntity.setDate(new Date());
                seekerNotificationsEntity.setJobId(poEntity.getJobId().getJobId());
                seekerNotificationsEntity.setPoId(poEntity.getPoId().getId());
                seekerNotificationsEntity.setPoStatus(poEntity.getPoStatus().getStatus());
                seekerNotificationsEntity.setSkillSeekerEntityId(poEntity.getSkillSeekerId());
                seekerNotificationsEntity.setTaxIdBusinessLicense(poEntity.getJobId().getSkillSeeker().getTaxIdBusinessLicense());
                seekerNotificationsEntity.setMarkAsRead(false);
                seekerNotificationsRepository.save(seekerNotificationsEntity);
                return content;
            }

        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
        return null;
    }

    @Override
    public ContentEntity partnerInvoiceStatusNotification(Invoice invoice, Notification notificationDto) {
        PartnerNotificationsEntity partnerNotifications = new PartnerNotificationsEntity();
        SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        LocalDateTime now = LocalDateTime.now();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        if (invoice.getInvoiceStatus().getId() == 1) {
            ContentEntity content = contentRepository.findByInvoiceSubmitted();
            superAdminNotifications.setContent("The Invoice has been generated for" + " " + invoice.getId() + " " + "and the status is changes as " + " " + invoice.getInvoiceStatus().getStatus() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle(content.getTitle());
            superAdminNotifications.setDate(new Date());
            invoice.getInvoiceData().forEach(adminInvoiceData -> {
                superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            });
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setMarkAsRead(false);
            superAdminNotifications.setDate(new Date());
            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }
        if (invoice.getInvoiceStatus().getId() == 2) {
            ContentEntity content = contentRepository.findByInvoiceApproval();
            partnerNotifications.setContent("The Invoice status is Updated for" + " " + invoice.getId() + " " + "and the status is changes as " + " " + invoice.getInvoiceStatus().getStatus() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setTitle(content.getTitle());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setMarkAsRead(false);
            partnerNotifications.setDate(new Date());
            partnerNotifications.setSkillPartnerEntityId(invoice.getSkillPartner().getSkillPartnerId());
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        }
        if (invoice.getInvoiceStatus().getId() == 3) {
            ContentEntity content = contentRepository.findByInvoiceReject();
            partnerNotifications.setContent("The Invoice status is Updated for" + " " + invoice.getId() + " " + "and the status is changes as " + " " + invoice.getInvoiceStatus().getStatus() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setTitle(content.getTitle());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setMarkAsRead(false);
            partnerNotifications.setDate(new Date());
            partnerNotifications.setSkillPartnerEntityId(invoice.getSkillPartner().getSkillPartnerId());
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        }
        if (invoice.getInvoiceStatus().getId() == 5) {
            ContentEntity content = contentRepository.findByInvoicePending();
            partnerNotifications.setContent("The Invoice status is Updated for" + " " + invoice.getId() + " " + "and the status is changes as " + " " + invoice.getInvoiceStatus().getStatus() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setTitle(content.getTitle());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setMarkAsRead(false);
            partnerNotifications.setDate(new Date());
            partnerNotifications.setSkillPartnerEntityId(invoice.getSkillPartner().getSkillPartnerId());
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        }
        if (invoice.getInvoiceStatus().getId() == 4) {
            ContentEntity content = contentRepository.findByInvoicePaid();
            partnerNotifications.setContent("The Invoice status is Updated for" + " " + invoice.getId() + " " + "and the status is changes as " + " " + invoice.getInvoiceStatus().getStatus() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setTitle(content.getTitle());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setMarkAsRead(false);
            partnerNotifications.setDate(new Date());
            partnerNotifications.setSkillPartnerEntityId(invoice.getSkillPartner().getSkillPartnerId());
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        }

        return null;
    }


    public void reinitiateNotificationToPartner(SelectionPhase selectionPhase) {
        try {
            Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId()));
            Optional<Job> job = jobRepository.findByJobId(selectionPhase.getJob().getJobId());
            if (skillOwner.isPresent()) {
                ContentEntity content = contentRepository.findByReinitiation();
                PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
                notifications.setContentId(content.getId());

                notifications.setTitle(selectionPhase.getJob().getSkillSeeker().getSkillSeekerName() + " | " + content.getTitle());

                notifications.setSkillPartnerEntityId(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                notifications.setJobId(selectionPhase.getJob().getJobId());
                notifications.setDate(new Date());
                notifications.setMarkAsRead(false);
                notifications.setContentObj(content);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
                DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
                LocalDateTime now = LocalDateTime.now();
                TimeZone.setDefault(TimeZone.getTimeZone("EST"));   //TODO - As of now, we're having in EST

                notifications.setContent("Hi " + skillOwner.get().getSkillPartnerEntity().getBusinessName() + ", Interview for" + selectionPhase.getJob().getJobTitle() + " is Re-initiated for " + skillOwner.get().getFirstName() + "(" + skillOwner.get().getSkillOwnerEntityId() + ")" + " by the " + selectionPhase.getJob().getSkillSeeker().getSkillSeekerName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                notifications.setRequirementPhaseName(null);
                notifications.setDateOfInterview(null);
                notifications.setOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                notifications.setStage(selectionPhase.getCurrentStage());
                partnerNotificationsRepository.save(notifications);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(PARAMS_MISMATCH.getErrorCode(), PARAMS_MISMATCH.getErrorDesc());
        }
    }

    /**
     * @param id skillSeeker
     * @return boolean
     */

    @Override
    public Boolean markAsReadSeeker(int id) {
        Optional<SeekerNotificationsEntity> seekerNotification = Optional.ofNullable(seekerNotificationsRepository.findById(id));
        if (seekerNotification.isPresent()) {
            if (!seekerNotification.get().getMarkAsRead()) {
                seekerNotification.get().setMarkAsRead(true);
                seekerNotificationsRepository.save(seekerNotification.get());
                return true;
            } else {
                seekerNotification.get().setMarkAsRead(false);
                seekerNotificationsRepository.save(seekerNotification.get());
                return false;
            }
        }
        throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
    }

    @Override
    public Boolean markAsReadOwner(int id) {
        Optional<OwnerNotificationsEntity> ownerNotification = Optional.ofNullable(ownerNotificationsRepository.findById(id));
        if (ownerNotification.isPresent()) {
            if (!ownerNotification.get().getMarkAsRead()) {
                ownerNotification.get().setMarkAsRead(true);
                ownerNotificationsRepository.save(ownerNotification.get());
                return true;
            } else {
                ownerNotification.get().setMarkAsRead(false);
                ownerNotificationsRepository.save(ownerNotification.get());
                return false;
            }
        }
        throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
    }

    @Override
    public Boolean markAsReadPartner(int id) {
        Optional<PartnerNotificationsEntity> partnerNotification = Optional.ofNullable(partnerNotificationsRepository.findById(id));
        if (partnerNotification.isPresent()) {
            if (!partnerNotification.get().getMarkAsRead()) {
                partnerNotification.get().setMarkAsRead(true);
                partnerNotificationsRepository.save(partnerNotification.get());
                return true;
            } else {
                partnerNotification.get().setMarkAsRead(false);
                partnerNotificationsRepository.save(partnerNotification.get());
                return false;
            }
        }
        throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
    }

    @Override
    public Boolean markAsReaderAdmin(int id) {
        Optional<SuperAdminNotifications> adminNotifications = superAdminNotificationRepositoy.findById(id);
        if (adminNotifications.isPresent()) {
            if (!adminNotifications.get().getMarkAsRead()) {
                adminNotifications.get().setMarkAsRead(true);
                superAdminNotificationRepositoy.save(adminNotifications.get());
                return true;
            } else {
                adminNotifications.get().setMarkAsRead(false);
                superAdminNotificationRepositoy.save(adminNotifications.get());
                return false;
            }
        }
        throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
    }


    @Override
    public List<OwnerNotificationsEntity> getLastFiveNotificationOfOwner(int ownerId) {
        try {
            List<OwnerNotificationsEntity> ownerNotificationsEntities = ownerNotificationsRepository.findLastFiveNotification(ownerId);
            if (!ownerNotificationsEntities.isEmpty()) {
                logger.info("SkillPartnerServiceImpl || getSkillPartnerNotifications || getLastFiveNotificationOfPartner");
                return ownerNotificationsEntities;
            } else {
                throw new ServiceException(INVALID_ID_REQUEST.getErrorCode(), INVALID_ID_REQUEST.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }


    @Override
    public List<SeekerNotificationsEntity> getSeekerLastFiveNotification(int id) {
        try {
            List<SeekerNotificationsEntity> seekerNotificationsEntities = new ArrayList<>();
            Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(id);
            if (skillSeeker.isPresent()) {
                Optional<List<SeekerNotificationsEntity>> bySkillSeekerEntityId = Optional.ofNullable(seekerNotificationsRepository.findLastFiveNotificationByTaxId(skillSeeker.get().getTaxIdBusinessLicense()));
                if (bySkillSeekerEntityId.isPresent() && !bySkillSeekerEntityId.get().isEmpty()) {

                    Map<Integer, List<Integer>> map = new HashMap<>();
                    map.put(1, List.of(2));
                    map.put(2, List.of(2));
                    map.put(3, List.of(2, 3));
                    map.put(4, List.of(2));
                    map.put(6, List.of(1, 2, 3, 4, 8));
                    map.put(10, List.of(2, 3, 8));

                    List<SeekerModulesEntity> accessEntities = skillSeekerService.getAccessDetails(id);
                    if (accessEntities.isEmpty()) {
                        return Collections.emptyList();
                    }
                    if (1 == skillSeeker.get().getSubRoles().getId()) {
                        return bySkillSeekerEntityId.get();
                    }
                    for (SeekerNotificationsEntity seekerNotifications : bySkillSeekerEntityId.get()) {
                        for (SeekerModulesEntity accessEntity : accessEntities) {
                            if (map.get(seekerNotifications.getContentId()).contains(accessEntity.getId())) {
                                seekerNotificationsEntities.add(seekerNotifications);
                                break;
                            }
                        }
                    }

                } else {
                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
                }
                logger.info("SkillSeekerServiceImpl || getSeekerNotification || get All Notification in seeker ");
                return seekerNotificationsEntities;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


    @Override
    @Transactional
    public List<OwnerDetails> getOwnerDetailsInPartner(int partnerId) {
        try {
            Optional<List<SkillOwnerEntity>> ownerEntities = skillOwnerRepository.findBySkillPartnerId(partnerId);

            List<OwnerDetails> skillOwnerEntities = new ArrayList<>();
            if (ownerEntities.isPresent() && !ownerEntities.get().isEmpty()) {
                List<SkillOwnerEntity> skillOwnerEntities1 = ownerEntities.get();
                skillOwnerEntities1.forEach(skillOwnerEntity -> {
                    OwnerDetails ownerDetails = new OwnerDetails();
                    BeanUtils.copyProperties(skillOwnerEntity, ownerDetails, NullPropertyName.getNullPropertyNames(skillOwnerEntity));
                    ownerDetails.setEmployeeId(skillOwnerEntity.getSkillOwnerEntityId());
                    ownerDetails.setStatus(skillOwnerEntity.isAccountStatus());
                    ownerDetails.setLocation(skillOwnerEntity.getCity() + "," + skillOwnerEntity.getState());
                    ownerDetails.setLeavingDate(null);
                    ownerDetails.setJoinedDate(skillOwnerEntity.getOnBoardingDate());
                    ownerDetails.setEmployeeName(skillOwnerEntity.getFirstName() + " " + skillOwnerEntity.getLastName());
                    ownerDetails.setLevelExperience(skillOwnerEntity.getExpYears() + "+");
                    ownerDetails.setRate(skillOwnerEntity.getRateCard());

                    Optional<List<RequirementPhase>> requirementPhaseList = requirementPhaseRepository.findBySkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
                    Optional<PoEntity> po = poRepository.findByOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
                    Optional<List<SelectionPhase>> selectionPhaseList = selectionPhaseRepository.findBySkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
                    if (po.isPresent() && po.get().getPoStatus().getId() == 6) {
                        ownerDetails.setOwnerStatus("In Contract");
                    } else if (!requirementPhaseList.get().isEmpty()) {
                        selectionPhaseList.get().forEach(selectionPhase -> {
                            if (selectionPhase.getRejectedOn() == null || po.isPresent() && po.get().getPoStatus().getId() == 1) {
                                ownerDetails.setOwnerStatus("In Hiring");
                            } else if (selectionPhase.getRejectedOn() != null) {
                                ownerDetails.setOwnerStatus("In Bench");
                            } else {
                                throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
                            }
                            if (selectionPhase.getCreatedAt() != null && selectionPhase.getRejectedOn() == null) {
                                ownerDetails.setDateOfShortListing(selectionPhase.getCreatedAt());
                            }

                        });
                    } else {
                        ownerDetails.setOwnerStatus("In Bench");
                    }
                    skillOwnerEntities.add(ownerDetails);
                });
            } else {
                throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
            }
            logger.info("SkillPartnerServiceImpl || getOwnerDetailsInPartner || get all owner details in partner with partnerId");
            skillOwnerEntities.sort(Comparator.comparing(OwnerDetails::getEmployeeId).reversed());
            return skillOwnerEntities;
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public List<JobHistory> getJobHistoryInPartner(int ownerId) {
        try {
            Optional<List<SelectionPhase>> bySkillOwnerId = selectionPhaseRepository.findBySkillOwnerId(ownerId);
            List<JobHistory> jobHistories = new ArrayList<>();

            if (bySkillOwnerId.isPresent() && !bySkillOwnerId.get().isEmpty()) {
                List<SelectionPhase> selectionPhaseList = bySkillOwnerId.get();
                selectionPhaseList.forEach(selectionPhase -> {
                    Optional<RegistrationEntity> registrationEntity = registrationRepository.findById(selectionPhase.getJob().getSkillSeeker().getId());
                    JobHistory jobHistory = new JobHistory();
                    BeanUtils.copyProperties(selectionPhase, jobHistory, NullPropertyName.getNullPropertyNames(selectionPhase));
                    jobHistory.setJobId(selectionPhase.getJob().getJobId());

                    jobHistory.setHiringStatus(selectionPhase.getJob().getStatus());
                    jobHistory.setExpByName(ownerSkillYearOfExperienceRepository.findByExperience(selectionPhase.getSkillOwnerEntity().getExpYears().toString() + "+").getOwnerSkillLevelEntity().getSkillLevelDescription());

                    jobHistory.setRequirementPhases(selectionPhase.getRequirementPhase());
                    jobHistory.setCurrentStage(selectionPhaseService.currentStage(selectionPhase));
                    jobHistory.setJobTitle(selectionPhase.getJob().getJobTitle());
                    jobHistory.setBusinessName(registrationEntity.get().getBusinessName());
                    jobHistory.setLocation(selectionPhase.getJob().getJobLocation());
                    jobHistory.setLevelExperience(selectionPhase.getSkillOwnerEntity().getExpYears().toString() + "+");
                    jobHistories.add(jobHistory);

                });
            } else {
                throw new ServiceException(INVALID_ID_REQUEST.getErrorCode(), INVALID_ID_REQUEST.getErrorDesc());
            }
            logger.info("SkillPartnerServiceImpl || getJobHistoryInPartner || get all Job details in partner with OwnerId");
            return jobHistories;
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public List<PartnerNotificationsEntity> getNotificationForParticularOwner(int ownerId, String jobId) {
        try {
            Optional<List<PartnerNotificationsEntity>> byOwnerId = partnerNotificationsRepository.findByOwnerIdAndJobId(ownerId, jobId);
            List<PartnerNotificationsEntity> notificationList = new ArrayList<>();

            if (!byOwnerId.isEmpty()) {
                List<PartnerNotificationsEntity> partnerNotificationsEntities = byOwnerId.get();
                notificationList.addAll(partnerNotificationsEntities);
            } else {
                throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
            }
            logger.info("SkillPartnerServiceImpl || getJobHistoryInPartner || get all Notifications details in partner with jobId");

            return notificationList;
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public List<PartnerNotificationsEntity> getLastFiveNotificationOfPartner(int partnerId) {

        try {
            List<PartnerNotificationsEntity> partnerNotificationsEntities = partnerNotificationsRepository.findLastFiveNotification(partnerId);
            if (!partnerNotificationsEntities.isEmpty()) {
                logger.info("SkillPartnerServiceImpl || getSkillPartnerNotifications || getLastFiveNotificationOfPartner");

                return partnerNotificationsEntities;
            } else {
                throw new ServiceException(INVALID_ID_REQUEST.getErrorCode(), INVALID_ID_REQUEST.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public List<SeekerNotificationsEntity> getSeekerNotificationByOwner(int ownerId, String jobId) {
        try {
            Optional<List<SeekerNotificationsEntity>> byOwnerId = seekerNotificationsRepository.findByOwnerIdAndJobId(ownerId, jobId);
            List<SeekerNotificationsEntity> notificationList = new ArrayList<>();

            if (!byOwnerId.isEmpty()) {
                List<SeekerNotificationsEntity> ownerNotificationsEntities = byOwnerId.get();
                notificationList.addAll(ownerNotificationsEntities);
            } else {
                throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
            }
            logger.info("SkillSeekerServiceImpl || getSeekerNotification || get Notification in SelectionPhase using ownerId");
            return notificationList;
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public List<OwnerNotificationsEntity> getJobSpecificNotificationForOwner(int skillOwnerId, String jobId) {
        try {
            logger.info("SkillOwnerServiceImpl || getOwnerNotification || get Notification in SelectionPhase using ownerId And JobId");
            List<OwnerNotificationsEntity> bySkillOwnerEntityIdAndJobId = ownerNotificationsRepository.findBySkillOwnerEntityIdAndJobId(skillOwnerId, jobId);
            if (!bySkillOwnerEntityIdAndJobId.isEmpty()) {
                return bySkillOwnerEntityIdAndJobId;
            } else {
                throw new ServiceException();
            }
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public ContentEntity seekerInvoiceStatusNotification(InvoiceAdmin skillSeekerInvoice, Notification notificationDto) {

        SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
        SeekerNotificationsEntity seekerNotificationsEntity = new SeekerNotificationsEntity();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        LocalDateTime now = LocalDateTime.now();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        if (skillSeekerInvoice.getInvoiceStatus().getId() == 1) {
            ContentEntity content = contentRepository.findByInvoiceSubmitted();

            seekerNotificationsEntity.setContent(skillSeekerInvoice.getId() + " has been created by Flexcub Admin");
            seekerNotificationsEntity.setContentId(content.getId());
            seekerNotificationsEntity.setTitle(content.getTitle());
            seekerNotificationsEntity.setContentObj(content);
            seekerNotificationsEntity.setDate(new Date());
            skillSeekerInvoice.getInvoiceData().forEach(adminInvoiceData -> {
                seekerNotificationsEntity.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
                seekerNotificationsEntity.setTaxIdBusinessLicense(adminInvoiceData.getSkillSeeker().getTaxIdBusinessLicense());
            });

//                            superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            seekerNotificationsEntity.setMarkAsRead(false);
            seekerNotificationsRepository.save(seekerNotificationsEntity);
            return content;
        }
        if (skillSeekerInvoice.getInvoiceStatus().getId() == 2) {
            ContentEntity content = contentRepository.findByInvoiceApproval();
            superAdminNotifications.setContent("The invoice status is Changed as" + " " + skillSeekerInvoice.getInvoiceStatus().getStatus() + " " + "for the invoiceId" + " " + skillSeekerInvoice.getId());
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle(content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setDate(new Date());
//                            superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            superAdminNotifications.setMarkAsRead(false);

            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }
        if (skillSeekerInvoice.getInvoiceStatus().getId() == 3) {
            ContentEntity content = contentRepository.findByInvoiceReject();
            superAdminNotifications.setContent("The invoice status is" + " " + skillSeekerInvoice.getInvoiceStatus().getStatus() + " " + "for the InvoiceId" + " " + skillSeekerInvoice.getId());
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle(content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setDate(new Date());
            skillSeekerInvoice.getInvoiceData().forEach(adminInvoiceData -> {
                superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            });
//                        superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            superAdminNotifications.setMarkAsRead(false);
            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }
        if (skillSeekerInvoice.getInvoiceStatus().getId() == 5) {
            ContentEntity content = contentRepository.findByInvoicePending();
            superAdminNotifications.setContent("The invoice status ID is" + " " + skillSeekerInvoice.getInvoiceStatus().getStatus() + " " + "for the InvoiceId" + " " + skillSeekerInvoice.getId() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle(content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setDate(new Date());
//                        superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            superAdminNotifications.setMarkAsRead(false);
            skillSeekerInvoice.getInvoiceData().forEach(adminInvoiceData -> {
                superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            });

            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }
        if (skillSeekerInvoice.getInvoiceStatus().getId() == 4) {
            ContentEntity content = contentRepository.findByInvoicePaid();
            superAdminNotifications.setContent("The invoice status ID is" + " " + skillSeekerInvoice.getInvoiceStatus().getStatus() + " " + "for the InvoiceId" + " " + skillSeekerInvoice.getId() + " on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle(content.getTitle());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setDate(new Date());
            superAdminNotifications.setMarkAsRead(false);
            skillSeekerInvoice.getInvoiceData().forEach(adminInvoiceData -> {
                superAdminNotifications.setSkillSeekerEntityId(adminInvoiceData.getSkillSeeker().getId());
            });


            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        }

        return null;
    }

    @Scheduled(fixedRateString = "PT23H")
    public void seekerNotificationScheduler() {
        try {
            seekerNotificationsRepository.deleteByDate();
            logger.info("NotificationServiceImpl || seekerNotificationsRepository || deleting last 3 months Seeker's Notification data's ", LocalDateTime.now());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
        }
    }

    //commenting it out as requirement changed , will uncomment if requirement comes again
//    @Override
//    public ContentEntity getContractNotificationsInSeeker(int ownerId) {
//
//        Optional<PoEntity> poEntity = poRepository.findByOwnerId(ownerId);
//        Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkRepository.findByOwnerId(ownerId);
//        Optional<SkillSeekerMSAEntity> skillSeekerMSAEntity = Optional.ofNullable(seekerMsaRepository.findByOwnerId(ownerId));
//        Optional<SkillOwnerEntity> byId = skillOwnerRepository.findById(ownerId);
//
//        SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
//        notifications.setOwnerId(ownerId);
//        notifications.setSkillSeekerEntityId(skillSeekerMSAEntity.get().getSkillSeekerId());
//        notifications.setSkillOwnerId(ownerId);
//        notifications.setJobId(skillSeekerMSAEntity.get().getJobId().getJobId());
//        notifications.setDate(new Date());
//        notifications.setMarkAsRead(false);
//        notifications.setTaxIdBusinessLicense(skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getTaxIdBusinessLicense());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
//        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
//        LocalDateTime now = LocalDateTime.now();
//        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//
//        ContentEntity contentpo = contentRepository.findByPO();
//        ContentEntity contentsow = contentRepository.findBySow();
//        ContentEntity contentmsa = contentRepository.findByMSA();
//
//        if (skillSeekerMSAEntity.isPresent()) {
//            notifications.setTitle(contentmsa.getTitle());
//            notifications.setContentId(contentmsa.getId());
//            notifications.setContentObj(contentmsa);
//            notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                    " has created the MSA contract request for the " + byId.get().getFirstName() +" "+ byId.get().getLastName() +
//                    " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//            );
//
//            if (statementOfWorkEntity.isPresent() && poEntity.isPresent()) {
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the PO contract request for the "
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName()
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getLastName() +
//                        " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                seekerNotificationsRepository.save(notifications);
//                return contentpo;
//
//            }
//            if (poEntity.isPresent() && !statementOfWorkEntity.isPresent()) {
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the *PO contract request for the "
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName()
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getLastName() +
//                        " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                seekerNotificationsRepository.save(notifications);
//                return contentpo;
//            }
//            if (!poEntity.isPresent() && statementOfWorkEntity.isPresent()) {
//                notifications.setTitle(contentsow.getTitle());
//                notifications.setContentId(contentsow.getId());
//                notifications.setContentObj(contentsow);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the SOW contract request for the "
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName()
//                        + skillSeekerMSAEntity.get().getSkillOwnerEntity().getLastName() +
//                        " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                seekerNotificationsRepository.save(notifications);
//                return contentsow;
//            } else {
//                seekerNotificationsRepository.save(notifications);
//                return contentmsa;
//            }
//
//        } else {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
//        }
//    }
    //commenting it out as requirement changed , will uncomment if requirement comes again


//    @Override
//    public ContentEntity getContractNotificationsInPartner(int ownerId) {
//
//        Optional<PoEntity> poEntity = poRepository.findByOwnerId(ownerId);
//        Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkRepository.findByOwnerId(ownerId);
//        Optional<SkillSeekerMSAEntity> skillSeekerMSAEntity = Optional.ofNullable(seekerMsaRepository.findByOwnerId(ownerId));
//
//        Optional<SkillOwnerEntity> byId = skillOwnerRepository.findById(ownerId);
//        PartnerNotificationsEntity notifications = new PartnerNotificationsEntity();
//
////        notifications.setOwnerId(skillOwnerRepository.findById(ownerId).get().getSkillOwnerEntityId());
//        notifications.setSkillPartnerEntityId(skillOwnerRepository.findBySkillOwnerEntityId(ownerId).getSkillPartnerEntity().getSkillPartnerId());
//        notifications.setDate(new Date());
//        notifications.setMarkAsRead(false);
//        notifications.setJobId(skillSeekerMSAEntity.get().getJobId().getJobId());
//        notifications.setOwnerId(ownerId);
//        notifications.setDateOfInterview(LocalDate.now());
//        notifications.setTimeOfInterview(LocalTime.now());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
//        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
//        LocalDateTime now = LocalDateTime.now();
//        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//
//        ContentEntity contentpo = contentRepository.findByPO();
//        ContentEntity contentsow = contentRepository.findBySow();
//        ContentEntity contentmsa = contentRepository.findByMSA();
//
//        if (skillSeekerMSAEntity.isPresent()) {
//            notifications.setTitle(contentmsa.getTitle());
//            notifications.setContentId(contentmsa.getId());
//            notifications.setContentObj(contentmsa);
//            notifications.setContent("Hi, " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                    " has created the MSA contract request for the " + byId.get().getFirstName() +" "+ byId.get().getLastName()+" on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//            );
//
//            if (statementOfWorkEntity.isPresent() && poEntity.isPresent()) {
//
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the PO contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                partnerNotificationsRepository.save(notifications);
//                return contentpo;
//
//            }
//            if (poEntity.isPresent() && !statementOfWorkEntity.isPresent()) {
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the *PO contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                partnerNotificationsRepository.save(notifications);
//                return contentpo;
//            }
//            if (!poEntity.isPresent() && statementOfWorkEntity.isPresent()) {
//
//                notifications.setTitle(contentsow.getTitle());
//                notifications.setContentId(contentsow.getId());
//                notifications.setContentObj(contentsow);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the SOW contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                partnerNotificationsRepository.save(notifications);
//                return contentsow;
//            } else {
//                partnerNotificationsRepository.save(notifications);
//                return contentmsa;
//            }
//
//        } else {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
//        }
//    }


    //commenting it out as requirement changed , will uncomment if requirement comes again

//    @Override
//    public ContentEntity getContractNotificationsInOwner(int ownerId) {
//
//        Optional<PoEntity> poEntity = poRepository.findByOwnerId(ownerId);
//        Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkRepository.findByOwnerId(ownerId);
//        Optional<SkillSeekerMSAEntity> skillSeekerMSAEntity = Optional.ofNullable(seekerMsaRepository.findByOwnerId(ownerId));
//        Optional<SkillOwnerEntity> byId = skillOwnerRepository.findById(ownerId);
//
//        OwnerNotificationsEntity notifications = new OwnerNotificationsEntity();
//
//        notifications.setSkillOwnerEntityId(skillOwnerRepository.findById(ownerId).get().getSkillOwnerEntityId());
//
//        notifications.setDate(new Date());
//        notifications.setDateOfInterview(LocalDate.now());
//        notifications.setJobId(skillSeekerMSAEntity.get().getJobId().getJobId());
//        notifications.setMarkAsRead(false);
//        notifications.setStage(notifications.getStage());
//        notifications.setTimeOfInterview(LocalTime.now());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
//        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
//        LocalDateTime now = LocalDateTime.now();
//        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//
//        ContentEntity contentpo = contentRepository.findByPO();
//        ContentEntity contentsow = contentRepository.findBySow();
//        ContentEntity contentmsa = contentRepository.findByMSA();
//
//        if (skillSeekerMSAEntity.isPresent()) {
//            notifications.setTitle(contentmsa.getTitle());
//            notifications.setContentId(contentmsa.getId());
//            notifications.setContentObj(contentmsa);
//            notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                    " has created the MSA contract request for the " + byId.get().getFirstName()+" "+byId.get().getLastName()+ " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//            );
//            if (statementOfWorkEntity.isPresent() && poEntity.isPresent()) {
//
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the PO contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                ownerNotificationsRepository.save(notifications);
//                return contentpo;
//
//            }
//            if (poEntity.isPresent() && !statementOfWorkEntity.isPresent()) {
//                notifications.setTitle(contentpo.getTitle());
//                notifications.setContentId(contentpo.getId());
//                notifications.setContentObj(contentpo);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the *PO contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                ownerNotificationsRepository.save(notifications);
//                return contentpo;
//            }
//            if (!poEntity.isPresent() && statementOfWorkEntity.isPresent()) {
//                notifications.setTitle(contentsow.getTitle());
//                notifications.setContentId(contentsow.getId());
//                notifications.setContentObj(contentsow);
//                notifications.setContent("Hi " + skillSeekerMSAEntity.get().getJobId().getSkillSeeker().getSkillSeekerName() +
//                        " has created the SOW contract request for the " + skillSeekerMSAEntity.get().getSkillOwnerEntity().getFirstName() + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())
//                );
//                ownerNotificationsRepository.save(notifications);
//                return contentsow;
//            } else {
//                ownerNotificationsRepository.save(notifications);
//                return contentmsa;
//            }
//
//        } else {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
//        }
//    }

    @Override
    public List<SuperAdminNotifications> getSuperAdminNotification() {
        try {
            logger.info("NotificationServiceImpl|| getSuperAdminNotifications || get all notifications for super admin");
            return superAdminNotificationRepositoy.findAll();
        } catch (Exception e) {
            throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());

        }
    }


    @Scheduled(fixedRateString = "PT23H")
    public void ownerNotificationScheduler() {
        try {
            ownerNotificationsRepository.deleteByDate();
            logger.info("NotificationServiceImpl || ownerNotificationsRepository || deleting last 3 months Owner's Notification data's ", LocalDateTime.now());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
        }
    }

    @Scheduled(fixedRateString = "PT23H")
    public void partnerNotificationScheduler() {
        try {
            partnerNotificationsRepository.deleteByDate();
            logger.debug("NotificationServiceImpl || partnerNotificationsRepository || deleting last 3 months Partner's Notification data's ", LocalDateTime.now());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());

        }
    }

    public List<SuperAdminNotifications> getLastFiveAdminNotification() {
        try {
            List<SuperAdminNotifications> superAdminNotifications = superAdminNotificationRepositoy.findLastFiveNotification();
            if (!superAdminNotifications.isEmpty()) {
                logger.info("SkillPartnerServiceImpl || getSkillPartnerNotifications || getLastFiveNotificationOfPartner");

                return superAdminNotifications;
            } else {
                throw new ServiceException(INVALID_ID_REQUEST.getErrorCode(), INVALID_ID_REQUEST.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }

    }

    @Override
    @Transactional
    public List<HistoryOfJobs> getHistoryOfJobs(int ownerId) {

        Optional<List<PoEntity>> poEntity = poRepository.findListBySkillOwnerId(ownerId);

        List<HistoryOfJobs> historyOfJobs = new ArrayList<>();
        poEntity.get().forEach(poEntity1 -> {
            if (poEntity1.getPoStatus().getId() == 6) {
                HistoryOfJobs historyOfJobs2 = new HistoryOfJobs();
                Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(poEntity1.getSkillOwnerEntity().getSkillOwnerEntityId());

                if (skillOwner.get().getOnBoardingDate() != null) {
                    historyOfJobs2.setJobId(poEntity1.getJobId().getJobId());
                    historyOfJobs2.setJobTitle(poEntity1.getJobId().getJobTitle());
                    historyOfJobs2.setLocation(skillOwner.get().getCity());
                    historyOfJobs2.setSeekerName(poEntity1.getJobId().getSkillSeeker().getSkillSeekerName());
                    historyOfJobs2.setLevelExperience(skillOwner.get().getExpYears() + "Years," + skillOwner.get().getExpMonths() + " Months");
                    if (poEntity1.getSkillSeekerProject() == null) {
                        historyOfJobs2.setProject(Default);
                    } else {
                        historyOfJobs2.setProject(poEntity1.getSkillSeekerProject().getTitle());
                    }
//                    historyOfJobs2.setFromDate(poEntity1.getStartDate());
//                    historyOfJobs2.setToDate(poEntity1.getEndDate());
                    historyOfJobs2.setContractStatus("Active");
                    DateFormat formatter = new SimpleDateFormat("yyyy/MM/DD");
                    Date today = new Date();
                    try {
                        Date expiry = formatter.parse(formatter.format(poEntity1.getEndDate()));
                        Date todayWithZeroTime = formatter.parse(formatter.format(today));
                        if (expiry.equals(todayWithZeroTime)) {
                            historyOfJobs2.setContractStatus("Expired");
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    historyOfJobs.add(historyOfJobs2);
                }
            }

        });

        return historyOfJobs;
    }

    @Override
    public ContentEntity partnerMsaNotification(SkillPartnerEntity skillPartnerEntity, Notification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            PartnerNotificationsEntity partnerNotifications = new PartnerNotificationsEntity();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            ContentEntity content = contentRepository.findBySubmitted();
            partnerNotifications.setContent("MSA has been sent by FlexCub on " + " " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setSkillPartnerEntityId(skillPartnerEntity.getSkillPartnerId());
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setTitle("MSA - " + content.getTitle());
            partnerNotifications.setDate(new Date());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setMarkAsRead(false);
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
    }

    public ContentEntity superAdminMsaNotification(SkillPartnerEntity skillPartnerEntity, Notification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            if (skillPartnerEntity.getMsaStatusId().getId() == 17) {
                ContentEntity content = contentRepository.findByApproved();
                superAdminNotifications.setContent("MSA has been approved by " + skillPartnerEntity.getBusinessName() + " on  " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setTitle("MSA - " + content.getTitle());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setMsaId(skillPartnerEntity.getMsaId().getId());
                superAdminNotifications.setMsaStatus(skillPartnerEntity.getMsaStatusId().getStatus());
                superAdminNotifications.setSkillPartnerId(skillPartnerEntity.getSkillPartnerId());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            } else if (skillPartnerEntity.getMsaStatusId().getId() == 18) {
                ContentEntity content = contentRepository.findByReject();
                superAdminNotifications.setContent("MSA has been Rejected by " + skillPartnerEntity.getBusinessName() + " on  " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setTitle("MSA - " + content.getTitle());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setDate(new Date());
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setMsaId(skillPartnerEntity.getMsaId().getId());
                superAdminNotifications.setMsaStatus(skillPartnerEntity.getMsaStatusId().getStatus());
                superAdminNotifications.setSkillPartnerId(skillPartnerEntity.getSkillPartnerId());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            }
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
        return null;
    }

    @Override
    public ContentEntity superAdminToSeekerMsaNotification(SkillSeekerEntity skillSeekerEntity) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            SeekerNotificationsEntity seekerNotificationsEntity = new SeekerNotificationsEntity();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            if (skillSeekerEntity.getMsaStatusId().getId() == 16) {
                ContentEntity content = contentRepository.findByInReview();
                seekerNotificationsEntity.setContent("MSA is currently under review with FlexCub" + " on  " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                seekerNotificationsEntity.setContentId(content.getId());
                seekerNotificationsEntity.setTitle("MSA - " + content.getTitle());
                seekerNotificationsEntity.setContentObj(content);
                seekerDetails(skillSeekerEntity, seekerNotificationsEntity);
                seekerNotificationsRepository.save(seekerNotificationsEntity);
                return content;
            } else if (skillSeekerEntity.getMsaStatusId().getId() == 17) {
                ContentEntity content = contentRepository.findByApproved();
                seekerNotificationsEntity.setContent("MSA has been Approved by FlexCub" + " on  " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                seekerNotificationsEntity.setContentId(content.getId());
                seekerNotificationsEntity.setTitle("MSA - " + content.getTitle());
                seekerNotificationsEntity.setContentObj(content);
                seekerDetails(skillSeekerEntity, seekerNotificationsEntity);
                seekerNotificationsRepository.save(seekerNotificationsEntity);
                return content;
            } else if (skillSeekerEntity.getMsaStatusId().getId() == 18) {
                ContentEntity content = contentRepository.findByReject();
                seekerNotificationsEntity.setContent("MSA has been Rejected by FlexCub  " + " on  " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                seekerNotificationsEntity.setContentId(content.getId());
                seekerNotificationsEntity.setTitle("MSA - " + content.getTitle());
                seekerNotificationsEntity.setContentObj(content);
                seekerDetails(skillSeekerEntity, seekerNotificationsEntity);
                seekerNotificationsRepository.save(seekerNotificationsEntity);
                return content;
            }

        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
        return null;
    }

    private static void seekerDetails(SkillSeekerEntity skillSeekerEntity, SeekerNotificationsEntity seekerNotificationsEntity) {
        seekerNotificationsEntity.setDate(new Date());
        seekerNotificationsEntity.setMarkAsRead(false);
        seekerNotificationsEntity.setMsaId(skillSeekerEntity.getMsaId().getId());
        seekerNotificationsEntity.setMsaStatus(skillSeekerEntity.getMsaStatusId().getStatus());
        seekerNotificationsEntity.setSkillSeekerEntityId(skillSeekerEntity.getId());
        seekerNotificationsEntity.setTaxIdBusinessLicense(skillSeekerEntity.getTaxIdBusinessLicense());
    }


    @Override
    public ContentEntity seekerToSuperAdminMsaNotification(SkillSeekerEntity skillSeekerEntity, Notification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            ContentEntity content = contentRepository.findBySubmitted();
            superAdminNotifications.setContent(skillSeekerEntity.getSkillSeekerName() + " has sent the MSA " + " on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            superAdminNotifications.setSkillSeekerEntityId(skillSeekerEntity.getId());
            superAdminNotifications.setContentId(content.getId());
            superAdminNotifications.setTitle("MSA - " + content.getTitle());
            superAdminNotifications.setDate(new Date());
            superAdminNotifications.setContentObj(content);
            superAdminNotifications.setMarkAsRead(false);
            superAdminNotifications.setMsaId(skillSeekerEntity.getMsaId().getId());
            superAdminNotifications.setMsaStatus(skillSeekerEntity.getMsaStatusId().getStatus());
            superAdminNotificationRepositoy.save(superAdminNotifications);
            return content;
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
    }

    @Override
    public ContentEntity newSlotByOwnerNotification(AcceptRejectDto acceptRejectDto, int stage) {

        ContentEntity content = contentRepository.findByNewSlot();
        Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(acceptRejectDto.getSkillOwnerEntityId());
        Optional<SkillSeekerEntity> seekerId = skillSeekerRepository.findById(acceptRejectDto.getSeekerId());
        Optional<Job> job = jobRepository.findByJobId(acceptRejectDto.getJobId());
        if (!skillOwner.isPresent()) {
            logger.info("Owner not found");
        }
        SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
        notifications.setContentId(content.getId());
        notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle()
                + " | " + content.getTitle() : job.get().getSeekerProject().getTitle()
                + " | " + job.get().getJobTitle() + " | " + content.getTitle());
        notifications.setJobId(acceptRejectDto.getJobId());
        notifications.setDate(new Date());
        notifications.setOwnerId(skillOwner.get().getSkillOwnerEntityId());
        notifications.setSkillSeekerEntityId(seekerId.get().getId());
        notifications.setTaxIdBusinessLicense(seekerId.get().getTaxIdBusinessLicense());
        notifications.setMarkAsRead(false);
        notifications.setStage(stage);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        LocalDateTime now = LocalDateTime.now();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

        notifications.setContent("Hi " + seekerId.get().getSkillSeekerName() + " "
                + ", Please send new slots to  " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName()
                + " on " + formatter.format(now) + ", Time : "
                + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));

        notifications.setContentObj(content);
        seekerNotificationsRepository.save(notifications);
        return content;
    }

//    @Override
//    public ContentEntity acceptNotificationForSeeker(AcceptRejectDto acceptRejectDto, int currentStage) {
//        ContentEntity content = contentRepository.findByAccepted();
//        Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(acceptRejectDto.getSkillOwnerEntityId());
//        Optional<SkillSeekerEntity> seekerId = skillSeekerRepository.findById(acceptRejectDto.getSeekerId());
//        Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId(), currentStage);
//        Optional<Job> job = jobRepository.findByJobId(acceptRejectDto.getJobId());
//        SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
//        notifications.setContentId(content.getId());
//        notifications.setTitle(null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle()
//                + " | " + content.getTitle() : job.get().getSeekerProject().getTitle()
//                + " | " + job.get().getJobTitle() + " | " + content.getTitle());
//        notifications.setJobId(acceptRejectDto.getJobId());
//        notifications.setDate(new Date());
//        notifications.setOwnerId(skillOwner.get().getSkillOwnerEntityId());
//        notifications.setSkillSeekerEntityId(seekerId.get().getId());
//        notifications.setTaxIdBusinessLicense(seekerId.get().getTaxIdBusinessLicense());
//        notifications.setMarkAsRead(false);
//        notifications.setStage(currentStage);
//        notifications.setContentObj(content);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
//        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
//        LocalDateTime now = LocalDateTime.now();
//        TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST
//        if (acceptRejectDto.getFromSeekerSide()) {
//            notifications.setContent("Hi " + seekerId.get().getSkillSeekerName() + ", " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() +
//                    " has accepted your interview scheduled for " + formatter.format(requirementPhase.get().getDateOfInterview()) +
//                    " at " + time.format(requirementPhase.get().getTimeOfInterview()) + " on " + formatter.format(now) +
//                    ", Time: " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
//            seekerNotificationsRepository.save(notifications);
//        } else {
//            notifications.setContent("Hi " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + ", " +
//                    seekerId.get().getSkillSeekerName() + " has rejected your interview scheduled for " +
//                    formatter.format(requirementPhase.get().getDateOfInterview()) + " at " + time.format(requirementPhase.get().getTimeOfInterview()) +
//                    " on " + formatter.format(now) + ", Time: " +
//                    time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
//            OwnerNotificationsEntity ownerNotifications = new OwnerNotificationsEntity();
//            ownerNotifications.setContentId(notifications.getContentId());
//            ownerNotifications.setTitle(notifications.getTitle());
//            ownerNotifications.setJobId(notifications.getJobId());
//            ownerNotifications.setDate(notifications.getDate());
//            ownerNotifications.setSkillOwnerEntityId(notifications.getOwnerId());
//            ownerNotifications.setSeekerId(notifications.getSkillSeekerEntityId());
//            ownerNotifications.setMarkAsRead(notifications.getMarkAsRead());
//            ownerNotifications.setStage(notifications.getStage());
//            ownerNotifications.setContentObj(notifications.getContentObj());
//            ownerNotifications.setContent(notifications.getContent());
//            ownerNotificationsRepository.save(ownerNotifications);
//        }
//
//        return content;
//    }


    @Override
    public ContentEntity acceptNotificationForSeekerAndOwner(AcceptRejectDto acceptRejectDto, int currentStage) {
        ContentEntity content = contentRepository.findByAccepted();
        Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(acceptRejectDto.getSkillOwnerEntityId());
        Optional<SkillSeekerEntity> seekerId = skillSeekerRepository.findById(acceptRejectDto.getSeekerId());
        Optional<RequirementPhase> requirementPhase = requirementPhaseRepository.findByJobIdSkillOwnerIdAndStage(acceptRejectDto.getJobId(), acceptRejectDto.getSkillOwnerEntityId(), currentStage);
        Optional<Job> job = jobRepository.findByJobId(acceptRejectDto.getJobId());
        SeekerNotificationsEntity notifications = new SeekerNotificationsEntity();
        OwnerNotificationsEntity ownerNotifications = new OwnerNotificationsEntity();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        LocalDateTime now = LocalDateTime.now();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));  //TODO - As of now, we're having in EST

        notifications.setContentId(content.getId());
        notifications.setJobId(acceptRejectDto.getJobId());
        notifications.setDate(new Date());
        notifications.setOwnerId(skillOwner.get().getSkillOwnerEntityId());
        notifications.setSkillSeekerEntityId(seekerId.get().getId());
        notifications.setTaxIdBusinessLicense(seekerId.get().getTaxIdBusinessLicense());
        notifications.setMarkAsRead(false);
        notifications.setStage(currentStage);
        notifications.setContentObj(content);

        if (acceptRejectDto.getFromSeekerSide()) {
            notifications.setTitle(getNotificationTitle(job, content));
            notifications.setContent(getOwnerNotificationContent(seekerId, skillOwner, requirementPhase, now));
            copyNotificationData(notifications, ownerNotifications);
            ownerNotifications.setContent(notifications.getContent());
            ownerNotificationsRepository.save(ownerNotifications);
        } else {
            notifications.setTitle(getNotificationTitle(job, content));
            notifications.setContent(getSeekerNotificationContent(seekerId, skillOwner, requirementPhase, now));
            seekerNotificationsRepository.save(notifications);
        }

        return content;
    }

    private String getNotificationTitle(Optional<Job> job, ContentEntity content) {
        return null == job.get().getSeekerProject() ? Default + " | " + job.get().getJobTitle()
                + " | " + content.getTitle() : job.get().getSeekerProject().getTitle()
                + " | " + job.get().getJobTitle() + " | " + content.getTitle();
    }

    private String getSeekerNotificationContent(Optional<SkillSeekerEntity> seekerId, Optional<SkillOwnerEntity> skillOwner, Optional<RequirementPhase> requirementPhase, LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        return "Hi " + seekerId.get().getSkillSeekerName() + ", " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() +
                " has accepted your interview scheduled for " + formatter.format(requirementPhase.get().getDateOfInterview()) +
                " at " + time.format(requirementPhase.get().getTimeOfInterview()) + " on " + formatter.format(now) +
                ", Time: " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
    }

    private String getOwnerNotificationContent(Optional<SkillSeekerEntity> seekerId, Optional<SkillOwnerEntity> skillOwner, Optional<RequirementPhase> requirementPhase, LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
        return "Hi " + skillOwner.get().getFirstName() + " " + skillOwner.get().getLastName() + ", " +
                seekerId.get().getSkillSeekerName() + " has accepted your interview scheduled for " +
                formatter.format(requirementPhase.get().getDateOfInterview()) + " at " + time.format(requirementPhase.get().getTimeOfInterview()) +
                " on " + formatter.format(now) + ", Time: " +
                time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
    }

    private void copyNotificationData(SeekerNotificationsEntity source, OwnerNotificationsEntity destination) {
        destination.setContentId(source.getContentId());
        destination.setTitle(source.getTitle());
        destination.setJobId(source.getJobId());
        destination.setDate(source.getDate());
        destination.setSkillOwnerEntityId(source.getOwnerId());
        destination.setSeekerId(source.getSkillSeekerEntityId());
        destination.setMarkAsRead(source.getMarkAsRead());
        destination.setStage(source.getStage());
        destination.setContentObj(source.getContentObj());
    }

    @Override
    public ContentEntity superAdminToPartnerSowNotification(StatementOfWorkEntity statementOfWorkEntity, Notification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            PartnerNotificationsEntity partnerNotifications = new PartnerNotificationsEntity();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            ContentEntity content = contentRepository.findBySubmitted();
            partnerNotifications.setContent("FlexCub has Sent SOW for " + statementOfWorkEntity.getSkillPartnerEntity().getBusinessName() + " on" + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
            partnerNotifications.setContentId(content.getId());
            partnerNotifications.setContentObj(content);
            partnerNotifications.setTitle("SOW Sent");
            partnerNotifications.setMarkAsRead(false);
            partnerNotifications.setDate(new Date());
            partnerNotifications.setSkillPartnerEntityId(statementOfWorkEntity.getSkillPartnerEntity().getSkillPartnerId());
            partnerNotificationsRepository.save(partnerNotifications);
            return content;
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
    }

    @Override
    public ContentEntity partnerToSuperAdminSowNotification(StatementOfWorkEntity statementOfWorkEntity, Notification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            DateTimeFormatter time = DateTimeFormatter.ofPattern(timeType);
            LocalDateTime now = LocalDateTime.now();
            SuperAdminNotifications superAdminNotifications = new SuperAdminNotifications();
            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            if (statementOfWorkEntity.getSowStatus().getId() == 21) {
                ContentEntity content = contentRepository.findByInReview();
                superAdminNotifications.setContent("SOW Created for " + statementOfWorkEntity.getSkillPartnerEntity().getBusinessName() + " is under Review from " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setTitle("SOW is In-Review");
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setSkillPartnerId(statementOfWorkEntity.getSkillPartnerEntity().getSkillPartnerId());
                superAdminNotifications.setDate(new Date());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;
            } else if (statementOfWorkEntity.getSowStatus().getId() == 22) {
                ContentEntity content = contentRepository.findByApproved();
                superAdminNotifications.setContent("Sow Created for " + statementOfWorkEntity.getSkillPartnerEntity().getBusinessName() + " was approved on " + formatter.format(now) + ", Time : " + time.format(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setContentId(content.getId());
                superAdminNotifications.setContentObj(content);
                superAdminNotifications.setMarkAsRead(false);
                superAdminNotifications.setTitle("Sow Approved");
                superAdminNotifications.setSkillPartnerId(statementOfWorkEntity.getSkillPartnerEntity().getSkillPartnerId());
                superAdminNotifications.setDate(new Date());
                superAdminNotificationRepositoy.save(superAdminNotifications);
                return content;

            }
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
        return null;

    }


}
