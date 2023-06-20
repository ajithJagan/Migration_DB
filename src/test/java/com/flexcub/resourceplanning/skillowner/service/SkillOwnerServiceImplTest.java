package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.notifications.repository.OwnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.service.impl.NotificationServiceImpl;
import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerGender;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerMaritalStatus;
import com.flexcub.resourceplanning.skillowner.entity.*;
import com.flexcub.resourceplanning.skillowner.repository.*;
import com.flexcub.resourceplanning.skillowner.service.impl.SkillOwnerServiceImpl;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.repository.*;
import com.flexcub.resourceplanning.verificationmail.entity.VerificationToken;
import com.flexcub.resourceplanning.verificationmail.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SkillOwnerServiceImpl.class)
class SkillOwnerServiceImplTest {


    @MockBean
    NotificationServiceImpl notificationService;
    @Autowired
    SkillOwnerServiceImpl skillOwnerService;
    @MockBean
    SkillOwnerRepository skillOwnerRepository;
    @MockBean
    RegistrationRepository registrationRepository;
    @MockBean
    RegistrationService registrationService;
    @MockBean
    OwnerMaritalStatusRepository ownerMaritalStatusRepository;
    @MockBean
    OwnerGenderRepository genderRepository;

    @MockBean
    SkillOwnerDocumentRepository skillOwnerDocumentRepository;

    @MockBean
    OwnerPortfolioRepository skillOwnerPortfolioRepo;

    @MockBean
    OwnerNotificationsRepository ownerNotificationsRepository;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @MockBean
    SkillOwnerResumeAndImageRepository resumeAndImageRepository;

    @Autowired
    SkillOwnerResumeAndImageRepository skillOwnerResumeAndImageRepository;
    @MockBean
    PoRepository poRepository;

    @MockBean
    StatementOfWorkRepository workRepository;

    @MockBean
    SkillSeekerProjectRepository skillSeekerProjectRepository;


    @MockBean
    SkillOwnerSlotsRepository skillOwnerSlotsRepository;

    @MockBean
    OwnerSkillSetRepository ownerSkillSetRepository;

    @MockBean
    VisaStatusRepository visaStatusRepository;
    @MockBean
    ModelMapper modelMapper;
    @MockBean
    VerificationTokenRepository verificationTokenRepository;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    RegistrationEntity registration = new RegistrationEntity();
    Registration registrationDto = new Registration();

    VisaEntity visaEntity=new VisaEntity();
    SkillOwnerMaritalStatusEntity skillOwnerMaritalStatusEntity = new SkillOwnerMaritalStatusEntity();
    SkillOwnerMaritalStatus skillOwnerMaritalStatus = new SkillOwnerMaritalStatus();
    List<SkillOwnerMaritalStatusEntity> skillOwnerMaritalStatusEntities = new ArrayList<>();
    SkillOwnerGenderEntity skillOwnerGenderEntity = new SkillOwnerGenderEntity();
    List<SkillOwnerGenderEntity> skillOwnerGenderEntities = new ArrayList<>();
    SkillOwnerGender skillOwnerGender = new SkillOwnerGender(0);
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    SkillOwnerDto skillOwnerDto = new SkillOwnerDto();
    List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();
    SkillOwnerDocuments skillOwnerDocuments = new SkillOwnerDocuments();
    List<MultipartFile> multipartFiles = new ArrayList<>();
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    List<SkillOwnerResumeAndImage> skillOwnerResumeAndImageList = new ArrayList<>();
    SkillOwnerResumeAndImage skillOwnerResumeAndImage = new SkillOwnerResumeAndImage();
    SkillOwnerPortfolio skillOwnerPortfolio = new SkillOwnerPortfolio();
    List<SkillOwnerPortfolio> skillOwnerPortfolios = new ArrayList<>();
    SkillOwnerDto ownerDto = new SkillOwnerDto();
    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();

    List<SkillOwnerDocuments> skillOwnerDocumentsList = new ArrayList<>();
    MultipartFile multipartFile;
    VerificationToken verify=new VerificationToken();

    ContractStatus contractStatus=new ContractStatus();

    @MockBean
    private SkillSeekerInterviewStagesRepository skillSeekerInterviewStagesRepository;

