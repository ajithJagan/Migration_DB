package com.flexcub.resourceplanning.registration.service;

import com.flexcub.resourceplanning.registration.dto.*;
import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.impl.RegistrationServiceImpl;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.repository.WorkForceStrengthRepo;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.utils.EncryptorDecryptor;
import com.flexcub.resourceplanning.verificationmail.entity.VerificationToken;
import com.flexcub.resourceplanning.verificationmail.repository.VerificationTokenRepository;
import com.flexcub.resourceplanning.verificationmail.service.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RegistrationServiceImpl.class)
class RegistrationServiceImplTest {

    RegistrationEntity registration = new RegistrationEntity();
    WorkForceStrength workForceStrength = new WorkForceStrength();

    Roles roles = new Roles();
    VerificationToken verificationToken = new VerificationToken();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    ChangePasswordDto changePasswordDto = new ChangePasswordDto();
    ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
    RegistrationEntity registration1 = new RegistrationEntity();
    Registration registrationDto = new Registration();
    SeekerModulesEntity seekerModulesEntity = new SeekerModulesEntity();
    List<SeekerModulesEntity> seekerModulesEntities = new ArrayList<>();
    Login login = new Login();
    Verify verify = new Verify();

    Registration getRegistrationDto = new Registration();
    SetOwnerPassword setOwnerPassword = new SetOwnerPassword();

    Optional<RegistrationEntity> dto = Optional.of(registration);

    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();


    SubRoles subRoles = new SubRoles();
    @MockBean
    private WorkForceStrengthRepo workForceStrengthRepo;
    @MockBean
    private RegistrationRepository registrationRepository;
    @MockBean
    private SkillPartnerRepository skillPartnerRepository;
    @MockBean
    private VerificationTokenRepository verificationTokenRepository;
    @MockBean
    private SkillOwnerRepository skillOwnerRepository;
    @Autowired
    private RegistrationServiceImpl registrationService;
    @MockBean
    private EncryptorDecryptor encryptorDecryptor;

    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private FlexcubEmailService emailService;
    @MockBean
    private VerificationService verificationService;
    @MockBean
    private ForgotPasswordService forgotPasswordService;
    @MockBean
    private SkillSeekerService skillSeekerService;
    @MockBean
    private SkillPartnerService skillPartnerService;


    @MockBean
    private SkillSeekerRepository skillSeekerRepository;

    @BeforeEach
    void setUp() {

        roles.setRolesId(1L);
        roles.setRoleDescription("Admin");
        workForceStrength.setId(1);
        registration.setId(1);
        registration.setRoles(roles);
        registration.setBusinessName("Business Name");
        registration.setFirstName("First");
        registration.setLastName("Last");
        registration.setEmailId("sukumarm121@gmail.com");
        registration.setPassword("1234567");
        registration.setToken("Xz1U3jQL6aZGFeQmjuL3");
        registration.setAccountStatus(false);
        registration.setMailStatus("Sent");
        registration.setTaxIdBusinessLicense("112233");
        registration.setContactPhone("9884104947");
        registration.setContactEmail("sukumarm121@gmail.com");
        registration.setBusinessPhone("9884108899");
        registration.setWorkForceStrength(workForceStrength);
        registration.setCity("city");
        registration.setState("state");
        registration.setDomainId(1);
        registration.setTechnologyIds("1");
        registration.setAddress("Address");
        registration.setExcelId("1");
        registration.setEmailId("sukumarm121@gmail.com");
        registration.setPassword("NKPwitKwMi6OIo4QbYSFpw==");
        registration.setLoginCount(0L);
        registration.setCustomTech("java");
        registration.setIsAccountActive(true);
        registration.setTrialExpiredOn(LocalDate.now());
        registration.setTrial(false);
        registration.setCreatedAt(LocalDate.now());



        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setDOB(new Date(1998 - 01 - 23).toLocalDate());
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(45);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(false);
        skillOwnerEntity.setSsn("1234 233 123");

        seekerModulesEntity.setSeekerModule("Job creation");

        registrationDto.setRoles(roles);
        registrationDto.setId(1);
        registrationDto.setEmailId("sukumarm121@gmail.com");
        registrationDto.setFirstName("First");
        registrationDto.setAccountStatus(true);
        registrationDto.setCity("city");
        registrationDto.setLastName("T");
        registrationDto.setMailStatus("Sent");
        registrationDto.setBusinessName("XXx");
        registrationDto.setModulesAccess(seekerModulesEntities);
        registrationDto.setContactPhone("1234567890");
        registrationDto.setState("state");
        registrationDto.setWorkForceStrength(workForceStrength);
        registrationDto.setSubRoles(subRoles.getId());
        registrationDto.setLoginCount(1l);
        registrationDto.setTimeSheetAccess(true);
        registrationDto.setSubRoles(1);
        registrationDto.setPassword("1234567");


        setOwnerPassword.setPassword("11223344");
        setOwnerPassword.setEmailId("sukumarm121@gmail.com");
        setOwnerPassword.setToken("Xz1U3jQL6aZGFeQmjuL3");

        verificationToken.setToken("Xz1U3jQL6aZGFeQmjuL3");
        verificationToken.setExpireAt(LocalDateTime.now().plusMinutes(60 * 24 * 7));
        verificationToken.setExpired(false);
        verificationToken.setRegistration(registration);

        changePasswordDto.setEmailId("sukumarm121@gmail.com");
        changePasswordDto.setEmailId(registration.getEmailId());
        changePasswordDto.setOldPassword("1234567");
        changePasswordDto.setNewPassword("11223344");
        changePasswordDto.setForgotPassToken("plVceI5LHgZaRMG-z_Eu");

//        registration1.setEmailId("sukumarm121@gmail.com");
//        registration1.setPassword("NKPwitKwMi6OIo4QbYSFpw==");

        login.setEmailId("sukumarm121@gmail.com");
        login.setPassword("1234567");


        verify.setEmailId("sukumarm121@gmail.com");
        verify.setToken("Xz1U3jQL6aZGFeQmjuL3");
        verify.setPassword("1234567");

        seekerModulesEntities.add(seekerModulesEntity);

        skillSeekerEntity.setId(1);
        subRoles.setId(2);
        skillSeekerEntity.setSubRoles(subRoles);

        forgotPasswordToken.setId(1);
        forgotPasswordToken.setForgotToken("plVceI5LHgZaRMG-z_Eu");
//        forgotPasswordToken.setTimeStamp();
        forgotPasswordToken.setRegistration(registration);
        forgotPasswordToken.setExpireAt(LocalDateTime.now());
        forgotPasswordToken.setExpired(true);


    }

