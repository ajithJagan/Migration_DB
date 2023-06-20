package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRateCardRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerTechnologyServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTechnology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillSeekerTechnologyServiceImpl.class)
class SkillSeekerTechnologyServiceTest {

    @Autowired
    SkillSeekerTechnologyService skillSeekerTechnologyService;

    @MockBean
    SkillSeekerRateCardRepository seekerTechRepo;

    @MockBean
    ModelMapper modelMapper;

    SkillSeekerTechnologyData skillSeekerTechnologyData = new SkillSeekerTechnologyData();
    SkillSeekerTechnology skillSeekerTechnology = new SkillSeekerTechnology();

    List<SkillSeekerTechnologyData> skillSeekerTechList = new ArrayList<>();

    OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = new OwnerSkillTechnologiesEntity();
    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();
    OwnerSkillRolesEntity ownerSkillRolesEntity = new OwnerSkillRolesEntity();


    @BeforeEach
    void beforeTest() {
        skillSeekerTechnologyData.setId(1);
        skillSeekerTechnologyData.setMaxRate(450);
        skillSeekerTechnologyData.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        skillSeekerTechnologyData.setOwnerSkillRolesEntity(ownerSkillRolesEntity);
        skillSeekerTechnologyData.setOwnerSkillLevelEntity(ownerSkillLevelEntity);
        skillSeekerTechList.add(skillSeekerTechnologyData);
    }


    @Test
    void insertMultipleDataTest() {
        when(seekerTechRepo.saveAllAndFlush(skillSeekerTechList)).thenReturn(skillSeekerTechList);
        assertEquals(1, skillSeekerTechnologyService.insertMultipleData(skillSeekerTechList).size());
    }


    @Test
    void updateDataTest() {
        Mockito.when(modelMapper.map(skillSeekerTechnology, SkillSeekerTechnologyData.class)).thenReturn(skillSeekerTechnologyData);
        Mockito.when(seekerTechRepo.findById(skillSeekerTechnologyData.getId())).thenReturn(Optional.of(skillSeekerTechnologyData));
        Mockito.when(modelMapper.map(skillSeekerTechnologyData, SkillSeekerTechnology.class)).thenReturn(skillSeekerTechnology);
        assertEquals(skillSeekerTechnology.getId(), skillSeekerTechnologyService.updateData(skillSeekerTechnology).getId());

    }

    @Test
    void deleteDataTest() {
        seekerTechRepo.deleteById(1);
        Mockito.verify(seekerTechRepo, times(1)).deleteById(1);
    }

    @Test
    void getDataByProjectIdTest() {
        when(seekerTechRepo.findByProjectId(Mockito.anyInt())).thenReturn(skillSeekerTechList);
        assertEquals(1, skillSeekerTechnologyService.getDataByProjectId(1).size());

    }
}




