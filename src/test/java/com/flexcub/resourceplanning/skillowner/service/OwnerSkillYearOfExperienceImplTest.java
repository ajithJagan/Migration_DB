package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillYearOfExperienceRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillYearOfExperienceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OwnerSkillYearOfExperienceImpl.class)
class OwnerSkillYearOfExperienceImplTest {

    @MockBean
    OwnerSkillYearOfExperienceRepository ownerSkillYearOfExperienceRepository;
    @Autowired
    OwnerSkillYearOfExperienceImpl ownerSkillYearOfExperienceService;


    OwnerSkillYearOfExperience ownerSkillYearOfExperience = new OwnerSkillYearOfExperience();

    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();

    List<OwnerSkillYearOfExperience> ownerSkillYearOfExperiences = new ArrayList<>();

    @BeforeEach
    public void setup() {
        ownerSkillYearOfExperience.setId(1);
        ownerSkillYearOfExperience.setExperience("2");
    }

    @Test
    void getData() {
        Mockito.when(ownerSkillYearOfExperienceRepository.findAll()).thenReturn(ownerSkillYearOfExperiences);
        assertEquals(ownerSkillYearOfExperienceService.getData(), ownerSkillYearOfExperiences);
    }

}