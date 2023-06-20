package com.flexcub.resourceplanning.template;

import com.flexcub.resourceplanning.template.entity.TemplateTable;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TemplateService {

    List<TemplateTable> uploadAgreementTemplate(List<MultipartFile> multipartFiles, String templateType) throws IOException;

    ResponseEntity<Resource> getSkillPartnerMsaTemplateDownload(int partnerId,String templateType) throws IOException;
}
