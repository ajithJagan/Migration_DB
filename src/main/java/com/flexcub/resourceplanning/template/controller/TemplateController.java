package com.flexcub.resourceplanning.template.controller;

import com.flexcub.resourceplanning.template.TemplateService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/template")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Server Error"), @ApiResponse(responseCode = "400", description = "Bad Request"), @ApiResponse(responseCode = "400", description = "Bad Request")})
public class TemplateController {
    @Autowired
    TemplateService templateService;

    Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @PostMapping(value = "/uploadAgreementTemplate", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<TemplateTable>> uploadAgreementTemplate(@RequestParam(value = "document") List<MultipartFile> multipartFiles, @RequestParam String templateType) throws IOException {
        logger.info("TemplateController|| uploadAgreementTemplate ||upload AgreementTemplate called ");
        return new ResponseEntity<>(templateService.uploadAgreementTemplate(multipartFiles, templateType), HttpStatus.OK);
    }

    @GetMapping(value = "/getSkillPartnerMsaTemplate", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> getSkillPartnerMsaTemplate(@RequestParam int partnerId,@RequestParam String templateType) throws IOException {
        logger.info("TemplateController|| downloadTemplate || downloading Msa Template for SkillPartner");
        return templateService.getSkillPartnerMsaTemplateDownload(partnerId,templateType);
    }
}
