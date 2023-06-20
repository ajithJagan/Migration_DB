package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillRolesRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillRolesServiceImpl;
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


@SpringBootTest(classes = OwnerSkillRolesServiceImpl.class)
class OwnerSkillRolesServiceImplTest {

    @MockBean
    OwnerSkillRolesRepository ownerSkillRolesRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    OwnerSkillRolesServiceImpl ownerSkillRolesService;

    OwnerSkillRolesEntity ownerSkillRolesEntity = new OwnerSkillRolesEntity();

    List<OwnerSkillRolesEntity> ownerSkillRolesEntities = new ArrayList<>();
    OwnerSkillRoles ownerSkillRoles=new OwnerSkillRoles();

    @BeforeEach
    public void setup() {
        ownerSkillRolesEntity.setRolesId(1);
        ownerSkillRolesEntity.setRolesDescription("Java");
        ownerSkillRolesEntities.add(ownerSkillRolesEntity);
    }

    @Test
    void getDataRolesTest() {
        Mockito.when(ownerSkillRolesRepository.findAll()).thenReturn(ownerSkillRolesEntities);
        Mockito.when(modelMapper.map(ownerSkillRolesEntity, OwnerSkillRoles.class)).thenReturn(ownerSkillRoles);
        assertEquals(1,ownerSkillRolesService.getDataroles().size());
        Assertions.assertNotNull(ownerSkillRolesService.getDataroles());
    }

    @Test
    void insertDataRolesTest() {
        Mockito.when(modelMapper.map(ownerSkillRoles,OwnerSkillRolesEntity.class)).thenReturn(ownerSkillRolesEntity);
        ownerSkillRolesRepository.save(ownerSkillRolesEntity);
        Mockito.when(modelMapper.map(ownerSkillRolesEntity,OwnerSkillRoles.class)).thenReturn(ownerSkillRoles);
        assertEquals(ownerSkillRoles,ownerSkillRolesService.insertDataroles(ownerSkillRoles));
    }

//    @Test
//    void deleteDataRolesTest() {
//        ownerSkillRolesService.deleteDataroles(1);
//        Mockito.verify(ownerSkillRolesRepository, times(1)).deleteById(1);
//    }

    @Test
    void updateSkillRolesTest() {
        Mockito.when(modelMapper.map(ownerSkillRoles,OwnerSkillRolesEntity.class)).thenReturn(ownerSkillRolesEntity);
        ownerSkillRolesRepository.saveAndFlush(ownerSkillRolesEntity);
        Mockito.when(modelMapper.map(ownerSkillRolesEntity,OwnerSkillRoles.class)).thenReturn(ownerSkillRoles);
        assertEquals(ownerSkillRoles,ownerSkillRolesService.updateSkillRoles(ownerSkillRoles));
        }
}