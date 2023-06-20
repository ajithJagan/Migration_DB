package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerSkillSet;
import com.flexcub.resourceplanning.skillowner.entity.*;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillSetServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.asynchttpclient.util.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = OwnerSkillSetServiceImpl.class)
class OwnerSkillSetServiceImplTest {

    @MockBean
    OwnerSkillSetRepository ownerSkillSetRepository;
    @Autowired
    OwnerSkillSetServiceImpl ownerSkillSetService;
    @MockBean
    JobRepository jobRepository;
    @MockBean
    ModelMapper modelMapper;

    SkillOwnerSkillSet skillOwnerSkillSetDto = new SkillOwnerSkillSet();

    List<SkillOwnerSkillSet> skillOwnerSkillSetDtos = new ArrayList<>();

    OwnerSkillSetEntity ownerSkillSetEntity = new OwnerSkillSetEntity();

    List<OwnerSkillSetEntity> ownerSkillSetEntities = new ArrayList<>();
    List<Integer> integers=new ArrayList<>();

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    OwnerSkillRolesEntity ownerSkillRolesEntity = new OwnerSkillRolesEntity();
    OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = new OwnerSkillTechnologiesEntity();
    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();


    @BeforeEach
    public void setup() {

        skillOwnerEntity.setSkillOwnerEntityId(1);

        ownerSkillDomainEntity.setDomainId(1);
        ownerSkillDomainEntity.setDomainValues("HealthCare");

        ownerSkillRolesEntity.setRolesId(1);
        ownerSkillRolesEntity.setRolesDescription("Developer");

        ownerSkillTechnologiesEntity.setTechnologyId(1);
        ownerSkillTechnologiesEntity.setTechnologyValues("Java");

        ownerSkillLevelEntity.setSkillSetLevelId(1);
        ownerSkillLevelEntity.setSkillLevelDescription("junior java developer");

        ownerSkillSetEntity.setOwnerSkillSetId(1);
        ownerSkillSetEntity.setExperience("3+");
        ownerSkillSetEntity.setLastUsed("22-09-2016");
        ownerSkillSetEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        ownerSkillSetEntity.setSkillOwnerEntityId(1);
        ownerSkillSetEntity.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        ownerSkillSetEntity.setOwnerSkillRolesEntity(ownerSkillRolesEntity);
        ownerSkillSetEntity.setOwnerSkillLevelEntity(ownerSkillLevelEntity);
        ownerSkillSetEntity.getOwnerSkillSetId();
        ownerSkillSetEntities.add(ownerSkillSetEntity);


        skillOwnerSkillSetDto.setOwnerSkillSetId(1);
        skillOwnerSkillSetDto.setSkillOwnerEntityId(1);
        skillOwnerSkillSetDto.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillOwnerSkillSetDto.setOwnerSkillRolesEntity(ownerSkillRolesEntity);
        skillOwnerSkillSetDto.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        skillOwnerSkillSetDto.setExperience("5");
        skillOwnerSkillSetDtos.add(skillOwnerSkillSetDto);
    }

    @Test
    void getSkillSet() {
        Mockito.when(ownerSkillSetRepository.findBySkillOwnerEntityId(Mockito.anyInt())).thenReturn(ownerSkillSetEntities);
        Mockito.when(modelMapper.map(ownerSkillSetEntity, SkillOwnerSkillSet.class)).thenReturn(skillOwnerSkillSetDto);
        assertEquals(1,ownerSkillSetService.getSkillset(Mockito.anyInt()).size());
        Assertions.assertNotNull(ownerSkillSetService.getSkillset(Mockito.anyInt()));
    }

    @Test
    void insertDataset() {
    Mockito.when(modelMapper.map(skillOwnerSkillSetDto,OwnerSkillSetEntity.class)).thenReturn(ownerSkillSetEntity);
    ownerSkillSetRepository.save(ownerSkillSetEntity);
    Mockito.when(modelMapper.map(ownerSkillSetEntity,SkillOwnerSkillSet.class)).thenReturn(skillOwnerSkillSetDto);
    assertEquals(skillOwnerSkillSetDto,ownerSkillSetService.insertSkillSet(skillOwnerSkillSetDto));
    }

    @Test
    void deleteDataset() {
        ownerSkillSetService.deleteSkillset(1);
        Mockito.verify(ownerSkillSetRepository, times(1)).deleteById(1);
    }

//    @Test
//    void updateSkillSet() {
//        Mockito.when(modelMapper.map(skillOwnerSkillSetDto,OwnerSkillSetEntity.class)).thenReturn(ownerSkillSetEntity);
//        Mockito.when(ownerSkillSetRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(ownerSkillSetEntity));
//        ownerSkillSetRepository.save(ownerSkillSetEntities.get(Mockito.anyInt()));
//        Mockito.when(modelMapper.map(ownerSkillSetEntity,SkillOwnerSkillSet.class)).thenReturn(skillOwnerSkillSetDto);
//        assertEquals(skillOwnerSkillSetDto,ownerSkillSetService.updateSkillSet(skillOwnerSkillSetDto));
//        }

    @Test
    void skillSetPercentage() {
        Mockito.when(jobRepository.findAllTechnologyIdByJobId("FJB-001")).thenReturn(integers);
        Mockito.when(ownerSkillSetRepository.findAllTechnologyIdByOwnerSkillSetId(1)).thenReturn(integers);
        assertThat(ownerSkillSetService.skillSetPercentage("FJB-0001", ownerSkillSetEntities)).isZero();
    }
}