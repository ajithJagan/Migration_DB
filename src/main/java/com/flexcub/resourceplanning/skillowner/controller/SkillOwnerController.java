package com.flexcub.resourceplanning.skillowner.controller;


import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerGender;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerMaritalStatus;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerDocuments;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerDocumentRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.utils.FlexcubConstants;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_FOUND;

@RestController
@RequestMapping(value = FlexcubConstants.API)
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})

public class SkillOwnerController {

    @Autowired
    SkillOwnerService skillOwnerService;
    Logger logger = LoggerFactory.getLogger(SkillOwnerController.class);
    @Value("${flexcub.downloadURLOwnerResume}")
    private String downloadURLOwnerResume;

    @Value("${flexcub.downloadURLOwnerFederal}")
    private String downloadURLOwnerFederal;

    @Value("${flexcub.downloadURLOwnerImage}")
    private String downloadURLOwnerImage;
    @Value("${flexcub.downloadURLOwnerOtherDocument}")
    private String downloadURLOwnerOtherDocument;
    @Autowired
    private SkillOwnerDocumentRepository skillOwnerDocumentRepository;

    @GetMapping(value = "/getBySkillOwnerId", produces = {"application/json"})
    public ResponseEntity<SkillOwnerDto> getById(@RequestParam int id) {

        try {
            logger.info("SkillOwnerController || getById || Getting the Id from the SkillOwnerEntity");
            return new ResponseEntity<>(skillOwnerService.getSkillOwner(id), HttpStatus.OK);
        } catch (ServiceException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid input");
        }
    }


    @PostMapping(value = "/insertData", produces = {"application/json"})
    public ResponseEntity<List<SkillOwnerEntity>> insertDetails(@Valid @RequestBody List<SkillOwnerEntity> skillOwnerEntity) throws IOException {
        List<SkillOwnerEntity> skillOwnerEntityList = skillOwnerService.insertData(skillOwnerEntity);
        logger.info("SkillOwnerController || insertDetails || Inserting the Details for SkillOwnerEntity");
        return new ResponseEntity<>(skillOwnerEntityList, HttpStatus.OK);
    }

    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<SkillOwnerEntity> updateOwnerDetails(@RequestBody SkillOwnerEntity skillOwnerEntity) {
        logger.info("SkillOwnerController || updateOwnerDetails || Updating the Details from the SkillOwnerEntity");
        return new ResponseEntity<>(skillOwnerService.updateOwnerDetails(skillOwnerEntity), HttpStatus.OK);
    }

    @GetMapping(value = "/gender", produces = {"application/json"})
    public ResponseEntity<List<SkillOwnerGender>> getGenderList() {
        logger.info("SkillOwnerController || genderList || Getting the genderList ");
        return new ResponseEntity<>(skillOwnerService.genderList(), HttpStatus.OK);
    }

    @GetMapping(value = "/maritalStatus", produces = {"application/json"})
    public ResponseEntity<List<SkillOwnerMaritalStatus>> maritalStatusList() {
        logger.info("SkillOwnerController || maritalStatusList || Getting the maritalStatusList ");
        return new ResponseEntity<>(skillOwnerService.maritalStatus(), HttpStatus.OK);
    }

    @PutMapping(value = "/updateOwnerProfile", produces = {"application/json"})
    public ResponseEntity<SkillOwnerDto> updateOwnerProfile(@RequestBody SkillOwnerEntity skillOwnerEntity) {
        logger.info("SkillOwnerController || updateOwnerProfile || updating list");
        return new ResponseEntity<>(skillOwnerService.updateOwnerProfile(skillOwnerEntity), HttpStatus.OK);
    }


