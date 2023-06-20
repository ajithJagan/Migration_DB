package com.flexcub.resourceplanning.skillowner.service;


import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillStatus;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillStatusRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillStatusServiceImpl;
import org.junit.jupiter.api.Assertions;
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


@SpringBootTest(classes = OwnerSkillStatusServiceImpl.class)
class OwnerSkillStatusServiceImplTest {

    @MockBean
    OwnerSkillStatusRepository ownerSkillStatusRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    OwnerSkillStatusServiceImpl ownerSkillStatusService;

    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();

    List<OwnerSkillStatusEntity> ownerSkillStatusEntities = new ArrayList<>();
    OwnerSkillStatus ownerSkillStatus=new OwnerSkillStatus();

    @BeforeEach
    public void setup() {
        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        ownerSkillStatusEntity.setStatusDescription("Java");
        ownerSkillStatusEntities.add(ownerSkillStatusEntity);
    }

    @Test
    void getDataStatus() {

        Mockito.when(ownerSkillStatusRepository.findAll()).thenReturn(ownerSkillStatusEntities);
        Mockito.when(modelMapper.map(ownerSkillStatusEntity,OwnerSkillStatus.class)).thenReturn(ownerSkillStatus);
        assertEquals(1,ownerSkillStatusService.getDataStatus().size());
        Assertions.assertNotNull(ownerSkillStatusService.getDataStatus());
    }
    @Test
    void insertDataStatus() {

        Mockito.when(modelMapper.map(ownerSkillStatus,OwnerSkillStatusEntity.class)).thenReturn(ownerSkillStatusEntity);
        ownerSkillStatusRepository.save(ownerSkillStatusEntity);
        Mockito.when(modelMapper.map(ownerSkillStatusEntity,OwnerSkillStatus.class)).thenReturn(ownerSkillStatus);
        assertEquals(ownerSkillStatus,ownerSkillStatusService.insertDataStatus(ownerSkillStatus));
    }


    @Test
    void updateOwnerDetails() {
        Mockito.when(modelMapper.map(ownerSkillStatus,OwnerSkillStatusEntity.class)).thenReturn(ownerSkillStatusEntity);
        ownerSkillStatusRepository.saveAndFlush(ownerSkillStatusEntity);
        Mockito.when(modelMapper.map(ownerSkillStatusEntity,OwnerSkillStatus.class)).thenReturn(ownerSkillStatus);
        assertEquals(ownerSkillStatus,ownerSkillStatusService.updateOwnerDetails(ownerSkillStatus));
    }

//    @Test
//    void deleteDataStatus() {
//        ownerSkillStatusService.deleteDataStatus(1);
//        Mockito.verify(ownerSkillStatusRepository, times(1)).deleteById(1);
//    }
}