package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerProjectService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = SeekerProjectController.class)
class SeekerProjectControllerTest {

    @Autowired
    SeekerProjectController seekerProjectController;

    @MockBean
    SkillSeekerProjectService seekerProject;

    SkillSeekerProject skillSeekerProjectTest = new SkillSeekerProject();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    List<SkillSeekerTechnologyData> seekerRateCardList = new ArrayList<>();
    List<SkillSeekerProject> skillSeekerProjectTests = new ArrayList<SkillSeekerProject>();

    @BeforeEach
    void beforeTest() {
        skillSeekerProjectTest.setId(1);
        skillSeekerProjectTest.setTitle("SriRam");
        skillSeekerProjectTest.setPrimaryContactEmail("ajith.j@qbrainx.com");
        skillSeekerProjectTest.setSecondaryContactPhone("9786363526");
        skillSeekerProjectTest.setSummary("good morning");
        skillSeekerProjectTest.setSecondaryContactEmail("sriram.k@qbrainx.com");
        skillSeekerProjectTest.setSecondaryContactPhone("8523697562");
        skillSeekerProjectTest.setOwnerSkillDomainEntity(ownerSkillDomainEntity);

        skillSeekerProjectTests.add(skillSeekerProjectTest);
    }

    @Test
    void addSeekerProjectDetailsTest() {

        Mockito.when(seekerProject.insertData(skillSeekerProjectTests)).thenReturn(skillSeekerProjectTests);
        assertEquals(200, seekerProjectController.insertSeekerProjectDetails(skillSeekerProjectTests).getStatusCodeValue());
    }


    @Test
    void updateSeekerProjectDetailsTest() {
        Mockito.when(seekerProject.updateData(skillSeekerProjectTest)).thenReturn(skillSeekerProjectTest);
        assertEquals(200, seekerProjectController.updateSeekerProjectDetails(skillSeekerProjectTest).getStatusCodeValue());
    }

    @Test
    void deleteSeekerProjectDetailsTest() throws Exception {
        seekerProjectController.deleteSeekerProjectDetails(1);
        seekerProjectController.deleteSeekerProjectDetails(2);
        Mockito.verify(seekerProject, times(2)).deleteData(Mockito.anyInt());
    }

    @Test
    void seekerProjectDetailsTest() {
        Mockito.when(seekerProject.getProjectData(Mockito.anyInt())).thenReturn(skillSeekerProjectTests);
        assertEquals(200, seekerProjectController.seekerProjectDetails(Mockito.anyInt()).getStatusCodeValue());
    }
}