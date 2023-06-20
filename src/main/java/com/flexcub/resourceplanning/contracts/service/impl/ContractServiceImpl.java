package com.flexcub.resourceplanning.contracts.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexcub.resourceplanning.config.PartnerContractPagination;
import com.flexcub.resourceplanning.config.SeekerContractPagination;
import com.flexcub.resourceplanning.contracts.dto.*;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.contracts.service.ContractService;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.*;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerProjectService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import io.netty.handler.codec.http.HttpUtil;
import liquibase.pro.packaged.T;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.Null;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FileService.createContract;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class ContractServiceImpl implements ContractService {

    private static final int MSA_STATUS_ID = 17;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SkillOwnerService skillOwnerService;
    @Autowired
    PoService poService;
    @Autowired
    JobService jobService;
    @Autowired
    StatementOfWorkService statementOfWorkService;
    @Autowired
    SkillSeekerProjectService skillSeekerProjectService;
    @Autowired
    ContractFileRepository contractFileRepository;
    @Autowired
    ContractStatusRepository contractStatusRepository;
    @Autowired
    SkillSeekerRepository skillSeekerRepository;
    @Autowired
    SkillPartnerRepository skillPartnerRepository;
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    SelectionPhaseService selectionPhaseService;
    @Autowired
    SkillSeekerService skillSeekerService;
    @Autowired
    SkillSeekerProjectRepository seekerProjectRepository;
    @Autowired
    RequirementPhaseRepository requirementPhaseRepository;
    @Autowired
    SkillPartnerService partnerService;
    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;

    @Autowired
    JobRepository jobRepository;
    @Autowired
    PoRepository poRepository;
    Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);
    @Autowired
    PartnerContractPagination partnerContractPagination;
    @Autowired
    SeekerContractPagination seekerContractPagination;


    @Value("${file.content.disposition}")
    private String content;

    @Value("${file.name}")
    private String fileName;


    @Override
    public MsaInitiate msaInitiate(MsaInitiate initiate) {
        Optional<RegistrationEntity> register = registrationRepository.findById(initiate.getId());
        if (register.get().getRoles().getRolesId() == 1) {
            Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerService.getSkillSeekerEntity(initiate.getId());
            if (skillSeekerEntity.isPresent()) {
                skillSeekerEntity.get().setMsaStatusId(contractStatusSeeker(skillSeekerEntity));
                skillSeekerRepository.save(skillSeekerEntity.get());
            }
        }
        if (register.get().getRoles().getRolesId() == 2) {
            Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(initiate.getId());
            if (skillPartnerEntity.isPresent()) {
                skillPartnerEntity.get().setMsaStatusId(contractStatusPartner(skillPartnerEntity));
                skillPartnerRepository.save(skillPartnerEntity.get());
            }
        }
        return initiate;
    }


    @Transactional
    @Override
    public MsaFilesResponse createSeekerMsa(MultipartFile multipartFile, String seekerMsaFileDetails) throws IOException {

        MsaFilesResponse msaFilesResponse = new MsaFilesResponse();
        if (multipartFile.isEmpty()) {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }
        SeekerMsaFileRequest seekerMsaFileRequest = objectMapper.readValue(seekerMsaFileDetails, SeekerMsaFileRequest.class);
        Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerService.getSkillSeekerEntity(seekerMsaFileRequest.getSeekerId());
        if (!skillSeekerEntity.isPresent()) {
            throw new ServiceException(SEEKER_NOT_FOUND.getErrorCode(), SEEKER_NOT_FOUND.getErrorDesc());
        }
        skillSeekerEntity.map(SkillSeekerEntity::getMsaId)
                .map(msaId -> {
                    try {
                        return getSaveAndFlush(multipartFile, msaId);
                    } catch (ServiceException e) {
                        throw new ServiceException(UNABLE_SAVE_MULTIPART_FILE.getErrorCode(), UNABLE_SAVE_MULTIPART_FILE.getErrorDesc());
                    }
                })
                .ifPresentOrElse(contractFile -> {
                            try {
                                msaAlreadyExists(multipartFile, msaFilesResponse, seekerMsaFileRequest, skillSeekerEntity);
                            } catch (ServiceException | IOException e) {
                                throw new ServiceException(UNABLE_CREATE_NEW_MSA_FILE.getErrorCode(), UNABLE_CREATE_NEW_MSA_FILE.getErrorDesc());
                            }
                        },
                        () -> {
                            try {
                                newMsaCreate(multipartFile, msaFilesResponse, seekerMsaFileRequest, skillSeekerEntity);
                            } catch (ServiceException | IOException e) {
                                throw new ServiceException(UNABLE_CREATE_NEW_MSA_FILE.getErrorCode(), UNABLE_CREATE_NEW_MSA_FILE.getErrorDesc());
                            }
                        });

        logger.info("ContractServiceImpl || createMsaForSeeker || agreement for SkillSeeker ");

        return msaFilesResponse;
    }

    private ContractFiles getSaveAndFlush(MultipartFile multipartFile, ContractFiles msaId) {
        try {
            return contractFileRepository.saveAndFlush(updateContract(multipartFile, msaId.getId()));
        } catch (ServiceException e) {
            throw new ServiceException(UNABLE_SAVE_MULTIPART_FILE.getErrorCode(), UNABLE_SAVE_MULTIPART_FILE.getErrorDesc());
        }
    }

    private void newMsaCreate(MultipartFile multipartFile, MsaFilesResponse msaFilesResponse, SeekerMsaFileRequest seekerMsaFileRequest, Optional<SkillSeekerEntity> skillSeekerEntity) throws IOException {
        if (skillSeekerEntity.isPresent() && Objects.isNull(skillSeekerEntity.get().getMsaId())) {
            dataSetSeeker(multipartFile, msaFilesResponse, seekerMsaFileRequest, skillSeekerEntity);
        }
    }

    private void dataSetSeeker(MultipartFile multipartFile, MsaFilesResponse msaFilesResponse, SeekerMsaFileRequest seekerMsaFileRequest, Optional<SkillSeekerEntity> skillSeekerEntity) throws IOException {
        ContractFiles contractFiles = getContractFile(multipartFile);
        skillSeekerEntity.get().setMsaId(contractFiles);
        skillSeekerEntity.get().setMsaStartDate(seekerMsaFileRequest.getContractStartDate());
        skillSeekerEntity.get().setMsaEndDate(seekerMsaFileRequest.getContractExpiryDate());
        if (skillSeekerEntity.get().getMsaStatusId().getId() == 14) {
            updateSeekerMsaStatus(skillSeekerEntity.get().getMsaId().getId(), 15);

            Notification notification = new Notification();
            notificationService.seekerToSuperAdminMsaNotification(skillSeekerEntity.get(), notification);
        }
        skillSeekerService.saveOptionalSeeker(skillSeekerEntity);
        msaFilesResponse.setFileId(contractFiles.getId());
        msaFilesResponse.setMsaStatusId(skillSeekerEntity.get().getMsaStatusId().getId());
        msaFilesResponse.setMsaStatus(skillSeekerEntity.get().getMsaStatusId().getStatus());
    }

    private void msaAlreadyExists(MultipartFile multipartFile, MsaFilesResponse msaFilesResponse, SeekerMsaFileRequest seekerMsaFileRequest, Optional<SkillSeekerEntity> skillSeekerEntity) throws IOException {

        if (skillSeekerEntity.isPresent() && skillSeekerEntity.get().getMsaStatusId().getId() == 14 && seekerMsaFileRequest.getContractStartDate().isAfter(skillSeekerEntity.get().getMsaEndDate()) || skillSeekerEntity.isPresent() && skillSeekerEntity.get().getMsaStatusId().getId() == 16) {
            dataSetSeeker(multipartFile, msaFilesResponse, seekerMsaFileRequest, skillSeekerEntity);
        } else {
            throw new ServiceException(UNABLE_SAVE_MULTIPART_FILE.getErrorCode(), UNABLE_SAVE_MULTIPART_FILE.getErrorDesc());
        }
    }

    private ContractFiles getContractFile(MultipartFile multipartFile) throws IOException {
        ContractFiles contract = createContract(multipartFile);
        try {
            return contractFileRepository.save(contract);
        } catch (ServiceException e) {
            throw new ServiceException(UNABLE_SAVE_MULTIPART_FILE.getErrorCode(), UNABLE_SAVE_MULTIPART_FILE.getErrorDesc());
        }
    }

    private ContractStatus contractStatusSeeker(Optional<SkillSeekerEntity> skillSeekerEntity) {
        skillSeekerEntity.ifPresent(entity -> entity.setMsaStatusId(contractStatusRepository.findById(14).
                orElseThrow(() -> new ServiceException(INVALID_CONTRACT_STATUS.getErrorCode(), INVALID_CONTRACT_STATUS.getErrorDesc()))));
        return skillSeekerEntity.get().getMsaStatusId();
    }

    private ContractStatus contractStatusPartner(Optional<SkillPartnerEntity> skillPartnerEntity) {
        skillPartnerEntity.ifPresent(entity -> entity.setMsaStatusId(contractStatusRepository.findById(14).
                orElseThrow(() -> new ServiceException(INVALID_CONTRACT_STATUS.getErrorCode(), INVALID_CONTRACT_STATUS.getErrorDesc()))));
        return skillPartnerEntity.get().getMsaStatusId();
    }

    @Transactional
    @Override
    public MsaFilesResponse createPartnerMsa(MultipartFile multipartFile, String msaFileDetails) throws IOException {
        MsaFilesResponse msaFilesResponse = new MsaFilesResponse();
        if (!multipartFile.isEmpty()) {
            ContractFiles contractFiles = getContractFile(multipartFile);
            PartnerMsaFileRequest msaFileRequest = objectMapper.readValue(msaFileDetails, PartnerMsaFileRequest.class);
            Optional<SkillPartnerEntity> skillPartnerEntity = Optional.ofNullable((skillPartnerRepository.findById(msaFileRequest.getPartnerId()).orElseThrow(() -> new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc()))));
            if (skillPartnerEntity.isPresent()) {
                if (Objects.isNull(skillPartnerEntity.get().getMsaId())) {
                    dataSetPartner(msaFilesResponse, contractFiles, msaFileRequest, skillPartnerEntity);
                } else if (skillPartnerEntity.get().getMsaStatusId().getId() == 14 && msaFileRequest.getMsaContractStartDate().isAfter(skillPartnerEntity.get().getMsaEndDate())) {
                    dataSetPartner(msaFilesResponse, contractFiles, msaFileRequest, skillPartnerEntity);
                } else {
                    throw new ServiceException(UNABLE_CREATE_NEW_MSA_FILE.getErrorCode(), UNABLE_CREATE_NEW_MSA_FILE.getErrorDesc());
                }
            }
        } else {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }
        logger.info("ContractServiceImpl || createMsaForPartner || New MSA agreement for SkillPartner ");
        return msaFilesResponse;
    }

    private void dataSetPartner(MsaFilesResponse msaFilesResponse, ContractFiles contractFiles, PartnerMsaFileRequest msaFileRequest, Optional<SkillPartnerEntity> skillPartnerEntity) {
        skillPartnerEntity.get().setMsaId(contractFiles);
        skillPartnerEntity.get().setMsaStartDate(msaFileRequest.getMsaContractStartDate());
        skillPartnerEntity.get().setMsaEndDate(msaFileRequest.getMsaContractExpiryDate());
        updatePartnerMsaStatus(skillPartnerEntity.get().getSkillPartnerId(), 15);
        skillPartnerRepository.save(skillPartnerEntity.get());
        Notification notification = new Notification();
        notificationService.partnerMsaNotification(skillPartnerEntity.get(), notification);
        msaFilesResponse.setFileId(contractFiles.getId());
        msaFilesResponse.setMsaStatusId(skillPartnerEntity.get().getMsaStatusId().getId());
        msaFilesResponse.setMsaStatus(skillPartnerEntity.get().getMsaStatusId().getStatus());
    }


    @Transactional
    @Override
    public List<PartnerContractDetails> getPartnerMsaDetailsInSuperAdmin() {

        List<PartnerContractDetails> partnerMsaDetails = new ArrayList<>();
        List<SkillPartnerEntity> entityList = skillPartnerRepository.findAll();
        entityList.stream()
                .filter(skillPartnerEntity -> skillPartnerEntity.getMsaStatusId() != null)
                .forEach(skillPartnerEntity -> {
                    PartnerContractDetails msaDetails = new PartnerContractDetails();
                    msaDetails.setSkillPartnerId(skillPartnerEntity.getSkillPartnerId());
                    msaDetails.setProject(null);
                    msaDetails.setDepartment(null);
                    msaDetails.setJobId(null);
                    msaDetails.setNoOfResource(null);
                    msaDetails.setBusinessName(skillPartnerEntity.getBusinessName());
                    msaDetails.setEmail(skillPartnerEntity.getBusinessEmail());
                    msaDetails.setPhone(skillPartnerEntity.getPrimaryContactPhone());
                    msaDetails.setContractStartDate(skillPartnerEntity.getMsaStartDate());
                    msaDetails.setContractExpiryDate(skillPartnerEntity.getMsaEndDate());
                    msaDetails.setStatus(skillPartnerEntity.getMsaStatusId().getStatus());
                    msaDetails.setStatusId(skillPartnerEntity.getMsaStatusId().getId());
                    partnerMsaDetails.add(msaDetails);
                });

        logger.info("ContractServiceImpl || getPartnerMsaDetailsInSuperAdmin || Getting all partner Msa details in super admin ");
        return partnerMsaDetails;

    }


    @Transactional
    @Override
    public ResponseEntity<Resource> downloadSkillPartnerMsaAgreement(int id) throws IOException {
        Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(id);
        if (skillPartnerEntity.isPresent()) {
            logger.info("ContractServiceImpl || downloadSkillPartnerMsaAgreement || download Msa Agreement for SkillPartner");
            return ResponseEntity.
                    ok().
                    contentType(MediaType.parseMediaType(skillPartnerEntity.get().getMsaId().getMimeType())).
                    header("Content-disposition", "attachment; filename=" + skillPartnerEntity.get().getBusinessName() + "_MSA").
                    body(new ByteArrayResource(skillPartnerEntity.get().getMsaId().getData()));
        } else {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public MsaFilesResponse updatePartnerMsaStatus(int partnerId, int msaStatusId) {
        Optional<SkillPartnerEntity> skillPartnerEntity = Optional.ofNullable(skillPartnerRepository.findById(partnerId).orElseThrow(() -> new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc())));
        Optional<ContractStatus> statusId = contractStatusRepository.findById(msaStatusId);
        MsaFilesResponse msaFilesResponse = new MsaFilesResponse();
        if (skillPartnerEntity.isPresent() && statusId.isPresent()) {
            skillPartnerEntity.get().setMsaStatusId(statusId.get());
            skillPartnerRepository.save(skillPartnerEntity.get());
            Notification notification = new Notification();
            notificationService.superAdminMsaNotification(skillPartnerEntity.get(), notification);
            logger.info("ContractServiceImpl || updatePartnerMsaStatus || Update MSA status for SkillPartner ");
            msaFilesResponse.setFileId(skillPartnerEntity.get().getMsaId().getId());
            msaFilesResponse.setMsaStatusId(skillPartnerEntity.get().getMsaStatusId().getId());
            msaFilesResponse.setMsaStatus(skillPartnerEntity.get().getMsaStatusId().getStatus());
        }
        return msaFilesResponse;
    }


//    @Override
//    @Transactional
//    public PoResponse createPO(MultipartFile multipartFile, String poDetails) throws IOException {
//        PoResponse poResponse = new PoResponse();
//        PoEntity poEntity = new PoEntity();
//        PoRequest poRequest = objectMapper.readValue(poDetails, PoRequest.class);
//
//        if (!multipartFile.isEmpty()) {
//            ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));
//            Optional<SkillOwnerEntity> skillOwner = skillOwnerService.getSkillOwnerEntity(poRequest.getOwnerId());
//            if (skillOwner.isPresent()) {
//                Optional<PoEntity> existingPo = poService.getBySkillOwnerIdAndJobId(skillOwner.get().getSkillOwnerEntityId(), poRequest.getJobId());
//                if (existingPo.isEmpty()) {
//                    logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || File Type verified !!");
//                    try {
//                        Optional<Job> existingJob = jobService.getJob(poRequest.getJobId());
//                        poEntity.setPoId(contractFiles);
//                        poEntity.setSkillSeekerId(poRequest.getSeekerId());
//                        poEntity.setSkillOwnerEntity(skillOwner.get());
//                        if (existingJob.isPresent()) {
//                            int baseRate = existingJob.get().getBaseRate();
//                            int maxRate = existingJob.get().getMaxRate();
//                            String baseRateRange = String.valueOf(baseRate);
//                            String maxRateRange = String.valueOf(maxRate);
//                            poEntity.setJobId(existingJob.get());
//                            if (existingJob.get().getSeekerProject() == null) {
//                                poEntity.setSkillSeekerProject(skillSeekerProjectService.getById(0));
//                            } else {
//                                poEntity.setSkillSeekerProject(existingJob.get().getSeekerProject());
//                            }
//                            poEntity.setRole(existingJob.get().getJobTitle());
//                            poEntity.setPriceRange(baseRateRange + " to " + maxRateRange);
//                            poEntity.setOwnerSkillDomainEntity(existingJob.get().getOwnerSkillDomainEntity());
//                        }
//                        poEntity.setRateCard(skillOwner.get().getRateCard());
//                        poEntity.setContractStartDate(poRequest.getContractStartDate());
//                        poEntity.setContractEndDate(poRequest.getContractEndDate());
//                        Optional<ContractStatus> contractStatus = Optional.of(contractStatusRepository.findById(7).get());
//                        if (contractStatus.isPresent()) {
//                            poEntity.setPoStatus(contractStatus.get());
//                        }
//                        poService.save(poEntity);
//                        poResponse.setPoId(poEntity.getId());
//                        poResponse.setPoStatus(poEntity.getPoStatus().getStatus());
//                        Notification notification = new Notification();
//                        notificationService.poStatusNotification(poEntity, notification);
//                        //reducing available number of position
//                        Job byJobJobId = jobService.getById(poRequest.getJobId());
//                        if (byJobJobId.getNumberOfPositions() > 0 && !statementOfWorkService.findByOwnerId(poRequest.getOwnerId()).isPresent()) {
//                            byJobJobId.setNumberOfPositions(byJobJobId.getNumberOfPositions() - 1);
//                            jobService.saveAndFlush(byJobJobId);
//                            logger.info("PurchaseOrderServiceImpl || checkNumbersOfPositions ||checkNumbersOfPositions and reduces -1 ");
//                            poService.removeCandidates(poRequest.getOwnerId(), poRequest.getJobId());
//                        }
//
//
//                        //changing skill status of candidate
//                        OwnerSkillStatusEntity ownerSkillStatus = new OwnerSkillStatusEntity();
//                        ownerSkillStatus.setSkillOwnerStatusId(4);
//                        skillOwner.get().setOwnerSkillStatusEntity(ownerSkillStatus);
//                        skillOwnerService.save(skillOwner.get());
//
//
//                        logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || Uploaded the file successfully: {} // ->", multipartFile.getOriginalFilename());
//                    } catch (NullPointerException e) {
//                        throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid request");
//                    } catch (Exception e) {
//                        logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || Could not upload the file: {} // ->", multipartFile.getOriginalFilename());
//                        throw new ServiceException(EXPECTATION_FAILED.getErrorCode(), EXPECTATION_FAILED.getErrorDesc());
//                    }
//
//                } else {
//                    throw new ServiceException(PO_FILE_SUBMITTED_ALREADY.getErrorCode(), PO_FILE_SUBMITTED_ALREADY.getErrorDesc());
//                }
//
//            } else {
//                throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
//            }
//        }
//        logger.info("PurchaseOrderServiceImpl || addData || Adding Purchase Order");
//        return poResponse;
//    }


    @Override
    @Transactional
    public PoResponse updatePOByAdmin(MultipartFile multipartFile, String updateDetails) throws IOException {
        PoResponse poResponse = new PoResponse();
        UpdatePORequest updatePORequest = objectMapper.readValue(updateDetails, UpdatePORequest.class);

        Optional<PoEntity> po = poService.getById(updatePORequest.getPoId());
        if (po.isPresent()) {
            PoEntity poEntity = po.get();
            if (po.get().getPoStatus().getId() == 7 || po.get().getPoStatus().getId() == 12) {
                if (!(multipartFile == null)) {
                    ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));
                    poEntity.setPoId(contractFiles);
                }
                poEntity.setContractStartDate(updatePORequest.getContractStartDate());
                poEntity.setContractEndDate(updatePORequest.getContractEndDate());
                poEntity.setRateCard(updatePORequest.getRateCard());
                Optional<ContractStatus> contractStatus = Optional.of(contractStatusRepository.findById(12).get());
                if (contractStatus.isPresent()) {
                    poEntity.setPoStatus(contractStatus.get());
                }
                poService.saveAndFlush(poEntity);
                poResponse.setPoId(poEntity.getId());
                poResponse.setPoStatus(poEntity.getPoStatus().getStatus());
                Notification notification = new Notification();
                notificationService.poStatusNotification(poEntity, notification);
            } else {
                throw new ServiceException(PO_FILE_APPROVED_ALREADY.getErrorCode(), PO_FILE_APPROVED_ALREADY.getErrorDesc());
            }
        } else {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid request");
        }

        logger.info("PurchaseOrderServiceImpl || update PO by SuperAdmin || Updating Purchase Order");
        return poResponse;
    }


