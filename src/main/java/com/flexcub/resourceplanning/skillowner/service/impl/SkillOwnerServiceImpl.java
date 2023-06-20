package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.notifications.repository.OwnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.service.impl.NotificationServiceImpl;
import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerGender;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerMaritalStatus;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerDocuments;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerGenderEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerMaritalStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerPortfolio;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerSlotsEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerGenderRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerMaritalStatusRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerPortfolioRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerDocumentRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerResumeAndImageRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerSlotsRepository;
import com.flexcub.resourceplanning.skillowner.repository.VisaStatusRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.dto.Contracts;
import com.flexcub.resourceplanning.skillseeker.dto.OnBoarding;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ServerException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.APPLICATION_VND_PDF;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.DOC;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.DOCX;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.IMG_JPEG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.IMG_JPG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.IMG_PNG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.JPEG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.JPG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.MAIL_NOT_SENT;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.PDF;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.PHONE_REGEX;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.PHONE_SPL_CHAR_REPLACE;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.PNG;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.TEXT_DOC;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.TEXT_DOCX;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;


@Service
@RequiredArgsConstructor
@Component
public class SkillOwnerServiceImpl implements SkillOwnerService {

    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    Logger logger = LoggerFactory.getLogger(SkillOwnerServiceImpl.class);
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    OwnerMaritalStatusRepository ownerMaritalStatusRepository;
    @Autowired
    OwnerGenderRepository genderRepository;
    @Autowired
    SkillOwnerDocumentRepository skillOwnerDocumentRepository;
    @Autowired
    SkillOwnerResumeAndImageRepository skillOwnerResumeAndImageRepository;
    @Autowired
    OwnerNotificationsRepository ownerNotificationsRepository;
    @Autowired
    OwnerPortfolioRepository skillOwnerPortfolioRepo;
    @Autowired
    SkillPartnerRepository skillPartnerRepository;
    @Autowired
    PoRepository poRepository;
    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;
    @Lazy
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;
    @Autowired
    SkillSeekerRepository skillSeekerRepository;

    @Autowired
    OwnerSkillSetRepository ownerSkillSetRepository;

    @Autowired
    SkillOwnerSlotsRepository skillOwnerSlotsRepository;

    @Autowired
    VisaStatusRepository visaStatusRepository;
    @Value("${owner.msaInProgress}")
    private String msaInProgress;
    @Value("${owner.poInProgress}")
    private String poInProgress;
    @Value("${owner.sowReleased}")
    private String sowReleased;
    @Value("${owner.sowInProgress}")
    private String sowInProgress;
    @Value("${owner.expiringSoon}")
    private String expiringSoon;
    @Value("${owner.expired}")
    private String expired;
    @Value("${owner.msaReleased}")
    private String msaReleased;
    @Value("${flexcub.emailRegex}")
    private String emailRegex;

    @Value("${file.content.disposition}")
    private String content;

    @Value("${file.name}")
    private String fileName;
    @Autowired
    private ModelMapper modelMapper;

    private static final int MSA_STATUS_ID = 17;

