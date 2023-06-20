package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.registration.service.FlexcubEmailService;
import com.flexcub.resourceplanning.skillowner.dto.OwnerTimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.entity.TimesheetDocument;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillowner.repository.TimesheetDocumentRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerTimeSheetService;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTaskService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class OwnerTimeSheetServiceImpl implements OwnerTimeSheetService {

    @Autowired
    OwnerTimeSheetRepository ownerTimeSheetRepository;
    @Autowired
    TimesheetDocumentRepository timesheetDocumentRepository;

    @Autowired
    OwnerTimeSheetRepository getOwnerTimeSheetRepository;


    @Autowired
    PoRepository poRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SkillSeekerTaskService skillSeekerTaskService;

    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;
    Logger logger = LoggerFactory.getLogger(OwnerTimeSheetServiceImpl.class);
    @Value("${flexcub.baseURLSkillOwnerTimeSheet}")
    private String baseURLSkillOwnerTimeSheet;
    @Autowired
    private FlexcubEmailService emailService;

    @Transactional
    @Override
    public List<TimeSheetResponse> insertTimeSheet(List<OwnerTimeSheet> ownerTimeSheet) {

        List<TimeSheetResponse> timeSheetResponses = new ArrayList<>();
        List<OwnerTimeSheetEntity> ownerTimeSheetEntityList = new ArrayList<>();
        ownerTimeSheet.forEach(timeSheet -> {
            Optional<SkillSeekerProjectEntity> skillSeekerProject = skillSeekerProjectRepository.findById(timeSheet.getSkillSeekerProjectEntityId());
            if (skillSeekerProject.get().getStartDate().before(timeSheet.getStartDate()) && (skillSeekerProject.get().getEndDate().after(timeSheet.getStartDate()) || DateUtils.isSameDay( skillSeekerProject.get().getEndDate(),timeSheet.getStartDate()))) {
                Optional<List<OwnerTimeSheetEntity>> byStartDateAndEndDate = ownerTimeSheetRepository.findByStartDateAndEndDate(timeSheet.getStartDate(), timeSheet.getEndDate(), timeSheet.getSkillOwnerEntityId());
                //checking if timesheet exists and if invoice is generated for it or not , if generated then throwing error
                // else deleting current data and inserting new
                if (byStartDateAndEndDate.isPresent() && !byStartDateAndEndDate.get().isEmpty()) {
                    for (OwnerTimeSheetEntity sheet : byStartDateAndEndDate.get()) {
                        if (sheet.isInvoiceGenerated()) {
                            throw new ServiceException(TIMESHEET_ALREADY_EXISTS.getErrorCode(), TIMESHEET_ALREADY_EXISTS.getErrorDesc());
                        } else {
                            ownerTimeSheetRepository.deleteById(sheet.getTimeSheetId());
                        }
                    }
                }
                Optional<PoEntity> poEntity = poRepository.findByOwnerId(timeSheet.getSkillOwnerEntityId());
                if (poEntity.isPresent() && poEntity.get().getOnBoarding().before(timeSheet.getStartDate())) {
                    timeSheet.getEfforts().forEach(efforts -> {
                        OwnerTimeSheetEntity ownerTimeSheetEntity = modelMapper.map(timeSheet, OwnerTimeSheetEntity.class);

                        if (null != efforts.getSkillSeekerTaskEntity() && efforts.getSkillSeekerTaskEntity().getId() != 0) {
                            ownerTimeSheetEntity.setSkillSeekerTaskEntity(efforts.getSkillSeekerTaskEntity());
                        } else {
                            ownerTimeSheetEntity.setSkillSeekerTaskEntity(null);
                        }
                        ownerTimeSheetEntity.setHours(efforts.getHours());
                        if (timeSheet.getSkillSeekerProjectEntityId() == 0) {
                            ownerTimeSheetEntity.setSkillSeekerProjectEntity(null);

                        } else {
                            ownerTimeSheetEntity.setSkillSeekerProjectEntity(skillSeekerProjectRepository.getById(timeSheet.getSkillSeekerProjectEntityId()));
                        }
                        ownerTimeSheetEntity.setSkillSeekerEntity(poEntity.get().getJobId().getSkillSeeker());
                        ownerTimeSheetEntity.setPartnerId(poEntity.get().getSkillOwnerEntity().getSkillPartnerEntity().getSkillPartnerId());
                        ownerTimeSheetEntityList.add(ownerTimeSheetRepository.save(ownerTimeSheetEntity));
                        logger.info("OwnerTimeSheetServiceImpl || insertTimeSheet || time_sheet saved successfully  ");

                    });
                } else {
                    throw new ServiceException(Project_Expired.getErrorCode(), Project_Expired.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
            }
        });
        ownerTimeSheetEntityList.forEach(dto -> {

            TimeSheetResponse map = modelMapper.map(dto, TimeSheetResponse.class);
            if (null != dto.getSkillSeekerProjectEntity()) {
                map.setTitle(dto.getSkillSeekerProjectEntity().getTitle());
            }
//            TimeZone.setDefault(TimeZone.getTimeZone("EST"));
            timeSheetResponses.add(map);
        });


//            AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
//                emailContext.skillOwnerTimeSheetApproval(emailContext, timeSheet);
//                try {
//                    emailService.skillOwnerTimeSheetApproval(emailContext);
//                    emailContext.baseURLSkillOwnerTimeSheet(baseURLSkillOwnerTimeSheet);
//                    logger.info("Mail sent");
//                } catch (MessagingException e) {
//                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Mail not Send");
//                }

        logger.info("OwnerTimeSheetServiceImpl || insertTimeSheet || Inserting timeSheet Details");
        return timeSheetResponses;

    }


    @Transactional
    @Override
    public TimeSheetResponse updateTimeSheet(TimeSheetResponse ownerTimeSheet) {
        try {
            Optional<OwnerTimeSheetEntity> ownerTimeSheetEntities = ownerTimeSheetRepository.findByTimeSheetId(ownerTimeSheet.getTimeSheetId());
            if (ownerTimeSheetEntities.isPresent()) {
                BeanUtils.copyProperties(ownerTimeSheet, ownerTimeSheetEntities.get(), NullPropertyName.getNullPropertyNames(ownerTimeSheet));
                Hibernate.initialize(ownerTimeSheetEntities.get().getSkillOwnerEntity());
                OwnerTimeSheetEntity timeSheet = ownerTimeSheetRepository.save(ownerTimeSheetEntities.get());
                logger.info("OwnerTimeSheetServiceImpl || updateTimeSheet || TimeSheet was updated in OwnerTimeSheetEntity");
                return modelMapper.map(timeSheet, TimeSheetResponse.class);
            } else {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public void deleteTimeSheetById(int id) {

        Optional<OwnerTimeSheetEntity> ownerTimeSheet = ownerTimeSheetRepository.findByTimeSheetId(id);
        if (ownerTimeSheet.isPresent()) {
            logger.info("OwnerTimeSheetServiceImpl || deleteTimeSheetData || TimeSheet Data was deleted ");
            ownerTimeSheetRepository.deleteById(id);
        } else {
            throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
        }
    }


    @Transactional
    @Override
    public List<TimeSheetResponse> getTimeSheetHours(Date startDate, int ownerId) {
        List<OwnerTimeSheetEntity> timeSheet = ownerTimeSheetRepository
                .findByStartDateAndSkillOwnerEntity(startDate, ownerId).orElse(null);

        if (timeSheet == null || timeSheet.isEmpty()) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

        Hibernate.initialize(timeSheet.stream().map(OwnerTimeSheetEntity::getSkillOwnerEntity)
                .map(SkillOwnerEntity::getPortfolioUrl).collect(Collectors.toList()));

        logger.info("OwnerTimeSheetServiceImpl || getDataLastSpecificWeek || Get the hours for last week specified");

        return timeSheet.stream().map(evaluate -> {
            TimeSheetResponse response = modelMapper.map(evaluate, TimeSheetResponse.class);
            response.setTotalHours(Arrays.stream(response.getHours().split(","))
                    .mapToInt(Integer::parseInt)
                    .sum());
            response.setFirstName(evaluate.getSkillOwnerEntity().getFirstName() + " " + evaluate.getSkillOwnerEntity().getLastName());
            response.setTitle(evaluate.getSkillSeekerProjectEntity() != null ? evaluate.getSkillSeekerProjectEntity().getTitle() : "Default");
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<SkillSeekerProject> getProjectDetails(int skillOwnerId) {

        List<SkillSeekerProject> skillSeekerProject = new ArrayList<>();
        List<SkillSeekerProjectEntity> seekerProjectEntities = new ArrayList<>();
        Optional<List<PoEntity>> ownerId = poRepository.findListBySkillOwnerId(skillOwnerId);
        if (!ownerId.get().isEmpty()) {
            ownerId.get().forEach(project -> {
                if (null == project.getSkillSeekerProject()) {
                    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
                    skillSeekerProjectEntity.setTitle("Default");
                    skillSeekerProjectEntity.setId(0);
                    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
                    skillSeekerEntity.setId(project.getSkillSeekerId());
                    skillSeekerProjectEntity.setSkillSeeker(skillSeekerEntity);
                    seekerProjectEntities.add(skillSeekerProjectEntity);
                } else {
                    project.setSkillSeekerProject(skillSeekerProjectRepository.getById(project.getSkillSeekerProject().getId()));
                    SkillSeekerProjectEntity skillSeekerProject1 = project.getSkillSeekerProject();
                    seekerProjectEntities.add(skillSeekerProject1);
                }
            });
            seekerProjectEntities.forEach(project -> {
                SkillSeekerProject seekerProject = modelMapper.map(project, SkillSeekerProject.class);
                if (null != project) {
                    List<SkillSeekerTask> taskData = skillSeekerTaskService.getTaskData(project.getId(), project.getSkillSeeker().getId());
                    seekerProject.setTaskData(taskData);
                }
                skillSeekerProject.add(seekerProject);
            });
        } else {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
        logger.info("OwnerTimeSheetServiceImpl || getProjectDetails || Get the Project Details Of SkillOwner");
        return skillSeekerProject;
    }

    @Transactional
    @Override
    public List<TimeSheet> getOwnerTimeSheetDetails(int skillOwnerId) {
        List<OwnerTimeSheetEntity> timeSheetEntities = ownerTimeSheetRepository.findByOwnerId(skillOwnerId).orElse(null);


        if (null == timeSheetEntities || timeSheetEntities.isEmpty()) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
        logger.info("OwnerTimeSheetServiceImpl || getOwnerTimeSheetDetails || Getting the TimeSheet Of SkillOwner");

        return timeSheetEntities.stream()
                .map(values -> {
                    TimeSheet timeSheet = modelMapper.map(values, TimeSheet.class);
                    timeSheet.setFirstName(values.getSkillOwnerEntity().getFirstName() + " " + values.getSkillOwnerEntity().getLastName());
                    timeSheet.setTotalHours(Arrays.stream(timeSheet.getHours().split(","))
                            .mapToInt(Integer::parseInt)
                            .sum());
                    timeSheet.setTitle(values.getSkillSeekerProjectEntity() != null ? values.getSkillSeekerProjectEntity().getTitle() : "Default");
                    return timeSheet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TimesheetDocument timesheetDocuments(MultipartFile multipartFile, int timesheetId) throws IOException {
        try {
            Optional<OwnerTimeSheetEntity> timesheet = getOwnerTimeSheetRepository.findById(timesheetId);
            String contentType = multipartFile.getContentType();
            if (isValidFileType(contentType))
                logger.info("OwnerTimeSheetServiceImpl || timesheetDocuments || File Type verified !!");
            TimesheetDocument timesheetDocument = new TimesheetDocument(
                    timesheet.get().getSkillOwnerEntity().getSkillOwnerEntityId(),
                    multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(),
                    contentType,
                    multipartFile.getSize(),
                    timesheet.get().getSkillSeekerEntity().getId(),
                    timesheet.get().getSkillSeekerProjectEntity().getId(),
                    timesheetId
            );
            return timesheetDocumentRepository.save(timesheetDocument);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_TIMESHEET_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_TIMESHEET_ID_OR_MULTIPART_FILE.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public Optional<TimesheetDocument> downloadTimesheetDocuments(int id) {
        return Optional.ofNullable(timesheetDocumentRepository.findByTimesheetId(id)
                .orElseThrow(() -> new ServiceException(INVALID_TIMESHEETID.getErrorCode(), INVALID_TIMESHEETID.getErrorDesc())));
    }


    @Transactional
    @Override
    public TimesheetDocument urlDownloadTimesheetDocuments(int id) {

        if (id == 0) {
            throw new ServiceException(INVALID_TIMESHEETID.getErrorCode(), INVALID_TIMESHEETID.getErrorDesc());
        }

        Optional<TimesheetDocument> optionalDoc = Optional.ofNullable(timesheetDocumentRepository.findByTimesheetId(id).orElseThrow(() -> new ServiceException(INVALID_TIMESHEETID.getErrorCode(), INVALID_TIMESHEETID.getErrorDesc())));
        TimesheetDocument doc = optionalDoc.get();
        Set<String> allowedTypes = new HashSet<>(Arrays.asList("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "image/png", "image/jpg", "image/jpeg"));

        if (!allowedTypes.contains(doc.getType())) {
            doc = null;
        }
        return doc;

    }


    private boolean isValidFileType(String contentType) {
        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(PDF, APPLICATION_VND_PDF);
        fileTypeList.put(PNG, IMG_PNG);
        fileTypeList.put(JPG, IMG_JPG);
        fileTypeList.put(JPEG, IMG_JPEG);
        return fileTypeList.containsValue(contentType);
    }


}


