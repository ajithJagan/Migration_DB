package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTechnologyService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTechnology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = SeekerTechnologyController.class)
class SeekerTechnologyControllerTest {

    @Autowired
    SeekerTechnologyController seekerTechnologyController;
    @MockBean
    SkillSeekerTechnologyService seekerTechnologyService;

    SkillSeekerTechnologyData skillSeekerRateCardTest = new SkillSeekerTechnologyData();
    SkillSeekerTechnology skillSeekerTechnology = new SkillSeekerTechnology();
    List<SkillSeekerTechnology> skillSeekerTechnologies = new ArrayList<>();
    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();
    OwnerSkillRolesEntity ownerSkillRolesEntity = new OwnerSkillRolesEntity();
    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();

    @BeforeEach
    void beforeTest() {
        skillSeekerRateCardTest.setId(1);
        skillSeekerRateCardTest.setMaxRate(450);
        skillSeekerRateCardTest.setBaseRate(50);
        ownerSkillLevelEntity.setSkillLevelDescription("Intermediate");
        ownerSkillLevelEntity.setSkillSetLevelId(1);
        ownerSkillRolesEntity.setRolesId(2);
        ownerSkillRolesEntity.setRolesDescription("Developer");
        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        ownerSkillStatusEntity.setStatusDescription("available");

    }

    @Test
    void getDataByProjectIdTest() {
        Mockito.when(seekerTechnologyService.getDataByProjectId(anyInt())).thenReturn(skillSeekerTechnologies);
        assertEquals(200, seekerTechnologyController.getDataByProjectId(anyInt()).getStatusCodeValue());
    }

    @Test
    void updateSkillSeekerRateCardControllerTest() {
        Mockito.when(seekerTechnologyService.updateData(skillSeekerTechnology)).thenReturn(skillSeekerTechnology);
        assertEquals(200, seekerTechnologyController.updateSeekerTechnologyDetails(skillSeekerTechnology).getStatusCodeValue());
    }

    @Test
    void deleteSkillSeekerRateCardControllerTestTest() throws Exception {
        seekerTechnologyController.deleteSeekerTechDetails(1);
        seekerTechnologyController.deleteSeekerTechDetails(2);
        Mockito.verify(seekerTechnologyService, times(2)).deleteData(Mockito.anyInt());
    }

}