//    @Override
//    public PurchaseOrder updateStatus(int poId, int statusId) {
//        PurchaseOrder purchaseOrder;
//        try {
//            Optional<PoEntity> poEntity = poService.getById(poId);
//            Optional<ContractStatus> poStatus = contractStatusRepository.findById(statusId);
//
//            purchaseOrder = new PurchaseOrder();
//            if (poEntity.isPresent() && poStatus.isPresent()) {
//                poEntity.get().setPoStatus(poStatus.get());
//                poEntity.get().setContractStartDate(LocalDate.now());
//                poService.save(poEntity.get());
//                purchaseOrder.setId(poEntity.get().getId());
//                purchaseOrder.setStatus(poStatus.get().getStatus());
//                Notification notification = new Notification();
//                notificationService.poStatusNotification(poEntity.get(), notification);
//            } else {
//                throw new NullPointerException();
//            }
//        } catch (NullPointerException e) {
//            throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
//        return purchaseOrder;
//    }

    public Boolean seekerMSAValidation(int seekerId) {
        Optional<SkillSeekerEntity> seeker = skillSeekerService.getSkillSeekerEntity(seekerId);
        return seeker.isPresent() && Objects.nonNull(seeker.get().getMsaId())
                && LocalDate.now().isBefore(seeker.get().getMsaEndDate()) && seeker.get().getMsaStatusId().getId() == 17;
    }

    @Override
    @Transactional
    public MsaFilesResponse updateSeekerMsaStatus(int msaId, int msaStatusId) {
        Optional<SkillSeekerEntity> skillSeekerEntity = Optional.ofNullable(skillSeekerRepository.findByMsaId(msaId).orElseThrow(() -> new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc())));
        Optional<ContractStatus> statusId = contractStatusRepository.findById(msaStatusId);
        MsaFilesResponse msaFilesResponse = new MsaFilesResponse();
        if (skillSeekerEntity.isPresent() && statusId.isPresent()) {
            skillSeekerEntity.get().setMsaStatusId(statusId.get());
            skillSeekerRepository.save(skillSeekerEntity.get());
            notificationService.superAdminToSeekerMsaNotification(skillSeekerEntity.get());
            logger.info("ContractServiceImpl || updateSeekerMsaStatus || Update MSA status for SkillSeeker ");
            msaFilesResponse.setFileId(msaId);
            msaFilesResponse.setMsaStatusId(skillSeekerEntity.get().getMsaStatusId().getId());
            msaFilesResponse.setMsaStatus(skillSeekerEntity.get().getMsaStatusId().getStatus());
        }
        return msaFilesResponse;

    }

    @Override
    @Transactional
    public List<SeekerMSADetails> getSeekerMsaDetailsInSuperAdmin() {

        List<SeekerMSADetails> seekerMSADetails = new ArrayList<>();
        List<SkillSeekerEntity> entityList = skillSeekerRepository.findAll();
        entityList.stream().filter(skillSeekerEntity -> skillSeekerEntity.getMsaId() != null).forEach(skillSeekerEntity -> {

            SeekerMSADetails seekerMSADetails1 = new SeekerMSADetails();
            seekerMSADetails1.setNoOfResource(0);

            seekerMSADetails1.setSkillSeekerId(skillSeekerEntity.getId());
            seekerMSADetails1.setBusinessName(skillSeekerEntity.getSkillSeekerName());
            seekerMSADetails1.setMsaContractStartDate(skillSeekerEntity.getMsaStartDate());
            seekerMSADetails1.setMsaContractExpiryDate(skillSeekerEntity.getMsaEndDate());
            seekerMSADetails1.setStatus(skillSeekerEntity.getMsaStatusId().getStatus());
            seekerMSADetails1.setContractId(skillSeekerEntity.getMsaId().getId());
            try {
                List<Job> jobs = jobRepository.findBySkillSeekerId(skillSeekerEntity.getId());
                jobs.stream().filter(k -> Objects.nonNull(k.getSkillSeeker().getMsaId())).forEach(j -> {

                    if(j.getSkillSeeker().getMsaId()!=null) {
                        seekerMSADetails1.setJobId(j.getJobId());
                        if(j.getSeekerProject()==null) {
                            seekerMSADetails1.setProjectId(0);
                            seekerMSADetails1.setProjectName("Default");
                        }else{
                            seekerMSADetails1.setProjectId(j.getSeekerProject().getId());
                            seekerMSADetails1.setProjectName(j.getSeekerProject().getTitle());
                            seekerMSADetails1.setDepartment(j.getOwnerSkillDomainEntity().getDomainValues());
                        }

                    }

                });
            } catch (NullPointerException e) {
                logger.info("Null found");
            }
            seekerMSADetails1.setStatusId(skillSeekerEntity.getMsaStatusId().getId());
            seekerMSADetails1.setEmail(skillSeekerEntity.getEmail());
            seekerMSADetails1.setPhoneNumber(skillSeekerEntity.getPhone());
            seekerMSADetails.add(seekerMSADetails1);

        });
        logger.info("ContractServiceImpl || getSeekerMsaDetailsInSuperAdmin || Getting all Seeker Msa details in super admin ");
        return seekerMSADetails;
    }

    @Override
    public ResponseEntity<Resource> downloadSeekerMsa(int seekerId) {

        Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerService.getSkillSeekerEntity(seekerId);

        if (!skillSeekerEntity.isPresent()) {
            throw new ServiceException(NO_SEEKER_MSA_FOUND.getErrorCode(), NO_SEEKER_MSA_FOUND.getErrorDesc());
        }

        return ResponseEntity
                .ok()
                .header(content, fileName + (skillSeekerEntity.get().getMsaId().getFileName()))
                .contentType(MediaType.valueOf(skillSeekerEntity.get().getMsaId().getMimeType()))
                .body(new ByteArrayResource(skillSeekerEntity.get().getMsaId().getData()));

    }


    public ContractFiles getMsa(int seekerId) {
        Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerService.getSkillSeekerEntity(seekerId);
        if (skillSeekerEntity.isPresent()) {
            Optional<ContractFiles> contractFiles = getContractFiles(skillSeekerEntity.get().getMsaId().getId());
            if (contractFiles.isPresent()) {
                return contractFiles.get();
            } else {
                throw new ServiceException(CONTRACT_FILE_NOT_FOUND.getErrorCode(), CONTRACT_FILE_NOT_FOUND.getErrorDesc());
            }
        } else {
            throw new ServiceException(NO_SEEKER_MSA_FOUND.getErrorCode(), NO_SEEKER_MSA_FOUND.getErrorDesc());
        }
    }

    public ContractFiles updateContract(MultipartFile multipartFile, int id) {

        Optional<ContractFiles> contractFiles = getContractFiles(id);
        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(PDF, APPLICATION_VND_PDF);
        fileTypeList.put(DOC, TEXT_DOC);
        fileTypeList.put(DOCX, TEXT_DOCX);
        if (!contractFiles.isPresent() && !fileTypeList.containsValue(multipartFile.getContentType())) {
            throw new ServiceException(UNSUPPORTED_FILE_FORMAT.getErrorCode(), UNSUPPORTED_FILE_FORMAT.getErrorDesc());
        }

        contractFiles.ifPresent(files -> {
            try {
                files.setFileName(multipartFile.getOriginalFilename());
                files.setData(multipartFile.getBytes());
                files.setMimeType(multipartFile.getContentType());
                files.setSize(multipartFile.getSize());
            } catch (ServiceException | IOException e) {
                throw new ServiceException(UNABLE_UPDATE_MULTIPART_FILE.getErrorCode(), UNABLE_UPDATE_MULTIPART_FILE.getErrorDesc());
            }
        });
        return contractFiles.orElse(null);
    }

    public Optional<ContractFiles> getContractFiles(int id) {
        return Optional.ofNullable(contractFileRepository.findById(id)).orElseThrow(() -> new ServiceException(CONTRACT_FILE_NOT_FOUND.getErrorCode(), CONTRACT_FILE_NOT_FOUND.getErrorDesc()));
    }

    @Override
    public ContractFiles saveContract(ContractFiles contractFiles) {
        return contractFileRepository.save(contractFiles);
    }

    @Override
    public PartnerContractDetails getPartnerMsa(int id) {
        Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(id);
        PartnerContractDetails partnerMsaDetails = new PartnerContractDetails();
        if (skillPartnerEntity.isPresent() && skillPartnerEntity.get().getMsaStatusId() != null) {
            partnerMsaDetails.setProject(null);
            partnerMsaDetails.setDepartment(null);
            partnerMsaDetails.setJobId(null);
            partnerMsaDetails.setNoOfResource(null);
            partnerMsaDetails.setSkillPartnerId(skillPartnerEntity.get().getSkillPartnerId());
            partnerMsaDetails.setBusinessName(skillPartnerEntity.get().getBusinessName());
            partnerMsaDetails.setContractStartDate(skillPartnerEntity.get().getMsaStartDate());
            partnerMsaDetails.setContractExpiryDate(skillPartnerEntity.get().getMsaEndDate());
            partnerMsaDetails.setStatus(skillPartnerEntity.get().getMsaStatusId().getStatus());
            partnerMsaDetails.setStatusId(skillPartnerEntity.get().getMsaStatusId().getId());
        }
        return partnerMsaDetails;
    }

    @Transactional
    @Override
    public List<PartnersOwnerData> getProjectDetails(int projectId) {
        List<PartnersOwnerData> projectDataList = new ArrayList<>();
        Optional<SkillSeekerProjectEntity> projectEntity = skillSeekerProjectService.getById(projectId);
        if (projectEntity.isPresent()) {
            List<Job> jobs = jobService.getBySeekerProject(projectId);
            for (Job job : jobs) {
                Optional<List<SelectionPhase>> selectionPhases = selectionPhaseService.getByJobJobId(job.getJobId());
                List<SkillPartnerEntity> partners = new ArrayList<>();
                Map<SkillPartnerEntity, List<JobOwnerData>> partnerOwnersMap = new HashMap<>();

                for (SelectionPhase selectionPhase : selectionPhases.orElse(Collections.emptyList())) {
                    SkillOwnerEntity owner = selectionPhase.getSkillOwnerEntity();

                    if (hasScheduledOfferRelease(owner.getSkillOwnerEntityId(), job.getJobId())) {
                        SkillPartnerEntity partner = owner.getSkillPartnerEntity();
                        if (!partners.contains(partner)) {
                            partners.add(partner);
                        }
                        List<JobOwnerData> owners = partnerOwnersMap.getOrDefault(partner, new ArrayList<>());
                        JobOwnerData ownerData = new JobOwnerData();
                        ownerData.setOwnerId(owner.getSkillOwnerEntityId());
                        ownerData.setRate(selectionPhase.getRate());
                        ownerData.setPartnerId(partner.getSkillPartnerId());
                        owners.add(ownerData);
                        partnerOwnersMap.put(partner, owners);
                    }
                }
                for (SkillPartnerEntity partner : partners) {
                    List<JobOwnerData> owners = partnerOwnersMap.get(partner);
                    int noOfResources = owners.size();
                    int resourcesRate = owners.stream().mapToInt(JobOwnerData::getRate).sum();
                    PartnersOwnerData projectData = new PartnersOwnerData();
                    projectData.setProjectId(projectId);

                    JobData jobData = new JobData();
                    jobData.setJobId(job.getJobId());
                    jobData.setNoOfResources(noOfResources);
                    jobData.setResourcesRate(resourcesRate);
                    jobData.setPartnerId(partner.getSkillPartnerId());

                    List<OwnerData> ownerDataList = new ArrayList<>();
                    for (JobOwnerData owner : owners) {
                        OwnerData ownerData = new OwnerData();
                        ownerData.setOwnerId(owner.getOwnerId());
                        ownerData.setRate(owner.getRate());
                        ownerData.setPartnerId(owner.getPartnerId());
                        ownerDataList.add(ownerData);
                    }
                    jobData.setOwners(ownerDataList);

                    projectData.addJob(jobData);
                    projectDataList.add(projectData);
                }
            }
        } else {
            throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
        }
        logger.info("Contract ServiceImpl || getProjectDetails || getting List of Partners associated with the Project ");

        return projectDataList;
    }

    @Transactional
    @Override
    public List<PartnersOwnerData> getPartnersData(int partnerId) {
        List<PartnersOwnerData> projectDataList = new ArrayList<>();
        List<SkillSeekerProjectEntity> projects = seekerProjectRepository.findAll();
        for (SkillSeekerProjectEntity project : projects) {
            int projectId = project.getId();
            PartnersOwnerData projectData = new PartnersOwnerData();
            projectData.setProjectId(projectId);
            List<JobData> jobDataList = new ArrayList<>();
            List<Job> jobs = jobService.getBySeekerProject(projectId);
            for (Job job : jobs) {
                String jobId = job.getJobId();
                JobData jobData = new JobData();
                jobData.setJobId(jobId);
                Optional<List<SelectionPhase>> selectionPhases = selectionPhaseService.getByJobJobId(jobId);

                int noOfResources = 0;
                int resourcesRate = 0;

                for (SelectionPhase selectionPhase : selectionPhases.orElse(Collections.emptyList())) {
                    SkillOwnerEntity owner = selectionPhase.getSkillOwnerEntity();
                    if (hasScheduledOfferRelease(owner.getSkillOwnerEntityId(), jobId)) {
                        SkillPartnerEntity partner = owner.getSkillPartnerEntity();
                        if (partner.getSkillPartnerId() == partnerId) {
                            noOfResources++;
                            resourcesRate += selectionPhase.getRate();
                            OwnerData ownerData = new OwnerData();
                            ownerData.setOwnerId(owner.getSkillOwnerEntityId());
                            ownerData.setPartnerId(owner.getSkillPartnerEntity().getSkillPartnerId());
                            ownerData.setRate(selectionPhase.getRate());
                            jobData.getOwners().add(ownerData);
                        }

                    }
                }
                jobData.setNoOfResources(noOfResources);
                jobData.setResourcesRate(resourcesRate);
                jobData.setPartnerId(partnerId);
                jobDataList.add(jobData);
            }
            projectData.setJobs(jobDataList);
            projectDataList.add(projectData);
        }
        logger.info("Contract ServiceImpl || getPartnersData || getting the list of Projects for the Partner ");
        return projectDataList;
    }


    public boolean hasClearedLastStage(int ownerId, String jobId) {
        List<RequirementPhase> phases = retrievePhasesByOwnerId(ownerId, jobId);
        phases.sort(Comparator.comparingInt(RequirementPhase::getStage).reversed());
        if (!phases.isEmpty()) {
            RequirementPhase lastPhase = phases.get(0);
            String status = lastPhase.getStatus();
            return status != null && status.equalsIgnoreCase("cleared");
        }
        return false;
    }

    public boolean hasScheduledOfferRelease(int ownerId, String jobId) {
        List<RequirementPhase> phases = retrievePhasesByOwnerId(ownerId, jobId);
        phases.sort(Comparator.comparingInt(RequirementPhase::getStage));

        boolean offerReleaseScheduled = false;
        boolean previousOfferReleaseCleared = false;

        for (int i = phases.size() - 1; i >= 0; i--) {
            RequirementPhase currentPhase = phases.get(i);
            String currentStage = currentPhase.getRequirementPhaseName();
            String currentStatus = currentPhase.getStatus();

            if ("Offer Release".equalsIgnoreCase(currentStage)) {
                if ("scheduled".equalsIgnoreCase(currentStatus)) {
                    offerReleaseScheduled = true;
                } else if ("cleared".equalsIgnoreCase(currentStatus)) {
                    previousOfferReleaseCleared = true;
                    break;
                }
            }
        }

        return offerReleaseScheduled || previousOfferReleaseCleared;
    }


    public List<RequirementPhase> retrievePhasesByOwnerId(int ownerId, String jobId) {
        return requirementPhaseRepository.findBySkillOwnerIdAndJobIdOrderByStageDesc(ownerId, jobId);
    }


    @Override
    public List<SowResponse> getSeekerDetails(int seekerId) {
        List<SowResponse> sowResponseList = new ArrayList<>();
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerService.getById(seekerId);
        if (skillSeeker.isPresent()) {
            if (skillSeeker.get().getMsaStatusId().getId() == 17 || skillSeeker.get().getMsaStatusId().getId() == 11) {
                Optional<List<SkillSeekerProjectEntity>> seekerProjects = skillSeekerProjectService.getBySeekerId(seekerId);
                if (seekerProjects.isPresent()) {
                    Map<String, SowResponse> sowResponseMap = new HashMap<>(); //  to store SowResponse objects for each project

                    seekerProjects.get().forEach(project -> {
                        List<Job> jobs = jobService.getBySeekerIdAndProjectId(seekerId, project.getId());

                        jobs.forEach(job -> {
                            int totalNumberOfResources = 0;
                            int totalAmount = 0;
                            totalNumberOfResources += job.getNumberOfPositions();
                            List<SelectionPhase> selectionPhases = selectionPhaseService.getByJobId(job.getJobId());
                            if (!selectionPhases.isEmpty()) {
                                for (SelectionPhase selectionPhase : selectionPhases) {
                                    totalAmount += selectionPhase.getRate();
                                }
                            }

                            SowResponse sowResponse = new SowResponse();
                            sowResponse.setProjectId(project.getId());
                            sowResponse.setProjectName(project.getTitle());
                            sowResponse.setSeekerId(seekerId);
                            sowResponse.setSeekerName(skillSeeker.get().getSkillSeekerName());
                            sowResponse.setNumberOfResources(totalNumberOfResources);
                            sowResponse.setAmountForEachResource(totalAmount);
                            sowResponse.setJobId(job.getJobId());
                            sowResponse.setJobTitle(job.getJobTitle());

                            sowResponseMap.put(job.getJobId(), sowResponse);
                        });
                    });
                    sowResponseList.addAll(sowResponseMap.values());
                } else {
                    throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
                }
            } else {
                throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
            }
        } else {
            throw new ServiceException(SEEKER_NOT_FOUND.getErrorCode(), SEEKER_NOT_FOUND.getErrorDesc());
        }
        return sowResponseList;
    }

    @Override
    public SowPoInitiateResponse initiateSow(InitiateSowPoRequest sowRequest) {

        SowPoInitiateResponse sowPoInitiateResponse = new SowPoInitiateResponse();
        StatementOfWorkEntity sowEntity = new StatementOfWorkEntity();
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerService.getById(sowRequest.getSeekerId());

        if (skillSeeker.get().getMsaStatusId().getId() == 17 || skillSeeker.get().getMsaStatusId().getId() == 11) {
            Optional<SkillSeekerProjectEntity> seekerProject = skillSeekerProjectService.getById(sowRequest.getProjectId());
            if (seekerProject.isPresent()) {
                if (seekerProject.get().getTitle() == null) {
                    seekerProject = skillSeekerProjectService.getById(0);
                    sowEntity.setSkillSeekerProject(seekerProject.get());
                } else {
                    sowEntity.setSkillSeekerProject(seekerProject.get());
                }
                Optional<StatementOfWorkEntity> sow = statementOfWorkService.getByJobId(sowRequest.getJobId());
//                    if (sow.isEmpty()) {
                Optional<Job> job = jobService.getByJobId(sowRequest.getJobId());
                if (job.isPresent()) {
                    if (sow.isEmpty()) {

                        int jobResources = job.get().getNumberOfPositions();
                        if (jobResources > 1) {
                            sowEntity.setSkillSeekerId(sowRequest.getSeekerId());
                            sowEntity.setSkillSeekerProject(seekerProject.get());
                            sowEntity.setJobId(job.get());
                            sowEntity.setOwnerSkillDomainEntity(seekerProject.get().getOwnerSkillDomainEntity());
                            sowEntity.setNumberOfResources(sowRequest.getNumberOfResources());
                            sowEntity.setAmountForEachResource(sowRequest.getAmountForEachResource());


                            Optional<ContractStatus> contractStatus = contractStatusRepository.findById(19);
                            if (contractStatus.isPresent()) {
                                sowEntity.setSowStatus(contractStatus.get());
                            }
                            statementOfWorkService.save(sowEntity);
                            sowPoInitiateResponse.setSowEntityId(sowEntity.getId());
                            sowPoInitiateResponse.setSeekerId(sowEntity.getSkillSeekerId());
                            sowPoInitiateResponse.setProjectId(sowEntity.getSkillSeekerProject().getId());
                            sowPoInitiateResponse.setSowStatus(sowEntity.getSowStatus().getStatus());
                            sowPoInitiateResponse.setJobId(sowEntity.getJobId().getJobId());
                            sowPoInitiateResponse.setJobTitle(sowEntity.getJobId().getJobTitle());
                            sowPoInitiateResponse.setDepartment(sowEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                            sowPoInitiateResponse.setNumberOfResources(sowEntity.getNumberOfResources());
                            sowPoInitiateResponse.setAmountForEachResource(sowEntity.getAmountForEachResource());

                        } else if (jobResources == 1) {
                            sowPoInitiateResponse = initiatePo(sowRequest);
                        }
                    } else {
                        throw new ServiceException(SOW_ALREADY_ADDED.getErrorCode(), SOW_ALREADY_ADDED.getErrorDesc());
                    }
                } else {
                    throw new ServiceException(JOB_NOT_FOUND.getErrorCode(), JOB_NOT_FOUND.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
            }
        } else {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }

        return sowPoInitiateResponse;
    }


    public SowPoInitiateResponse initiatePo(InitiateSowPoRequest sowRequest) {
        SowPoInitiateResponse sowPoInitiateResponse = new SowPoInitiateResponse();
        PoEntity poEntity = new PoEntity();
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerService.getById(sowRequest.getSeekerId());
        if (skillSeeker.get().getMsaStatusId().getId() == 17 || skillSeeker.get().getMsaStatusId().getId() == 11) {

            Optional<SkillSeekerProjectEntity> seekerProject = skillSeekerProjectService.getById(sowRequest.getProjectId());
            if (seekerProject.isPresent()) {
                if (seekerProject.get().getTitle() == null) {
                    seekerProject = skillSeekerProjectService.getById(0);
                    poEntity.setSkillSeekerProject(seekerProject.get());
                } else {
                    poEntity.setSkillSeekerProject(seekerProject.get());
                }


                Optional<Job> job = jobService.getByJobId(sowRequest.getJobId());

                if (job.isPresent()) {
                    Optional<PoEntity> po = poService.getByJobId(sowRequest.getJobId());
                    if (po.isEmpty()) {

                        poEntity.setSkillSeekerId(sowRequest.getSeekerId());
                        poEntity.setSkillSeekerProject(seekerProject.get());
                        poEntity.setJobId(job.get());
                        poEntity.setNumberOfResources(sowRequest.getNumberOfResources());
                        poEntity.setAmountForEachResource(sowRequest.getAmountForEachResource());


                        Optional<ContractStatus> contractStatus = contractStatusRepository.findById(24);
                        if (contractStatus.isPresent()) {
                            poEntity.setPoStatus(contractStatus.get());
                        }
                        poService.save(poEntity);
                        sowPoInitiateResponse.setPoEntityId(poEntity.getId());
                        sowPoInitiateResponse.setSeekerId(poEntity.getSkillSeekerId());
                        sowPoInitiateResponse.setProjectId(poEntity.getSkillSeekerProject().getId());
                        sowPoInitiateResponse.setJobId(poEntity.getJobId().getJobId());
                        sowPoInitiateResponse.setJobTitle(poEntity.getJobId().getJobTitle());
                        sowPoInitiateResponse.setPoStatus(poEntity.getPoStatus().getStatus());
                        sowPoInitiateResponse.setDepartment(poEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                        sowPoInitiateResponse.setNumberOfResources(poEntity.getNumberOfResources());
                        sowPoInitiateResponse.setAmountForEachResource(poEntity.getAmountForEachResource());

                    } else {
                        throw new ServiceException(PO_ALREADY_ADDED.getErrorCode(), PO_ALREADY_ADDED.getErrorDesc());
                    }
                } else {
                    throw new ServiceException(JOB_NOT_FOUND.getErrorCode(), JOB_NOT_FOUND.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
            }
        } else {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }
        return sowPoInitiateResponse;
    }


    @Override
    public SowCreateResponse sowCreation(MultipartFile multipartFile, int sowEntityId) throws IOException {
        SowCreateResponse sowCreateResponse = new SowCreateResponse();
        Optional<StatementOfWorkEntity> sowEntity = statementOfWorkService.getById(sowEntityId);
        if (sowEntity.isPresent()) {
            if (!multipartFile.isEmpty()) {
                ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));
                List<Job> jobs = jobService.getBySeekerIdAndProjectId(sowEntity.get().getSkillSeekerId(), sowEntity.get().getSkillSeekerProject().getId());
                int jobResources = 0;

                for (Job job : jobs) {
                    int numberOfPositions = job.getNumberOfPositions();
                    jobResources += numberOfPositions;
                }
                if (jobResources > 1) {
                    sowEntity.get().setId(sowEntity.get().getId());
                    sowEntity.get().setSowId(contractFiles);
                    Optional<ContractStatus> contractStatus = contractStatusRepository.findById(20);
                    if (contractStatus.isPresent()) {
                        sowEntity.get().setSowStatus(contractStatus.get());
                    }
                    statementOfWorkService.save(sowEntity.get());
                    sowCreateResponse.setSeekerId(sowEntity.get().getSkillSeekerId());
                    sowCreateResponse.setProjectId(sowEntity.get().getSkillSeekerProject().getId());
                    sowCreateResponse.setSowId(sowEntity.get().getId());
                    sowCreateResponse.setSowStatus(sowEntity.get().getSowStatus().getStatus());
                    Notification notification = new Notification();
                    notificationService.sowStatusNotification(sowEntity.get(), notification);
                } else if (jobResources == 1) {
                    throw new ServiceException("number of resource is 1. So, create Po");
                }
            } else {
                throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
            }
        } else {
            throw new ServiceException(INVALID_SOW_ID.getErrorCode(), INVALID_SOW_ID.getErrorDesc());
        }
        return sowCreateResponse;
    }

    @Override
    public PoCreateResponse poCreation(MultipartFile multipartFile, int poEntityId) throws IOException {
        PoCreateResponse poCreateResponse = new PoCreateResponse();
        Optional<PoEntity> poEntity = poService.getById(poEntityId);
        if (poEntity.isPresent()) {

            if (!multipartFile.isEmpty()) {
                ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));

                Optional<Job> job = jobService.getByJobId(poEntity.get().getJobId().getJobId());
                if (job.isPresent()) {
                    int baseRate = job.get().getBaseRate();
                    int maxRate = job.get().getMaxRate();
                    String baseRateRange = String.valueOf(baseRate);
                    String maxRateRange = String.valueOf(maxRate);
                    poEntity.get().setId(poEntity.get().getId());
                    poEntity.get().setPoId(contractFiles);
                    poEntity.get().setSkillSeekerId(poEntity.get().getSkillSeekerId());
                    poEntity.get().setSkillSeekerProject(poEntity.get().getSkillSeekerProject());
                    poEntity.get().setOwnerSkillDomainEntity(poEntity.get().getOwnerSkillDomainEntity());
                    poEntity.get().setEndDate(poEntity.get().getSkillSeekerProject().getEndDate().toLocalDate());
                    poEntity.get().setContractStartDate(poEntity.get().getSkillSeekerProject().getStartDate().toLocalDate());
                    poEntity.get().setContractEndDate(poEntity.get().getSkillSeekerProject().getEndDate().toLocalDate());
                    poEntity.get().setJobId(job.get());
                    poEntity.get().setRole(job.get().getJobTitle());
                    poEntity.get().setPriceRange(baseRateRange + " to " + maxRateRange);

                    Optional<ContractStatus> contractStatus = contractStatusRepository.findById(25);
                    if (contractStatus.isPresent()) {
                        poEntity.get().setPoStatus(contractStatus.get());
                    }
                    List<SelectionPhase> selectionPhases = selectionPhaseService.getByJobId(job.get().getJobId());
                    for (SelectionPhase selectionPhase : selectionPhases) {
                        poEntity.get().setRateCard(selectionPhase.getRate());
                        poEntity.get().setSkillOwnerEntity(selectionPhase.getSkillOwnerEntity());
                    }

                    poService.save(poEntity.get());
                    poCreateResponse.setPoId(poEntity.get().getId());
                    poCreateResponse.setProjectId(poEntity.get().getSkillSeekerProject().getId());
                    poCreateResponse.setSeekerId(poEntity.get().getSkillSeekerId());
                    poCreateResponse.setPoStatus(poEntity.get().getPoStatus().getStatus());
                    Notification notification = new Notification();
                    notificationService.poStatusNotification(poEntity.get(), notification);
                }

            } else {
                throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
            }
        } else {
            throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
        }
        return poCreateResponse;
    }

    @Override
    @Transactional
    public Resource sowDocument(String jobId) {

        try {
            Optional<StatementOfWorkEntity> sowEntity = statementOfWorkService.getByJobId(jobId);
            ByteArrayResource resource;

            if (sowEntity.isPresent()) {

                InputStream fileSOW = getClass().getClassLoader().getResourceAsStream("templates/SOW_Qbrainx Inc.docx.docx");
                XWPFDocument documentSOW = new XWPFDocument(fileSOW);

                // Replace the existing name with a new name
                String[] searchTerms = {"name", "job", "endDate", "startDate", "mailId", "rate", "count"};
                String[] replacements = {
                        sowEntity.get().getSkillSeekerProject().getSkillSeeker().getSkillSeekerName(),
                        sowEntity.get().getJobId().getJobTitle(),
                        String.valueOf(sowEntity.get().getSkillSeekerProject().getEndDate()),
                        String.valueOf(sowEntity.get().getSkillSeekerProject().getStartDate()),
                        sowEntity.get().getSkillSeekerProject().getSkillSeeker().getEmail(),
                        String.valueOf(sowEntity.get().getAmountForEachResource()),
                        String.valueOf(sowEntity.get().getNumberOfResources())
                };

                for (XWPFParagraph paragraph : documentSOW.getParagraphs()) {
                    for (int i = 0; i < searchTerms.length; i++) {
                        String searchTerm = searchTerms[i];
                        String replacement = replacements[i];
                        for (XWPFRun run : paragraph.getRuns()) {
                            String text = run.getText(0);
                            if (text != null && text.contains(searchTerm)) {
                                text = text.replace(searchTerm, replacement);
                                run.setText(text, 0);
                            }
                        }
                    }
                }

                // Save the modified document to a ByteArrayOutputStream
                PdfOptions pdfOptions = PdfOptions.create();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfConverter.getInstance().convert(documentSOW, outputStream, pdfOptions);
                documentSOW.close();
                outputStream.close();

                byte[] pdfBytes = outputStream.toByteArray();

                resource = new ByteArrayResource(pdfBytes);

                return resource;
            } else {
                throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
            }
        } catch (IOException e) {
            throw new ServiceException(INVALID_FILE.getErrorCode(), INVALID_FILE.getErrorDesc());
        }
    }


    @Override
    @Transactional
    public Resource poDocument(String jobId) {

        try {
            Optional<PoEntity> poEntity = poService.getByJobId(jobId);
            ByteArrayResource resource;

            if (poEntity.isPresent()) {
                String inputPO = "templates/PO_Qbrainx Inc.docx.docx";
                InputStream filePO = getClass().getClassLoader().getResourceAsStream(inputPO);
                XWPFDocument documentPO = new XWPFDocument(filePO);

                // Replace the existing name with a new name
                String[] searchTerms = {"name", "job", "endDate", "startDate", "mailId", "rate", "count"};
                String[] replacements = {
                        poEntity.get().getSkillSeekerProject().getSkillSeeker().getSkillSeekerName(),
                        poEntity.get().getJobId().getJobTitle(),
                        String.valueOf(poEntity.get().getSkillSeekerProject().getEndDate()),
                        String.valueOf(poEntity.get().getSkillSeekerProject().getStartDate()),
                        poEntity.get().getSkillSeekerProject().getSkillSeeker().getEmail(),
                        String.valueOf(poEntity.get().getAmountForEachResource()),
                        String.valueOf(poEntity.get().getNumberOfResources())
                };

                for (XWPFParagraph paragraph : documentPO.getParagraphs()) {
                    for (int i = 0; i < searchTerms.length; i++) {
                        String searchTerm = searchTerms[i];
                        String replacement = replacements[i];
                        for (XWPFRun run : paragraph.getRuns()) {
                            String text = run.getText(0);
                            if (text != null && text.contains(searchTerm)) {
                                text = text.replace(searchTerm, replacement);
                                run.setText(text, 0);
                            }
                        }
                    }
                }

                // Save the modified document to a ByteArrayOutputStream
                PdfOptions pdfOptions = PdfOptions.create();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfConverter.getInstance().convert(documentPO, outputStream, pdfOptions);
                documentPO.close();
                outputStream.close();

                byte[] pdfBytes = outputStream.toByteArray();

                resource = new ByteArrayResource(pdfBytes);

                return resource;
            } else {
                throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
            }
        } catch (IOException e) {
            throw new ServiceException(INVALID_FILE.getErrorCode(), INVALID_FILE.getErrorDesc());
        }
    }



    @Override
    public StatementOfWorkStatus updateSowStatus(int sowId, int statusId) {
        StatementOfWorkStatus statementOfWorkStatus = new StatementOfWorkStatus();
        try {
            Optional<StatementOfWorkEntity> sowEntity = statementOfWorkService.getBySowId(sowId);
            Optional<ContractStatus> sowStatus = contractStatusRepository.findById(statusId);

            if (sowEntity.isPresent() && sowStatus.isPresent()) {
                sowEntity.get().setSowStatus(sowStatus.get());
                statementOfWorkService.save(sowEntity.get());
                statementOfWorkStatus.setId(sowEntity.get().getId());
                statementOfWorkStatus.setStatus(sowStatus.get().getStatus());
                Notification notification = new Notification();
                notificationService.sowStatusNotification(sowEntity.get(), notification);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_SOW_ID.getErrorCode(), INVALID_SOW_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
        return statementOfWorkStatus;
    }

    @Override
    public PurchaseOrder updatePoStatus(int poId, int statusId) {
        PurchaseOrder purchaseOrder;
        try {
            Optional<PoEntity> poEntity = poService.getByPoId(poId);
            Optional<ContractStatus> poStatus = contractStatusRepository.findById(statusId);

            purchaseOrder = new PurchaseOrder();
            if (poEntity.isPresent() && poStatus.isPresent()) {
                poEntity.get().setPoStatus(poStatus.get());
                poEntity.get().setContractStartDate(LocalDate.now());
                poService.save(poEntity.get());
                purchaseOrder.setId(poEntity.get().getId());
                purchaseOrder.setStatus(poStatus.get().getStatus());
                Notification notification = new Notification();
                notificationService.poStatusNotification(poEntity.get(), notification);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
        return purchaseOrder;
    }

    @Override
    public PartnerSowResponse initiateSowPartner(InitiatePartnerSow partnerSow) {
        PartnerSowResponse sowResponse = new PartnerSowResponse();
        Optional<SkillPartnerEntity> partner = partnerService.getSkillPartnerEntity(partnerSow.getPartnerId());
        Optional<SkillSeekerProjectEntity> project = skillSeekerProjectService.getById(partnerSow.getProjectId());
        if (partner.isPresent() && partner.get().getMsaStatusId().getId() == MSA_STATUS_ID && project.isPresent()) {
            Optional<StatementOfWorkEntity> existingSow = statementOfWorkService.getByPartnerJob(partnerSow.getPartnerId(), partnerSow.getJobId());
            if (existingSow.isPresent()) {
                throw new ServiceException(SOW_EXISTS.getErrorCode(), SOW_EXISTS.getErrorDesc());
            }
            Optional<ContractStatus> status = contractStatusRepository.findById(19);
            StatementOfWorkEntity workEntity = new StatementOfWorkEntity();
            workEntity.setSkillPartnerEntity(partner.get());
            workEntity.setSkillSeekerProject(project.get());
            workEntity.setOwnerSkillDomainEntity(project.get().getOwnerSkillDomainEntity());
            workEntity.setJobId(jobService.getById(partnerSow.getJobId()));
            workEntity.setNumberOfResources(partnerSow.getNumberOfResources());
            workEntity.setTotalResourcesRate(partnerSow.getTotalResourcesRate());
            workEntity.setSowStartDate(project.get().getStartDate().toLocalDate());
            workEntity.setSowEndDate(project.get().getEndDate().toLocalDate());
            workEntity.setSowStatus(status.get());
            statementOfWorkService.save(workEntity);
            logger.info("ContractServiceImpl || InitiatePartnerSow || initiating Sow for the partner");
            sowResponse.setSowStatus(workEntity.getSowStatus().getStatus());
            sowResponse.setSowEntityId(workEntity.getId());
        } else {
            throw new ServiceException(INVALID_PARTNER_OR_PROJECT_DATA.getErrorCode(), INVALID_PARTNER_OR_PROJECT_DATA.getErrorDesc());
        }
        return sowResponse;
    }

    @Transactional
    @Override
    public PartnerSowResponse createPartnerSow(MultipartFile multipartFile, int sowEntityId) throws IOException {
        PartnerSowResponse sowResponse = new PartnerSowResponse();
        if (!multipartFile.isEmpty()) {
            Optional<StatementOfWorkEntity> workEntity = statementOfWorkService.getById(sowEntityId);
            if (workEntity.isPresent()) {
                ContractFiles contractFiles = contractFileRepository.save(createContract(multipartFile));
                Optional<ContractStatus> contractStatus = Optional.of(contractStatusRepository.findById(20).get());
                StatementOfWorkEntity existingWorkEntity = workEntity.get();
                existingWorkEntity.setSowId(contractFiles);
                existingWorkEntity.setSowStatus(contractStatus.get());
                statementOfWorkService.save(existingWorkEntity);
                Notification notification = new Notification();
                notificationService.superAdminToPartnerSowNotification(existingWorkEntity, notification);
                logger.info("ContractServiceImpl || CreatePartnerSow || create a sow for the partner");

                sowResponse.setSowId(contractFiles.getId());
                sowResponse.setSowStatus(existingWorkEntity.getSowStatus().getStatus());
                sowResponse.setSowEntityId(existingWorkEntity.getId());
            } else {
                throw new ServiceException(PARTNER_ACTIVE_MSA_NOT_FOUND.getErrorCode(), PARTNER_ACTIVE_MSA_NOT_FOUND.getErrorDesc());
            }
        }

        return sowResponse;
    }

    @Transactional
    @Override
    public PartnerSowResponse updatePartnerSowStatus(int sowId, int sowStatusId) {
        Optional<StatementOfWorkEntity> workEntity = statementOfWorkService.getBySowId(sowId);
        Optional<ContractStatus> statusId = contractStatusRepository.findById(sowStatusId);
        PartnerSowResponse sowResponse = new PartnerSowResponse();
        if (workEntity.isPresent() && statusId.isPresent()) {
            workEntity.get().setSowStatus(statusId.get());
            statementOfWorkService.save(workEntity.get());
            Notification notification = new Notification();
            notificationService.partnerToSuperAdminSowNotification(workEntity.get(), notification);
            logger.info("ContractServiceImpl || updatePartnerSowStatus || Update SOW status for SkillPartner ");
            sowResponse.setSowId(sowId);
            sowResponse.setSowEntityId(workEntity.get().getId());
            sowResponse.setSowStatus(statusId.get().getStatus());
        }
        return sowResponse;
    }


    @Override
    @Transactional
    public SeekerMSADetails getSeekerMsa(int seekerId) {

        Optional<SkillSeekerEntity> byId = skillSeekerRepository.findById(seekerId);
        SeekerMSADetails seekerMSADetails1 = new SeekerMSADetails();
        if (byId.get().getMsaId() != null) {
            List<Job> bySkillSeekerId = jobRepository.findBySkillSeekerId(seekerId);
            seekerMSADetails1.setSkillSeekerId(seekerId);
            seekerMSADetails1.setMsaContractExpiryDate(byId.get().getMsaEndDate());
            seekerMSADetails1.setMsaContractStartDate(byId.get().getMsaStartDate());
            seekerMSADetails1.setBusinessName(byId.get().getSkillSeekerName());
            seekerMSADetails1.setPhoneNumber(byId.get().getPhone());
            seekerMSADetails1.setEmail(byId.get().getEmail());
            seekerMSADetails1.setStatusId(byId.get().getMsaStatusId().getId());
            seekerMSADetails1.setContractId(byId.get().getMsaId().getId());
            seekerMSADetails1.setStatus(byId.get().getMsaStatusId().getStatus());
            bySkillSeekerId.forEach(job -> {
                if(job.getSkillSeeker().getMsaId()!=null) {
                    seekerMSADetails1.setJobId(job.getJobId());
                    if(job.getSeekerProject()==null) {
                        seekerMSADetails1.setProjectId(0);
                        seekerMSADetails1.setProjectName("Default");
                    }else{
                        seekerMSADetails1.setProjectId(job.getSeekerProject().getId());
                        seekerMSADetails1.setProjectName(job.getSeekerProject().getTitle());
                        seekerMSADetails1.setDepartment(job.getOwnerSkillDomainEntity().getDomainValues());

                    }

                }
            });
            seekerMSADetails1.setNoOfResource(0);
            return seekerMSADetails1;
        } else {
            throw new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc());
        }

    }

    public Page<PartnerContractDetails> getPartnerContractsInAdmin(int page, int size) {
        List<PartnerContractDetails> partnerContractDetails = getPartnerMsaDetailsInSuperAdmin();
        List<PartnerContractDetails> partnerContractDetails1 = getPartnerSowDetailsInSuperAdmin();
        List<PartnerContractDetails> partnerContractDetailsList = new ArrayList<>();
        if (!partnerContractDetails.isEmpty() || !partnerContractDetails1.isEmpty()) {
            partnerContractDetailsList.addAll(partnerContractDetails);
            partnerContractDetailsList.addAll(partnerContractDetails1);
            return partnerContractPagination.getPartnerContractDetails(page, size, partnerContractDetailsList);
        } else {
            throw new ServiceException(CONTRACTS_NOT_FOUND.getErrorCode(), CONTRACTS_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public Map<Integer, String> createNew() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Create MSA");
        map.put(2, "Create SOW");
        map.put(3, "Create PO");
        return map;
    }

    @Override
    public List<PartnerContractDetails> getPartnerSowDetailsInSuperAdmin() {
        List<StatementOfWorkEntity> statementOfWorkEntities = statementOfWorkRepository.findAll();
        List<PartnerContractDetails> partnerContractDetails = new ArrayList<>();
        if (!statementOfWorkEntities.isEmpty()) {
            for (StatementOfWorkEntity sow : statementOfWorkEntities) {
                if (sow.getSkillPartnerEntity() != null) {
                    PartnerContractDetails partnerContractDetails1 = new PartnerContractDetails();
                    partnerContractDetails1.setBusinessName(sow.getSkillPartnerEntity().getBusinessName());
                    partnerContractDetails1.setEmail(sow.getSkillPartnerEntity().getBusinessEmail());
                    partnerContractDetails1.setPhone(sow.getSkillPartnerEntity().getPrimaryContactPhone());
                    partnerContractDetails1.setProject(sow.getSkillSeekerProject().getTitle());
                    partnerContractDetails1.setNoOfResource(String.valueOf(sow.getNumberOfResources()));
                    partnerContractDetails1.setSkillPartnerId(sow.getSkillPartnerEntity().getSkillPartnerId());
                    partnerContractDetails1.setDepartment(sow.getOwnerSkillDomainEntity().getDomainValues());
                    partnerContractDetails1.setStatus(sow.getSowStatus().getStatus());
                    partnerContractDetails1.setStatusId(sow.getSowStatus().getId());
                    partnerContractDetails1.setContractStartDate(sow.getSowStartDate());
                    partnerContractDetails1.setContractExpiryDate(sow.getSowEndDate());
                    partnerContractDetails1.setJobId(sow.getJobId().getJobId());
                    partnerContractDetails.add(partnerContractDetails1);
                }
            }
        }
        return partnerContractDetails;
    }


    @Override
    @Transactional
    public Resource partnerSowDocument(String jobId, int partnerId) {
        try {
            Optional<StatementOfWorkEntity> sowEntity = statementOfWorkService.getByPartnerJob(partnerId, jobId);

            if (sowEntity.isPresent()) {
                ByteArrayResource resource;

                InputStream fileSOW = getClass().getClassLoader().getResourceAsStream("templates/PartnerSow_Qbrainx_Inc.Draft.docx");
                XWPFDocument documentSOW = new XWPFDocument(fileSOW);

                // Replace the existing name with a new name
                String[] searchTerms = {"name", "job", "endDate", "startDate", "email", "totalRate", "count"};
                String[] replacements = {
                        sowEntity.get().getSkillSeekerProject().getSkillSeeker().getSkillSeekerName(),
                        sowEntity.get().getJobId().getJobTitle(),
                        String.valueOf(sowEntity.get().getSkillSeekerProject().getEndDate()),
                        String.valueOf(sowEntity.get().getSkillSeekerProject().getStartDate()),
                        sowEntity.get().getSkillSeekerProject().getSkillSeeker().getEmail(),
                        String.valueOf(sowEntity.get().getTotalResourcesRate()),
                        String.valueOf(sowEntity.get().getNumberOfResources())
                };

                for (XWPFParagraph paragraph : documentSOW.getParagraphs()) {
                    for (int i = 0; i < searchTerms.length; i++) {
                        String searchTerm = searchTerms[i];
                        String replacement = replacements[i];
                        for (XWPFRun run : paragraph.getRuns()) {
                            String text = run.getText(0);
                            if (text != null && text.contains(searchTerm)) {
                                text = text.replace(searchTerm, replacement);
                                run.setText(text, 0);
                            }
                        }
                    }
                }

                // Save the modified document to a ByteArrayOutputStream
                PdfOptions pdfOptions = PdfOptions.create();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfConverter.getInstance().convert(documentSOW, outputStream, pdfOptions);
                documentSOW.close();
                outputStream.close();

                byte[] pdfBytes = outputStream.toByteArray();


                resource = new ByteArrayResource(pdfBytes);

                return resource;
            } else {
                throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
            }
        } catch (IOException e) {
            throw new ServiceException(INVALID_FILE.getErrorCode(), INVALID_FILE.getErrorDesc());
        }
    }

//    public byte[] partnerSowDocument(String jobId, int partnerId) {
//        try {
//            Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkService.getByPartnerJob(partnerId, jobId);
//            if (statementOfWorkEntity.isPresent()) {
//                String inputSOW = "templates/PartnerSow_Qbrainx_Inc.Draft.docx";
//                InputStream fileSOW = getClass().getClassLoader().getResourceAsStream(inputSOW);
//                XWPFDocument documentSOW = new XWPFDocument(fileSOW);
//                String[] searchTerms = {"name", "count", "startDate", "endDate", "totalRate", "job", "email"};
//                String[] replacements = {
//                        statementOfWorkEntity.get().getSkillPartnerEntity().getBusinessName(),
//                        String.valueOf(statementOfWorkEntity.get().getNumberOfResources()),
//                        String.valueOf(statementOfWorkEntity.get().getSkillSeekerProject().getStartDate()),
//                        String.valueOf(statementOfWorkEntity.get().getSkillSeekerProject().getEndDate()),
//                        String.valueOf(statementOfWorkEntity.get().getTotalResourcesRate()),
//                        statementOfWorkEntity.get().getJobId().getJobTitle(),
//                        statementOfWorkEntity.get().getSkillSeekerProject().getSkillSeeker().getEmail()
//
//                };
//                for (XWPFParagraph paragraph : documentSOW.getParagraphs()) {
//                    for (int i = 0; i < searchTerms.length; i++) {
//                        String searchTerm = searchTerms[i];
//                        String replacement = replacements[i];
//                        for (XWPFRun run : paragraph.getRuns()) {
//                            String text = run.getText(0);
//                            if (text != null && text.contains(searchTerm)) {
//                                text = text.replace(searchTerm, replacement);
//                                run.setText(text, 0);
//                            }
//                        }
//                    }
//                }
//                ByteArrayOutputStream outputStreamSOW = new ByteArrayOutputStream();
//                documentSOW.write(outputStreamSOW);
//                documentSOW.close();
//                return outputStreamSOW.toByteArray();
//            } else {
//                throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
//            }
//        } catch (IOException e) {
//            throw new ServiceException(INVALID_FILE.getErrorCode(), INVALID_FILE.getErrorDesc());
//        }
//
//    }


    @Transactional
    @Override
    public List<JobDetail> getJobDetails(String jobId) {
        List<JobDetail> jobDetailsList = new ArrayList<>();
        Job job = jobService.getById(jobId);
        if (job == null) {
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }
        Optional<SkillSeekerProjectEntity> projectEntity;
        if (job.getSeekerProject() == null) {
            SkillSeekerProjectEntity defaultProject = new SkillSeekerProjectEntity();
            defaultProject.setTitle("default");
            defaultProject.setId(0);
            projectEntity = Optional.of(defaultProject);
        } else {
            projectEntity = skillSeekerProjectService.getById(job.getSeekerProject().getId());
        }

        if (!projectEntity.isPresent()) {
            throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
        }

        List<SelectionPhase> selectionPhases = selectionPhaseService.getByJobId(job.getJobId());

        JobDetail jobDetailsDto = new JobDetail();
        jobDetailsDto.setJobId(jobId);
        jobDetailsDto.setProjectId(projectEntity.get().getId());
        jobDetailsDto.setPartners(new ArrayList<>());

        Map<SkillPartnerEntity, List<JobOwnerData>> partnerOwnersMap = new HashMap<>();
        for (SelectionPhase selectionPhase : selectionPhases) {
            SkillOwnerEntity owner = selectionPhase.getSkillOwnerEntity();

            if (hasScheduledOfferRelease(owner.getSkillOwnerEntityId(), job.getJobId())) {
                SkillPartnerEntity partner = owner.getSkillPartnerEntity();
                List<JobOwnerData> owners = partnerOwnersMap.getOrDefault(partner, new ArrayList<>());
                JobOwnerData ownerData = new JobOwnerData();
                ownerData.setOwnerId(owner.getSkillOwnerEntityId());
                ownerData.setRate(selectionPhase.getRate());
                ownerData.setPartnerId(partner.getSkillPartnerId());
                owners.add(ownerData);
                partnerOwnersMap.put(partner, owners);
            }
        }
        for (Map.Entry<SkillPartnerEntity, List<JobOwnerData>> entry : partnerOwnersMap.entrySet()) {
            SkillPartnerEntity partner = entry.getKey();
            List<JobOwnerData> owners = entry.getValue();
            PartnerDetail partnerDataDto = new PartnerDetail();
            partnerDataDto.setPartnerId(partner.getSkillPartnerId());
            partnerDataDto.setTotalResources(owners.size());
            partnerDataDto.setTotalResourcesRate(owners.stream().mapToInt(JobOwnerData::getRate).sum());
            partnerDataDto.setOwners(new ArrayList<>());
            for (JobOwnerData owner : owners) {
                OwnerDetail ownerDataDto = new OwnerDetail();
                ownerDataDto.setOwnerId(owner.getOwnerId());
                ownerDataDto.setRate(owner.getRate());
                partnerDataDto.getOwners().add(ownerDataDto);
            }

            jobDetailsDto.getPartners().add(partnerDataDto);
        }

        jobDetailsList.add(jobDetailsDto);
        return jobDetailsList;
    }


    @Override
    @Transactional
    public List<PartnerContractDetails> getByPartnerIdSowDetail(int partnerId) {
        List<StatementOfWorkEntity> statementOfWorkEntities = statementOfWorkRepository.findByPartner(partnerId);
        List<PartnerContractDetails> partnerContractDetails = new ArrayList<>();
        if (!statementOfWorkEntities.isEmpty()) {
            for (StatementOfWorkEntity sow : statementOfWorkEntities) {
                PartnerContractDetails partnerContractDetails1 = new PartnerContractDetails();
                partnerContractDetails1.setBusinessName(sow.getSkillPartnerEntity().getBusinessName());
                partnerContractDetails1.setEmail(sow.getSkillPartnerEntity().getBusinessEmail());
                partnerContractDetails1.setPhone(sow.getSkillPartnerEntity().getPrimaryContactPhone());
                partnerContractDetails1.setProject(sow.getSkillSeekerProject().getTitle());
                partnerContractDetails1.setNoOfResource(String.valueOf(sow.getNumberOfResources()));
                partnerContractDetails1.setSkillPartnerId(sow.getSkillPartnerEntity().getSkillPartnerId());
                partnerContractDetails1.setDepartment(sow.getOwnerSkillDomainEntity().getDomainValues());
                partnerContractDetails1.setStatus(sow.getSowStatus().getStatus());
                partnerContractDetails1.setStatusId(sow.getSowStatus().getId());
                partnerContractDetails1.setContractStartDate(sow.getSowStartDate());
                partnerContractDetails1.setContractExpiryDate(sow.getSowEndDate());
                partnerContractDetails1.setJobId(sow.getJobId().getJobId());
                partnerContractDetails.add(partnerContractDetails1);
            }
        }
        return partnerContractDetails;
    }

    @Override
    @Transactional
    public List<SeekerMSADetails> getPurchaseOrderDetails(int skillSeekerId) {

        Optional<List<PoEntity>> productOwnerEntities = poRepository.findBySkillSeekerId(skillSeekerId);
        Optional<SkillSeekerEntity> byId = skillSeekerRepository.findById(skillSeekerId);
        List<SeekerMSADetails> seekerProductOwners = new ArrayList<>();
        if (!productOwnerEntities.get().isEmpty()) {

            for (PoEntity poEntity : productOwnerEntities.get()) {

                SeekerMSADetails seekerProductOwner = new SeekerMSADetails();
                seekerProductOwner.setSkillSeekerId(poEntity.getSkillSeekerId());
                seekerProductOwner.setBusinessName(poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName());
                seekerProductOwner.setProjectId(poEntity.getSkillSeekerProject().getId());
                seekerProductOwner.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                seekerProductOwner.setDepartment(poEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                seekerProductOwner.setJobId(poEntity.getJobId().getJobId());
                seekerProductOwner.setStatus(poEntity.getPoStatus().getStatus());
                seekerProductOwner.setNoOfResource(poEntity.getNumberOfResources());
                seekerProductOwner.setMsaContractStartDate(poEntity.getContractStartDate());
                seekerProductOwner.setMsaContractExpiryDate(poEntity.getContractEndDate());
                seekerProductOwner.setEmail(null);
                seekerProductOwner.setPhoneNumber(null);
                if (poEntity.getPoId() == null) {
                    seekerProductOwner.setContractId(0);
                } else {
                    seekerProductOwner.setContractId(poEntity.getPoId().getId());
                }
                seekerProductOwner.setStatusId(poEntity.getPoStatus().getId());
                seekerProductOwners.add(seekerProductOwner);
            }
            logger.info("PurchaseOrderServiceImpl || getPurchaseOrderDetails || SeekerPurchaseOrderDto Added");
        } else {
            logger.info("Purchase order is not found");
        }
        return seekerProductOwners;
    }

    @Override
    @Transactional
    public List<SeekerMSADetails> getSowDetails(int seekerId) {

        Optional<List<StatementOfWorkEntity>> sow = statementOfWorkService.findBySkillSeekerId(seekerId);
        Optional<SkillSeekerEntity> byId = skillSeekerRepository.findById(seekerId);
        List<SeekerMSADetails> statementOfWorkGetDetailsList = new ArrayList<>();
        if (!sow.get().isEmpty()) {

            for (StatementOfWorkEntity statementOfWorkEntity : sow.get()) {
                SeekerMSADetails statementOfWorkGetDetails = new SeekerMSADetails();
                statementOfWorkGetDetails.setSkillSeekerId(statementOfWorkEntity.getSkillSeekerId());
                statementOfWorkGetDetails.setBusinessName(statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName());
                statementOfWorkGetDetails.setJobId(statementOfWorkEntity.getJobId().getJobId());
                statementOfWorkGetDetails.setProjectId(statementOfWorkEntity.getSkillSeekerProject().getId());
                statementOfWorkGetDetails.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
                statementOfWorkGetDetails.setDepartment(statementOfWorkEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                statementOfWorkGetDetails.setPhoneNumber(null);
                statementOfWorkGetDetails.setEmail(null);
                statementOfWorkGetDetails.setNoOfResource(statementOfWorkEntity.getNumberOfResources());
                if (statementOfWorkEntity.getSowId() == null) {
                    statementOfWorkGetDetails.setContractId(0);
                } else {
                    statementOfWorkGetDetails.setContractId(statementOfWorkEntity.getSowId().getId());
                }
                statementOfWorkGetDetails.setStatusId(statementOfWorkEntity.getSowStatus().getId());
                statementOfWorkGetDetails.setStatus(statementOfWorkEntity.getSowStatus().getStatus());
                statementOfWorkGetDetails.setMsaContractExpiryDate(statementOfWorkEntity.getSowEndDate());
                statementOfWorkGetDetails.setMsaContractStartDate(statementOfWorkEntity.getSowStartDate());

                statementOfWorkGetDetailsList.add(statementOfWorkGetDetails);
            }
            logger.info("StatementOfWorkServiceImpl || getSowDetails || getting StatementOfWork Details");

        } else {
            logger.info("Statement of work is not found");
        }

        return statementOfWorkGetDetailsList;
    }

    @Override
    @Transactional
    public List<SeekerMSADetails> getAllContractsDetails(int skillSeekerId) {
        List<SeekerMSADetails> allContracts = new ArrayList<>();
        SeekerMSADetails seekerMSADetails = getSeekerMsa(skillSeekerId);
        List<SeekerMSADetails> purchaseOrderDetails = getPurchaseOrderDetails(skillSeekerId);
        List<SeekerMSADetails> sowDetails = getSowDetails(skillSeekerId);
//        if (seekerMSADetails != null || !seekerMSADetails1.isEmpty() || !seekerMSADetails2.isEmpty()) {
        if (seekerMSADetails != null || !purchaseOrderDetails.isEmpty() || !sowDetails.isEmpty()) {

            allContracts.add(seekerMSADetails);
            allContracts.addAll(purchaseOrderDetails);
            allContracts.addAll(sowDetails);
            return allContracts;
        } else {
            throw new ServiceException(CONTRACTS_NOT_FOUND.getErrorCode(), CONTRACTS_NOT_FOUND.getErrorDesc());
        }

    }

    @Override
    public List<SeekerMSADetails> getAllSowDetails() {
//        try {
        List<StatementOfWorkEntity> sow = statementOfWorkService.findAll();

        if (!sow.isEmpty()) {
            List<SeekerMSADetails> statementOfWorkGetDetailsList = new ArrayList<>();
            for (StatementOfWorkEntity statementOfWorkEntity : sow) {
                SeekerMSADetails statementOfWorkGetDetails = new SeekerMSADetails();
                Optional<SkillSeekerEntity> byId = skillSeekerRepository.findById(statementOfWorkEntity.getSkillSeekerId());
                if (statementOfWorkEntity.getSowId() != null) {
                    if (statementOfWorkEntity.getSkillSeekerId() != 0) {
                        statementOfWorkGetDetails.setSkillSeekerId(statementOfWorkEntity.getSkillSeekerId());
                        statementOfWorkGetDetails.setBusinessName(statementOfWorkEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName());
                        statementOfWorkGetDetails.setJobId(statementOfWorkEntity.getJobId().getJobId());
                        statementOfWorkGetDetails.setProjectId(statementOfWorkEntity.getSkillSeekerProject().getId());
                        statementOfWorkGetDetails.setProjectName(statementOfWorkEntity.getSkillSeekerProject().getTitle());
                        statementOfWorkGetDetails.setDepartment(statementOfWorkEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                        statementOfWorkGetDetails.setStatus(statementOfWorkEntity.getSowStatus().getStatus());
                        statementOfWorkGetDetails.setMsaContractExpiryDate(statementOfWorkEntity.getSowEndDate());
                        statementOfWorkGetDetails.setMsaContractStartDate(statementOfWorkEntity.getSowStartDate());
                        statementOfWorkGetDetails.setEmail(null);
                        statementOfWorkGetDetails.setNoOfResource(statementOfWorkEntity.getNumberOfResources());
                        statementOfWorkGetDetails.setPhoneNumber(null);
                        try {
                            statementOfWorkGetDetails.setContractId(statementOfWorkEntity.getSowId().getId());
                        } catch (NullPointerException e) {
                            logger.info("null value captured");
                            continue;
                        }

                        statementOfWorkGetDetails.setStatusId(statementOfWorkEntity.getSowStatus().getId());

                        statementOfWorkGetDetailsList.add(statementOfWorkGetDetails);
                    }
                }
            }

            logger.info("StatementOfWorkServiceImpl || getAllSowDetails || getting All StatementOfWork Details");
            return statementOfWorkGetDetailsList;
//            } else {
//                throw new ServiceException();
        }
//        } catch (ServiceException e) {
//            throw new ServiceException(DATA_NOT_FOUNDED.getErrorCode(), DATA_NOT_FOUNDED.getErrorDesc());
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
        return null;
    }

    @Override
    public List<SeekerMSADetails> getAllPoDetails() {
//        try {
        List<PoEntity> productOwnerEntities = poService.findAll();
        List<SeekerMSADetails> seekerProductOwners = new ArrayList<>();
        if (!productOwnerEntities.isEmpty()) {

            for (PoEntity poEntity : productOwnerEntities) {
                SeekerMSADetails seekerProductOwner = new SeekerMSADetails();
                Optional<SkillSeekerEntity> byId = skillSeekerRepository.findById(poEntity.getSkillSeekerId());
                if (poEntity.getPoId() != null) {
                    seekerProductOwner.setSkillSeekerId(poEntity.getSkillSeekerId());
                    seekerProductOwner.setBusinessName(poEntity.getSkillSeekerProject().getSkillSeeker().getSkillSeekerName());
                    seekerProductOwner.setProjectId(poEntity.getSkillSeekerProject().getId());
                    seekerProductOwner.setProjectName(poEntity.getSkillSeekerProject().getTitle());
                    seekerProductOwner.setDepartment(poEntity.getSkillSeekerProject().getOwnerSkillDomainEntity().getDomainValues());
                    seekerProductOwner.setJobId(poEntity.getJobId().getJobId());
                    seekerProductOwner.setStatusId(poEntity.getPoStatus().getId());
                    seekerProductOwner.setStatus(poEntity.getPoStatus().getStatus());
                    seekerProductOwner.setMsaContractExpiryDate(poEntity.getContractEndDate());
                    seekerProductOwner.setMsaContractStartDate(poEntity.getContractStartDate());
                    seekerProductOwner.setEmail(null);
                    seekerProductOwner.setNoOfResource(poEntity.getNumberOfResources());
                    seekerProductOwner.setPhoneNumber(null);
                    try {
                        seekerProductOwner.setContractId(seekerProductOwner.getContractId());
                    } catch (NullPointerException e) {
                        logger.info("null value captured");
                        continue;
                    }

//                    seekerProductOwner.setContractId(poEntity.getPoId().getId());
                    seekerProductOwners.add(seekerProductOwner);
                }
            }
            logger.info("PurchaseOrderServiceImpl || getPurchaseOrderDetails || SeekerPurchaseOrderDto Added");

//            } else {
//                throw new ServiceException();
        }
//        } catch (ServiceException e) {
//            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        return seekerProductOwners;
    }

    @Override
    @Transactional
    public List<SeekerMSADetails> getAllContractsDetails() {
        List<SeekerMSADetails> allContracts = new ArrayList<>();
        List<SeekerMSADetails> seekerMSADetails = getSeekerMsaDetailsInSuperAdmin();
        List<SeekerMSADetails> seekerMSADetails1 = getAllSowDetails();
        List<SeekerMSADetails> seekerMSADetails2 = getAllPoDetails();
        if (seekerMSADetails != null || !seekerMSADetails1.isEmpty() || !seekerMSADetails2.isEmpty()) {
            allContracts.addAll(seekerMSADetails2);
            allContracts.addAll(seekerMSADetails1);
            allContracts.addAll(seekerMSADetails);
        } else {
            throw new ServiceException(CONTRACTS_NOT_FOUND.getErrorCode(), CONTRACTS_NOT_FOUND.getErrorDesc());
        }
        return allContracts;
    }

    @Override
    @Transactional
    public List<PartnerContractDetails> getContractByPartnerId(int partnerId) {
        PartnerContractDetails partnerMsa = getPartnerMsa(partnerId);
        List<PartnerContractDetails> byPartnerIdSowDetail = getByPartnerIdSowDetail(partnerId);
        List<PartnerContractDetails> partnerContractDetails = new ArrayList<>();
        if (!byPartnerIdSowDetail.isEmpty() || (partnerMsa != null && partnerMsa.getStatusId() != 0)) {
            partnerContractDetails.add(partnerMsa);
            partnerContractDetails.addAll(byPartnerIdSowDetail);
            logger.info("ContractServiceImpl || getContractByPartnerId ||  listed the Contracts of SkillPartners");
            return partnerContractDetails;
        } else {
            throw new ServiceException(CONTRACTS_NOT_FOUND.getErrorCode(), CONTRACTS_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public Page<SeekerMSADetails> getAllViewData(int page, int size) {
        List<SeekerMSADetails> byAllViewData = contractFileRepository.findSeekerMSADetailsByAllViewData();
        List<SeekerMSADetails> allSeekerDetails = new ArrayList<>();

        for (SeekerMSADetails k : byAllViewData) {
            try {
                Optional<ContractStatus> id = contractStatusRepository.findById(k.getStatusId());
                if (id.isPresent()) {
                    SeekerMSADetails seekerMSADetails = new SeekerMSADetails(
                            k.getSkillSeekerId(),
                            k.getBusinessName(),
                            k.getJobId(),
                            k.getMsaContractExpiryDate(),
                            k.getMsaContractStartDate(),
                            k.getProjectId(),
                            k.getProjectName(),
                            k.getStatus(),
                            k.getStatusId(),
                            k.getDepartment(),
                            k.getNoOfResource(),
                            k.getEmail(),
                            k.getPhoneNumber()

                    );
                    seekerMSADetails.setStatus(id.get().getStatus());
                    allSeekerDetails.add(seekerMSADetails);
                }
            } catch (ServiceException e) {
                throw new ServiceException(CONTRACTS_NOT_FOUND.getErrorCode(), CONTRACTS_NOT_FOUND.getErrorDesc());
            }
        }
        return seekerContractPagination.getSeekerContractDetails(page, size, allSeekerDetails);
    }

    @Transactional
    @Override
   public Map<Integer, String> getPartnersProject(int partnerId){
        Map<Integer, String> projectMap = new HashMap<>();
        List<SkillSeekerProjectEntity> projects = seekerProjectRepository.findAll();
        for (SkillSeekerProjectEntity project : projects) {
            int projectId = project.getId();
            boolean hasOwners = false;

            List<Job> jobs = jobService.getBySeekerProject(projectId);
            for (Job job : jobs) {
                String jobId = job.getJobId();
                Optional<List<SelectionPhase>> selectionPhases = selectionPhaseService.getByJobJobId(jobId);

                for (SelectionPhase selectionPhase : selectionPhases.orElse(Collections.emptyList())) {
                    SkillOwnerEntity owner = selectionPhase.getSkillOwnerEntity();
                    if (hasScheduledOfferRelease(owner.getSkillOwnerEntityId(), jobId)) {
                        SkillPartnerEntity partner = owner.getSkillPartnerEntity();
                        if (partner.getSkillPartnerId() == partnerId) {
                            hasOwners = true;
                            break;
                        }
                    }
                }
                if (hasOwners) {
                    break;
                }
            }

            if (hasOwners) {
                projectMap.put(projectId, project.getTitle());
            }
        }
        logger.info("Contract ServiceImpl || getProjectsByPartnerId || getting the map of Project IDs and Names for the Partner");
        return projectMap;
    }
    @Transactional
    @Override
    public Map<String, String> getJobNamesByProjectId(int projectId, int id) {
        Map<String, String> jobMap = new HashMap<>();
        List<Job> jobs = jobRepository.findBySeekerProjectId(projectId);
        if (!jobs.isEmpty()) {
            if (isPartnerId(id)) {
                for (Job job : jobs) {
                    String jobId = job.getJobId();
                    Optional<List<SelectionPhase>> selectionPhases = selectionPhaseService.getByJobJobId(jobId);
                    boolean hasOwners = false;
                    for (SelectionPhase selectionPhase : selectionPhases.orElse(Collections.emptyList())) {
                        SkillOwnerEntity owner = selectionPhase.getSkillOwnerEntity();
                        if (hasScheduledOfferRelease(owner.getSkillOwnerEntityId(), jobId)) {
                            SkillPartnerEntity partner = owner.getSkillPartnerEntity();
                            if (partner.getSkillPartnerId() == id) {
                                hasOwners = true;
                                break;
                            }
                        }
                    }
                    if (hasOwners) {
                        jobMap.put(job.getJobId(), job.getJobTitle());
                    }
                }
            } else if (isSeekerId(id)) {
                for (Job job : jobs) {
                    if (job.getSeekerProject().getSkillSeeker().getId() == id) {
                        jobMap.put(job.getJobId(), job.getJobTitle());
                    }
                }
            } else {
                throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
            }
            return jobMap;
        } else {
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }
    }

    private boolean isPartnerId(int id) {
        Optional<SkillPartnerEntity> partner=skillPartnerRepository.findById(id);
        return partner.isPresent();
    }
    private boolean isSeekerId(int id) {
       Optional<List<SkillSeekerProjectEntity>> seekerProject=seekerProjectRepository.findBySkillSeekerId(id);
       return seekerProject.isPresent();

    }

    }





