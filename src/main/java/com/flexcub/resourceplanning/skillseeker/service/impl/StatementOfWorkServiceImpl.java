package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillDomainService;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerProjectService;
import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FileService.createContract;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.SOWDATA_NOT_SAVED;

@Service
@Log4j2
public class StatementOfWorkServiceImpl implements StatementOfWorkService {

    Logger logger = LoggerFactory.getLogger(StatementOfWorkServiceImpl.class);
    @Autowired
    PoService poService;
    @Autowired
    SkillOwnerService skillOwnerService;

    @Autowired
    JobService jobService;
    @Autowired
    OwnerSkillDomainService ownerSkillDomainService;

    @Autowired
    SkillSeekerProjectService skillSeekerProjectService;
    @Autowired
    PoRepository poRepository;
    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;
    @Autowired
    ContractStatusRepository sowStatusRepository;
    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    OwnerSkillDomainRepository ownerSkillDomainRepository;

    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;

    @Autowired
    JobRepository jobRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    ContractFileRepository contractFileRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${project.default}")
    private String Default;

//    @Override
//    @Transactional
//    public StatementOfWork addDocument(MultipartFile multipartFile, String sowRequestDto) throws IOException {
//        SowRequest sowRequest = objectMapper.readValue(sowRequestDto, SowRequest.class);
//        Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(Optional.ofNullable(skillOwnerService.getOwnerId(sowRequest.getOwnerId())).orElseThrow(() -> new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc())));
//        StatementOfWorkEntity statementOfWorkEntity = new StatementOfWorkEntity();
//        StatementOfWork statementOfWork = new StatementOfWork();
//        if (!multipartFile.isEmpty() && skillOwner.isPresent()) {
//            ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));
//            Optional<StatementOfWorkEntity> existingSow = statementOfWorkRepository.findByJobIdAndSkillOwnerId(sowRequest.getJobId(), sowRequest.getOwnerId());
//            if (existingSow.isEmpty()) {
//                try {
//                    statementOfWorkEntity.setSow(contractFiles);
//                    Optional<Job> job = Optional.ofNullable(jobService.findByJobId(sowRequest.getJobId()));
//                    job.ifPresent(statementOfWorkEntity::setJobId);
//                    statementOfWorkEntity.setSkillSeekerId(sowRequest.getSeekerId());
//                    if (job.get().getSeekerProject().getId() == 0) {
//                        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProjectService.getById(0));
//                    } else {
//                        statementOfWorkEntity.setSkillSeekerProject(skillSeekerProjectService.getById(job.get().getSeekerProject().getId()));
//                    }
//                    if (job.get().getOwnerSkillDomainEntity().getDomainId() == 0) {
//                        statementOfWorkEntity.setOwnerSkillDomainEntity(ownerSkillDomainService.getById(9));
//                    } else {
//                        statementOfWorkEntity.setOwnerSkillDomainEntity(ownerSkillDomainService.getById(job.get().getOwnerSkillDomainEntity().getDomainId()));
//                    }
//                    statementOfWorkEntity.setSkillOwnerEntity(skillOwnerService.getOwnerId(sowRequest.getOwnerId()));
//                    statementOfWorkEntity.setSowStartDate(sowRequest.getSowStartDate());
//                    statementOfWorkEntity.setSowEndDate(sowRequest.getSowEndDate());
//                    statementOfWorkEntity.setRoles(job.get().getJobTitle());
//                    statementOfWorkEntity.setSowStatus(getBySowStatusId().get());
//                    LocalDate date = LocalDate.now();
//                    statementOfWorkEntity.setDateOfRelease(date);
//                    statementOfWorkRepository.save(statementOfWorkEntity);
//
//                    Notification notification = new Notification();
//                    notificationService.sowStatusNotification(statementOfWorkEntity, notification);
//
//                    Job byJobJobId = jobService.findByJobId(sowRequest.getJobId());
//                    if (byJobJobId.getNumberOfPositions() > 0 && poService.findByOwnerId(sowRequest.getOwnerId())) {
//                        byJobJobId.setNumberOfPositions(byJobJobId.getNumberOfPositions() - 1);
//                        jobService.saveAndFlush(byJobJobId);
//                        logger.info("StatementOfWorkServiceImpl || checkNumbersOfPositions ||checksNumbersOfPositions and reduces -1 ");
//                        poService.removeCandidates(sowRequest.getOwnerId(), sowRequest.getJobId());
//                    }
//
//                    OwnerSkillStatusEntity ownerSkillStatus = new OwnerSkillStatusEntity();
//                    ownerSkillStatus.setSkillOwnerStatusId(4);
//                    skillOwner.get().setOwnerSkillStatusEntity(ownerSkillStatus);
//                    skillOwnerService.save(skillOwner.get());
//                    statementOfWork.setId(statementOfWorkEntity.getId());
//                    statementOfWork.setStatus(statementOfWorkEntity.getSowStatus().getStatus());
//                    logger.info("StatementOfWorkServiceImpl || checkFileTypeAndUpload || Uploaded the file successfully: {} // ->", multipartFile.getOriginalFilename());
//
//                } catch (NullPointerException e) {
//                    throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//                } catch (Exception e) {
//                    throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
//                }
//            } else {
//                throw new ServiceException(SOW_ALREADY_ADDED.getErrorCode(), SOW_ALREADY_ADDED.getErrorDesc());
//            }
//        } else {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
//
//        return statementOfWork;
//    }
//
//    private Optional<ContractStatus> getBySowStatusId() {
//        return sowStatusRepository.findById(7);
//    }
//
//    @Override
//    @Transactional
//    public List<StatementOfWorkGetDetails> getSowDetails(int skillSeekerId) {
//        try {
//            Optional<List<StatementOfWorkEntity>> sow = statementOfWorkRepository.findBySkillSeekerId(skillSeekerId);
//            if (!sow.get().isEmpty()) {
//                List<StatementOfWorkGetDetails> statementOfWorkGetDetailsList = new ArrayList<>();
//                for (StatementOfWorkEntity statementOfWorkEntity : sow.get()) {
//                    StatementOfWorkGetDetails statementOfWorkGetDetails = new StatementOfWorkGetDetails();
//                    statementOfWorkGetDetails.setId(statementOfWorkEntity.getId());
//                    statementOfWorkGetDetails.setSkillOwnerName(statementOfWorkEntity.getSkillOwnerEntity().getFirstName());
//                    statementOfWorkGetDetails.setRole(statementOfWorkEntity.getRoles());
//                    if (null == statementOfWorkEntity.getSkillSeekerProject()) {
//                        statementOfWorkGetDetails.setProject(Default);
//                    } else {
//                        statementOfWorkGetDetails.setProject(statementOfWorkEntity.getSkillSeekerProject().getTitle());
//                    }
//                    if (null == statementOfWorkEntity.getOwnerSkillDomainEntity()) {
//                        statementOfWorkGetDetails.setDepartment(Default);
//                    } else {
//                        statementOfWorkGetDetails.setDepartment(statementOfWorkEntity.getOwnerSkillDomainEntity().getDomainValues());
//                    }
//                    statementOfWorkGetDetails.setEmail(statementOfWorkEntity.getSkillOwnerEntity().getPrimaryEmail());
//                    statementOfWorkGetDetails.setStatus(statementOfWorkEntity.getSowStatus().getStatus());
//                    statementOfWorkGetDetails.setPhone(statementOfWorkEntity.getSkillOwnerEntity().getPhoneNumber());
//                    statementOfWorkGetDetails.setOwnerId(statementOfWorkEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
//                    statementOfWorkGetDetails.setJobId(statementOfWorkEntity.getJobId().getJobId());
//                    statementOfWorkGetDetailsList.add(statementOfWorkGetDetails);
//                }
//                logger.info("StatementOfWorkServiceImpl || getSowDetails || getting StatementOfWork Details");
//                return statementOfWorkGetDetailsList;
//            } else {
//                throw new ServiceException();
//            }
//        } catch (ServiceException e) {
//            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
//    }
//
//    @Override
//    @Transactional
//    public List<StatementOfWorkGetDetails> getAllSowDetails() {
//        try {
//            Optional<List<StatementOfWorkEntity>> sow = Optional.of(statementOfWorkRepository.findAll());
//            if (!sow.get().isEmpty()) {
//                List<StatementOfWorkGetDetails> statementOfWorkGetDetailsList = new ArrayList<>();
//                for (StatementOfWorkEntity statementOfWorkEntity : sow.get()) {
//                    if (null != statementOfWorkEntity.getSkillOwnerEntity()) {
//                        StatementOfWorkGetDetails statementOfWorkGetDetails = new StatementOfWorkGetDetails();
//                        statementOfWorkGetDetails.setId(statementOfWorkEntity.getId());
//                        statementOfWorkGetDetails.setSkillOwnerName(statementOfWorkEntity.getSkillOwnerEntity().getFirstName());
//                        statementOfWorkGetDetails.setRole(statementOfWorkEntity.getRoles());
//                        if (null == statementOfWorkEntity.getSkillSeekerProject()) {
//                            statementOfWorkGetDetails.setProject(Default);
//                        } else {
//                            statementOfWorkGetDetails.setProject(statementOfWorkEntity.getSkillSeekerProject().getTitle());
//                        }
//                        if (null == statementOfWorkEntity.getOwnerSkillDomainEntity()) {
//                            statementOfWorkGetDetails.setDepartment(Default);
//                        } else {
//                            statementOfWorkGetDetails.setDepartment(statementOfWorkEntity.getOwnerSkillDomainEntity().getDomainValues());
//                        }
//                        statementOfWorkGetDetails.setEmail(statementOfWorkEntity.getSkillOwnerEntity().getPrimaryEmail());
//                        statementOfWorkGetDetails.setStatus(statementOfWorkEntity.getSowStatus().getStatus());
//                        statementOfWorkGetDetails.setPhone(statementOfWorkEntity.getSkillOwnerEntity().getPhoneNumber());
//                        statementOfWorkGetDetails.setOwnerId(statementOfWorkEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
//                        statementOfWorkGetDetails.setJobId(statementOfWorkEntity.getJobId().getJobId());
//                        statementOfWorkGetDetailsList.add(statementOfWorkGetDetails);
//                    }
//                }
//                logger.info("StatementOfWorkServiceImpl || getAllSowDetails || getting All StatementOfWork Details");
//                return statementOfWorkGetDetailsList;
//            } else {
//                throw new ServiceException();
//            }
//        } catch (ServiceException e) {
//            throw new ServiceException(DATA_NOT_FOUNDED.getErrorCode(), DATA_NOT_FOUNDED.getErrorDesc());
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
//    }
//
//    @Override
//    @Transactional
//    public StatementOfWork upDateSow(MultipartFile multipartFile, String sowUpDateRequest) throws IOException {
//        SowUpdateRequest sowUpdateRequest = objectMapper.readValue(sowUpDateRequest, SowUpdateRequest.class);
//        Optional<StatementOfWorkEntity> statementOfWork = statementOfWorkRepository.findById(sowUpdateRequest.getSowId());
//        StatementOfWork statementOfWorkStatus = new StatementOfWork();
//        if (statementOfWork.isPresent() && statementOfWork.get().getSowStatus().getId() == 7) {
//            ContractFiles contractFiles = null;
//            if (!multipartFile.isEmpty()) {
//                contractFileRepository.findById(statementOfWork.get().getSow().getId());
//                contractFiles = contractFileRepository.save(createContract(multipartFile));
//            }
//            BeanUtils.copyProperties(sowUpdateRequest, statementOfWork.get(), NullPropertyName.getNullPropertyNames(sowUpDateRequest));
//            statementOfWork.get().setSow(contractFiles);
//            statementOfWork.get().setSowStatus(sowStatusRepository.findById(12).get());
//            statementOfWorkRepository.save(statementOfWork.get());
//            Notification notification = new Notification();
//            notificationService.sowStatusNotification(statementOfWork.get(), notification);
//            statementOfWorkStatus.setId(statementOfWork.get().getId());
//            statementOfWorkStatus.setStatus(statementOfWork.get().getSowStatus().getStatus());
//        }
//        return statementOfWorkStatus;
//    }
//
//
//    /**
//     * @return dto
//     */
//    @Override
//    public SowStatusDto updateSowStatus(int id, int sowStatusId) {
//        Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkRepository.findById(id);
//        Optional<ContractStatus> sowStatus = sowStatusRepository.findById(sowStatusId);
//        SowStatusDto sowStatusDto = new SowStatusDto();
//        if (statementOfWorkEntity.isPresent() && sowStatus.isPresent()) {
//            statementOfWorkEntity.get().setSowStatus(sowStatus.get());
//            statementOfWorkRepository.save(statementOfWorkEntity.get());
//            sowStatusDto.setSowId(statementOfWorkEntity.get().getId());
//            sowStatusDto.setSowStatusId(sowStatus.get().getId());
//            if (sowStatus.get().getId() == 8) {
//                Notification notification = new Notification();
//                notificationService.sowStatusNotification(statementOfWorkEntity.get(), notification);
//                statementOfWorkEntity.get().setSowStartDate(LocalDate.now());
//                statementOfWorkRepository.save(statementOfWorkEntity.get());
//            }
//            if (sowStatus.get().getId() == 9) {
//                Notification notification = new Notification();
//                notificationService.sowStatusNotification(statementOfWorkEntity.get(), notification);
//            }
//        } else {
//            throw new ServiceException(MSA_ID_NOT_FOUND.getErrorCode(), MSA_ID_NOT_FOUND.getErrorDesc());
//        }
//        return sowStatusDto;
//    }

