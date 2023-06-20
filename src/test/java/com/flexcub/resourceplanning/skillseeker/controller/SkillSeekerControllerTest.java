package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseeker.service.USALocationService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkillSeekerController.class)
class SkillSeekerControllerTest {

    @Autowired
    SkillSeekerController skillSeekerController;
    SkillSeeker skillSeeker = new SkillSeeker();

    List<ContractDetails> contractDetailsList = new ArrayList<>();

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    List<SkillSeeker> skillSeekerList = new ArrayList<>();
    SeekerRoleListing seekerRoleListing;
    List<SeekerRoleListing> seekerRoleListingList = new ArrayList<>();
    List<SeekerModulesEntity> seekerModulesEntityList = new ArrayList<>();
    SeekerModulesEntity seekerModulesEntity;
    List<SubRoles> subRolesList = new ArrayList<>();
    List<SeekerAccess> seekerAccesses = new ArrayList<>();
    SubRole subRole;

    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    Contracts contracts = new Contracts();
    OnBoarding onBoarding=new OnBoarding();
    @MockBean
    private USALocationService usaLocationService;
    @MockBean
    private SkillSeekerService skillSeekerService;

    ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
    TaskList taskList = new TaskList();
    List<TaskList> taskLists = new ArrayList<>();
    Task task = new Task();
    List<Task> list = new ArrayList<>();

    @BeforeEach
    void setup() {

        skillSeeker.setId(1);
        skillSeeker.setSkillSeekerName("vicky");
        skillSeeker.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeeker.setAddressLine1("no2,6th street,alabama");
        skillSeeker.setAddressLine2("2nd floor,building number 4,alabama");
        skillSeeker.setState("alabama");
        skillSeeker.setZipCode(1234);
        skillSeeker.setPhone("9876543211");
        skillSeeker.setEmail("vicky@gmail.com");
        skillSeeker.setPrimaryContactFullName("vignesh");
        skillSeeker.setPrimaryContactEmail("vignesh@gmail.com");
        skillSeeker.setPrimaryContactPhone("9898989898");
        skillSeeker.setSecondaryContactFullName("karthik");
        skillSeeker.setSecondaryContactEmail("karthik@gmail.com");
        skillSeeker.setSecondaryContactPhone("9090909090");

        skillSeekerList.add(skillSeeker);

        skillOwnerEntity.setSkillOwnerEntityId(1);

        contracts.setStatus("Released");
        contracts.setLocation("New York");
        contracts.setName("Sukumar");
        contracts.setExpiresOn(new Date());
        contracts.setSeekerName("Business name");
        contracts.setPosition("Developer");
        contracts.setOwnerContactNumber("9884104947");
        contracts.setProcessedOn(LocalDate.parse("2023-05-03"));
//
//        onBoarding.setSeekerId(1);
//        onBoarding.setSkillOwnerEntityId(1);
//        onBoarding.setStatus("Active");
//        onBoarding.setProjectId(1);
//        onBoarding.setProjectName("Development");
//        onBoarding.setStartDate(new Date());

        projectTaskDetails.setSkillSeekerProjectAndTaskList(taskLists);

        taskList.setSkillSeekerTasks(list);
        taskList.setSkillSeekerProjectEntity(taskList.getSkillSeekerProjectEntity());

        taskLists.add(taskList);

        task.setTaskId(1);
        task.setTaskTitle("Flexcub");
        task.setTaskDescription("Implementation to the task");

        list.add(task);
    }


    @Test
    void updateClientDetails() {
        Mockito.when(skillSeekerService.updateData(skillSeeker)).thenReturn(skillSeeker);
        assertEquals(200, skillSeekerController.updateClientDetails(skillSeeker).getStatusCodeValue());
    }

    @Test
    void deleteClientDetails() {
        skillSeekerController.deleteClientDetails(1);
        skillSeekerController.deleteClientDetails(2);
        Mockito.verify(skillSeekerService, times(2)).deleteData(Mockito.anyInt());
    }

    @Test
    void getLocationByKeyword() {
        when(usaLocationService.getLocationFromDatabase("ABC")).thenReturn(Collections.singletonList("New York, New York"));
        assertEquals("New York, New York", Objects.requireNonNull(skillSeekerController.getLocationByKeyword("ABC").getBody()).get(0));
    }

    @Test
    void getSeekerById() {
        Mockito.when(skillSeekerService.getSkillSeeker(skillSeeker.getTaxIdBusinessLicense())).thenReturn(skillSeekerList);
        assertEquals(200, skillSeekerController.getSeekerById(skillSeeker.getTaxIdBusinessLicense()).getStatusCodeValue());
    }

    @Test
    void getAccessById() {
        Mockito.when(skillSeekerService.getAccessByTaxId(skillSeeker.getTaxIdBusinessLicense())).thenReturn(seekerRoleListingList);
        assertEquals(200, skillSeekerController.getAccessById(skillSeeker.getTaxIdBusinessLicense()).getStatusCodeValue());
    }

    @Test
    void getModules() {
        Mockito.when(skillSeekerService.getModules()).thenReturn(seekerModulesEntityList);
        assertEquals(200, skillSeekerController.getModules().getStatusCodeValue());
    }

    @Test
    void getSubRoles() {
        Mockito.when(skillSeekerService.getRoles()).thenReturn(subRolesList);
        assertEquals(200, skillSeekerController.getSubRoles().getStatusCodeValue());
    }

    @Test
    void addSeekerSubRoles() {
        Mockito.when(skillSeekerService.addSeekerSubRoles(Mockito.anyInt(), Mockito.anyInt())).thenReturn(skillSeeker);
        assertEquals(200, skillSeekerController.addSeekerSubRoles(Mockito.anyInt(), Mockito.anyInt()).getStatusCodeValue());
    }

    @Test
    void addSubRole() {
        Mockito.when(skillSeekerService.addSubRole(subRole)).thenReturn(seekerAccesses);
        assertEquals(200, skillSeekerController.addSubRole(subRole).getStatusCodeValue());
    }

    @Test
    void getProjectTaskDetailsBySeekerTest(){
        Mockito.when(skillSeekerService.getProjectTaskDetailsBySeeker(1)).thenReturn(projectTaskDetails);
        assertEquals(200,skillSeekerController.getProjectTaskDetailsBySeeker(1).getStatusCodeValue());
    }

//    @Test
//    void getContractDetails() {
//        Mockito.when(skillSeekerService.getContractDetails(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn((List<Contracts>) contracts);
//        assertEquals(200, skillSeekerController.getContractDetails(skillOwnerEntity.getSkillOwnerEntityId()).getStatusCodeValue());
//    }

    @Test
    void getListsOfContractDetails() {
        Mockito.when(skillSeekerService.getListsOfContractDetails(1)).thenReturn(contractDetailsList);
        assertEquals(200, skillSeekerController.getListsOfContractDetails(1).getStatusCodeValue());
    }



//    @Test
//    void onBoarding(){
//        Mockito.when(skillSeekerService.onBoardingSkillOwner())
//    }
}
