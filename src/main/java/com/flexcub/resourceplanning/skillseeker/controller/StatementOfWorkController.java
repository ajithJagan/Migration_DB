package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillseeker.dto.SowStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWork;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWorkGetDetails;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
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

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.INVALID_SOW_ID;

@RestController
@RequestMapping(value = "/v1/statementOfWorkController")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
public class StatementOfWorkController {

    Logger logger = LoggerFactory.getLogger(StatementOfWorkController.class);
    @Autowired
    StatementOfWorkService statementOfWorkService;
    @Value("${flexcub.downloadURLSOW}")
    String downloadURLSOW;
    @Autowired
    ContractFileRepository contractFileRepository;


//    @PostMapping(value = "/uploadSOW", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<StatementOfWork> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestPart String sowRequestDto) throws Exception {
//        logger.info("StatementOfWorkController|| uploadFile || upload File called");
//        return new ResponseEntity<>(statementOfWorkService.addDocument(multipartFile, sowRequestDto), HttpStatus.OK);
//    }

//    @PutMapping(value = "/updateSOW", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<StatementOfWork> updateFile(@RequestParam("multipart")MultipartFile multipartFile, @RequestPart String sowRequestDto) throws Exception {
//        logger.info("StatementOfWorkController|| updateFile || updating The File");
//        return new ResponseEntity<>(statementOfWorkService.upDateSow(multipartFile,sowRequestDto), HttpStatus.OK);
//    }

//    @GetMapping(value = "/getSowDetails")
//    public ResponseEntity<List<StatementOfWorkGetDetails>> getSowDetails(@RequestParam int skillSeekerId) {
//        logger.info("StatementOfWorkController|| getSowDetails || getSowDetails");
//        return new ResponseEntity<>(statementOfWorkService.getSowDetails(skillSeekerId), HttpStatus.OK);
//    }

//    @GetMapping(value = "/getAllSowDetails", produces = {"application/json"})
//    public ResponseEntity<List<StatementOfWorkGetDetails>> getAllSowDetails() {
//        logger.info("StatementOfWorkController|| getAllSowDetails || get AllSowDetails");
//        return new ResponseEntity<>(statementOfWorkService.getAllSowDetails(), HttpStatus.OK);
//    }

//    @PutMapping(value = "/updateSowStatus", produces = {"application/json"})
//    public ResponseEntity<SowStatusDto> updateSowStatus(@RequestParam int sowId, @RequestParam int sowStatusId) {
//        logger.info("StatementOfWorkController|| updateSowStatus || updateSowStatus called");
//        return new ResponseEntity<>(statementOfWorkService.updateSowStatus(sowId, sowStatusId), HttpStatus.OK);
//    }

    @GetMapping(value = "/downloadOwnerAgreementForSow", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Resource> downloadAgreementSow(@RequestParam int id) {
        Optional<ContractFiles> contractFiles = contractFileRepository.findById(id);
        Optional<StatementOfWorkEntity> statementOfWorkEntity = Optional.of(this.statementOfWorkService.downloadAgreementSOW(id));

        if (statementOfWorkEntity.isPresent()) {

            String file = String.valueOf(contractFiles.get().getFileName());
            file = file.substring(file.indexOf("."));
            logger.info("StatementOfWorkController|| downloadAgreementSow || downloadAgreementSow called");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contractFiles.get().getMimeType()))
                    .header("Content-disposition", "attachment; filename=" + statementOfWorkEntity.get().getSkillOwnerEntity().getFirstName() + "_SOW" + file)
                    .body(new ByteArrayResource(contractFiles.get().getData()));
        } else {
            throw new ServiceException(INVALID_SOW_ID.getErrorCode(), INVALID_SOW_ID.getErrorDesc());
        }
    }

    @GetMapping(value = "/downloadAgreementForSow", produces = {"application/json"})
    public FileResponse downloadOwnerAgreement(int id) throws FileNotFoundException {
        Optional<ContractFiles> contractFiles = contractFileRepository.findById(id);
        StatementOfWorkEntity sow = statementOfWorkService.getSow(contractFiles.get().getId());
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLSOW + id).
                toUriString();
        String file = String.valueOf(contractFiles.get().getFileName());
        file = file.substring(file.indexOf("."));
        logger.info("StatementOfWorkController|| downloadAgreementForSow || /downloadAgreementForSow response URL for download");
        return new FileResponse(sow.getSkillOwnerEntity().getFirstName() + "_SOW" + file, fileDownloadUri, contractFiles.get().getMimeType(), contractFiles.get().getSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/getSowTemplate", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Resource> getSowTemplate() throws IOException {
        logger.info("StatementOfWorkController|| getSowTemplate || download SOW Template");
        return statementOfWorkService.templateDownload();
    }

}
