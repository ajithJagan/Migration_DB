package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillTechnologies;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillTechnologiesRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillTechnologiesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = OwnerSkillTechnologiesServiceImpl.class)
class OwnerSkillTechnologiesServiceImplTest {
    @MockBean
    OwnerSkillTechnologiesRepository ownerSkillTechnologiesRepository;

    @MockBean
    ModelMapper modelMapper;
    @Autowired
    OwnerSkillTechnologiesServiceImpl ownerSkillTechnologiesService;

    OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = new OwnerSkillTechnologiesEntity();

    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntities = new ArrayList<>();
    OwnerSkillTechnologies ownerSkillTechnologies=new OwnerSkillTechnologies();

    @BeforeEach
    public void setup() {
        ownerSkillTechnologiesEntity.setTechnologyId(1);
        ownerSkillTechnologiesEntity.setTechnologyValues("Java");
        ownerSkillTechnologiesEntity.setPriority(1);
        ownerSkillTechnologiesEntities.add(ownerSkillTechnologiesEntity);
    }

    @Test
    void getDataTech() {
        Mockito.when(ownerSkillTechnologiesRepository.findAll(Sort.by(Sort.Direction.ASC, "priority"))).thenReturn(ownerSkillTechnologiesEntities);
        Mockito.when(modelMapper.map(ownerSkillTechnologiesEntity, OwnerSkillTechnologies.class)).thenReturn(ownerSkillTechnologies);
        assertEquals(1, ownerSkillTechnologiesService.getDataTech().size());

    }


    @Test
    void insertDataTech() {
        Mockito.when(ownerSkillTechnologiesRepository.findByTechnologyValuesIgnoreCase(Mockito.anyString())).thenReturn(ownerSkillTechnologiesEntity);
        Mockito.when(modelMapper.map(ownerSkillTechnologies,OwnerSkillTechnologiesEntity.class)).thenReturn(ownerSkillTechnologiesEntity);
        ownerSkillTechnologiesRepository.save(ownerSkillTechnologiesEntity);
        Mockito.when(modelMapper.map(ownerSkillTechnologiesEntity,OwnerSkillTechnologies.class)).thenReturn(ownerSkillTechnologies);
        assertEquals(ownerSkillTechnologies,ownerSkillTechnologiesService.insertDataTech(ownerSkillTechnologies));


    }

//    @Test
//    void deleteDataTech() {
//        ownerSkillTechnologiesService.deleteDataTech(1);
//        Mockito.verify(ownerSkillTechnologiesRepository, times(1)).deleteById(Mockito.anyInt());
//    }


    @Test
    void updateOwnerDetails() {
        Mockito.when(modelMapper.map(ownerSkillTechnologies, OwnerSkillTechnologiesEntity.class)).thenReturn(ownerSkillTechnologiesEntity);
        ownerSkillTechnologiesRepository.saveAndFlush(ownerSkillTechnologiesEntity);
        Mockito.when(modelMapper.map(ownerSkillTechnologiesEntity, OwnerSkillTechnologies.class)).thenReturn(ownerSkillTechnologies);
        assertEquals(ownerSkillTechnologies, ownerSkillTechnologiesService.updateOwnerDetails(ownerSkillTechnologies));
    }
}