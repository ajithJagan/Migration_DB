package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillTechnologies;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillTechnologiesServiceImpl;
import org.assertj.core.api.Assertions;
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

@SpringBootTest(classes = OwnerSkillTechnologiesController.class)
class OwnerSkillTechnologiesControllerTest {


    @Autowired
    OwnerSkillTechnologiesController ownerSkillTechnologiesController;

    @MockBean
    OwnerSkillTechnologiesServiceImpl ownerSkillTechnologiesService;


    OwnerSkillTechnologies ownerSkillTechnology = new OwnerSkillTechnologies();

    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntitys = new ArrayList<OwnerSkillTechnologiesEntity>();
List<OwnerSkillTechnologies> ownerSkillTechnologies=new ArrayList<>();


    @BeforeEach
    public void setup() {
        ownerSkillTechnology.setTechnologyId(1);
        ownerSkillTechnology.setTechnologyValues("Java");
        ownerSkillTechnology.setPriority(1);
    }

    @Test
    void getDetailsTech() {
        ownerSkillTechnologies.add(ownerSkillTechnology);
        ownerSkillTechnologies.add(ownerSkillTechnology);
        Mockito.when((ownerSkillTechnologiesService.getDataTech())).thenReturn(ownerSkillTechnologies);
        assertEquals(2, ownerSkillTechnologiesController.getDetailsTech().size());
    }


}