    @Transactional
    @Override
    public SkillOwnerDto getSkillOwner(int id) {

        SkillOwnerEntity skillOwnerEntity = skillOwnerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc()));

        SkillOwnerDto ownerDto = new SkillOwnerDto();

        Hibernate.initialize(skillOwnerEntity.getPortfolioUrl());
        BeanUtils.copyProperties(skillOwnerEntity, ownerDto, NullPropertyName.getNullPropertyNames(skillOwnerEntity));

        ownerDto.setSsn(Objects.nonNull(skillOwnerEntity.getSsn()) ? new String(Base64.getDecoder().decode(skillOwnerEntity.getSsn())) : null);
        ownerDto.setGender(Objects.nonNull(skillOwnerEntity.getGender()) ? new SkillOwnerGender(skillOwnerEntity.getGender().getId()) : null);
        ownerDto.setSkillPartnerId(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId());

        ownerDto.setFederalCheckStartDate(skillOwnerEntity.isFederalSecurityClearance() ? skillOwnerEntity.getFederalStartDate() : null);
        ownerDto.setFederalCheckEndDate(skillOwnerEntity.isFederalSecurityClearance() ? skillOwnerEntity.getFederalEndDate() : null);

        return ownerDto;

    }

    @Override
    public Optional<SkillOwnerEntity> getSkillOwnerEntity(int id) {
        Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(id);
        return skillOwnerEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SkillOwnerEntity> insertData(List<SkillOwnerEntity> skillOwnerEntityList) throws IOException {

        List<SkillOwnerEntity> savedSkillOwnerList = new ArrayList<>();
        for (SkillOwnerEntity skillOwnerEntity : skillOwnerEntityList) {
           checkMsaValidity(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId());
            try {
                checkValidMail(skillOwnerEntity.getPrimaryEmail());
                OptionalInt opInt = OptionalInt.of(skillOwnerEntity.getRateCard());
                if (null == skillOwnerEntity.getRateCard() || skillOwnerEntity.getRateCard() == 0 || opInt.isEmpty()) {
                    throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
                }
            } catch (ServiceException e) {
                throw new ServiceException(INVALID_RATECARD_VALUE.getErrorCode(), INVALID_RATECARD_VALUE.getErrorDesc());
            } catch (Exception e) {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid Email ID or Null ");
            }
            skillOwnerEntity.setPhoneNumber(checkValidPhone(skillOwnerEntity.getPhoneNumber()));
            checkValidDate(skillOwnerEntity.getDOB());

            checkPhoneAndEmailInDB(skillOwnerEntity);

            logger.info("SkillOwnerServiceImpl || insertData  || Saving the Skill Owner Entity {}", skillOwnerEntity.getPrimaryEmail());

            RegistrationEntity registration = new RegistrationEntity();
            Roles roles = new Roles();
            roles.setRolesId(3L);
            registration.setFirstName(skillOwnerEntity.getFirstName());
            registration.setLastName(skillOwnerEntity.getLastName());
            registration.setContactPhone(skillOwnerEntity.getPhoneNumber());
            registration.setEmailId(skillOwnerEntity.getPrimaryEmail());
            registration.setCity(skillOwnerEntity.getCity());
            registration.setState(skillOwnerEntity.getState());
            registration.setRoles(roles);

            Registration registeredOwner = registrationService.insertDetails(registration);

            skillOwnerEntity.setSkillOwnerEntityId(registeredOwner.getId());
            if (registeredOwner.getMailStatus().equalsIgnoreCase(MAIL_NOT_SENT)) {
                logger.info("SkillOwnerServiceImpl || insertData  || Registration has failed for SkillOwner");
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
            } else {
                savedSkillOwnerList.add(skillOwnerRepository.save(skillOwnerEntity));
                //limited user in trial version
                RegistrationEntity registration1 = registrationRepository.findById(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId()).orElseThrow(() -> new IllegalArgumentException("Invalid partner ID"));
                if (skillOwnerRepository.findBySkillPartnerId(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId()).get().size() <= 4 && registration1.isTrial()) {
                    skillOwnerRepository.save(skillOwnerEntity);
                } else if (!registration1.isTrial()) {
                    skillOwnerRepository.save(skillOwnerEntity);
                } else {
                    throw new ServiceException(USER_LIMIT_EXCEEDED.getErrorCode(), "User limit exceeded");
                }
            }
        }
        return savedSkillOwnerList;

    }

    @Transactional
    public void checkPhoneAndEmailInDB(SkillOwnerEntity skillOwnerEntity) {
        SkillOwnerEntity existingOwnerInfo = skillOwnerRepository.findByPrimaryEmail(skillOwnerEntity.getPrimaryEmail());
        if (!ObjectUtils.isEmpty(existingOwnerInfo)) {
            logger.error(EXISTING_EMAIL.getErrorDesc());
            throw new ServiceException(EXISTING_EMAIL.getErrorCode(), EXISTING_EMAIL.getErrorDesc());
        } else {
            SkillOwnerEntity existingPhoneInfo = skillOwnerRepository.findByPhoneNumber(skillOwnerEntity.getPhoneNumber());
            if (!ObjectUtils.isEmpty(existingPhoneInfo)) {
                logger.error(EXISTING_PRIMARYPHONE.getErrorDesc());
                throw new ServiceException(EXISTING_PRIMARYPHONE.getErrorCode(), EXISTING_PRIMARYPHONE.getErrorDesc());
            }
        }
    }

    private void checkValidDate(LocalDate dob) {
//        LocalDate date = dob.toLocalDate();
        LocalDate curDate = LocalDate.now();
        if (Period.between(dob, curDate).getYears() < 18) {
            logger.error(INVALID_DOB.getErrorDesc());
            throw new ServiceException(INVALID_DOB.getErrorCode(), INVALID_DOB.getErrorDesc());
        }
    }

    private String checkValidPhone(String phoneNumber) {


        try {
            phoneNumber = phoneNumber.replaceAll(PHONE_SPL_CHAR_REPLACE, "");
            Pattern phonePattern = Pattern.compile(PHONE_REGEX);
            Matcher phoneMatcher = phonePattern.matcher(phoneNumber);
            boolean validPhone = phoneMatcher.find();
            if (!validPhone) {
                logger.error(INVALID_PRIMARYPHONE.getErrorDesc(), phoneNumber);
                throw new ServiceException(INVALID_PRIMARYPHONE.getErrorCode(), INVALID_PRIMARYPHONE.getErrorDesc());

            }
            return phoneNumber;
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_PHONE.getErrorCode(), INVALID_PHONE.getErrorDesc());
        }
    }

    private void checkValidMail(String primaryEmail) {
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(primaryEmail);
        boolean validEmail = emailMatcher.find();
        if (!validEmail) {
            logger.error(INVALID_EMAIL_ID.getErrorDesc(), primaryEmail);
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }
    }


    private void checkMsaValidity(int skillPartnerId) {
        SkillPartnerEntity partnerEntity = skillPartnerRepository
                .findByIdAndMsaStatusId(skillPartnerId, MSA_STATUS_ID)
                .orElseThrow(() -> new ServiceException(MSA_FILE_NOT_FOUND.getErrorCode(), MSA_FILE_NOT_FOUND.getErrorDesc()));
    }


    /**
     * @param id id of portfolio
     */
    @Override
    public void deletePortfolioUrl(int id) {
        try {
            Optional<SkillOwnerPortfolio> skillOwnerData = skillOwnerPortfolioRepo.findById(id);
            if (skillOwnerData.isPresent()) {
                logger.info("SkillOwnerServiceImpl || deletePortfolioUrl || deleted the data by using id: {}", id);
                skillOwnerPortfolioRepo.deleteById(id);
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid request");
        }
    }


    /**
     * @return list of gender
     */
    @Override
    public List<SkillOwnerGender> genderList() {

        Optional<List<SkillOwnerGenderEntity>> skillOwnerGenderEntities = Optional.of(genderRepository.findAll());
        List<SkillOwnerGender> skillOwnerGendersDtoList = new ArrayList<>();
        if (!skillOwnerGenderEntities.get().isEmpty()) {
            for (SkillOwnerGenderEntity skillOwnerGenderEntity : skillOwnerGenderEntities.get()) {
                SkillOwnerGender skillOwnerGender = modelMapper.map(skillOwnerGenderEntity, SkillOwnerGender.class);
                skillOwnerGendersDtoList.add(skillOwnerGender);
            }
            return skillOwnerGendersDtoList;
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * @return list of marital status
     */
    @Override
    public List<SkillOwnerMaritalStatus> maritalStatus() {
        Optional<List<SkillOwnerMaritalStatusEntity>> skillOwnerMaritalStatusEntities = Optional.of(ownerMaritalStatusRepository.findAll());
        List<SkillOwnerMaritalStatus> skillOwnerMaritalStatusesDtoList = new ArrayList<>();
        if (!skillOwnerMaritalStatusEntities.get().isEmpty()) {
            for (SkillOwnerMaritalStatusEntity skillOwnerMaritalStatusEntity : skillOwnerMaritalStatusEntities.get()) {
                SkillOwnerMaritalStatus skillOwnerMaritalStatus = modelMapper.map(skillOwnerMaritalStatusEntity, SkillOwnerMaritalStatus.class);
                skillOwnerMaritalStatusesDtoList.add(skillOwnerMaritalStatus);
            }
            return skillOwnerMaritalStatusesDtoList;
        } else {
            throw new ServiceException(MARITAL_STATUS_NOT_FOUND.getErrorCode(), MARITAL_STATUS_NOT_FOUND.getErrorDesc());
        }
    }

    /**
     * @param skillOwnerEntity details
     * @return updated object
     */
    @Override
    @Transactional
    public SkillOwnerDto updateOwnerProfile(SkillOwnerEntity skillOwnerEntity) {
        try {
            SkillOwnerDto ownerDto = new SkillOwnerDto();
            Optional<SkillOwnerEntity> ownerEntity = skillOwnerRepository.findById(skillOwnerEntity.getSkillOwnerEntityId());

            boolean status = true;

            int skillOwnerStatusId = skillOwnerEntity.getOwnerSkillStatusEntity().getSkillOwnerStatusId();
            if (skillOwnerStatusId == ownerEntity.get().getOwnerSkillStatusEntity().getSkillOwnerStatusId()) {
                status = false;
            }

            if (ownerEntity.isPresent()) {
                if (skillOwnerEntity.isFederalSecurityClearance()) {
                    ownerEntity.get().setFederalStartDate(skillOwnerEntity.getFederalStartDate());
                    ownerEntity.get().setFederalEndDate(skillOwnerEntity.getFederalEndDate());
                }
                if (null != skillOwnerEntity.getPortfolioUrl()) {
                    List<SkillOwnerPortfolio> skillOwnerPortfolios = new ArrayList<>();
                    if (!ownerEntity.get().getPortfolioUrl().isEmpty()) {
                        skillOwnerPortfolios = ownerEntity.get().getPortfolioUrl();
                    }
                    for (SkillOwnerPortfolio ownerPortfolio : skillOwnerEntity.getPortfolioUrl()) {
                        List<SkillOwnerPortfolio> portfolioUrlsAlreadyExist = skillOwnerPortfolioRepo.findByPortfolioUrlsAndPortfolio_url_id(ownerPortfolio.getPortfolioUrls(), skillOwnerEntity.getSkillOwnerEntityId());
                        if (portfolioUrlsAlreadyExist.isEmpty()) {
                            List<SkillOwnerPortfolio> finalSkillOwnerPortfolios1 = skillOwnerPortfolios;
                            SkillOwnerPortfolio portfolio = skillOwnerPortfolioRepo.saveAndFlush(ownerPortfolio);
                            finalSkillOwnerPortfolios1.add(portfolio);
                        }
                    }
                    skillOwnerEntity.setPortfolioUrl(skillOwnerPortfolios);
                }

                if (ownerEntity.get().isAccountStatus()) skillOwnerEntity.setAccountStatus(true);
                BeanUtils.copyProperties(skillOwnerEntity, ownerEntity.get(), NullPropertyName.getNullPropertyNames(skillOwnerEntity));
                if (null != skillOwnerEntity.getSsn()) {
                    String encodedSsn = Base64.getEncoder().encodeToString((skillOwnerEntity.getSsn()).getBytes());
                    Optional<List<SkillOwnerEntity>> skillOwnerSsnList = skillOwnerRepository.findBySsn(encodedSsn);
                    if (skillOwnerSsnList.isPresent() && !skillOwnerSsnList.get().isEmpty()) {
                        throw new ServiceException(SSN_NOT_VALID.getErrorCode(), SSN_NOT_VALID.getErrorDesc());
                    }
                    ownerEntity.get().setSsn(Base64.getEncoder().encodeToString((skillOwnerEntity.getSsn()).getBytes()));
                }

                SkillOwnerEntity skillowner = skillOwnerRepository.findByPrimaryEmail(skillOwnerEntity.getPrimaryEmail());
                skillowner.setPermanentAddress(skillOwnerEntity.getPermanentAddress());
                skillowner.setPermanentCity(skillOwnerEntity.getPermanentCity());
                skillowner.setPermanentState(skillOwnerEntity.getPermanentState());

                SkillOwnerEntity skillOwner = skillOwnerRepository.save(ownerEntity.get());

                if (status) {
                    notificationService.ownerAvailabilityNotification(skillOwner.getSkillOwnerEntityId());
                }
                BeanUtils.copyProperties(skillOwner, ownerDto, NullPropertyName.getNullPropertyNames(skillOwner));
                ownerDto.setSkillPartnerId(skillOwner.getSkillPartnerEntity().getSkillPartnerId());
                Hibernate.initialize(ownerDto.getPortfolioUrl());


                return ownerDto;

            } else {
                throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public SkillOwnerEntity updateOwnerDetails(SkillOwnerEntity skillOwnerEntity) {
        try {
            RegistrationEntity regFromDB = registrationRepository.findByEmailIdIgnoreCase(skillOwnerEntity.getPrimaryEmail());

            regFromDB.setFirstName(skillOwnerEntity.getFirstName());
            regFromDB.setLastName(skillOwnerEntity.getLastName());
            regFromDB.setContactPhone(skillOwnerEntity.getPhoneNumber());
            registrationRepository.saveAndFlush(regFromDB);
            logger.info("SkillOwnerServiceImpl || updateOwnerDetails || Updated and saving the data from SkillOwner to registration=={}", skillOwnerEntity);


            skillOwnerRepository.saveAndFlush(skillOwnerEntity);
            return skillOwnerEntity;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid EmailId or Null");
        }
    }


    public SkillOwnerEntity getOwnerId(int id) {
        SkillOwnerEntity skillOwnerEntity = skillOwnerRepository.findBySkillOwnerEntityId(id);
        return skillOwnerEntity;
    }


    /**
     * @param documents uploaded
     * @param image     uploaded
     *                  //     * @param id        of skill Owner
     * @return boolean value
     */
    @Transactional
    @Override
    public Boolean insertAttachment(Optional<MultipartFile> resume, List<MultipartFile> documents, Optional<MultipartFile> image, Optional<MultipartFile> federal, int ownerId) throws IOException {
        Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(ownerId);
        if (skillOwnerEntity.isPresent()) {
            try {
                SkillOwnerEntity ownerEntity = skillOwnerEntity.get();
                Optional<List<SkillOwnerResumeAndImage>> byOwnerID = skillOwnerResumeAndImageRepository.findByOwnerID(ownerId);
                if (resume.isPresent()) {
                    SkillOwnerResumeAndImage skillOwnerResume = new SkillOwnerResumeAndImage();
                    if (byOwnerID.get().size() > 0) {
                        for (SkillOwnerResumeAndImage resumeAndImage : byOwnerID.get()) {
                            if (resumeAndImage.isResume()) {
                                skillOwnerResume.setId(resumeAndImage.getId());
                            }
                        }
                    }
                    storeResume(resume, ownerId);
                    ownerEntity.setResumeAvailable(true);
                }
                if (image.isPresent()) {
                    SkillOwnerResumeAndImage skillOwnerImage = new SkillOwnerResumeAndImage();
                    if (byOwnerID.get().size() > 0) {
                        for (SkillOwnerResumeAndImage resumeAndImage : byOwnerID.get()) {
                            if (resumeAndImage.isImage()) {
                                skillOwnerImage.setId(resumeAndImage.getId());
                            }
                        }
                    }
                    storeImage(image, ownerId);
                    ownerEntity.setImageAvailable(true);
                }

                if (federal.isPresent()) {
                    storeFederal(federal, ownerId);
                }
                if (null != documents && !documents.isEmpty()) {
                    AtomicInteger sequence = new AtomicInteger(1);

                    for (MultipartFile document : documents) {
                        String fileName = StringUtils.cleanPath(Objects.requireNonNull(document.getOriginalFilename()));
                        SkillOwnerDocuments skillOwnerDocuments = new SkillOwnerDocuments(ownerId, fileName, document.getContentType(), document.getBytes(), sequence.get(), document.getSize());
                        sequence.getAndIncrement();

                        List<SkillOwnerDocuments> skillOwnerDocuments1 = skillOwnerDocumentRepository.findByOwnerId(ownerId);

                        int max = 0;
                        for (int i = 0; i < skillOwnerDocuments1.size(); i++) {
                            if (max < skillOwnerDocuments1.get(i).getCount())
                                max = skillOwnerDocuments1.get(i).getCount();
                        }
                        if (max > 0) {
                            skillOwnerDocuments.setCount(max + 1);
                        }
                        skillOwnerDocumentRepository.saveAndFlush(skillOwnerDocuments);

                    }
                }
                skillOwnerRepository.save(ownerEntity);
                logger.info("SkillOwnerServiceImpl || insertAttachment || Attaching Documents");
                return true;
            } catch (Exception e) {
                throw new ServiceException(ATTACHMENT_UPDATE_FAILED.getErrorCode(), ATTACHMENT_UPDATE_FAILED.getErrorDesc());
            }
        } else {
            throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
        }
    }

    public SkillOwnerResumeAndImage storeResume(Optional<MultipartFile> resume, int ownerId) throws IOException {
        try {
            HashMap<String, String> fileTypeList = new HashMap<>();
            fileTypeList.put(PDF, APPLICATION_VND_PDF);
            fileTypeList.put(DOC, TEXT_DOC);
            fileTypeList.put(DOCX, TEXT_DOCX);

            if (!fileTypeList.containsValue(resume.get().getContentType())) {
                throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
            }
            Optional<SkillOwnerResumeAndImage> byOwnerID = skillOwnerResumeAndImageRepository.findByOwnerId(ownerId);
            SkillOwnerResumeAndImage skillOwnerResume = byOwnerID.orElse(new SkillOwnerResumeAndImage());
            skillOwnerResume.setOwnerId(ownerId);
            skillOwnerResume.setResumeType(resume.get().getContentType());
            skillOwnerResume.setResumeName(StringUtils.cleanPath(resume.get().getOriginalFilename()));
            skillOwnerResume.setResume(true);
            skillOwnerResume.setResumeData(resume.get().getBytes());
            skillOwnerResume.setResumeSize(resume.get().getSize());
            logger.info("SkillOwnerServiceImpl || storeResume || storeResume data for owner resume inserted");
            return skillOwnerResumeAndImageRepository.saveAndFlush(skillOwnerResume);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
        }
    }

    public SkillOwnerResumeAndImage storeFederal(Optional<MultipartFile> federal, int ownerId) throws IOException {

        try {
            HashMap<String, String> fileTypeList = new HashMap<>();
            fileTypeList.put(PDF, APPLICATION_VND_PDF);
            fileTypeList.put(DOC, TEXT_DOC);
            fileTypeList.put(DOCX, TEXT_DOCX);
            if (!fileTypeList.containsValue(federal.get().getContentType())) {
                throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
            }
            Optional<SkillOwnerResumeAndImage> skillOwner = skillOwnerResumeAndImageRepository.findByOwnerId(ownerId);
            SkillOwnerResumeAndImage skillOwnerImage = skillOwner.orElse(new SkillOwnerResumeAndImage());
            skillOwnerImage.setOwnerId(ownerId);
            skillOwnerImage.setFederal(true);
            skillOwnerImage.setFederalType(federal.get().getContentType());
            skillOwnerImage.setFederalName(StringUtils.cleanPath(federal.get().getOriginalFilename()));
            skillOwnerImage.setFederalData(federal.get().getBytes());
            skillOwnerImage.setFederalSize(federal.get().getSize());
            logger.info("SkillOwnerServiceImpl || storeFederal || SkillOwnerResumeAndImage called ! data for federal files inserted");
            return skillOwnerResumeAndImageRepository.saveAndFlush(skillOwnerImage);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
        }

    }

    @Override
    public SkillOwnerResumeAndImage storeImage(Optional<MultipartFile> image, int ownerId) throws IOException {
        try {
            HashMap<String, String> fileTypeList = new HashMap<>();
            fileTypeList.put(PNG, IMG_PNG);
            fileTypeList.put(JPG, IMG_JPG);
            fileTypeList.put(JPEG, IMG_JPEG);
            if (!fileTypeList.containsValue(image.get().getContentType())) {
                throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
            }
            Optional<SkillOwnerResumeAndImage> byOwnerId = skillOwnerResumeAndImageRepository.findByOwnerId(ownerId);
            SkillOwnerResumeAndImage skillOwnerImage = byOwnerId.orElse(new SkillOwnerResumeAndImage());
            skillOwnerImage.setOwnerId(ownerId);
            skillOwnerImage.setImage(true);
            skillOwnerImage.setImageType(image.get().getContentType());
            skillOwnerImage.setImageName(StringUtils.cleanPath(image.get().getOriginalFilename()));
            skillOwnerImage.setImageData(image.get().getBytes());
            skillOwnerImage.setImageSize(image.get().getSize());
            logger.info("SkillOwnerServiceImpl || storeImage || storeImage data for owner image inserted");
            return skillOwnerResumeAndImageRepository.saveAndFlush(skillOwnerImage);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorCode(), INVALID_OWNER_ID_OR_MULTIPART_FILE.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public SkillOwnerDocuments getOtherDocuments(int ownerId, int count) {
        try {
            Optional<SkillOwnerDocuments> skillOwnerDocuments = skillOwnerDocumentRepository.findByOwnerIdAndCount(ownerId, count);
            if (skillOwnerDocuments.isPresent()) {
                return skillOwnerDocuments.get();
            } else {
                throw new ServiceException(INVALID_OWNER_ID_OR_COUNT.getErrorCode(), INVALID_OWNER_ID_OR_COUNT.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID_OR_COUNT.getErrorCode(), INVALID_OWNER_ID_OR_COUNT.getErrorDesc());
        }
    }

    /**
     * @param id of skill Owner
     * @return image
     */
    @Override
    @Transactional
    public ResponseEntity<Resource> downloadImage(int id) {
        try {
            Optional<List<SkillOwnerResumeAndImage>> ownerID = skillOwnerResumeAndImageRepository.findByOwnerID(id);
            byte[] data = new byte[0];
            String name = null;
            String type = null;
            for (SkillOwnerResumeAndImage skillOwnerResumeAndImage : ownerID.get()) {
                if (skillOwnerResumeAndImage.getImageType().equals("image/png") || skillOwnerResumeAndImage.getImageType().equals("image/jpg") || skillOwnerResumeAndImage.getImageType().equals("image/jpeg")) {
                    data = skillOwnerResumeAndImage.getImageData();
                    name = skillOwnerResumeAndImage.getImageName();
                    type = skillOwnerResumeAndImage.getImageType();
                }
            }
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok().header(content, fileName + (name)).contentType(MediaType.valueOf(type)).body(resource);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
    }


    /**
     * //     * @param id of skillOwner
     *
     * @return resume of owner
     */
    @Override
    @Transactional
    public ResponseEntity<Resource> downloadResume(int id) {
        try {
            Optional<List<SkillOwnerResumeAndImage>> ownerID = skillOwnerResumeAndImageRepository.findByOwnerID(id);
            byte[] data = new byte[0];
            String name = null;
            String type = null;
            for (SkillOwnerResumeAndImage skillOwnerResumeAndImage : ownerID.get()) {
                if (skillOwnerResumeAndImage.getResumeType().equals("application/pdf") || skillOwnerResumeAndImage.getResumeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    data = skillOwnerResumeAndImage.getResumeData();
                    name = skillOwnerResumeAndImage.getResumeName();
                    type = skillOwnerResumeAndImage.getResumeType();
                }
            }
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok().header(content, fileName + (name)).contentType(MediaType.valueOf(type)).body(resource);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }

    }

    @Override
    @Transactional
    public SkillOwnerResumeAndImage getResume(int ownerId) throws FileNotFoundException {
        try {
            Optional<List<SkillOwnerResumeAndImage>> skillOwnerResumeAndImages = skillOwnerResumeAndImageRepository.findByOwnerID(ownerId);

            for (SkillOwnerResumeAndImage skillOwnerResumeAndImage : skillOwnerResumeAndImages.get()) {
                if (skillOwnerResumeAndImage.getResumeType().equals("application/pdf") || skillOwnerResumeAndImage.getResumeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || skillOwnerResumeAndImage.getResumeType().equals("application/msword"))
                    return skillOwnerResumeAndImage;
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
        return null;
    }

    @Override
    public SkillOwnerResumeAndImage getImage(int ownerId) throws FileNotFoundException {
        try {
            Optional<List<SkillOwnerResumeAndImage>> skillOwnerResumeAndImages = skillOwnerResumeAndImageRepository.findByOwnerID(ownerId);

            for (SkillOwnerResumeAndImage skillOwnerResumeAndImage : skillOwnerResumeAndImages.get()) {
                if (skillOwnerResumeAndImage.getImageType().equals("image/png") || skillOwnerResumeAndImage.getImageType().equals("image/jpg") || skillOwnerResumeAndImage.getImageType().equals("image/jpeg"))
                    return skillOwnerResumeAndImage;
            }
            return null;
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
    }

    @Override
    public SkillOwnerResumeAndImage getFederal(int ownerId) throws FileNotFoundException {
        try {
            Optional<SkillOwnerResumeAndImage> byOwnerId = skillOwnerResumeAndImageRepository.findByOwnerId(ownerId);
            HashMap<String, String> fileTypeList = new HashMap<>();
            fileTypeList.put(PDF, APPLICATION_VND_PDF);
            fileTypeList.put(DOC, TEXT_DOC);
            fileTypeList.put(DOCX, TEXT_DOCX);
            if (fileTypeList.containsValue(byOwnerId.get().getFederalType())) return byOwnerId.get();
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
        return null;
    }

    @Override
    @Transactional
    public Contracts ownerContractDetails(SkillSeekerMSAEntity skillSeekerMSAEntity) {
        try {
            Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
            if (skillOwner.isPresent()) {
                Contracts contracts = new Contracts();
                contracts.setIsMsaCreated(true);
                contracts.setMsaId(skillSeekerMSAEntity.getId());

                contracts.setJobId(skillSeekerMSAEntity.getJobId().getJobId());
                contracts.setMsaStatus("MSA " + skillSeekerMSAEntity.getMsaStatus().getStatus());

                contracts.setOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                contracts.setName(skillSeekerMSAEntity.getSkillOwnerEntity().getFirstName() + " " + skillSeekerMSAEntity.getSkillOwnerEntity().getLastName());
                contracts.setPosition(skillSeekerMSAEntity.getJobId().getJobTitle());
                contracts.setLocation(skillSeekerMSAEntity.getJobId().getJobLocation());
                contracts.setSeekerName(skillSeekerRepository.findById(skillSeekerMSAEntity.getSkillSeekerId()).get().getSkillSeekerName());
                contracts.setSeekerContactEmail(skillSeekerRepository.findById(skillSeekerMSAEntity.getSkillSeekerId()).get().getEmail());
                contracts.setSeekerContactPhone(skillSeekerRepository.findById(skillSeekerMSAEntity.getSkillSeekerId()).get().getPhone());

                Optional<SkillPartnerEntity> byId = skillPartnerRepository.findById(skillOwner.get().getSkillPartnerEntity().getSkillPartnerId());
                contracts.setPartner(byId.get().getBusinessName());

                if (null != skillSeekerMSAEntity.getSkillSeekerProject() && skillSeekerMSAEntity.getSkillSeekerProject().getId() != 0) {
                    contracts.setProjectId(skillSeekerMSAEntity.getSkillSeekerProject().getId());
                    contracts.setProjectName(skillSeekerMSAEntity.getSkillSeekerProject().getTitle());
                } else {
                    contracts.setProjectId(0);
                    contracts.setProjectName("Default");
                }
                contracts.setSeekerId(skillSeekerMSAEntity.getSkillSeekerId());
                contracts.setOwnerMailId(skillSeekerMSAEntity.getSkillOwnerEntity().getPrimaryEmail());
                contracts.setOwnerContactNumber(skillSeekerMSAEntity.getSkillOwnerEntity().getPhoneNumber());
                contracts.setProcessedOn(skillSeekerMSAEntity.getDateSigned());

                Optional<PoEntity> poEntity = poRepository.findByOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                Optional<StatementOfWorkEntity> sowEntity = statementOfWorkRepository.findByOwnerId(skillSeekerMSAEntity.getSkillOwnerEntity().getSkillOwnerEntityId());

                contracts.setStatus("MSA " + skillSeekerMSAEntity.getMsaStatus().getStatus());
                if (sowEntity.isPresent() && !poEntity.isPresent()) {
                    contracts.setIsSowCreated(true);
                    contracts.setSowId(sowEntity.get().getId());
                    contracts.setSowStatus("SOW " + sowEntity.get().getSowStatus().getStatus());
                    contracts.setStatus("SOW " + sowEntity.get().getSowStatus().getStatus());
                    contracts.setProcessedOn(sowEntity.get().getDateOfRelease());

                    logger.info("SkillSeekerServiceImpl || getContractDetails || SOW -  {}", sowInProgress);
                }
                if (poEntity.isPresent() && !sowEntity.isPresent()) {
                    contracts.setIsPoCreated(true);
                    contracts.setPoId(poEntity.get().getId());
                    contracts.setPoStatus("PO " + poEntity.get().getPoStatus().getStatus());
                    contracts.setStatus("PO " + poEntity.get().getPoStatus().getStatus());
                    logger.info("SkillSeekerServiceImpl || getContractDetails || MSA - {} ", sowReleased);
                }
                if (poEntity.isPresent() && sowEntity.isPresent()) {
                    contracts.setIsPoCreated(true);
                    contracts.setIsSowCreated(true);
                    contracts.setPoId(poEntity.get().getId());
                    contracts.setSowId(sowEntity.get().getId());
                    contracts.setPoStatus("PO " + poEntity.get().getPoStatus().getStatus());
                    contracts.setSowStatus("SOW " + sowEntity.get().getSowStatus().getStatus());
                    logger.info("SkillSeekerServiceImpl || getContractDetails || Po and Sow Released ");
                    contracts.setStatus("PO " + poEntity.get().getPoStatus().getStatus());
                }
                if (null != skillOwner.get().getOnBoardingDate()) {
                    contracts.setStatus("On-Boarded");

                    contracts.setContractDurationStartDate(skillOwner.get().getStartDate());
                    contracts.setContractDurationEndDate(skillOwner.get().getEndDate());
                    contracts.setOnBoarding(skillOwner.get().getOnBoardingDate());


                    if (LocalDate.now().plusDays(42).isAfter(skillOwner.get().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                        contracts.setStatus(expiringSoon);
                    }
                    if (LocalDate.now().isAfter(skillOwner.get().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                        contracts.setStatus(expired);
                    }
                }
                return contracts;
            } else {
                throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Resource> downloadOtherDocuments(int ownerId, int count) {


        try {
            Optional<SkillOwnerDocuments> skillOwnerDocuments = skillOwnerDocumentRepository.findByOwnerIdAndCount(ownerId, count);

            ByteArrayResource resource;

            if (skillOwnerDocuments.isPresent()) {
                resource = new ByteArrayResource(skillOwnerDocuments.get().getData());
            } else {
                throw new ServiceException(RESUME_NOT_FOUND.getErrorCode(), RESUME_NOT_FOUND.getErrorDesc());
            }
            logger.info("SkillSeekerServiceImpl || downloadOtherDocuments || download other documents from owner's DB ");

            return ResponseEntity.ok().header(content, fileName + skillOwnerDocuments.get().getName()).contentType(MediaType.valueOf(skillOwnerDocuments.get().getType())).body(resource);

        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_COUNT.getErrorCode(), INVALID_OWNER_COUNT.getErrorDesc());
        }


    }

    @Override
    @Transactional
    public void deleteOtherDocuments(int id, int count) {
        try {
            Optional<SkillOwnerDocuments> skillOwnerDocuments = skillOwnerDocumentRepository.findByOwnerIdAndCount(id, count);
            if (skillOwnerDocuments.isPresent()) {
                logger.info("SkillOwnerServiceImpl || deleteOtherDocuments || deleted the data by using id and count : {}", id + " " + count);
                skillOwnerDocumentRepository.deleteByOwnerIdAndCount(id, count);
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid request");
        }
    }


    @Transactional
    @Override
    public SkillOwnerDocuments documentUpdates(MultipartFile document, int ownerId, int count) throws IOException {
        try {
            SkillOwnerDocuments byOwnerIdAndCount = new SkillOwnerDocuments();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(document.getOriginalFilename()));
            byOwnerIdAndCount.setData(document.getBytes());
            byOwnerIdAndCount.setSize(document.getSize());
            byOwnerIdAndCount.setType(document.getContentType());
            byOwnerIdAndCount.setName(fileName);
            byOwnerIdAndCount.setOwnerId(ownerId);
            byOwnerIdAndCount.setCount(count);
            return skillOwnerDocumentRepository.saveAndFlush(byOwnerIdAndCount);
        } catch (ServerException e) {
            throw new ServiceException(INVALID_OWNER_ID_OR_COUNT.getErrorCode(), INVALID_OWNER_ID_OR_COUNT.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Resource> fileDownloadFederal(int id) {

        try {
            Optional<SkillOwnerResumeAndImage> byOwnerId = skillOwnerResumeAndImageRepository.findByOwnerId(id);
            byte[] data = new byte[0];
            String name = null;
            String type = null;
            HashMap<String, String> fileTypeList = new HashMap<>();
            fileTypeList.put(PDF, APPLICATION_VND_PDF);
            fileTypeList.put(DOC, TEXT_DOC);
            fileTypeList.put(DOCX, TEXT_DOCX);
            if (fileTypeList.containsValue(byOwnerId.get().getFederalType())) {
                data = byOwnerId.get().getFederalData();
                name = byOwnerId.get().getFederalName();
                type = byOwnerId.get().getFederalType();
            }
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity.ok().header(content, fileName + (name)).contentType(MediaType.valueOf(type)).body(resource);
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }

    }


    public OnBoarding getOnBoarding(OnBoarding onBoarding) {

        Optional<List<SkillSeekerProjectEntity>> bySkillSeekerId = skillSeekerProjectRepository.findBySkillSeekerId(onBoarding.getSeekerId());
        if (bySkillSeekerId.isPresent()) {
            for (SkillSeekerProjectEntity entity : bySkillSeekerId.get()) {
                onBoarding.setProjectId(entity.getId());
                onBoarding.setProjectName(entity.getTitle());
                onBoarding.setSeekerId(entity.getSkillSeeker().getId());
                onBoarding.setStartDate(onBoarding.getStartDate());
                onBoarding.setEndDate(onBoarding.getEndDate());
                onBoarding.setSkillOwnerEntityId(onBoarding.getSkillOwnerEntityId());
                onBoarding.setStatus(onBoarding.getStatus());
            }
        }
        return onBoarding;
    }

    @Override
    public SkillOwnerSlotsEntity findBySkillOwnerEntityId(int skillOwnerId) {
        return skillOwnerSlotsRepository.findBySkillOwnerEntityId(skillOwnerId);
    }

    public SkillOwnerEntity save(SkillOwnerEntity skillOwnerEntity) {
        SkillOwnerEntity ownerEntity = skillOwnerRepository.save(skillOwnerEntity);
        return ownerEntity;
    }



}