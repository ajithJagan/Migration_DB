package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerInterviewStages;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerInterviewStagesEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerInterviewStagesRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerInterviewStagesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SkillSeekerInterviewStagesServiceImpl.class)
class SkillSeekerInterviewStagesServiceTest {

    @MockBean
    SkillSeekerInterviewStagesRepository seekerInterviewStagesRepository;
    @MockBean
    ModelMapper modelMapper;

    @Autowired
    SkillSeekerInterviewStagesServiceImpl service;
    SkillSeekerInterviewStagesEntity skillSeekerInterviewStages = new SkillSeekerInterviewStagesEntity();
    List<SkillSeekerInterviewStagesEntity> interviewStagesEntities = new ArrayList<>();

    List<SkillSeekerInterviewStages> interviewStages = new ArrayList<>();

    SkillSeekerInterviewStages stages = new SkillSeekerInterviewStages();


    @BeforeEach
    void setup() {
        skillSeekerInterviewStages.setId(1);
        skillSeekerInterviewStages.setInterviewStages("selected");
        interviewStagesEntities.add(skillSeekerInterviewStages);
        interviewStagesEntities.add(skillSeekerInterviewStages);

        interviewStages.add(stages);
    }

    @Test
    void getSkillSeekerInterviewStagesTest() {
        Mockito.when(seekerInterviewStagesRepository.findAll()).thenReturn(interviewStagesEntities);
        Mockito.when(modelMapper.map(skillSeekerInterviewStages, SkillSeekerInterviewStages.class)).thenReturn(stages);
        assertEquals(2, service.getInterviewStages().size());
    }
}
