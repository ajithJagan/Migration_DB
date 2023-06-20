package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.job.service.impl.SelectionPhaseServiceImpl;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillseeker.dto.SeekerPurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class PoServiceImpl implements PoService {

    Logger logger = LoggerFactory.getLogger(PoServiceImpl.class);

    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;

    @Autowired
    SelectionPhaseServiceImpl selectionPhaseService;

    @Autowired
    SkillOwnerService skillOwnerService;

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    RequirementPhaseRepository requirementPhaseRepository;

    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    JobRepository jobRepository;
    @Autowired
    private PoRepository poRepository;
    @Autowired
    private SkillSeekerProjectRepository skillSeekerProjectRepository;

    @Autowired
    private SkillOwnerRepository skillOwnerRepository;

    @Autowired
    private OwnerSkillDomainRepository ownerSkillDomainRepository;

    @Autowired
    private ContractStatusRepository contractStatusRepository;

    @Value("${job.notCleared}")
    private String notCleared;

//    @Override
//    @Transactional
//    public PurchaseOrder addData(List<MultipartFile> multipartFiles, String role, int domainId, int ownerId, int seekerId, int projectID, String jobId) {
//        Optional<PoEntity> poEntityOptional = poRepository.findByAllFields(domainId, ownerId, seekerId, projectID, jobId);
//        PurchaseOrder purchaseOrder = new PurchaseOrder();
//        Optional<SkillOwnerEntity> skillOwner = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(ownerId));
//        HashMap<String, String> fileTypeList = new HashMap<>();
//
//        fileTypeList.put(PDF, APPLICATION_VND_PDF);
//        fileTypeList.put(DOC, TEXT_DOC);
//        fileTypeList.put(DOCX, TEXT_DOCX);
//        if (!multipartFiles.isEmpty() && skillOwner.isPresent()) {
//            for (MultipartFile multipartFile : multipartFiles) {
//                if (fileTypeList.containsValue(multipartFile.getContentType())) {
//                    logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || File Type verified !!");
//                    try {
//                        PoEntity poEntity = new PoEntity();
//                        if (!poEntityOptional.isPresent() || poEntityOptional.isPresent() || skillOwner.isPresent()) {
//                            poEntity = poRepository.save(create(multipartFile, role, domainId, ownerId, seekerId, projectID, jobId));
//                            Notification notification = new Notification();
//                            notificationService.poStatusNotification(poEntity, notification);
//                            //reducing available number of position
//                            Job byJobJobId = jobRepository.findByJobJobId(jobId);
//                            if (byJobJobId.getNumberOfPositions() > 0 && !statementOfWorkRepository.findByOwnerId(ownerId).isPresent()) {
//                                byJobJobId.setNumberOfPositions(byJobJobId.getNumberOfPositions() - 1);
//                                jobRepository.saveAndFlush(byJobJobId);
//                                logger.info("PurchaseOrderServiceImpl || checkNumbersOfPositions ||checkNumbersOfPositions and reduces -1 ");
//                                removeCandidates(ownerId, jobId);
//                            }
//                        } else {
////                            poEntityOptional.get().setName(multipartFile.getOriginalFilename());
////                            poEntityOptional.get().setMimeType(multipartFile.getContentType());
////                            poEntityOptional.get().setSize(multipartFile.getSize());
////                            poEntityOptional.get().setData(multipartFile.getBytes());
//                            poEntityOptional.get().setPoStatus(poEntity.getPoStatus());
//                            poEntity = poRepository.save(poEntityOptional.get());
//
//
//                        }
//                        //changing skill status of candidate
//                        OwnerSkillStatusEntity ownerSkillStatus = new OwnerSkillStatusEntity();
//                        ownerSkillStatus.setSkillOwnerStatusId(4);
//                        skillOwner.get().setOwnerSkillStatusEntity(ownerSkillStatus);
//                        skillOwnerRepository.save(skillOwner.get());
//
//                        purchaseOrder.setId(poEntity.getId());
//                        purchaseOrder.setStatus(poEntity.getPoStatus().getStatus());
//
//
//                        logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || Uploaded the file successfully: {} // ->", multipartFile.getOriginalFilename());
//                    } catch (NullPointerException e) {
//                        throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid request");
//                    } catch (Exception e) {
//                        logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || Could not upload the file: {} // ->", multipartFile.getOriginalFilename());
//                        throw new ServiceException(EXPECTATION_FAILED.getErrorCode(), EXPECTATION_FAILED.getErrorDesc());
//                    }
//                } else {
//                    logger.info("PurchaseOrderServiceImpl || checkFileTypeAndUpload || Wrong File Format ");
//                    throw new ServiceException(WRONG_FILE_FORMAT.getErrorCode(), WRONG_FILE_FORMAT.getErrorDesc());
//                }
//            }
//
//            logger.info("PurchaseOrderServiceImpl || addData || Adding Purchase Order");
//            return purchaseOrder;
//        } else {
//            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
//        }


//    }


    @Transactional
    public void removeCandidates(int ownerId, String jobId) {
        Optional<List<RequirementPhase>> bySkillOwnerIdReqPhase = requirementPhaseRepository.findBySkillOwnerId(ownerId);
        if (bySkillOwnerIdReqPhase.isPresent()) {
            bySkillOwnerIdReqPhase.get().forEach(requirementPhase -> {
                if (!requirementPhase.getJobId().equals(jobId)) {
                    requirementPhaseRepository.delete(requirementPhase);
                }
            });
            logger.info("Remove Candidates || removeCandidatesFromRequirementPhase || removeCandidatesFromRequirementPhase using ownerId");
        }
        Optional<List<SelectionPhase>> bySkillOwnerId = selectionPhaseRepository.findBySkillOwnerId(ownerId);
        if (bySkillOwnerId.isPresent()) {
            bySkillOwnerId.get().forEach(selectionPhase -> {
                if (!selectionPhase.getJob().getJobId().equals(jobId)) {
                    selectionPhaseRepository.delete(selectionPhase);
                }
            });
            logger.info("Remove Candidates  || removeCandidatesFromSelectionPhase || removeCandidatesFromSelectionPhase using ownerId");

        }

    }

    @Override
    public Optional<PoEntity> findByDeleteAtNull(int OwnerId) {
        return poRepository.findByDeleteAtNull(OwnerId);
    }

    @Override
    @Transactional
    public Optional<PoEntity> getByProjectId(int projectId) {
        return poRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<PoEntity> getBySkillOwnerIdAndJobId(int ownerId, String jobId) {
        return poRepository.findBySkillOwnerIdAndJobId(ownerId, jobId);
    }
    @Override
    @Transactional
    public List<PoEntity> findAll() {
        List<PoEntity> poEntityList = poRepository.findAll();
        return poEntityList;
    }

    @Override
    @Transactional
    public Optional<PoEntity> getByJobId(String jobId) {
        return poRepository.findByJobId(jobId);
    }

    @Override
    @Transactional
    public Optional<PoEntity> getByPoId(int poId) {
        return poRepository.findByPoId(poId);
    }

    /**
     * //     * @param id
     *
     * @return dto
     */
//    @Override
//    public PurchaseOrder updateStatus(int poId, int statusId) {
//        PurchaseOrder purchaseOrder;
//        try {
//            Optional<PoEntity> poEntity = poRepository.findById(poId);
//            Optional<ContractStatus> poStatus = contractStatusRepository.findById(statusId);
//
//            purchaseOrder = new PurchaseOrder();
//            if (poEntity.isPresent() && poStatus.isPresent()) {
//                poEntity.get().setPoStatus(poStatus.get());
//                poRepository.save(poEntity.get());
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
    @Override
    @Transactional
    public Optional<PoEntity> downloadAgreement(int id) {
        try {
            Optional<PoEntity> msa = poRepository.findById(id);
            if (msa.isPresent()) {
                logger.info("PurchaseOrderServiceImpl || downloadAgreement || ProductOwner_DownloadAgreement");
                return msa;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public List<SeekerPurchaseOrder> getPurchaseOrderDetails(int skillSeekerId) {
        try {
            Optional<List<PoEntity>> productOwnerEntities = poRepository.findBySkillSeekerId(skillSeekerId);
            if (!productOwnerEntities.get().isEmpty()) {
                List<SeekerPurchaseOrder> seekerProductOwners = new ArrayList<>();
                for (PoEntity poEntity : productOwnerEntities.get()) {
                    SeekerPurchaseOrder seekerProductOwner = new SeekerPurchaseOrder();
                    seekerProductOwner.setId(poEntity.getId());
                    if (null == poEntity.getSkillSeekerProject()) {
                        seekerProductOwner.setSkillSeekerProjectName("Default");
                    } else {
                        seekerProductOwner.setSkillSeekerProjectName(poEntity.getSkillSeekerProject().getTitle());
                    }
                    if (null == poEntity.getOwnerSkillDomainEntity()) {
                        seekerProductOwner.setSkillSeekerProjectDept("Default");
                    } else {
                        seekerProductOwner.setSkillSeekerProjectDept(poEntity.getOwnerSkillDomainEntity().getDomainValues());
                    }
//                    seekerProductOwner.setSkillSeekerProjectDept(poEntity.getOwnerSkillDomainEntity().getDomainValues());
                    seekerProductOwner.setOwnerName(poEntity.getSkillOwnerEntity().getFirstName());
                    seekerProductOwner.setEmail(poEntity.getSkillOwnerEntity().getPrimaryEmail());
                    seekerProductOwner.setRole(poEntity.getRole());
                    seekerProductOwner.setPhoneNumber(poEntity.getSkillOwnerEntity().getPhoneNumber());
                    seekerProductOwner.setStatus(poEntity.getPoStatus().getStatus());
                    seekerProductOwner.setOwnerId(poEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                    seekerProductOwner.setJobId(poEntity.getJobId().getJobId());
                    seekerProductOwner.setPriceRange(poEntity.getPriceRange());
                    seekerProductOwner.setRateCard(poEntity.getRateCard());
                    seekerProductOwner.setContractStartDate(poEntity.getContractStartDate());
                    seekerProductOwner.setContractEndDate(poEntity.getContractEndDate());
                    seekerProductOwners.add(seekerProductOwner);
                }
                logger.info("PurchaseOrderServiceImpl || getPurchaseOrderDetails || SeekerPurchaseOrderDto Added");
                return seekerProductOwners;
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
    public List<SeekerPurchaseOrder> getAllPurchaseOrderDetails() {
        try {
            Optional<List<PoEntity>> productOwnerEntities = Optional.of(poRepository.findAll());
            if (!productOwnerEntities.get().isEmpty()) {
                List<SeekerPurchaseOrder> seekerProductOwners = new ArrayList<>();
                for (PoEntity poEntity : productOwnerEntities.get()) {
                    if (null != poEntity.getSkillOwnerEntity()) {
                        SeekerPurchaseOrder seekerProductOwner = new SeekerPurchaseOrder();
                        seekerProductOwner.setId(poEntity.getId());
                        if (null == poEntity.getSkillSeekerProject()) {
                            seekerProductOwner.setSkillSeekerProjectName("Default");
                        } else {
                            seekerProductOwner.setSkillSeekerProjectName(poEntity.getSkillSeekerProject().getTitle());
                        }
                        if (null == poEntity.getOwnerSkillDomainEntity()) {
                            seekerProductOwner.setSkillSeekerProjectDept("Default");
                        } else {
                            seekerProductOwner.setSkillSeekerProjectDept(poEntity.getOwnerSkillDomainEntity().getDomainValues());
                        }

//                        seekerProductOwner.setSkillSeekerProjectDept(poEntity.getOwnerSkillDomainEntity().getDomainValues());
                        seekerProductOwner.setOwnerName(poEntity.getSkillOwnerEntity().getFirstName());
                        seekerProductOwner.setEmail(poEntity.getSkillOwnerEntity().getPrimaryEmail());
                        seekerProductOwner.setRole(poEntity.getRole());
                        seekerProductOwner.setPhoneNumber(poEntity.getSkillOwnerEntity().getPhoneNumber());
                        seekerProductOwner.setStatus(poEntity.getPoStatus().getStatus());
                        seekerProductOwner.setOwnerId(poEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                        seekerProductOwner.setJobId(poEntity.getJobId().getJobId());
                        seekerProductOwners.add(seekerProductOwner);
                    }
                }
                logger.info("PurchaseOrderServiceImpl || getAllPurchaseOrderDetails || get AllPurchaseOrder Details");
                return seekerProductOwners;
            } else {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(NO_PURCHASE_ORDER.getErrorCode(), NO_PURCHASE_ORDER.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<Resource> getPurchaseOrderTemplateDownload() {

        List<TemplateTable> downloadTemplate = templateRepository.findByTemplateFile("PO_TEMPLATE");
        ByteArrayResource resource = null;
        if (!downloadTemplate.isEmpty()) {
            resource = new ByteArrayResource(downloadTemplate.get(0).getData());

        } else {
            throw new ServiceException(FILE_NOT_FOUND.getErrorCode(), FILE_NOT_FOUND.getErrorDesc());
        }

        logger.info("PurchaseOrderServiceImpl || getSkillSeekerMsaTemplateDownload || download PurchaseOrder Template");
        return ResponseEntity.ok().header("Content-disposition", "attachment; filename=" + "Po_Template").contentType(MediaType.valueOf(downloadTemplate.get(0).getTemplateMimeType())).body(resource);
    }

    @Override
    @Transactional
    public PoEntity getPo(int id) throws FileNotFoundException {
        try {
            Optional<PoEntity> po = poRepository.findById(id);
            if (po.get().getPoId().getMimeType().equals("application/pdf") || po.get().getPoId().getMimeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || po.get().getPoId().getMimeType().equals("application/msword")) {
                return po.get();
            }
        } catch (ServiceException e) {
            throw new RuntimeException(INVALID_ID.getErrorDesc());
        }
        return null;
    }

    @Override
    public Optional<PoEntity> getById(int id) {
        return poRepository.findById(id);
    }

    @Override
    public boolean findByOwnerId(int id) {
        boolean poEntity = poRepository.findByOwnerId(id).isEmpty();
        return poEntity;
    }

    @Override
    public PoEntity save(PoEntity poEntity) {
        return poRepository.save(poEntity);
    }

    @Override
    public PoEntity saveAndFlush(PoEntity poEntity) {
        return poRepository.saveAndFlush(poEntity);
    }


//    @Transactional
//    private PoEntity create(MultipartFile multipartFile, String role, int domainId, int ownerId, int seekerId, int projectID, String jobId) {
//        Optional<SkillOwnerEntity> bySkillOwnerEntityId = Optional.ofNullable(skillOwnerRepository.findBySkillOwnerEntityId(ownerId));
//        Optional<PoEntity> po = poRepository.findByDeleteAtNull(ownerId);
//
//        //we should get skillOwner from selection phase :: need a clarity
//        PoEntity poEntity = new PoEntity();
//        if (!po.isPresent()) {
//
//            Optional<Job> job = jobRepository.findByJobId(jobId);
//
//            if (job.isPresent()) {
//                poEntity.setJobId(job.get());
//            }
//            poEntity.setSkillOwnerEntity(skillOwnerRepository.getById(ownerId));
//            poEntity.setSkillSeekerId(seekerId);
//            poEntity.setRole(role);
//            poEntity.setOwnerSkillDomainEntity(ownerSkillDomainRepository.getById(domainId));
//            if (projectID == 0) {
//                poEntity.setSkillSeekerProject(null);
//            } else {
//                poEntity.setSkillSeekerProject(skillSeekerProjectRepository.getById(projectID));
//            }
//            if (domainId == 0) {
//                poEntity.setOwnerSkillDomainEntity(null);
//            } else {
//                poEntity.setOwnerSkillDomainEntity(ownerSkillDomainRepository.getById(domainId));
//            }
//            poEntity.setPoStatus(poEntity.getPoStatus());
//            poEntity.setName(multipartFile.getOriginalFilename());
//            poEntity.setMimeType(multipartFile.getContentType());
//            poEntity.setSize(multipartFile.getSize());
//
//            try {
//                poEntity.setData(multipartFile.getBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//
//            Date date = new Date();
//            poEntity.setDateOfRelease(date);
//            //As of now, we're assuming expiry-date as 3-months from the date-sign
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.MONTH, 3);
//            poEntity.setExpiryDate(calendar.getTime());
//            poEntity.setPoStatus(contractStatusRepository.findById(1).get());
//
//            logger.info("PurchaseOrderServiceImpl || getSkillSeekerMsaTemplateDownload || download PurchaseOrder Template");
//            return poEntity;
//
//        } else {
//            throw new ServiceException(PO_ALREADY_ADDED.getErrorCode(), PO_ALREADY_ADDED.getErrorDesc());
//        }
//
//    }

}