    @BeforeEach
    public void setup() {
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setDOB(new Date(1998 - 01 - 23).toLocalDate());
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(67);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(true);
        skillOwnerEntity.setSsn("6543-98-097");
        skillOwnerEntity.setPortfolioUrl(skillOwnerPortfolios);
        skillOwnerEntity.setResumeAvailable(true);
        skillOwnerEntity.setImageAvailable(true);
        skillOwnerEntity.setPermanentState("new york");
        skillOwnerEntity.setPermanentCity("new york");
        skillOwnerEntity.setPermanentAddress("USA");
        skillOwnerEntity.setVisaStartDate(new java.util.Date());
        skillOwnerEntity.setVisaEndDate(new java.util.Date());
        skillOwnerEntity.setVisaStatus(visaEntity);
//        skillOwnerEntity.setStatusVisa("Active");
        skillOwnerEntity.setMaritalStatus(skillOwnerMaritalStatusEntity);
        skillOwnerEntity.setGender(skillOwnerGenderEntity);
        skillOwnerEntity.setAboutMe("testing");
        skillOwnerEntity.setSsn("123456");
        skillOwnerEntity.setExpYears(1);
        skillOwnerEntity.setExpMonths(10);
//        skillOwnerEntity.setUSC(false);
//        skillOwnerEntity.setUsAuthorization(false);
        skillOwnerEntity.setFederalSecurityClearance(false);
        skillOwnerEntity.setStartDate(new java.util.Date());
        skillOwnerEntity.setEndDate(new java.util.Date());
        skillOwnerEntity.setOnBoardingDate(new java.util.Date());
        skillPartnerEntity.setMsaStatusId(contractStatus);
        contractStatus.setId(8);
        skillPartnerEntity.setSkillPartnerId(1);




        skillOwnerEntity.setOwnerSkillStatusEntity(ownerSkillStatusEntity);

        ownerSkillStatusEntity.setSkillOwnerStatusId(1);

        skillOwnerEntities.add(skillOwnerEntity);

        registration.setFirstName(skillOwnerEntity.getFirstName());
        registration.setLastName(skillOwnerEntity.getLastName());
        registration.setContactPhone("9087654321");

        registration.setId(1);
        registration.setMailStatus(MAIL_NOT_SENT );

        skillOwnerMaritalStatus.setStatus("Married");
        skillOwnerMaritalStatus.setId(1);
        skillOwnerMaritalStatusEntities.add(skillOwnerMaritalStatusEntity);

        skillOwnerGenderEntity.setGender("Male");
        skillOwnerGenderEntity.setId(1);
        skillOwnerGenderEntities.add(skillOwnerGenderEntity);

        skillPartnerEntity.setSkillPartnerId(1);
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);

        skillOwnerDto.setSkillOwnerEntityId(1);
        skillOwnerDocuments.setId(1);

        registrationDto.setMailStatus("Mail Send Succesfully");

        skillOwnerResumeAndImage.setOwnerId(1);
        skillOwnerResumeAndImage.setId(1);
        skillOwnerResumeAndImage.setResume(true);
        skillOwnerResumeAndImage.setImage(true);
        skillOwnerResumeAndImage.setFederal(true);
        skillOwnerResumeAndImage.setImageName("image.jpg");
        skillOwnerResumeAndImage.setImageData(new byte[2]);
        skillOwnerResumeAndImage.setImageSize(4567l);
        skillOwnerResumeAndImage.setImageType("image/jpg");
        skillOwnerResumeAndImage.setResumeData(new byte[3]);
        skillOwnerResumeAndImage.setResumeType("application/pdf");
        skillOwnerResumeAndImage.setResumeName("resume.pdf");
        skillOwnerResumeAndImage.setResumeSize(797l);
        skillOwnerResumeAndImage.setFederalData(new byte[2]);
        skillOwnerResumeAndImage.setFederalSize(3456l);
        skillOwnerResumeAndImage.setFederalType("ffc.doc");
        skillOwnerResumeAndImage.setFederalName("");

        skillOwnerResumeAndImageList.add(skillOwnerResumeAndImage);

        skillOwnerPortfolio.setId(1);
        skillOwnerPortfolio.setPortfolioUrls("Google.com");
        skillOwnerPortfolios.add(skillOwnerPortfolio);

        ownerDto.setSkillOwnerEntityId(1);

        ownerDto.setPortfolioUrl(skillOwnerPortfolios);
        ownerDto.setSsn((skillOwnerEntity.getSsn()));
        ownerDto.setPermanentAddress("4th,street");
        ownerDto.setPermanentCity("coimbatore");
        ownerDto.setPermanentState("TN");

