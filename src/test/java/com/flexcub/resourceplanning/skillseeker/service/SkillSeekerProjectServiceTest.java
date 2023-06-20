package com.flexcub.resourceplanning.skillseeker.service;


import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerProjectServiceImpl;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerTechnologyServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
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

@SpringBootTest(classes = SkillSeekerProjectServiceImpl.class)
class SkillSeekerProjectServiceTest {

    @MockBean
    SkillSeekerProjectRepository repository;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;

    @MockBean
    SkillSeekerTechnologyServiceImpl skillSeekerTechnologyService;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    SkillSeekerProjectServiceImpl service;

    @MockBean
    ContractFileRepository contractFileRepository;


    SkillSeekerProject skillSeekerProject = new SkillSeekerProject();

    SkillSeekerProject seekerProject = new SkillSeekerProject();

    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    SkillSeeker skillSeeker = new SkillSeeker();

    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    List<SkillSeekerProjectEntity> skillSeekerProjectEntities = new ArrayList<>();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    List<SkillSeekerProject> skillSeekerProjectList = new ArrayList<>();
    List<SkillSeekerProject> skillSeekerProjects = new ArrayList<>();

    ContractFiles contractFiles = new ContractFiles();


    @BeforeEach
    void beforeTest() {

        skillSeekerProjectEntity.setId(1);
        skillSeekerProjectEntity.setTitle("python");
        skillSeekerProjectEntity.setSkillSeeker(skillSeekerEntity);
        skillSeekerProjectEntity.setPrimaryContactEmail("ram.k@qbrainx.com");
        skillSeekerProjectEntity.setSecondaryContactPhone("9890909091");
        skillSeekerProjectEntity.setSummary("proficient in backend");
        skillSeekerProjectEntity.setSecondaryContactEmail("ram.k@qbrainx.com");
        skillSeekerProjectEntity.setSecondaryContactPhone("9898989899");
        skillSeekerProjectEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerProjectEntities.add(skillSeekerProjectEntity);

        ownerSkillDomainEntity.setDomainId(3);
        ownerSkillDomainEntity.setDomainValues("Manufacturing");

        skillSeekerProject.setId(1);
        skillSeekerProject.setSkillSeeker(skillSeeker);
        skillSeekerProject.setTitle("java");
        skillSeekerProject.setPrimaryContactEmail("ram.k@qbrainx.com");
        skillSeekerProject.setSecondaryContactPhone("9890909091");
        skillSeekerProject.setSummary("good in backend");
        skillSeekerProject.setSecondaryContactEmail("ram.k@qbrainx.com");
        skillSeekerProject.setSecondaryContactPhone("9898989899");
        skillSeekerProject.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerProjectList.add(skillSeekerProject);

        seekerProject.setId(1);
        seekerProject.setSkillSeeker(skillSeeker);
        seekerProject.setTitle("java");
        seekerProject.setPrimaryContactEmail("ram.k@qbrainx.com");
        seekerProject.setSecondaryContactPhone("9890909091");
        seekerProject.setSummary("good in backend");
        seekerProject.setSecondaryContactEmail("ram.k@qbrainx.com");
        seekerProject.setSecondaryContactPhone("9898989899");
        seekerProject.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerProjects.add(seekerProject);

        contractFiles.setId(1);
        contractFiles.setFileName("");
        contractFiles.setFileVersion(2);
        contractFiles.setMimeType("");
        contractFiles.setData(new byte[1]);
        skillSeekerEntity.setMsaId(contractFiles);

        skillSeekerProject.setSkillSeeker(skillSeeker);

    }

    @Test
    void insertDataTest() {
        Mockito.when(skillSeekerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerEntity));
        Mockito.when(repository.findProjectByDepartmentAndSeekerId(skillSeekerProject.getOwnerSkillDomainEntity().getDomainId(), seekerProject.getSkillSeeker().getId())).thenReturn(Optional.ofNullable(skillSeekerProjectEntities));
        Mockito.when(modelMapper.map(skillSeekerProject, SkillSeekerProjectEntity.class)).thenReturn(skillSeekerProjectEntity);
        Mockito.when(repository.saveAll(skillSeekerProjectEntities)).thenReturn(skillSeekerProjectEntities);
        Mockito.when(modelMapper.map(skillSeekerProjectEntity, SkillSeekerProject.class)).thenReturn(skillSeekerProject);
        assertEquals(skillSeekerProjectList.size(), service.insertData(skillSeekerProjectList).size());
    }


    @Test
    void updateDataTest() {

        when(repository.findById(1)).thenReturn(Optional.of(skillSeekerProjectEntity));
        when(repository.findProjectByDepartmentAndSeekerId(3, 1)).thenReturn(Optional.of(skillSeekerProjectEntities));
        when(modelMapper.map(skillSeekerProjectEntity, SkillSeekerProjectEntity.class)).thenReturn(skillSeekerProjectEntity);
        when(modelMapper.map(skillSeekerProjectEntity, SkillSeekerProject.class)).thenReturn(skillSeekerProject);
        assertEquals(skillSeekerProject.getTitle(), seekerProject.getTitle());
    }

    @Test
    void deleteSkillSeekerProjectDataTest() {
        repository.deleteById(skillSeekerProject.getId());
        Mockito.verify(repository, times(1)).deleteById(1);

    }

    @Test
    void getProjectTest() {
        Mockito.when(repository.findBySkillSeekerId(Mockito.anyInt())).thenReturn(Optional.of(skillSeekerProjectEntities));
        when(modelMapper.map(skillSeekerProjectEntity, SkillSeekerProject.class)).thenReturn(skillSeekerProject);
        when(contractFileRepository.findById(skillSeekerEntity.getMsaId().getId())).thenReturn(Optional.ofNullable(contractFiles));
        assertEquals(2, service.getProjectData(1).size());
    }
}










