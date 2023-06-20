package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillLevel;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillYearOfExperienceService;
import liquibase.pro.packaged.M;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OwnerSkillLevelAndExperienceController.class)
class OwnerSkillLevelAndExperienceControllerTest {

    @Autowired
    OwnerSkillLevelAndExperienceController ownerSkillLevelAndExperienceController;
    @MockBean
    OwnerSkillYearOfExperienceService ownerSkillYearOfExperienceService;
    OwnerSkillYearOfExperience ownerSkillYearOfExperience = new OwnerSkillYearOfExperience();
    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();
    List<Object[]> objects = new ArrayList<>();






    @BeforeEach
    public void setup() {
        ownerSkillYearOfExperience.setId(1);
        ownerSkillYearOfExperience.setExperience("2");
    }

    @Test
    void getOwnerSkillYearOfExperienceDetails() {

        Mockito.when(ownerSkillYearOfExperienceService.getData()).thenReturn(objects);
        assertEquals(200, ownerSkillLevelAndExperienceController.getOwnerSkillYearOfExperienceDetails().getStatusCodeValue());
    }

}