        skillOwnerDocumentsList.add(skillOwnerDocuments);
    }

  @Test
    void getSkillOwnerTest() {
        Mockito.when(skillOwnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(skillOwnerEntity));
        Mockito.when(modelMapper.map(skillOwnerEntity, SkillOwnerEntity.class)).thenReturn(skillOwnerEntity);


    }

    @Test
    void insertDataTest() throws IOException {
        Mockito.when(skillPartnerRepository.findByIdAndMsaStatusId(1,17)).thenReturn(Optional.ofNullable(skillPartnerEntity));
        Mockito.when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartnerEntity));
        Mockito.when(skillOwnerRepository.findByPrimaryEmail("abc@gmail.com")).thenReturn(skillOwnerEntity);
        Mockito.when(skillOwnerRepository.findByPhoneNumber("1234567898")).thenReturn(skillOwnerEntity);
        Mockito.when(registrationService.insertDetails(Mockito.any())).thenReturn(registrationDto);
        Mockito.when(registrationRepository.findByEmailIdIgnoreCase(Mockito.any())).thenReturn(registration);
        Mockito.when(verificationTokenRepository.findByToken(Mockito.anyString())).thenReturn(verify);
        Mockito.when(modelMapper.map(registration,Registration.class)).thenReturn(registrationDto);
        when(skillOwnerRepository.findBySkillPartnerId(skillOwnerEntity.getSkillPartnerEntity().getSkillPartnerId())).thenReturn(Optional.ofNullable(skillOwnerEntities));
        Mockito.when(registrationRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(registration));

        //assertEquals(registration, registrationService.insertDetails(registration).getMailStatus());