    @Override
    @Transactional
    public StatementOfWorkEntity downloadAgreementSOW(int id) {
        Optional<StatementOfWorkEntity> sow = statementOfWorkRepository.findById(id);
        if (sow.isPresent()) {
            logger.info("StatementOfWorkImpl || downloadAgreementSOW || DownloadAgreement for StatementOfWork");
            return sow.get();
        } else {
            throw new ServiceException(INVALID_SOW_ID.getErrorCode(), INVALID_SOW_ID.getErrorDesc());

        }
    }

    @Override
    @Transactional
    public ResponseEntity<Resource> templateDownload() {
        List<TemplateTable> downloadTemplate = templateRepository.findByTemplateFile("SOW_TEMPLATE");

        ByteArrayResource resource = null;
        if (!downloadTemplate.isEmpty()) {
            resource = new ByteArrayResource(downloadTemplate.get(0).getData());
        } else {
            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
        }
        logger.info("StatementOfWorkImpl || templateDownload || StatementOfWorkTemplateDownload");
        return ResponseEntity.ok().header("Content-disposition", "attachment; filename=" + "Sow_Template").contentType(MediaType.valueOf(downloadTemplate.get(0).getTemplateMimeType())).body(resource);
    }


    @Override
    @Transactional
    public StatementOfWorkEntity getSow(int id) {
        try {
            Optional<ContractFiles> contractFiles = contractFileRepository.findById(id);
            Optional<StatementOfWorkEntity> sow = statementOfWorkRepository.findById(contractFiles.get().getId());
            if (contractFiles.get().getMimeType().equals("application/pdf") || contractFiles.get().getMimeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || contractFiles.get().getMimeType().equals("application/msword")) {
                return sow.get();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_ID.getErrorDesc());
        }
        return null;
    }

