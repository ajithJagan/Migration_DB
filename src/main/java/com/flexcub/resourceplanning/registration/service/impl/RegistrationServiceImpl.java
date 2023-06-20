package com.flexcub.resourceplanning.registration.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.registration.dto.*;
import com.flexcub.resourceplanning.registration.entity.AccountVerificationEmailContext;
import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.FlexcubEmailService;
import com.flexcub.resourceplanning.registration.service.ForgotPasswordService;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.repository.WorkForceStrengthRepo;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.utils.EncryptorDecryptor;
import com.flexcub.resourceplanning.verificationmail.entity.VerificationToken;
import com.flexcub.resourceplanning.verificationmail.repository.VerificationTokenRepository;
import com.flexcub.resourceplanning.verificationmail.service.VerificationService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.MAIL_NOT_SENT;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.SAVING_USER;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;


@Service
@Log4j2
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    WorkForceStrengthRepo workForceStrengthRepo;

    Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    @Lazy
    @Autowired
    PoRepository poRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private SkillPartnerRepository skillPartnerRepository;
    @Autowired
    private EncryptorDecryptor encryptorDecryptor;
    @Autowired
    private FlexcubEmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private SkillOwnerRepository skillOwnerRepository;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private SkillSeekerRepository skillSeekerRepository;

    @Lazy
    @Autowired
    private SkillSeekerService skillSeekerService;

    @Autowired
    private SkillPartnerService skillPartnerService;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${flexcub.baseurl}")
    private String baseURL;

    @Value("${flexcub.baseurlOwner}")
    private String baseURLOwner;
    @Value("${flexcub.baseURLforgotPassword}")
    private String baseURLforgotPassword;
    @Value("${flexcub.baseurlLogin}")
    private String baseurlLogin;

    @Value("${flexcub.superAdminMailId}")
    private String superAdminMailId;

    @Value("${flexcub.emailRegex}")
    private String emailRegex;

    @Override
    @Transactional
    public Registration getLoginDetails(Login registration) {
        String email = registration.getEmailId().toLowerCase();
        registration.setEmailId(email);
        Optional<RegistrationEntity> savedRegistration = Optional.ofNullable(registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId()));
        if (savedRegistration.isPresent()) {
        if(!superAdminMailId.equals(savedRegistration.get().getEmailId())) {

                log.debug("savedRegistration>>>" + savedRegistration);
                if (!ObjectUtils.isEmpty(registration)) {
                    String validPassword = EncryptorDecryptor.decrypt(String.valueOf(savedRegistration.get().getPassword()));
                    log.debug("validPassword>>>" + validPassword);
                    if (validPassword.equals(registration.getPassword())) {
                        long userLoginCount = savedRegistration.get().getLoginCount();
                        if (savedRegistration.get().getEmailId().equals(email)) {
                            userLoginCount += 1;
                            savedRegistration.get().setLoginCount(userLoginCount);
                            registrationRepository.saveAndFlush(savedRegistration.get());
                        }
                        Registration registrationDto = modelMapper.map(savedRegistration, Registration.class);
                        if (registrationDto.getRoles().getRolesId() == 1) {
                            SubRoles role = skillSeekerRepository.findById(savedRegistration.get().getId()).get().getSubRoles();
                            if (null != role) {
                                registrationDto.setSubRoles(role.getId());
                            }
                            registrationDto.setModulesAccess(skillSeekerService.getAccessDetails(registrationDto.getId()));
                        }
                        if (registrationDto.getRoles().getRolesId() == 3) {
                            Optional<PoEntity> poEntity = poRepository.findByOwnerId(savedRegistration.get().getId());
                            registrationDto.setTimeSheetAccess(poEntity.isPresent());
                        }
                        RegistrationEntity registrationEntity = registrationRepository.findByEmailIdIgnoreCase(registrationDto.getEmailId());
                        if (!registrationEntity.getIsAccountActive()) {
                            throw new ServiceException(LOGIN_RESTRICTED.getErrorCode(), LOGIN_RESTRICTED.getErrorDesc());
                        }
                        return registrationDto;
                    } else {
                        throw new ServiceException(INVALID_PASSWORD.getErrorCode(), INVALID_PASSWORD.getErrorDesc());
                    }
                }
                return modelMapper.map(registration, Registration.class);

            } else {
                throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
            }

        } else{
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }

    }


    public Registration insertDetails(RegistrationEntity registration) {

        String email = registration.getEmailId().toLowerCase();
        registration.setEmailId(email);
        RegistrationEntity existingUser = registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId());
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = null;
        try {
            emailMatcher = emailPattern.matcher(registration.getEmailId());
        } catch (Exception e) {
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }
        boolean validEmail = emailMatcher.find();
        registration.setCreatedAt(LocalDate.now());
        registration.setTrialExpiredOn(LocalDate.now().plusDays(7));
        if (validEmail) {
            if (Objects.nonNull(existingUser)) {
                if (existingUser.isAccountStatus()) {

                    logger.error(EMAIL_EXISTS.getErrorDesc(), registration.getEmailId());
                    throw new ServiceException(EMAIL_EXISTS.getErrorCode(), EMAIL_EXISTS.getErrorDesc());
                } else {
                    logger.info("Sending Verification Mail again: {}", existingUser.getEmailId());
                    VerificationToken vToken = verificationTokenRepository.findByToken(existingUser.getToken());
                    if (Objects.nonNull(vToken)) {
                        verificationTokenRepository.delete(vToken);
                    }
                    registrationRepository.delete(existingUser);
                    RegistrationEntity registration1 = sendRegistrationConfirmationEmail(registration);
                    Registration registrationDto = modelMapper.map(registration1, Registration.class);
                    if (Objects.nonNull(registration1)) {
                        try {
                            createSeekerAndPartner(registration1);

                            return registrationDto;
                        } catch (Exception e) {
                            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
                        }

                    } else {
                        registration.setMailStatus(MAIL_NOT_SENT);
                        RegistrationEntity savedRegistration = registrationRepository.save(registration);
                        Registration registrationDto1 = modelMapper.map(savedRegistration, Registration.class);

                        try {
                            createSeekerAndPartner(savedRegistration);

                            return registrationDto1;
                        } catch (Exception e) {
                            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
                        }

                    }
                }
            } else {
                logger.info(SAVING_USER, registration.getEmailId());
                RegistrationEntity registration1 = sendRegistrationConfirmationEmail(registration);
                Registration registrationDto = modelMapper.map(registration1, Registration.class);

                if (Objects.nonNull(registration1)) {
                    try {
                        createSeekerAndPartner(registration1);
                        return registrationDto;
                    } catch (Exception e) {
                        throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());

                    }

                } else {
                    registration.setMailStatus(MAIL_NOT_SENT);
                    logger.info("RegistrationServiceImpl || insertDetails || Verification mail  is not send ");
                    try {
                        RegistrationEntity savedRegistration = registrationRepository.save(registration);
                        Registration registrationDto1 = modelMapper.map(savedRegistration, Registration.class);
                        createSeekerAndPartner(savedRegistration);
                        return registrationDto1;
                    } catch (Exception e) {
                        throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
                    }

                }
            }
        } else {
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }
    }


    @Override
    public Registration verifyRegistration(Verify registration) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(registration.getToken());
        if (ObjectUtils.isEmpty(verificationToken)) {
            throw new ServiceException(INVALID_REGISTRATION_LINK.getErrorCode(), INVALID_REGISTRATION_LINK.getErrorDesc());
        }
        if (!verificationToken.isExpired() && verificationToken.getExpireAt().isAfter(LocalDateTime.now())) {
            RegistrationEntity registration1 = registrationRepository.findByToken(registration.getToken());
            if (registration1.isAccountStatus()) {
                throw new ServiceException(USER_ALREADY_VERIFIED.getErrorCode(), USER_ALREADY_VERIFIED.getErrorDesc());
            } else {
                registration1.setAccountStatus(true);
                logger.info("RegistrationServiceImpl || verifyRegistration|| verification has been expired");
                verificationTokenRepository.save(verificationToken);
                registration1.setPassword(EncryptorDecryptor.encrypt(registration.getPassword()));
                logger.info("RegistrationServiceImpl || verifyRegistration|| Account has been verified");

                registrationRepository.saveAndFlush(registration1);
                return modelMapper.map(registration1, Registration.class);
            }
        } else {
            throw new ServiceException(LINK_EXPIRED.getErrorCode(), LINK_EXPIRED.getErrorDesc());
        }
    }

    public RegistrationEntity sendRegistrationConfirmationEmail(RegistrationEntity registration) {

        String email = registration.getEmailId().toLowerCase();
        registration.setEmailId(email);
        VerificationToken verificationToken = verificationService.createVerificationToken();
        registration.setToken(verificationToken.getToken());
        try {
            registrationRepository.save(registration);
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Invalid Data");
        }
        verificationToken.setRegistration(registration);
        verificationTokenRepository.save(verificationToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(registration, emailContext);
        emailContext.setToken(verificationToken.getToken());
        emailContext.baseurlLogin(baseurlLogin);
        if (registration.getRoles().getRolesId() == 3) {
            emailContext.buildVerificationUrl(baseURLOwner, verificationToken.getToken());
        }
        else if (registration.getRoles().getRolesId() == 1 ){
            if(registration.getIsAccountActive()) {
                emailContext.buildVerificationUrl(baseURL, verificationToken.getToken());
            }
            else {
                registration.setMailStatus(MAIL_NOT_SENT);
                logger.info("RegistrationServiceImpl || insertDetails || Verification mail  is not send ");
                return registrationRepository.save(registration);
            }
        }
        else {
            emailContext.buildVerificationUrl(baseURL, verificationToken.getToken());
        }
        try {
            emailService.sendMail(emailContext, verificationToken.getToken());
            registration.setMailStatus("Mail send successfully");

            logger.info("RegistrationServiceImpl || sendRegistrationConfirmationEmail || Registration Mail Sent");

            try {
                return registrationRepository.save(registration);
            } catch (Exception e) {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RegistrationServiceImpl || sendRegistrationConfirmationEmail || Unable to send email for mail id {}", registration.getEmailId());
        }

        return registration;
    }
    @Override
    public Registration superAdminLoginScreen(Login registration) {

        String email = registration.getEmailId().toLowerCase();
        registration.setEmailId(email);

        RegistrationEntity savedRegistration = Optional.ofNullable(registrationRepository.findByEmailIdIgnoreCase(email))
                .orElseThrow(() -> new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc()));

        if (!superAdminMailId.equals(savedRegistration.getEmailId())) {
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }

        String validPassword = EncryptorDecryptor.decrypt(String.valueOf(savedRegistration.getPassword()));
        if (!Objects.equals(validPassword, registration.getPassword())) {
            throw new ServiceException(INVALID_PASSWORD.getErrorCode(), INVALID_PASSWORD.getErrorDesc());
        }

        long userLoginCount = savedRegistration.getLoginCount() + 1;
        savedRegistration.setLoginCount(userLoginCount);
        registrationRepository.saveAndFlush(savedRegistration);

        Registration registrationDto = modelMapper.map(savedRegistration, Registration.class);
        return registrationDto;
    }

    @Override
    public List<WorkForceStrength> getData() {
        return workForceStrengthRepo.findAll();
    }

    @Override
    public void sendMailForFailedOwnerRegistrations(Map<Integer, List<String>> failedMap) {
        failedMap.forEach((key, value) -> {
            Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(key);
            if (skillPartnerEntity.isPresent()) {
                AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
                emailContext.baseurlLogin(baseurlLogin);
                emailContext.fail(skillPartnerEntity.get(), emailContext, value);
                try {
                    emailService.sendFailedMail(emailContext);
                } catch (Exception e) {
                    logger.error("RegistrationServiceImpl || sendMailForFailedOwnerRegistrations || Unable to send email for mail id: {}", skillPartnerEntity.get().getBusinessEmail());
                }
            }
        });
    }

    @Transactional
    @Override
    public SkillOwnerEntity verifyRegistrationForOwner(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (ObjectUtils.isEmpty(verificationToken)) {
            throw new ServiceException(INVALID_REGISTRATION_LINK.getErrorCode(), INVALID_REGISTRATION_LINK.getErrorDesc());
        }
        if (!verificationToken.isExpired() && verificationToken.getExpireAt().isAfter(LocalDateTime.now())) {
            RegistrationEntity registration1 = registrationRepository.findByToken(token);
            if (registration1.isAccountStatus()) {
                throw new ServiceException(USER_ALREADY_VERIFIED.getErrorCode(), USER_ALREADY_VERIFIED.getErrorDesc());
            } else {
                logger.info("RegistrationServiceImpl || verifyRegistrationForOwner|| Account has been verified");
                SkillOwnerEntity skillOwnerEntity = skillOwnerRepository.findByPrimaryEmail(registration1.getEmailId());
                Hibernate.initialize(skillOwnerEntity.getPortfolioUrl());
                return skillOwnerEntity;
            }
        } else {
            throw new ServiceException(LINK_EXPIRED.getErrorCode(), LINK_EXPIRED.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public Registration setPasswordForOwner(SetOwnerPassword skillOwnerRegistrationEntity) {

        String email = skillOwnerRegistrationEntity.getEmailId().toLowerCase();
        skillOwnerRegistrationEntity.setEmailId(email);
        try {
            Optional<RegistrationEntity> registration = Optional.ofNullable(registrationRepository.findByEmailIdIgnoreCase(skillOwnerRegistrationEntity.getEmailId()));
            if (registration.isEmpty()) {
                throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
            }
            Optional<VerificationToken> verificationToken = Optional.ofNullable(verificationTokenRepository.findByToken(registration.get().getToken()));
            if (registration.isPresent() && verificationToken.isPresent() && !verificationToken.get().isExpired()) {
                registration.get().setPassword(EncryptorDecryptor.encrypt(skillOwnerRegistrationEntity.getPassword()));
                registration.get().setAccountStatus(true);
                SkillOwnerEntity skillOwner = skillOwnerRepository.findByPrimaryEmail(skillOwnerRegistrationEntity.getEmailId());
                skillOwner.setAccountStatus(true);
                verificationToken.get().setExpired(true);
                verificationTokenRepository.save(verificationToken.get());
                skillOwnerRepository.save(skillOwner);
                logger.info("RegistrationServiceImpl || setPasswordForOwner || Password has been successfully created");
                Registration registrationDto = modelMapper.map(registration, Registration.class);
                registrationRepository.saveAndFlush(registration.get());
                return registrationDto;
            } else {
                throw new ServiceException(LINK_EXPIRED.getErrorCode(), LINK_EXPIRED.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }

    }

    private void createSeekerAndPartner(RegistrationEntity registration) {
        if (registration.getRoles().getRolesId() == 1) {
            //create a skill seeker entry
            skillSeekerService.addEntryToSkillSeeker(registration);
        } else if (registration.getRoles().getRolesId() == 2) {
            //create a skill seeker entry
            skillPartnerService.addEntryToSkillPartner(registration);
        }
    }

    @Override
    public boolean changePassword(ChangePasswordDto changePasswordDto) {
        String email = changePasswordDto.getEmailId().toLowerCase();
        changePasswordDto.setEmailId(email);

        try {
            RegistrationEntity savedRegistration = (registrationRepository.findByEmailIdIgnoreCase(changePasswordDto.getEmailId()));

            String oldPasswordFromDB = EncryptorDecryptor.decrypt(savedRegistration.getPassword());

            if (oldPasswordFromDB.equals(changePasswordDto.getOldPassword())) {
                savedRegistration.setPassword(EncryptorDecryptor.encrypt(changePasswordDto.getNewPassword()));
                registrationRepository.saveAndFlush(savedRegistration);
                return true;
            } else {
                throw new ServiceException(INVALID_OLD_PASSWORD.getErrorCode(), INVALID_OLD_PASSWORD.getErrorDesc());
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }
    }

    @Override
    public boolean forgotPassword(String emailId) {
        try {
            ForgotPasswordToken forgotPasswordToken = forgotPasswordService.createForgotPasswordToken();
            RegistrationEntity registrationFromDb = registrationRepository.findByEmailIdIgnoreCase(emailId);
            forgotPasswordToken.setRegistration(registrationFromDb);
            forgotPasswordService.saveToken(forgotPasswordToken);
            AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
            emailContext.forgotPass(registrationFromDb, emailContext);
            emailContext.setForgotToken(forgotPasswordToken.getForgotToken());
            emailContext.baseurlLogin(baseurlLogin);
            emailContext.buildForgetPassUrl(baseURLforgotPassword, forgotPasswordToken.getForgotToken());
            try {
                emailService.forgotMail(emailContext, forgotPasswordToken.getForgotToken());
                logger.info("RegistrationServiceImpl || forgotPassword || Reset Password token is sent successfully");
                return true;
            } catch (Exception e) {
                e.printStackTrace();

            }
            return false;
        } catch (Exception e) {
            throw new ServiceException(INVALID_EMAIL_ID.getErrorCode(), INVALID_EMAIL_ID.getErrorDesc());
        }
    }


    @Override
    public boolean verifyForgottenPassword(ChangePasswordDto changePasswordDto) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByForgotToken(changePasswordDto.getForgotPassToken());
        Optional<RegistrationEntity> savedRegistration = (registrationRepository.findById(forgotPasswordToken.getRegistration().getId()));
        String oldPasswordFromDB = EncryptorDecryptor.decrypt(savedRegistration.get().getPassword());
        if (ObjectUtils.isEmpty(forgotPasswordToken)) {
            throw new ServiceException(INVALID_LINK_FOR_FORGOT_PASSWORD.getErrorCode(), INVALID_LINK_FOR_FORGOT_PASSWORD.getErrorDesc());
        }
        if (!forgotPasswordToken.isExpired() && forgotPasswordToken.getExpireAt().isAfter(LocalDateTime.now())) {
            RegistrationEntity registration = forgotPasswordToken.getRegistration();
            if (oldPasswordFromDB.equals(changePasswordDto.getOldPassword())) {
                throw new ServiceException(INVALID_NEW_PASSWORD.getErrorCode(), INVALID_NEW_PASSWORD.getErrorDesc());
            }
            registration.setPassword(EncryptorDecryptor.encrypt(changePasswordDto.getNewPassword()));
            forgotPasswordToken.setExpired(true);
            logger.info("RegistrationServiceImpl || verifyForgottenPassword || Token set to expired");
            registrationRepository.saveAndFlush(registration);
            forgotPasswordService.saveToken(forgotPasswordToken);
            logger.info("RegistrationServiceImpl || verifyForgottenPassword || Password successfully reset");
            return true;
        } else {
            throw new ServiceException(LINK_EXPIRED.getErrorCode(), LINK_EXPIRED.getErrorDesc());
        }
    }

    @Override
    public void resendMail(int id) {

            Optional<RegistrationEntity> registration = registrationRepository.findById(id);
            if (registration.isPresent() && !registration.get().isAccountStatus()) {
                RegistrationEntity registration1 = sendRegistrationConfirmationEmail(registration.get());
            } else {
                throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
            }
        }

    }

