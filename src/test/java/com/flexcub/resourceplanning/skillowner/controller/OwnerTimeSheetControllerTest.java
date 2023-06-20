package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerTimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerTimeSheetServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = OwnerTimeSheetController.class)
class OwnerTimeSheetControllerTest {

    @Autowired
    OwnerTimeSheetController ownerTimeSheetController;

    @MockBean
    OwnerTimeSheetServiceImpl ownerTimeSheetService;

    @MockBean
    OwnerTimeSheetRepository ownerTimeSheetRepository;
    OwnerTimeSheet ownerTimeSheet = new OwnerTimeSheet();
    List<OwnerTimeSheet> ownerTimeSheetList = new ArrayList<>();
    TimeSheetResponse timeSheetResponse = new TimeSheetResponse();
    List<TimeSheetResponse> timeSheetResponseList = new ArrayList<>();
    SkillSeekerProject skillSeekerProjectTest = new SkillSeekerProject();
    List<SkillSeekerProject> skillSeekerProjectTests = new ArrayList<>();
    TimeSheet timeSheet = new TimeSheet();
    List<TimeSheet> timeSheetList = new ArrayList<>();

    @BeforeEach
    public void setup() {

        ownerTimeSheet.setTimeSheetId(1);
        ownerTimeSheet.setSkillOwnerEntityId(1);
        ownerTimeSheet.setSkillSeekerEntityId(1);
        ownerTimeSheet.setSkillSeekerProjectEntityId(1);
        ownerTimeSheet.setEfforts(ownerTimeSheet.getEfforts());
        ownerTimeSheet.setStartDate(new Date(2023, 1, 1));
        ownerTimeSheet.setEndDate(new Date(2023, 2, 1));
        ownerTimeSheet.setApproved(true);
        ownerTimeSheet.setHours("8Hours");
        ownerTimeSheet.setTotalHours("8Hours");

        ownerTimeSheetList.add(ownerTimeSheet);

        timeSheetResponse.setTimeSheetId(1);
        timeSheetResponse.setSkillOwnerEntityId(1);
        timeSheetResponse.setSkillSeekerEntityId(1);
        timeSheetResponse.setSkillSeekerProjectEntityId(1);
        timeSheetResponse.setSkillSeekerTaskEntityId(1);
        timeSheetResponse.setTitle("Flexcub Project");
        timeSheetResponse.setTaskTitle("SkillOwner Details");
        timeSheetResponse.setTaskDescription("To add the SkillOwner Details");
        timeSheetResponse.setStartDate(new Date(2023, 1, 1));
        timeSheetResponse.setEndDate(new Date(2023, 2, 1));
        timeSheetResponse.setApproved(true);
        timeSheetResponse.setHours("8Hours");

        timeSheetResponseList.add(timeSheetResponse);

        skillSeekerProjectTest.setId(1);
        skillSeekerProjectTest.setTitle("Hema");
        skillSeekerProjectTest.setPrimaryContactEmail("hemamalini.a@qbrainx.com");
        skillSeekerProjectTest.setSecondaryContactPhone("9786363526");
        skillSeekerProjectTest.setSummary("good morning");
        skillSeekerProjectTest.setSecondaryContactEmail("hemamalini1114@gmail.com");
        skillSeekerProjectTest.setSecondaryContactPhone("8523697562");
        skillSeekerProjectTest.setOwnerSkillDomainEntity(skillSeekerProjectTest.getOwnerSkillDomainEntity());
        skillSeekerProjectTest.setTaskData(skillSeekerProjectTest.getTaskData());

        skillSeekerProjectTests.add(skillSeekerProjectTest);

        timeSheet.setSkillSeekerProjectEntityId(1);
        timeSheet.setSkillSeekerTaskEntityId(1);
        timeSheet.setHours("5Hours");
        timeSheet.setTitle("Flexcub");
        timeSheet.setApproved(false);
        timeSheet.setStartDate(new Date(2023, 1, 1));
        timeSheet.setEndDate(new Date(2023, 2, 1));
        timeSheet.setTaskDescription("to add the skill owner project details");
        timeSheet.setTaskTitle("Skill owner Implementation");

        timeSheetList.add(timeSheet);
    }

    @Test
    void insertTimeSheetTest() {
        Mockito.when(ownerTimeSheetService.insertTimeSheet(ownerTimeSheetList)).thenReturn(timeSheetResponseList);
        Assertions.assertThat(ownerTimeSheetController.insertTimeSheet(ownerTimeSheetList).getBody()).hasSize(1);
        assertEquals(200, ownerTimeSheetController.insertTimeSheet(ownerTimeSheetList).getStatusCodeValue());

    }

    @Test
    void updateTimeSheetTest() {
        Mockito.when(ownerTimeSheetService.updateTimeSheet(timeSheetResponse)).thenReturn(timeSheetResponse);
        assertEquals(200, ownerTimeSheetController.updateTimeSheet(timeSheetResponse).getStatusCodeValue());

    }

    @Test
    void deleteTimeSheetTest() {
        ownerTimeSheetController.deleteTimeSheetData(1);
        Mockito.verify(ownerTimeSheetService, times(1)).deleteTimeSheetById(1);

    }

    @Test
    void getTimeSheetTest() {
        Mockito.when(ownerTimeSheetService.getTimeSheetHours(new java.util.Date(), 1)).thenReturn(timeSheetResponseList);
        assertEquals(200, ownerTimeSheetController.getTimeSheet(new java.util.Date(), 1).getStatusCodeValue());
    }

    @Test
    void getProjectDetailsTest() {
        Mockito.when(ownerTimeSheetService.getProjectDetails(1)).thenReturn(skillSeekerProjectTests);
        assertEquals(200, ownerTimeSheetController.getProjectDetails(1).getStatusCodeValue());

    }

    @Test
    void getOwnerTimeSheetDetailsTest() {
        Mockito.when(ownerTimeSheetService.getOwnerTimeSheetDetails(Mockito.anyInt())).thenReturn(timeSheetList);
        assertEquals(200,ownerTimeSheetController.getOwnerTimeSheetDetails(Mockito.anyInt()).getStatusCodeValue());


    }

}

