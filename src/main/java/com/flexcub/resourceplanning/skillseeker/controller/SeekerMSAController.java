package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillseeker.dto.MsaStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerMsaDto;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerMSAService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdminMsa;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.INVALID_MSA_ID;

@RestController
@RequestMapping(value = "/v1/skillSeekerMSAController")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Server Error"), @ApiResponse(responseCode = "400", description = "Bad Request"), @ApiResponse(responseCode = "400", description = "Bad Request")})

public class SeekerMSAController {

    Logger logger = LoggerFactory.getLogger(SeekerMSAController.class);

    @Autowired
    private SkillSeekerMSAService skillSeekerMSAService;

    @Value("${flexcub.downloadURLMSA}")
    private String downloadURLMSA;

    @GetMapping(value = "/getMsaDetails", produces = {"application/json"})
    public ResponseEntity<List<SeekerAdminMsa>> getMsaDetails() {
        logger.info("SkillSeekerMSAController|| getMsaDetails || getting Msa Details");
        return new ResponseEntity<>(skillSeekerMSAService.getMsaDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMsaDetailsBySeeker", produces = {"application/json"})
    public ResponseEntity<List<SeekerAdminMsa>> getMsaDetailsBySeeker(int skillSeekerId) {
        logger.info("SkillSeekerMSAController|| getMsaDetails || getting Msa Details By Seeker");
        return new ResponseEntity<>(skillSeekerMSAService.getMsaDetailsBySeeker(skillSeekerId), HttpStatus.OK);
    }

    @PostMapping(value = "/uploadMSA", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SkillSeekerMsaDto> uploadFile(@RequestParam(value = "document") List<MultipartFile> multipartFile, @RequestParam("skillSeekerId") int seekerId, @RequestParam("skillSeekerProjectId") int projectID, @RequestParam("jobId") String jobId, @RequestParam("ownerId") int ownerId) throws Exception {
        logger.info("SkillSeekerMSAController|| uploadFile || upload File called");
        return new ResponseEntity<>(skillSeekerMSAService.addDocuments(multipartFile, seekerId, projectID, jobId, ownerId), HttpStatus.OK);
    }

    @GetMapping(value = "/downloadOwnerAgreement", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> downloadAgreement(int id) throws IOException {
        try {
            Optional<SkillSeekerMSAEntity> seekerMSAEntity = this.skillSeekerMSAService.downloadAgreement(id);
            if (seekerMSAEntity.isPresent()) {
                String file = String.valueOf(seekerMSAEntity.get().getName());
                file = file.substring(file.indexOf("."));
                logger.info("SkillSeekerMSAController|| downloadAgreement || downloadTemplate called");
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(seekerMSAEntity.get().getMimeType())).header("Content-disposition",
                                "attachment; filename=" + seekerMSAEntity.get().getSkillOwnerEntity().getFirstName()+"_MSA"+file)
                        .body(new ByteArrayResource(seekerMSAEntity.get().getData()));
            } else {
                throw new ServiceException(INVALID_MSA_ID.getErrorCode(), INVALID_MSA_ID.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_MSA_ID.getErrorCode(), INVALID_MSA_ID.getErrorDesc());
        }
    }

    @GetMapping(value = "/downloadAgreement", produces = {"application/json"})
    public FileResponse downloadOwnerAgreement(int id) throws FileNotFoundException {
        SkillSeekerMSAEntity msa = skillSeekerMSAService.getMSA(id);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLMSA + id).
                toUriString();
        String file = String.valueOf(msa.getName());
        file = file.substring(file.indexOf("."));
        logger.info("SkillSeekerMSAController|| downloadAgreement || /downloadAgreement response URL for download");
        return new FileResponse(msa.getSkillOwnerEntity().getFirstName()+"_MSA"+file, fileDownloadUri, msa.getMimeType(), msa.getSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/getSkillSeekerMsaTemplate", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Resource> getSkillSeekerMsaTemplate() throws IOException {
        logger.info("SkillSeekerMSAController|| downloadTemplate || download Msa Template");
        return skillSeekerMSAService.getSkillSeekerMsaTemplateDownload();
    }

    @GetMapping(value = "/contractStatus", produces = {"application/json"})
    public ResponseEntity<List<ContractStatus>> getContractStatus() {
        logger.info("Invoice Controller || getInvoiceStatus ||getting all invoice status ");
        return new ResponseEntity<>(skillSeekerMSAService.getMsaStatus(), HttpStatus.OK);
    }

    @PutMapping(value = "/msaStatusUpdate", produces = {"application/json"})
    public ResponseEntity<MsaStatusDto> updateMsaStatus(@RequestParam int msaId, @RequestParam int msaStatusId) {
        logger.info("SkillSeekerMSAController|| msaStatusUpdate || update msa status");
        return new ResponseEntity<>(skillSeekerMSAService.updateMsaStatus(msaId, msaStatusId), HttpStatus.OK);
    }
}