//        assertEquals(skillOwnerEntity, skillOwnerService.insertData(skillOwnerEntities).get(0));
        assertEquals(1,skillOwnerService.insertData(skillOwnerEntities).size());
    }

    @Test
    void updateOwnerDetailsTest() {
        when(registrationRepository.findByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(registration);
        when(skillOwnerRepository.saveAndFlush(skillOwnerEntity)).thenReturn(skillOwnerEntity);
        assertEquals(skillOwnerEntity, skillOwnerService.updateOwnerDetails(skillOwnerEntity));
    }

    @Test
    void maritalStatusTest() {
        Mockito.when(ownerMaritalStatusRepository.findAll()).thenReturn(skillOwnerMaritalStatusEntities);
        Mockito.when(modelMapper.map(skillOwnerMaritalStatusEntities, SkillOwnerMaritalStatus.class)).thenReturn(skillOwnerMaritalStatus);
        assertEquals(1, skillOwnerService.maritalStatus().size());
        Assertions.assertNotNull(skillOwnerService.maritalStatus());
    }

    @Test
    void getGenderTest() {
        Mockito.when(genderRepository.findAll()).thenReturn(skillOwnerGenderEntities);
        Mockito.when(modelMapper.map(skillOwnerGenderEntities, SkillOwnerGender.class)).thenReturn(skillOwnerGender);
        assertEquals(1, skillOwnerService.genderList().size());
        Assertions.assertNotNull(skillOwnerService.genderList());
    }


    @Test
    void updateOwnerProfileTest() {
        Mockito.when(skillOwnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(skillOwnerEntity));
        Mockito.when(skillOwnerPortfolioRepo.findByPortfolioUrlsAndPortfolio_url_id(Mockito.anyString(), Mockito.anyInt())).thenReturn(skillOwnerPortfolios);
        Mockito.when(skillOwnerPortfolioRepo.saveAndFlush(Mockito.any(SkillOwnerPortfolio.class))).thenReturn(skillOwnerPortfolio);
        Mockito.when(skillOwnerRepository.save(Mockito.any(SkillOwnerEntity.class))).thenReturn(skillOwnerEntity);
        assertEquals(ownerDto.getPortfolioUrl(), ownerDto.getPortfolioUrl());

    }

    @Test
    void insertAttachment() throws IOException {

        Optional<MultipartFile> image = Optional.of(new MockMultipartFile("image", "hello.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes()));
        Optional<MultipartFile> resume = Optional.of(new MockMultipartFile("resume", "welcome.pdf", MediaType.APPLICATION_PDF_VALUE, "welcome, World!".getBytes()));
        List<MultipartFile> documents = Collections.singletonList(new MockMultipartFile("documents", "welcome.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "welcome, World!".getBytes()));
        Optional<MultipartFile> federal = Optional.of(new MockMultipartFile("federal", "welcome.pdf", MediaType.APPLICATION_PDF_VALUE, "welcome, World!".getBytes()));

        when(skillOwnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillOwnerEntity));
        when(skillOwnerResumeAndImageRepository.findByOwnerID(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImageList));
        when(skillOwnerResumeAndImageRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImage));
        when(skillOwnerDocumentRepository.findByOwnerId(Mockito.anyInt())).thenReturn(skillOwnerDocumentsList);

        assertEquals(true, skillOwnerService.insertAttachment(resume, documents, image, federal, skillOwnerEntity.getSkillOwnerEntityId()));
    }

    @Test
    void downloadImageTest() throws IOException {

        when(resumeAndImageRepository.findByOwnerID(1)).thenReturn(Optional.ofNullable(skillOwnerResumeAndImageList));
        assertEquals(200, skillOwnerService.downloadImage(1).getStatusCodeValue());
    }

    @Test
    void downloadResumeTest() throws IOException {

        when(resumeAndImageRepository.findByOwnerID(1)).thenReturn(Optional.ofNullable(skillOwnerResumeAndImageList));
        assertEquals(200, skillOwnerService.downloadResume(1).getStatusCodeValue());
    }

    @Test
    void deleteOtherDocumentsTest() {
        when(skillOwnerDocumentRepository.findByOwnerIdAndCount(skillOwnerEntity.getSkillOwnerEntityId(), 1)).thenReturn(Optional.ofNullable(skillOwnerDocuments));
        skillOwnerService.deleteOtherDocuments(skillOwnerEntity.getSkillOwnerEntityId(), 1);
        verify(skillOwnerDocumentRepository, times(1)).deleteByOwnerIdAndCount(skillOwnerEntity.getSkillOwnerEntityId(), 1);
    }

    @Test
    void deletePortfolioTest() {

        when(skillOwnerPortfolioRepo.findById(1)).thenReturn(Optional.ofNullable(skillOwnerPortfolio));
        skillOwnerService.deletePortfolioUrl(skillOwnerEntity.getSkillOwnerEntityId());
        verify(skillOwnerPortfolioRepo, times(1)).deleteById(skillOwnerEntity.getSkillOwnerEntityId());

    }

    @Test
    void getResumeTest() throws FileNotFoundException {

        when(skillOwnerResumeAndImageRepository.findByOwnerID(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImageList));
        assertEquals(skillOwnerResumeAndImage.getId(), skillOwnerService.getResume(skillOwnerEntity.getSkillOwnerEntityId()).getId());
    }

    @Test
    void getImageTest() throws FileNotFoundException {
        when(skillOwnerResumeAndImageRepository.findByOwnerID(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImageList));
        assertEquals(skillOwnerResumeAndImage.getOwnerId(), skillOwnerService.getImage(skillOwnerEntity.getSkillOwnerEntityId()).getOwnerId());
    }

    @Test
    void storeResumeTest() throws IOException {
        Optional<MultipartFile> resume = Optional.of(new MockMultipartFile("resume", "resume.pdf", MediaType.APPLICATION_PDF_VALUE, "welcom, World!".getBytes()));
        when(skillOwnerResumeAndImageRepository.findByOwnerId(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImage));
        when(skillOwnerResumeAndImageRepository.saveAndFlush(skillOwnerResumeAndImage)).thenReturn(skillOwnerResumeAndImage);
        assertEquals(skillOwnerResumeAndImage.getResumeName(), skillOwnerService.storeResume(resume, skillOwnerEntity.getSkillOwnerEntityId()).getResumeName());
    }

    @Test
    void storeImageTest() throws IOException {

        Optional<MultipartFile> image = Optional.of(new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes()));
        when(skillOwnerResumeAndImageRepository.findByOwnerId(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(Optional.ofNullable(skillOwnerResumeAndImage));
        when(skillOwnerResumeAndImageRepository.saveAndFlush(skillOwnerResumeAndImage)).thenReturn(skillOwnerResumeAndImage);
        assertEquals(skillOwnerResumeAndImage.getImageName(),skillOwnerService.storeImage(image,skillOwnerEntity.getSkillOwnerEntityId()).getImageName());

    }

    @Test
    void getOtherDocumentsTest() {

        when(skillOwnerDocumentRepository.findByOwnerIdAndCount(skillOwnerEntity.getSkillOwnerEntityId(), 1)).thenReturn(Optional.ofNullable(skillOwnerDocuments));
        assertEquals(skillOwnerDocuments.getOwnerId(), skillOwnerService.getOtherDocuments(skillOwnerEntity.getSkillOwnerEntityId(), 1).getOwnerId());

    }

    @Test
    void documentUpdatesTest() throws IOException {
        MultipartFile document = new MockMultipartFile("resume", "welcome.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "welcom, World!".getBytes());
        when(skillOwnerDocumentRepository.findByOwnerIdAndCount(skillOwnerEntity.getSkillOwnerEntityId(), 1)).thenReturn(Optional.ofNullable(skillOwnerDocuments));
        assertNull(skillOwnerService.documentUpdates(document, skillOwnerEntity.getSkillOwnerEntityId(), 1));
    }

}