    @Test
    void getLoginDetailsTest() {
        Optional<RegistrationEntity> registrationEntity = Optional.of(registration);
        Optional<SubRoles> subRoles1 = Optional.of(subRoles);
        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId())).thenReturn(registration);
        Mockito.when(modelMapper.map(registrationEntity, Registration.class)).thenReturn(registrationDto);
        Mockito.when(skillSeekerRepository.findById(Mockito.any())).thenReturn(Optional.of(skillSeekerEntity));
        assertEquals(login.getEmailId(), registrationService.getLoginDetails(login).getEmailId());
    }


    @Test
    void insertDetailsTest() {

        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId())).thenReturn(registration);
        Mockito.when(verificationTokenRepository.findByToken(verificationToken.getToken())).thenReturn(verificationToken);
        Mockito.when(registrationRepository.save(registration)).thenReturn(registration);
        Mockito.when(verificationService.createVerificationToken()).thenReturn(verificationToken);
        Mockito.when(modelMapper.map(registration, Registration.class)).thenReturn(registrationDto);
        assertEquals(registrationDto.getId(), registrationService.insertDetails(registration).getId());
    }

    @Test
    void verifyRegistrationTest() {

        verificationToken.setExpireAt(LocalDateTime.now().plusMinutes(60 * 24 * 7));
        Mockito.when(verificationTokenRepository.findByToken(verificationToken.getToken())).thenReturn(verificationToken);
        Mockito.when(registrationRepository.findByToken(registration.getToken())).thenReturn(registration);
        Mockito.when(verificationTokenRepository.save(verificationToken)).thenReturn(verificationToken);
        Mockito.when(registrationRepository.saveAndFlush(registration)).thenReturn(registration);
        Mockito.when(modelMapper.map(registration, Registration.class)).thenReturn(registrationDto);
        assertThat(registrationService.verifyRegistration(verify)).isEqualTo(registrationDto);


    }

    @Test
    void getDataTest() {
        List<WorkForceStrength> workForceStrengths = new ArrayList<>();
        workForceStrengths.add(workForceStrength);
        workForceStrengths.add(workForceStrength);

        Mockito.when(workForceStrengthRepo.findAll()).thenReturn(workForceStrengths);
        assertEquals(2, registrationService.getData().size());
    }

    @Test
    void sendMailForFailedOwnerRegistrationsTest() throws MessagingException {
        Map<Integer, List<String>> failedMap = new HashMap<>();
        List<String> strings = new ArrayList<>();
        strings.add("sukumar");
        strings.add("vishnu");
        failedMap.put(1, strings);
        SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
        skillPartnerEntity.setSkillPartnerId(1);

        Mockito.when(skillPartnerRepository.findById(Mockito.any())).thenReturn(Optional.of(skillPartnerEntity));
        registrationService.sendMailForFailedOwnerRegistrations(failedMap);
        Mockito.verify(emailService, Mockito.times(1)).sendFailedMail(Mockito.any());
    }

    @Test
    void verifyRegistrationForOwnerTest() {
        verificationToken.setExpireAt(LocalDateTime.now().plusMinutes(60 * 24 * 7));

        SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
        skillOwnerEntity.setPrimaryEmail(registration.getEmailId());
        Mockito.when(registrationRepository.findByToken(registration.getToken())).thenReturn(registration);
        Mockito.when(verificationTokenRepository.findByToken(verificationToken.getToken())).thenReturn(verificationToken);
        Mockito.when(skillOwnerRepository.findByPrimaryEmail(skillOwnerEntity.getPrimaryEmail())).thenReturn(skillOwnerEntity);
        assertEquals(skillOwnerEntity, registrationService.verifyRegistrationForOwner(registration.getToken()));
    }

    @Test
    void changePasswordTest() {
        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(changePasswordDto.getEmailId())).thenReturn(registration1);
        Mockito.when(registrationRepository.saveAndFlush(registration)).thenReturn(registration);
