package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerDocuments;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerDocumentRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.SkillOwnerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillOwnerController.class)
class SkillOwnerControllerTest {


    @Autowired
    SkillOwnerController skillOwnerController;

    @MockBean
    SkillOwnerServiceImpl skillOwnerService;

    @MockBean
    SkillOwnerDocumentRepository documentRepository;


    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    SkillOwnerDto ownerDto = new SkillOwnerDto();

    List<SkillOwnerEntity> skillOwnerEntitys = new ArrayList<SkillOwnerEntity>();

    List<MultipartFile> multipartFiles = new ArrayList<>();
     MultipartFile multipartFile = null;

    ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    SkillOwnerResumeAndImage skillOwnerResumeAndImage = new SkillOwnerResumeAndImage();

    SkillOwnerDocuments skillOwnerDocuments = new SkillOwnerDocuments();
//    FileResponse fileResponse = new FileResponse();

    @BeforeEach
    public void setup() {
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(45);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(true);

        ownerDto.setSkillOwnerEntityId(1);
        ownerDto.setAddress("Salem");
        ownerDto.setCity("Salem");
        ownerDto.setFirstName("Ajith");
        ownerDto.setLastName("Kumar");
        ownerDto.setLinkedIn("linkdn");
        ownerDto.setPhoneNumber("9087654321");
        ownerDto.setPrimaryEmail("ajithashok2530@gmail.com");
        ownerDto.setRateCard(45);
        ownerDto.setState("Tamilnadu");
        ownerDto.setAccountStatus(true);

//        fileResponse.setFileType("application/msword");
//        fileResponse.setFileName("New DOC Document.doc");
//        fileResponse.setFileDownloadUri("http://localhost:8080/resource-planning/api/v1/fileDownloadResume?id=13");
//        fileResponse.setSize(9216);

        skillOwnerResumeAndImage.setOwnerId(1);
        skillOwnerResumeAndImage.setId(1);

        skillOwnerDocuments.setCount(1);
        skillOwnerDocuments.setId(1);
    }


    @Test
    void getByIdTest() {
        skillOwnerController.getById(1);
        Mockito.verify(skillOwnerService, times(1)).getSkillOwner(Mockito.anyInt());
    }

    @Test
    void insertDetailsTest() throws IOException {

        Mockito.when((skillOwnerService.insertData(skillOwnerEntitys))).thenReturn(skillOwnerEntitys);
        assertEquals(200, skillOwnerController.insertDetails(skillOwnerEntitys).getStatusCodeValue());

    }

    @Test
    void updateOwnerDetailsTest() {
        Mockito.when(skillOwnerService.updateOwnerDetails(skillOwnerEntity)).thenReturn(skillOwnerEntity);
        assertEquals(200, skillOwnerController.updateOwnerDetails(skillOwnerEntity).getStatusCodeValue());
    }


    @Test
    void getGenderListTest() {
        when(skillOwnerService.genderList()).thenReturn(Collections.emptyList());
        assertEquals(200, skillOwnerController.getGenderList().getStatusCodeValue());
    }

    @Test
    void maritalStatusListTest() {
        when(skillOwnerService.maritalStatus()).thenReturn(Collections.emptyList());
        assertEquals(200, skillOwnerController.maritalStatusList().getStatusCodeValue());
    }


    @Test
    void updateOwnerProfileTest() {
        when(skillOwnerService.updateOwnerProfile(skillOwnerEntity)).thenReturn(ownerDto);
        assertEquals(200, skillOwnerController.updateOwnerProfile(skillOwnerEntity).getStatusCodeValue());
    }

    @Test
    void insertAttachmentTest() throws IOException {
        Optional<MultipartFile> image = Optional.of(new MockMultipartFile("image", "hello.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes()));
        Optional<MultipartFile> resume = Optional.of(new MockMultipartFile("resume", "welcome.pdf", MediaType.APPLICATION_PDF_VALUE, "welcome, World!".getBytes()));
        List<MultipartFile> documents = Collections.singletonList(new MockMultipartFile("documents", "welcome.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "welcome, World!".getBytes()));
        Optional<MultipartFile> federal = Optional.of(new MockMultipartFile("federal", "welcome.pdf", MediaType.APPLICATION_PDF_VALUE, "welcome, World!".getBytes()));
        Mockito.when(skillOwnerService.insertAttachment(resume,documents,image,federal,1)).thenReturn(true);
        assertEquals(200, skillOwnerController.insertAttachment(resume,documents,image,federal,1).getStatusCodeValue());
    }

    @Test
    void insertAttachment() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "hello.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "Hello, World!".getBytes());
        Mockito.when(skillOwnerService.documentUpdates(image, 1, 1)).thenReturn(skillOwnerDocuments);
        assertEquals(200,skillOwnerController.insertAttachment(image,1,1).getStatusCodeValue());
    }

    @Test
    void downloadImageTest() throws IOException {

        Mockito.when(skillOwnerService.downloadImage(1)).thenReturn(resourceResponseEntity);
        assertThat(skillOwnerController.downloadImage(1).getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void downloadResumeTest() throws IOException {

        Mockito.when(skillOwnerService.downloadResume(1)).thenReturn(resourceResponseEntity);
        assertThat(skillOwnerController.fileDownloadResume(1).getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void downloadResumeFileTest() throws FileNotFoundException {
        Mockito.when(skillOwnerService.getResume(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(skillOwnerResumeAndImage);
        assertThat(skillOwnerController.downloadResume(1).getSize());
    }

    @Test
    void deletePortfolioTest() {
        skillOwnerController.deletePortfolio(1);
        Mockito.verify(skillOwnerService, times(1)).deletePortfolioUrl(Mockito.anyInt());
    }

    @Test
    void deleteOtherDocumentsTest() {
        skillOwnerService.deleteOtherDocuments(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(skillOwnerService, times(1)).deleteOtherDocuments(Mockito.anyInt(), Mockito.anyInt());

    }


    @Test
    void downloadImageFileTest() throws FileNotFoundException {
        Mockito.when(skillOwnerService.getImage(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(skillOwnerResumeAndImage);
        assertThat(skillOwnerController.downloadImageFile(1).getSize());
    }

    @Test
    void otherFilesDownloadTest() throws FileNotFoundException {
        Mockito.when(skillOwnerService.getOtherDocuments(skillOwnerEntity.getSkillOwnerEntityId(), 1)).thenReturn(skillOwnerDocuments);
        assertThat(skillOwnerController.otherFilesDownload(1, 1).getSize());
    }


}
