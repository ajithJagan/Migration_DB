package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.MsaStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerMsaDto;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerMSAService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdminMsa;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class SkillSeekerMSAServiceImpl implements SkillSeekerMSAService {

    Logger logger = LoggerFactory.getLogger(SkillSeekerMSAServiceImpl.class);
    @Autowired
    PoServiceImpl poService;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    private SelectionPhaseRepository selectionPhaseRepository;
    @Autowired
    private SkillSeekerMsaRepository skillSeekerMSARepository;
    @Autowired
    private SkillSeekerProjectRepository skillSeekerProjectRepository;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContractStatusRepository msaStatusRepository;

    @Override
    @Transactional
    public Optional<SkillSeekerMSAEntity> downloadAgreement(int id) {

        Optional<SkillSeekerMSAEntity> msa = skillSeekerMSARepository.findById(id);
        if (msa.isPresent()) {
            logger.info("SkillSeekerMSAServiceImpl || downloadAgreement || skillSeekerMsaDownloadAgreement");
            return msa;
        } else {
            throw new ServiceException(INVALID_MSA_ID.getErrorCode(), INVALID_MSA_ID.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public List<SeekerAdminMsa> getMsaDetails() {
        try {
            Optional<List<SkillSeekerMSAEntity>> skillSeekerMSA = Optional.of(skillSeekerMSARepository.findAll());
            if (!skillSeekerMSA.get().isEmpty()) {
                List<SeekerAdminMsa> seekerAdminMsaList = new ArrayList<>();
                for (SkillSeekerMSAEntity seekerMSA : skillSeekerMSA.get()) {
                    SeekerAdminMsa seekerAdminMsa = new SeekerAdminMsa();
                    seekerAdminMsa.setId(seekerMSA.getId());
                    seekerAdminMsa.setSkillSeekerId(seekerMSA.getSkillSeekerId());
                    if (null == seekerMSA.getSkillSeekerProject()) {
                        seekerAdminMsa.setSkillSeekerProjectName("Default");
                        seekerAdminMsa.setSkillSeekerProjectDept("Default");
                    } else {
                        seekerAdminMsa.setSkillSeekerProjectName(seekerMSA.getSkillSeekerProject().getTitle());
                        seekerAdminMsa.setSkillSeekerProjectDept(seekerMSA.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                    }
                    seekerAdminMsa.setDateSigned(seekerMSA.getDateSigned());
                    seekerAdminMsa.setJobId(seekerMSA.getJobId().getJobId());
                    seekerAdminMsaList.add(seekerAdminMsa);
                }
                logger.info("SkillSeekerMSAServiceImpl || getMsaDetails || SeekerAdminMsaDto Added");
                return seekerAdminMsaList;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_MSA_ID.getErrorCode(), INVALID_MSA_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public List<SeekerAdminMsa> getMsaDetailsBySeeker(int id) {
        try {
            Optional<List<SkillSeekerMSAEntity>> skillSeekerMSA = skillSeekerMSARepository.findBySkillSeekerId(id);
            if (!skillSeekerMSA.get().isEmpty()) {
                List<SeekerAdminMsa> seekerAdminMsaList = new ArrayList<>();
                for (SkillSeekerMSAEntity seekerMSA : skillSeekerMSA.get()) {
                    SeekerAdminMsa seekerAdminMsa = new SeekerAdminMsa();
                    seekerAdminMsa.setId(seekerMSA.getId());
                    seekerAdminMsa.setSkillSeekerId(seekerMSA.getSkillSeekerId());
                    if (null == seekerMSA.getSkillSeekerProject()) {
                        seekerAdminMsa.setSkillSeekerProjectName("Default");
                        seekerAdminMsa.setSkillSeekerProjectDept("Default");
                    } else {
                        seekerAdminMsa.setSkillSeekerProjectName(seekerMSA.getSkillSeekerProject().getTitle());
                        seekerAdminMsa.setSkillSeekerProjectDept(seekerMSA.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                    }
                    seekerAdminMsa.setDateSigned(seekerMSA.getDateSigned());
                    seekerAdminMsa.setJobId(seekerMSA.getJobId().getJobId());
                    seekerAdminMsaList.add(seekerAdminMsa);
                }
                logger.info("SkillSeekerMSAServiceImpl || getMsaDetails || SeekerAdminMsaDto Added");
                return seekerAdminMsaList;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public SkillSeekerMsaDto addDocuments(List<MultipartFile> multipartFiles, int seekerId, int projectID, String jobId, int ownerId) {
        HashMap<String, String> fileTypeList = new HashMap<>();
        SkillSeekerMsaDto skillSeekerMsaDto = new SkillSeekerMsaDto();

        fileTypeList.put(PDF, APPLICATION_VND_PDF);
        fileTypeList.put(DOC, TEXT_DOC);
        fileTypeList.put(DOCX, TEXT_DOCX);
        if (!multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (fileTypeList.containsValue(multipartFile.getContentType())) {
                    logger.info("SkillSeekerMSAServiceImpl || checkFileTypeAndUpload || File Type verified !!");
                    try {
                        SkillSeekerMSAEntity skillSeekerMSAEntity = skillSeekerMSARepository.save(create(multipartFile, seekerId, projectID, jobId, ownerId));
                        skillSeekerMsaDto.setId(skillSeekerMSAEntity.getId());
                        Notification notification = new Notification();
                        notificationService.msaStatusNotification(skillSeekerMSAEntity, notification);
                        poService.removeCandidates(ownerId, jobId);
                        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, ownerId);
                        if (selectionPhase.isPresent()) {
                            LocalDate localDate = LocalDate.now();
                            selectionPhase.get().setJoinedOn(localDate);
                            selectionPhaseRepository.saveAndFlush(selectionPhase.get());

                        }
                        logger.info("SkillSeekerMSAServiceImpl || checkFileTypeAndUpload || Uploaded the file successfully: {} // ->", multipartFile.getOriginalFilename());
                    } catch (NullPointerException e) {
                        throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
                    } catch (Exception e) {
                        logger.info("SkillSeekerMSAServiceImpl || checkFileTypeAndUpload || Could not upload the file: {} // ->", multipartFile.getOriginalFilename());
                        throw new ServiceException(EXPECTATION_FAILED.getErrorCode(), EXPECTATION_FAILED.getErrorDesc());
                    }
                } else {
                    logger.info("SkillSeekerMSAServiceImpl || checkFileTypeAndUpload || Wrong File Format ");
                    throw new ServiceException(WRONG_FILE_FORMAT.getErrorCode(), WRONG_FILE_FORMAT.getErrorDesc());
                }
            }
            logger.info("SkillSeekerMSAServiceImpl || addDocuments || Adding MSA Documents");

            return skillSeekerMsaDto;
        } else {
            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
        }
    }


    @Transactional
    private SkillSeekerMSAEntity create(MultipartFile multipartFile, int seekerId, int projectID, String jobId, int ownerId) throws IOException {
//        Contract contract=new Contract();
        Optional<SkillSeekerMSAEntity> seekerMSA = Optional.ofNullable(skillSeekerMSARepository.findByOwnerId(ownerId));
        if (!seekerMSA.isPresent()) {
            Optional<Job> job = jobRepository.findByJobId(jobId);
            if (job.isPresent()) {
                SkillSeekerMSAEntity skillSeekerMSAEntity = new SkillSeekerMSAEntity();
                skillSeekerMSAEntity.setSkillSeekerId(seekerId);
                if (projectID == 0) {
                    skillSeekerMSAEntity.setSkillSeekerProject(null);
                } else {
                    skillSeekerMSAEntity.setSkillSeekerProject(skillSeekerProjectRepository.getById(projectID));
                }
//                Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

                skillSeekerMSAEntity.setDateSigned(LocalDate.now());

                skillSeekerMSAEntity.setName(multipartFile.getOriginalFilename());
                skillSeekerMSAEntity.setMimeType(multipartFile.getContentType());
                skillSeekerMSAEntity.setSize(multipartFile.getSize());
                skillSeekerMSAEntity.setData(multipartFile.getBytes());
                skillSeekerMSAEntity.setJobId(job.get());
                SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
                skillOwnerEntity.setSkillOwnerEntityId(ownerId);
                skillSeekerMSAEntity.setSkillOwnerEntity(skillOwnerEntity);
                skillSeekerMSAEntity.setMsaStatus(msaStatusRepository.findById(1).get());
                return skillSeekerMSAEntity;
            } else {
                logger.error("Cant find job associated");
                throw new NullPointerException();
            }
        } else {
            throw new ServiceException(MSA_ALREADY_EXISTS.getErrorCode(), MSA_ALREADY_EXISTS.getErrorDesc());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<Resource> getSkillSeekerMsaTemplateDownload() {

        List<TemplateTable> downloadTemplate = templateRepository.findByTemplateFile("SEEKER_MSA_TEMPLATE");
        ByteArrayResource resource;

        if (!downloadTemplate.isEmpty()) {
            resource = new ByteArrayResource(downloadTemplate.get(0).getData());
        } else {
            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
        }
        logger.info("SkillSeekerMSAServiceImpl || getSkillSeekerMsaTemplateDownload || SkillSeekerMsaTemplateDownload");

        return ResponseEntity.ok().header("Content-disposition", "attachment; filename=" + "Msa_Template").contentType(MediaType.valueOf(downloadTemplate.get(0).getTemplateMimeType())).body(resource);
    }

    @Override
    public List<ContractStatus> getMsaStatus() {
        try {
            return msaStatusRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public MsaStatusDto updateMsaStatus(int msaId, int msaStatusId) {
        Optional<SkillSeekerMSAEntity> skillSeekerMSAEntity = skillSeekerMSARepository.findById(msaId);
        Optional<ContractStatus> msaStatus = msaStatusRepository.findById(msaStatusId);
        MsaStatusDto msaStatusDto = new MsaStatusDto();
        if (skillSeekerMSAEntity.isPresent() && msaStatus.isPresent()) {
            skillSeekerMSAEntity.get().setMsaStatus(msaStatus.get());
            skillSeekerMSARepository.save(skillSeekerMSAEntity.get());
            msaStatusDto.setMsaId(skillSeekerMSAEntity.get().getId());
            msaStatusDto.setMsaStatusId(msaStatus.get().getId());
            if (msaStatus.get().getId() == 3) {
                Notification notification = new Notification();
                notificationService.msaStatusNotification(skillSeekerMSAEntity.get(), notification);
            }
            if (msaStatus.get().getId() == 2) {
                Notification notification = new Notification();
                notificationService.msaStatusNotification(skillSeekerMSAEntity.get(), notification);
            }
            if (msaStatus.get().getId() == 4) {
                Notification notification = new Notification();
                notificationService.msaStatusNotification(skillSeekerMSAEntity.get(), notification);
            }
            if (msaStatus.get().getId() == 5) {
                Notification notification = new Notification();
                notificationService.msaStatusNotification(skillSeekerMSAEntity.get(), notification);
            }
            if (msaStatus.get().getId() == 6) {
                Notification notification = new Notification();
                notificationService.msaStatusNotification(skillSeekerMSAEntity.get(), notification);
            }
        } else {
            throw new ServiceException(MSA_ID_NOT_FOUND.getErrorCode(), MSA_ID_NOT_FOUND.getErrorDesc());

        }
        return msaStatusDto;
    }

    @Override
    @Transactional
    public SkillSeekerMSAEntity getMSA(int id) {
        try {
            Optional<SkillSeekerMSAEntity> byId = skillSeekerMSARepository.findById(id);

            if (byId.get().getMimeType().equals("application/pdf") || byId.get().getMimeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || byId.get().getMimeType().equals("application/msword")) {
                return byId.get();
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_ID.getErrorDesc());
        }
        return null;
    }
}






