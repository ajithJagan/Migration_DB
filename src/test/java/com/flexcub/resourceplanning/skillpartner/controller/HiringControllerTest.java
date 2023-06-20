package com.flexcub.resourceplanning.skillpartner.controller;

import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;
import com.flexcub.resourceplanning.skillpartner.service.HiringService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = HiringController.class)
class HiringControllerTest {

    @MockBean
    HiringService hiringService;

    @Autowired
    HiringController hiringController;

    HiringEntity hiringEntity = new HiringEntity();

    List<HiringEntity> hiringEntityList = new ArrayList<>();


    @BeforeEach
    public void setUp() {

        hiringEntity.setHiringID(1);
        hiringEntity.setRequirementID(1);
        hiringEntity.setCandidateID(1);
        hiringEntity.setHiringStatus("Active");
        hiringEntity.setHiringStartDt("11/02/2020");
        hiringEntity.setHiringEndDt("12/02/2020");
        hiringEntity.setInterviewStage("Initial");
        hiringEntity.setFeedBack1("Good");
        hiringEntity.setFeedBack2("Very Good");
        hiringEntity.setFeedBack3("Bad");

    }

    @Test
    void insertHiringDetailsDataTest() {
        Mockito.when(hiringService.insertData(hiringEntity)).thenReturn(hiringEntity);
        assertEquals(200, hiringController.insertHiringDetailsData(hiringEntity).getStatusCodeValue());
        assertEquals(hiringController.insertHiringDetailsData(hiringEntity).getBody().getHiringID(), hiringEntity.getHiringID());
        assertNotNull(hiringEntity.getHiringStatus());

    }

    @Test
    void getHiringDetailsTest() {

        hiringEntityList.add(hiringEntity);
        hiringEntityList.add(hiringEntity);
        Mockito.when((hiringService.getData())).thenReturn(hiringEntityList);
        Assertions.assertThat(hiringController.getHiringDetails().getBody()).hasSize(2);
        assertEquals(200, hiringController.getHiringDetails().getStatusCodeValue());
        assertNull(hiringEntity.getChangedAt());
    }

    @Test
    void updateHiringDetailsDataTest() {
        Mockito.when(hiringService.updateData(hiringEntity)).thenReturn(hiringEntity);
        assertEquals(200, hiringController.updateHiringDetailsData(hiringEntity).getStatusCodeValue());
        assertNotNull(hiringEntity);
        assertSame(1, hiringEntity.getCandidateID());
    }

    @Test
    void deleteDataTest() {
        hiringService.deleteData(1);
        Mockito.verify(hiringService, times(1)).deleteData(1);
    }
}