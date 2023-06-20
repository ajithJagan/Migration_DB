package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerInterviewStages;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerInterviewStagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SkillSeekerInterviewStagesController.class)
class SkillSeekerInterviewStagesControllerTest {

    @Autowired
    SkillSeekerInterviewStagesController skillSeekerInterviewStagesController;
    @MockBean
    SkillSeekerInterviewStagesService skillSeekerInterviewStagesService;

    SkillSeekerInterviewStages skillSeekerInterviewStages = new SkillSeekerInterviewStages();

    List<SkillSeekerInterviewStages> interviewStagesEntities = new ArrayList<>();

    @BeforeEach
    void setup() {
        skillSeekerInterviewStages.setId(1);
        skillSeekerInterviewStages.setInterviewStages("selected");
    }

    @Test
    void getSkillSeekerDetailsTest() {
        interviewStagesEntities.add(skillSeekerInterviewStages);
        interviewStagesEntities.add(skillSeekerInterviewStages);
        Mockito.when((skillSeekerInterviewStagesService.getInterviewStages())).thenReturn(interviewStagesEntities);
        assertEquals(200, skillSeekerInterviewStagesController.getInterviewStages().getStatusCodeValue());

    }
}
