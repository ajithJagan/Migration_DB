package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.service.RequirementService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerRequirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = SeekerRequirementController.class)
class SeekerRequirementControllerTest {
    @Autowired
    SeekerRequirementController seekerRequirementController;
    @MockBean
    RequirementService requirementService;

    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();

    SeekerRequirement seekerRequirement = new SeekerRequirement();
    List<SeekerRequirement> seekerRequirements = new ArrayList<>();
    HiringPriority hiringPriority = new HiringPriority();
    SkillSeekerProjectEntity skillSeekerProject = new SkillSeekerProjectEntity();
    JobDto jobDto = new JobDto();
    List<JobDto> jobDtoList = new ArrayList<>();

    @BeforeEach
    void beforeTest() {
        seekerRequirement.setJobId("FJB0001");
        seekerRequirement.setJobTitle("ApplicationDeveloper");
        seekerRequirement.setSkillSeeker(skillSeekerEntity);
        seekerRequirement.setSkillSeekerProjectEntity(skillSeekerProject);
        seekerRequirement.setJobDescription("Web designing application");
        seekerRequirement.setExpYears("5");
        seekerRequirement.setExpMonths("5");
        seekerRequirement.setJobLocation("Chicago");
        seekerRequirement.setExpiryDate(new Date(2022, Calendar.FEBRUARY, 12));
        seekerRequirement.setOriginalOfPositions(5);
        seekerRequirement.setPositionsAvailable(3);
        seekerRequirement.setRemote(500);
        seekerRequirement.setTravel(300);
        seekerRequirement.setBaseRate(75);
        seekerRequirement.setMaxRate(350);
        seekerRequirement.setFederalSecurityClearance(true);
        seekerRequirement.setScreeningQuestions(true);
        seekerRequirement.setHiringPriority(hiringPriority);
        seekerRequirement.setCoreTechnology("Java");
        seekerRequirement.setStatus("New");
        seekerRequirements.add(seekerRequirement);

    }

    @Test
    void getRequirementDetailsTest() {

        Mockito.when(requirementService.getDataById(Mockito.anyInt())).thenReturn(seekerRequirements);
        assertEquals(200, seekerRequirementController.getRequirementDetails(Mockito.anyInt()).getStatusCodeValue());

    }

    @Test
    void insertRequirementDetailsDataTest() {
        Mockito.when((requirementService.insertData(seekerRequirements))).thenReturn(jobDtoList);
        assertEquals(200, seekerRequirementController.insertRequirementDetailsData(seekerRequirements).getStatusCodeValue());

    }

    @Test
    void updateRequirementDetailsDataTest() {
        Mockito.when((requirementService.updateData(seekerRequirement))).thenReturn(jobDto);
        assertEquals(200, seekerRequirementController.updateRequirementDetailsData(seekerRequirement).getStatusCodeValue());

    }

    @Test
    void deleteDataTest() {
        seekerRequirementController.deleteData("1");
        seekerRequirementController.deleteData("2");
        Mockito.verify(requirementService, times(2)).deleteData(Mockito.anyString());
    }

}
