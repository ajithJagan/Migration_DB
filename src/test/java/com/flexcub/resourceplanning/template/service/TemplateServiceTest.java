package com.flexcub.resourceplanning.template.service;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.template.TemplateService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TemplateServiceImpl.class)
class TemplateServiceTest {

    @MockBean
    TemplateRepository templateRepository;
    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @MockBean
    ContractStatusRepository contractStatusRepository;

    @Autowired
    TemplateService templateService;
    TemplateTable templateTable = new TemplateTable();
    List<TemplateTable> templateTableList = new ArrayList<>();
    HashMap<String, String> fileTypeList = new HashMap<>();
    FileDB fileDB = new FileDB();
    List<MultipartFile> multipartFiles = new ArrayList<>();
    SkillPartnerEntity skillPartner = new SkillPartnerEntity();
    ContractFiles contractFiles = new ContractFiles();
    ContractStatus contractStatus = new ContractStatus();


    @BeforeEach
    public void setup() throws IOException {

        templateTable.setId(1L);
        templateTable.setSize(4L);
        templateTable.setTemplateName("SkillPArtner MSA Template.docx");
        templateTable.setTemplateMimeType("MSA/docx");
        templateTable.setTemplateVersion(1);
        templateTable.setTemplateType("PARTNER_MSA_TEMPLATE");
        templateTable.setData(new byte[3]);

        templateTableList.add(templateTable);

        fileDB.setSkillPartnerId("1");
        fileDB.setId(1);
        fileDB.setName("application.pdf");
        fileDB.setType("application/pdf");
        fileDB.setData(new byte[1]);
        fileDB.setSynced(false);
        MultipartFile multipartFile = new MockMultipartFile(fileDB.getName(), fileDB.getName(), "application/pdf", fileDB.getName().getBytes());
        multipartFiles.add(multipartFile);

        skillPartner.setSkillPartnerId(1);
        skillPartner.setMsaStartDate(LocalDate.parse("2023-05-12"));
        skillPartner.setMsaEndDate(LocalDate.parse("2023-10-12"));
        skillPartner.setBusinessName("QBX");
        skillPartner.setMsaId(contractFiles);
        skillPartner.setMsaStatusId(contractStatus);
        contractStatus.setId(1);

        contractFiles.setId(1);
        contractFiles.setFileName("application.pdf");
        contractFiles.setMimeType("application/pdf");
        contractFiles.setData(new byte[1]);
        contractFiles.setSize(55667);

    }

    @Test
    void getSkillPartnerMsaTemplateDownload() throws IOException {
        when(templateRepository.findByTemplateFile( "PARTNER_MSA_TEMPLATE")).thenReturn(templateTableList);
        when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartner));
        assertEquals(200, templateService.getSkillPartnerMsaTemplateDownload(1, "PARTNER_MSA_TEMPLATE" ).getStatusCodeValue());
    }

    @Test
    void uploadAgreementTemplate() throws IOException {
        when(templateRepository.findByTemplateFile(Mockito.anyString())).thenReturn(templateTableList);
        when(templateRepository.save(Mockito.any())).thenReturn(templateTable);
        assertEquals(templateTable.getTemplateType(), templateService.uploadAgreementTemplate(multipartFiles, "PARTNER_MSA_TEMPLATE").get(0).getTemplateType());
    }


}
