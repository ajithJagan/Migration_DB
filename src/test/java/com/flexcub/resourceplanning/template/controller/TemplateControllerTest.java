package com.flexcub.resourceplanning.template.controller;

import com.flexcub.resourceplanning.template.TemplateService;
import com.flexcub.resourceplanning.template.entity.TemplateTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TemplateController.class)
class TemplateControllerTest {

    @MockBean
    TemplateService templateService;

    @Autowired
    TemplateController templateController;

    ResponseEntity<Resource> resourceResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    TemplateTable templateTable = new TemplateTable();
    List<TemplateTable> templateTableList = new ArrayList<>();

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
    }


    @Test
    void getSkillPartnerMsaTemplateTest() throws IOException {
        Mockito.when(templateService.getSkillPartnerMsaTemplateDownload(Mockito.anyInt(), Mockito.anyString())).thenReturn(resourceResponseEntity);
        assertEquals(200, templateController.getSkillPartnerMsaTemplate(Mockito.anyInt(), Mockito.anyString()).getStatusCodeValue());
    }

    @Test
    void uploadAgreementTemplateTest() throws IOException {
        Mockito.when(templateService.uploadAgreementTemplate(Mockito.anyList(), Mockito.anyString())).thenReturn(templateTableList);
        assertEquals(200, templateController.uploadAgreementTemplate(Mockito.anyList(), Mockito.anyString()).getStatusCodeValue());
    }
}