    @PutMapping(value = "/insertAttachment", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> insertAttachment(@RequestParam("resume") Optional<MultipartFile> resume, @RequestParam(value = "document", required = false) List<MultipartFile> documents,
                                                   @RequestParam("image") Optional<MultipartFile> image,
                                                   @RequestParam("federal") Optional<MultipartFile> federal, @RequestParam("id") int skillOwnerID) throws IOException {
        logger.info("SkillOwnerController || insertAttachment || Inserting attachments");
        skillOwnerService.insertAttachment(resume, documents, image, federal, skillOwnerID);
        return new ResponseEntity<>("Attachment Inserted", HttpStatus.OK);

    }

    @PostMapping(value = "/documentUpdates", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> insertAttachment(@RequestParam(value = "document", required = false) MultipartFile document,
                                                   @RequestParam("ownerId") int ownerId,
                                                   @RequestParam("id") int count) throws IOException {
        logger.info("SkillOwnerController || documentUpdates || update's particular ownerId and count ");
        skillOwnerService.documentUpdates(document, ownerId, count);
        return new ResponseEntity<>("Inserted Successfully", HttpStatus.OK);

    }

    @GetMapping(value = "/fileDownloadImage", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> downloadImage(int id) throws IOException {
        logger.info("SkillOwnerController|| downloadTemplate || /downloadTemplate Excel called");
        return skillOwnerService.downloadImage(id);
    }

    @GetMapping(value = "/fileDownloadFederal", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> fileDownloadFederal(int id) throws IOException {
        logger.info("SkillOwnerController|| fileDownloadFederal || /fileDownloadFederal called");
        return skillOwnerService.fileDownloadFederal(id);
    }

    @GetMapping(value = "/fileDownloadResume", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> fileDownloadResume(int id) throws IOException {
        logger.info("SkillOwnerController|| downloadTemplate || /downloadTemplate Excel called");
        return skillOwnerService.downloadResume(id);

    }

    @GetMapping(value = "/downloadResume", produces = {"application/json"})
    public FileResponse downloadResume(int ownerId) throws FileNotFoundException {
        SkillOwnerResumeAndImage resume = skillOwnerService.getResume(ownerId);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLOwnerResume + ownerId).
                toUriString();
        logger.info("SkillOwnerController|| fileDownloadResume || /fileDownloadResume response URL for download");
        return new FileResponse(resume.getResumeName(), fileDownloadUri, resume.getResumeType(), resume.getResumeSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/downloadFederal", produces = {"application/json"})
    public FileResponse downloadFederal(int ownerId) throws FileNotFoundException {
        SkillOwnerResumeAndImage federal = skillOwnerService.getFederal(ownerId);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLOwnerFederal + ownerId).
                toUriString();
        logger.info("SkillOwnerController|| fileDownloadResume || /fileDownloadResume response URL for download");
        return new FileResponse(federal.getFederalName(), fileDownloadUri, federal.getFederalType(), federal.getFederalSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/downloadImage", produces = {"application/json"})
    public FileResponse downloadImageFile(int ownerId) throws FileNotFoundException {
        SkillOwnerResumeAndImage image = skillOwnerService.getImage(ownerId);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLOwnerImage + ownerId).
                toUriString();
        logger.info("SkillOwnerController|| fileDownloadImage || /fileDownloadImage response URL for download");
        return new FileResponse(image.getImageName(), fileDownloadUri, image.getImageType(), image.getImageSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/OtherDocuments", produces = {"application/json"})
    public FileResponse otherFilesDownload(int ownerId, int count) throws FileNotFoundException {
        SkillOwnerDocuments otherDocuments = skillOwnerService.getOtherDocuments(ownerId, count);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLOwnerOtherDocument + ownerId + "&count=" + count).
                toUriString();
        logger.info("SkillOwnerController|| otherFilesDownload || /otherFilesDownload response URL for download");

        return new FileResponse(otherDocuments.getName(), fileDownloadUri, otherDocuments.getType(), otherDocuments.getSize(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/otherFilesDownload", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> downloadOtherDocuments(int ownerId, int count) {
        logger.info("FileReadingController|| downloadTemplate || /download other documents called");
        return skillOwnerService.downloadOtherDocuments(ownerId, count);
    }

    @DeleteMapping(value = "/deletePortfolio")
    public void deletePortfolio(@RequestParam int id) {
        logger.info("SkillOwnerController || deletePortfolio || Deleting the Details from the Portfolio");
        skillOwnerService.deletePortfolioUrl(id);
    }

    @DeleteMapping(value = "/deleteOtherDocuments")
    public void deletePortfolio(@RequestParam int id, int count) {
        logger.info("SkillOwnerController || deleteOtherDocuments || Deleting the Details from the other documents");
        skillOwnerService.deleteOtherDocuments(id, count);
    }


}