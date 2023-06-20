package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillDomain;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.ClientServiceImpl;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillDomainServiceImpl;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OwnerSkillDomainServiceImpl.class)
class OwnerSkillDomainServiceImplTest {

    @MockBean
    OwnerSkillDomainRepository ownerSkillDomainRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    OwnerSkillDomainServiceImpl ownerSkillDomainService;


    @MockBean
    SkillOwnerRepository skillOwnerRepository;

    @MockBean
    ClientServiceImpl clientService;

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    List<OwnerSkillDomainEntity> ownerSkillDomainEntities = new ArrayList<>();
    OwnerSkillDomain ownerSkillDomain=new OwnerSkillDomain();



    @BeforeEach
    public void setup() {
        ownerSkillDomainEntity.setDomainId(1);
        ownerSkillDomainEntity.setDomainValues("Java");
        ownerSkillDomainEntity.setPriority(1);
        ownerSkillDomainEntities.add(ownerSkillDomainEntity);

    }

    @Test
    void getDataDomainTest() {
        Mockito.when(ownerSkillDomainRepository.findAll()).thenReturn(ownerSkillDomainEntities);
        Mockito.when(modelMapper.map(ownerSkillDomainEntities,OwnerSkillDomain.class)).thenReturn(ownerSkillDomain);
        assertNotNull(ownerSkillDomainEntities);
         }

    @Test
    void insertDataDomainTest() {
        Mockito.when(modelMapper.map(ownerSkillDomain,OwnerSkillDomainEntity.class)).thenReturn(ownerSkillDomainEntity);
        ownerSkillDomainRepository.save(ownerSkillDomainEntity);
        Mockito.when(modelMapper.map(ownerSkillDomainEntity,OwnerSkillDomain.class)).thenReturn(ownerSkillDomain);
        assertEquals(ownerSkillDomain,ownerSkillDomainService.insertDataDomain(ownerSkillDomain));
    }

//    @Test
//    void deleteDataDomainTest() {
//        ownerSkillDomainService.deleteDataDomain(1);
//        Mockito.verify(ownerSkillDomainRepository, times(1)).deleteById(1);
//    }

    @Test
    void updateDomainTest() {
        Mockito.when(modelMapper.map(ownerSkillDomain,OwnerSkillDomainEntity.class)).thenReturn(ownerSkillDomainEntity);
        ownerSkillDomainRepository.saveAndFlush(ownerSkillDomainEntity);
        Mockito.when(modelMapper.map(ownerSkillDomainEntity,OwnerSkillDomain.class)).thenReturn(ownerSkillDomain);
        assertEquals(ownerSkillDomain,ownerSkillDomainService.updateDomain(ownerSkillDomain));
          }
}