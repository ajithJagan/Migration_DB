package com.flexcub.resourceplanning.template.service;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.template.TemplateService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.INVALID_FILE;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.MSA_TEMPLATE_NOT_FOUND;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateRepository templateRepository;

    Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    @Autowired
    SkillPartnerRepository skillPartnerRepository;
    @Autowired
    ContractStatusRepository contractStatusRepository;


    @Transactional
    @Override
    public List<TemplateTable> uploadAgreementTemplate(List<MultipartFile> multipartFiles, String templateType) throws IOException {
        try {
            HashMap<String, String> fileTypeList = new HashMap<>();
            List<TemplateTable> templateList = new ArrayList<>();
            TemplateTable templateTable = new TemplateTable();
            fileTypeList.put(PDF, APPLICATION_VND_PDF);
            fileTypeList.put(DOC, TEXT_DOC);
            fileTypeList.put(DOCX, TEXT_DOCX);
            if (!multipartFiles.isEmpty()) {
                for (MultipartFile multipartFile : multipartFiles) {
                    if (fileTypeList.containsValue(multipartFile.getContentType())) {
                        List<TemplateTable> bySkillPartnerMSAFile = templateRepository.findByTemplateFile(templateType);
                        if (bySkillPartnerMSAFile.isEmpty()) {
                            templateTable.setTemplateVersion(1);
                        } else {
                            templateTable.setTemplateVersion(bySkillPartnerMSAFile.get(0).getTemplateVersion() + 1);
                        }
                        templateTable.setTemplateName(multipartFile.getOriginalFilename());
                        templateTable.setTemplateMimeType(multipartFile.getContentType());
                        templateTable.setSize(multipartFile.getSize());
                        templateTable.setData(multipartFile.getBytes());
                        templateTable.setTemplateType(templateType);
                        templateList.add(templateTable);
                        templateRepository.save(templateTable);
                        logger.info("TemplateServiceImpl || uploadAgreementTemplate ||Template inserted successfully ! ");
                    }
                }
            }
            return templateList;
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_FILE.getErrorCode(), INVALID_FILE.getErrorDesc());
        }

    }


    @Transactional
    @Override
    public ResponseEntity<Resource> getSkillPartnerMsaTemplateDownload(int partnerId,String templateType) throws IOException {
        List<TemplateTable> downloadTemplate = templateRepository.findByTemplateFile(templateType);

        ByteArrayResource resource;
        if (!downloadTemplate.isEmpty()) {
            Optional<SkillPartnerEntity> skillPartner=skillPartnerRepository.findById(partnerId);
            skillPartner.get().setMsaStatusId(contractStatusRepository.findByStatusId(14));
            resource = new ByteArrayResource(downloadTemplate.get(0).getData());
        } else {
            throw new ServiceException(MSA_TEMPLATE_NOT_FOUND.getErrorCode(), MSA_TEMPLATE_NOT_FOUND.getErrorDesc());
        }
        logger.info("TemplateServiceImpl || getSkillPartnerMsaTemplateDownload || SkillPartnerMsaTemplateDownload");
        return ResponseEntity.ok().header("Content-disposition", "attachment; filename=" + templateType).
                contentType(MediaType.valueOf(downloadTemplate.get(0).getTemplateMimeType())).body(resource);

    }
}