    @Override
    public Optional<StatementOfWorkEntity> findByOwnerId(int ownerId) {
        return statementOfWorkRepository.findByOwnerId(ownerId);
    }

    @Override
@Transactional
    public Optional<StatementOfWorkEntity> getBySowId(int sowId) {
        Optional<StatementOfWorkEntity> sow = statementOfWorkRepository.findBySowId(sowId);
        return  sow;
    }


    @Override

    @Transactional

    public Optional<StatementOfWorkEntity> getByProjectId(int projectId) {
        Optional<StatementOfWorkEntity> sow = statementOfWorkRepository.findByProjectId(projectId);
        return sow;
    }

    @Override
    public StatementOfWorkEntity save(StatementOfWorkEntity sowEntity) {
        StatementOfWorkEntity  statementOfWorkEntity = statementOfWorkRepository.save(sowEntity);
        return statementOfWorkEntity;
    }

    @Override
    public Optional<StatementOfWorkEntity> getById(int sowEntityId) {
        Optional<StatementOfWorkEntity> sow = statementOfWorkRepository.findById(sowEntityId);
        return sow;
    }

    @Override
    public Optional<StatementOfWorkEntity> getStatementofWorkEntity(int id) {
        return Optional.ofNullable(statementOfWorkRepository.findById(id)
                .orElseThrow(() -> new ServiceException(SOWDATA_NOT_SAVED.getErrorCode(),SOWDATA_NOT_SAVED.getErrorDesc())));
    }

    @Override
    public Optional<StatementOfWorkEntity> getByPartnerJob(int partnerId, String jobId) {
        return statementOfWorkRepository.findByPartnerJob(partnerId, jobId);
    }

    @Override
    @Transactional
    public Optional<StatementOfWorkEntity> getByJobId(String jobId) {
        return statementOfWorkRepository.findByJob(jobId);
    }

    @Override
    @Transactional
    public Optional<List<StatementOfWorkEntity>> findBySkillSeekerId(int seekerId) {
        Optional<List<StatementOfWorkEntity>> sowList = statementOfWorkRepository.findBySkillSeekerId(seekerId);
        return sowList;
    }

    @Override
    public List<StatementOfWorkEntity> findAll() {
        List<StatementOfWorkEntity> sowList = statementOfWorkRepository.findAll();
        return sowList;
    }

}