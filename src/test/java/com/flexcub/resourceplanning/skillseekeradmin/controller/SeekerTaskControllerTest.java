package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTaskService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = SeekerTaskController.class)
class SeekerTaskControllerTest {

    @Autowired
    SeekerTaskController seekerTaskController;

    @MockBean
    SkillSeekerTaskService seekerTask;

    SkillSeekerTask skillSeekerTaskTest = new SkillSeekerTask();

    List<SkillSeekerTask> skillSeekerTaskTests = new ArrayList<>();

    @BeforeEach
    void beforeTest() {

        skillSeekerTaskTest.setTaskId(1);
        skillSeekerTaskTest.setTaskTitle("Task1");
        skillSeekerTaskTest.setTaskDescription("Task Work Flow");
        skillSeekerTaskTests.add(skillSeekerTaskTest);
    }

    @Test
    void addSeekerTaskDetailsTest() {

        Mockito.when(seekerTask.insertData(skillSeekerTaskTests)).thenReturn(skillSeekerTaskTests);
        assertEquals(200, seekerTaskController.insertSeekerTaskDetails(skillSeekerTaskTests).getStatusCodeValue());
    }


    @Test
    void updateSeekerTaskDetailsTest() {
        Mockito.when(seekerTask.updateData(skillSeekerTaskTest)).thenReturn(skillSeekerTaskTest);
        assertEquals(200, seekerTaskController.updateSeekerTaskDetails(skillSeekerTaskTest).getStatusCodeValue());
    }

    @Test
    void deleteSeekerTaskDetailsTest() throws Exception {
        seekerTaskController.deleteSeekerTaskDetails(1);
        seekerTaskController.deleteSeekerTaskDetails(2);
        Mockito.verify(seekerTask, times(2)).deleteData(Mockito.anyInt());
    }

    @Test
    void seekerTaskDetailsTest() {
        Mockito.when(seekerTask.getTaskData(Mockito.anyInt(),Mockito.anyInt())).thenReturn(skillSeekerTaskTests);
        assertEquals(200, seekerTaskController.seekerTaskDetails(Mockito.anyInt(),Mockito.anyInt()).getStatusCodeValue());
    }
}