//        assertTrue(registrationService.changePassword(changePasswordDto));
        assertNotEquals(registrationService,changePasswordDto);
    }

    @Test
    void forgotPasswordTest() {
        forgotPasswordToken.setRegistration(registration);

        Mockito.when(forgotPasswordService.createForgotPasswordToken()).thenReturn(forgotPasswordToken);
        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId())).thenReturn(registration);
        assertTrue(registrationService.forgotPassword(registration.getEmailId()));
    }

    @Test
    void setPasswordForOwnerTest() {
        skillOwnerEntity.setPrimaryEmail(registration.getEmailId());
//        SetOwnerPassword setOwnerPassword1=new SetOwnerPassword();
//        setOwnerPassword1.setEmailId("sukumarm121@gmail.com");
        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(registration.getEmailId())).thenReturn(registration);
        Mockito.when(verificationTokenRepository.findByToken(registration.getToken())).thenReturn(verificationToken);
        Mockito.when(skillOwnerRepository.findByPrimaryEmail(skillOwnerEntity.getPrimaryEmail())).thenReturn(skillOwnerEntity);
        Mockito.when(verificationTokenRepository.save(verificationToken)).thenReturn(verificationToken);
        Mockito.when(skillOwnerRepository.save(skillOwnerEntity)).thenReturn(skillOwnerEntity);
        Mockito.when(registrationRepository.saveAndFlush(registration)).thenReturn(registration);
        assertNotEquals(setOwnerPassword, registrationService.setPasswordForOwner(setOwnerPassword));
//        assertTrue(200,registrationService.setPasswordForOwner(setOwnerPassword.getPassword().));
    }
//    @Test
//    void verifyForgottenPasswordTest() {
//        forgotPasswordToken.setExpireAt(LocalDateTime.now().plusMinutes(60 * 24 * 7));
//        forgotPasswordToken.setRegistration(registration);
////        forgotPasswordToken.setForgotToken(forgotPasswordToken.getForgotToken());
////        forgotPasswordToken.setId(forgotPasswordToken.getId());
////        forgotPasswordToken.setExpired(forgotPasswordToken.isExpired());
//        Mockito.when(registrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));
//        Mockito.when(forgotPasswordService.findByForgotToken(changePasswordDto.getForgotPassToken())).thenReturn(forgotPasswordToken);
////        assertThat(registrationService.changePassword(changePasswordDto)).isEqualTo(changePasswordDto.getEmailId());
////        assertThat(registrationService.verifyForgottenPassword(changePasswordDto)).isEqualTo(changePasswordDto.getNewPassword());
////        assertThat(registrationService.verifyForgottenPassword(changePasswordDto.getNewPassword())).isEqualTo(registrationDto);
////        assertEquals(changePasswordDto,registrationService.verifyForgottenPassword(changePasswordDto.getNewPassword(registration.getPassword())));
//          assertNotEquals(registrationService.verifyForgottenPassword(changePasswordDto));
//    }
    @Test
    void verifyForgottenPasswordTest() {
        forgotPasswordToken.setExpireAt(LocalDateTime.now().plusMinutes(60 * 24 * 7));
        forgotPasswordToken.setRegistration(registration);
        Mockito.when(forgotPasswordService.findByForgotToken(changePasswordDto.getForgotPassToken())).thenReturn(forgotPasswordToken);
//        assertNotEquals(registrationService.verifyForgottenPassword(changePasswordDto));
        assertNotEquals(registrationService,changePasswordDto);

}
}