package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillLevel;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillLevelRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillLevelServiceImpl;
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

@SpringBootTest(classes = OwnerSkillLevelServiceImpl.class)
class OwnerSkillLevelServiceImplTest {

    @MockBean
    OwnerSkillLevelRepository ownerSkillLevelRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    OwnerSkillLevelServiceImpl ownerSkillLevelService;

    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();

    List<OwnerSkillLevelEntity> ownerSkillLevelEntities = new ArrayList<>();
    OwnerSkillLevel ownerSkillLevel=new OwnerSkillLevel();
    List<OwnerSkillLevel>ownerSkillLevels=new ArrayList<>();

    @BeforeEach
    public void setup() {
        ownerSkillLevelEntity.setSkillSetLevelId(1);
        ownerSkillLevelEntity.setSkillLevelDescription("Java");
        ownerSkillLevelEntities.add(ownerSkillLevelEntity);
    }

    @Test
    void getDataLevel() {
        Mockito.when(ownerSkillLevelRepository.findAll()).thenReturn(ownerSkillLevelEntities);
        Mockito.when(modelMapper.map(ownerSkillLevelEntity,OwnerSkillLevel.class)).thenReturn(ownerSkillLevel);
        assertEquals(1,ownerSkillLevelService.getDatalevel().size());
        Assertions.assertNotNull(ownerSkillLevelService.getDatalevel());
    }

    @Test
    void insertDataLevel() {
        Mockito.when(modelMapper.map(ownerSkillLevel,OwnerSkillLevelEntity.class)).thenReturn(ownerSkillLevelEntity);
        ownerSkillLevelRepository.save(ownerSkillLevelEntity);
        Mockito.when(modelMapper.map(ownerSkillLevelEntity,OwnerSkillLevel.class)).thenReturn(ownerSkillLevel);
        assertEquals(ownerSkillLevel,ownerSkillLevelService.insertDatalevel(ownerSkillLevel));
    }

//    @Test
//    void deleteDataLevel() {
//        ownerSkillLevelService.deleteDatalevel(1);
//        Mockito.verify(ownerSkillLevelRepository, times(1)).deleteById(1);
//    }

    @Test
    void updateSkillLeveL() {
       Mockito.when(modelMapper.map(ownerSkillLevel,OwnerSkillLevelEntity.class)).thenReturn(ownerSkillLevelEntity);
       ownerSkillLevelRepository.saveAndFlush(ownerSkillLevelEntity);
       Mockito.when(modelMapper.map(ownerSkillLevelEntity,OwnerSkillLevel.class)).thenReturn(ownerSkillLevel);
       assertEquals(ownerSkillLevel,ownerSkillLevelService.updateSkillLeveL(ownerSkillLevel));
    }
}