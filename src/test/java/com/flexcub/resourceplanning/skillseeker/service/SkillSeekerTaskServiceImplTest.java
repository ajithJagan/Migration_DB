package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.skillseeker.entity.*;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerTaskRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.SkillSeekerTaskServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillSeekerTaskServiceImpl.class)
class SkillSeekerTaskServiceImplTest {

    @MockBean
    SkillSeekerTaskRepository repository;

    @MockBean
    ModelMapper modelMapper;
    @Autowired
    SkillSeekerTaskServiceImpl service;
    @MockBean
    OwnerSkillDomainEntity ownerSkillDomainEntity;
    @MockBean
    WorkForceStrength workForceStrength;
    @MockBean
    SeekerModulesEntity seekerModulesEntity;
    SkillSeekerTask skillSeekerTaskTest = new SkillSeekerTask();
    SkillSeekerTaskEntity skillSeekerTaskEntity = new SkillSeekerTaskEntity();
    List<SkillSeekerTaskEntity> skillSeekerTaskEntities = new ArrayList<SkillSeekerTaskEntity>();
    List<SkillSeekerTask> skillSeekerTaskList = new ArrayList<>();
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    SubRoles subRoles = new SubRoles();
    Registration registration = new Registration();
    Date date;
    List<SeekerModulesEntity> seekerModulesEntityList = new ArrayList<>();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
    SkillSeekerTask skillSeekerTask1=new SkillSeekerTask();




    @BeforeEach
    void beforeTest() {
        skillSeekerTaskTest.setTaskId(1);
        skillSeekerTaskTest.setTaskTitle("Task1");
        skillSeekerTaskTest.setTaskDescription("Task Work Flow");
        skillSeekerTaskTest.setSkillSeekerId(skillSeekerEntity.getId());
        skillSeekerTaskTest.setSkillSeekerProjectEntity(skillSeekerProjectEntity);
        skillSeekerTaskList.add(skillSeekerTaskTest);

        skillSeekerEntity.setId(1);
        skillSeekerEntity.setSkillSeekerName("Ajith");
        skillSeekerEntity.setEmail("ajith.j@qbrainx.com");
        skillSeekerEntity.setPrimaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeekerEntity.setSecondaryContactEmail("ajith.j+1@qbrainx.com");
        skillSeekerEntity.setCity("Salem");
        skillSeekerEntity.setEndDate(date);
        skillSeekerEntity.setPhone("9087654321");
        skillSeekerEntity.setAddressLine1("sangagiri");
        skillSeekerEntity.setAddressLine2("salem");
        skillSeekerEntity.setStatus("Ok");
        skillSeekerEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerEntity.setState("Tamilnadu");
        skillSeekerEntity.setStartDate(new java.sql.Date(2022 - 12 - 23));
        skillSeekerEntity.setEndDate(new java.sql.Date(2023 - 12 - 30));
        skillSeekerEntity.setSubRoles(subRoles);
        skillSeekerEntity.setZipCode(76542);
        skillSeekerEntity.setTaxIdBusinessLicense("23444");
        skillSeekerEntity.setAddedByAdmin(true);
        skillSeekerEntity.setIsActive(true);
        skillSeekerEntity.setPrimaryContactFullName("Ajith");
        skillSeekerEntity.setSecondaryContactFullName("Ajith");
        skillSeekerEntity.setPrimaryContactPhone("909877554433");
        skillSeekerEntity.setSecondaryContactPhone("1234567890");
        skillSeekerEntity.setId(registration.getId());
        skillSeekerTaskEntity.setSkillSeekerId(1);
        skillSeekerTaskEntity.setSkillSeekerId(skillSeekerEntity.getId());
        skillSeekerTaskEntity.setTaskTitle("abc");
        skillSeekerTaskEntity.setTaskDescription("implementation");
        skillSeekerTaskEntity.setSkillSeekerProject(skillSeekerProjectEntity);
        skillSeekerTaskEntities.add(skillSeekerTaskEntity);


        subRoles.setId(1);
        subRoles.setSubRoleDescription("Basic Level");

        registration.setId(1);
        registration.setCity("Salem");
        registration.setState("Tamilnadu");
        registration.setEmailId("ajith.j@qbrainx.com");
        registration.setAccountStatus(true);
        registration.setFirstName("Aji");
        registration.setSubRoles(1);
        registration.setPassword("1234567");
        registration.setBusinessName("Ajith");
        registration.setLastName("kumar");
        registration.setWorkForceStrength(workForceStrength);
        registration.setModulesAccess(seekerModulesEntityList);
        registration.setMailStatus("Ok");

        seekerModulesEntity.setId(1);
        seekerModulesEntity.setSeekerModule("Admin");

        skillSeekerTask1.setTaskTitle(skillSeekerTaskEntity.getTaskTitle());
        skillSeekerTask1.setTaskDescription(skillSeekerTaskEntity.getTaskDescription());
        skillSeekerTask1.setSkillSeekerProjectEntity(skillSeekerTaskEntity.getSkillSeekerProject());
        skillSeekerTask1.setTaskId(skillSeekerTaskEntity.getId());
        skillSeekerTask1.setSkillSeekerId(skillSeekerTaskEntity.getSkillSeekerId());

        seekerModulesEntityList.add(seekerModulesEntity);



        skillSeekerProjectEntity.setId(1);
    }

    @Test
    void insertTaskDataTest() {
        Mockito.when(repository.findBySkillSeekerProjectId(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerTaskEntities));
        Mockito.when(modelMapper.map(skillSeekerTaskTest, SkillSeekerTaskEntity.class)).thenReturn(skillSeekerTaskEntity);
        Mockito.when(repository.findByDefaultProject()).thenReturn(skillSeekerTaskEntities);
        Mockito.when(repository.save(skillSeekerTaskEntity)).thenReturn(skillSeekerTaskEntity);
        Mockito.when(modelMapper.map(skillSeekerEntity,SkillSeekerTask.class)).thenReturn(skillSeekerTaskTest);
        assertEquals(skillSeekerTaskTest.getTaskId(),service.insertData(skillSeekerTaskList).size());

    }


    @Test
    void updateDataTest() {
        Optional<SkillSeekerTaskEntity> skillSeekerTaskEntity = Optional.of(new SkillSeekerTaskEntity());
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(skillSeekerTaskEntity);
        Mockito.when(modelMapper.map(skillSeekerTaskEntity,SkillSeekerTask.class)).thenReturn(skillSeekerTask1);
        Mockito.when(repository.findBySkillSeekerProjectId(1)).thenReturn(Optional.ofNullable(skillSeekerTaskEntities));
        Mockito.when(repository.findByDefaultProject()).thenReturn(skillSeekerTaskEntities);
        assertEquals(skillSeekerTaskTest.getTaskTitle(), service.updateData(skillSeekerTaskTest).getTaskTitle());
    }

    @Test
    void deleteSkillSeekerTaskDataTest() {
        repository.deleteById(skillSeekerTaskTest.getTaskId());
        Mockito.verify(repository, times(1)).deleteById(1);

    }

    @Test
    void getTaskTest() {

        Mockito.when(repository.findBySkillSeekerProjectId(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillSeekerTaskEntities));
        when(modelMapper.map(skillSeekerTaskEntity, SkillSeekerTask.class)).thenReturn(skillSeekerTaskTest);
        assertEquals(1, service.getTaskData(1, skillSeekerTaskEntity.getSkillSeekerId()).size());
    }
}

