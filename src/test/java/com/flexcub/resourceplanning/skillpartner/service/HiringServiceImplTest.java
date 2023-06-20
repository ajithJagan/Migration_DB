package com.flexcub.resourceplanning.skillpartner.service;


import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;
import com.flexcub.resourceplanning.skillpartner.repository.HiringRepository;
import com.flexcub.resourceplanning.skillpartner.service.impl.HiringServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = HiringServiceImpl.class)
class HiringServiceImplTest {

    @MockBean
    HiringRepository hiringRepository;

    @Autowired
    HiringServiceImpl hiringServiceImpl;

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
    void insertDataTest() {
        when(hiringRepository.save(hiringEntity)).thenReturn(hiringEntity);
        assertThat(hiringServiceImpl.insertData(hiringEntity));
        assertEquals(hiringServiceImpl.insertData(hiringEntity).getHiringID(), hiringEntity.getHiringID());
    }

    @Test
    void getDataTest() {
        hiringEntityList.add(hiringEntity);
        hiringEntityList.add(hiringEntity);
        when(hiringServiceImpl.getData()).thenReturn(hiringEntityList);
        assertEquals(2, hiringEntityList.size());
        assertNotNull(hiringEntity.getHiringID(), "The id should not be null");
    }

    @Test
    void updateDataTest() {
        when(hiringServiceImpl.updateData(hiringEntity)).thenReturn(hiringEntity);
        assertEquals(1, hiringEntity.getHiringID());
        assertNotNull(hiringEntity.getHiringStatus());

    }

    @Test
    void deleteDataTest() {
        assertDoesNotThrow(() -> hiringServiceImpl.deleteData(hiringEntity.getHiringID()));
        Mockito.verify(hiringRepository, times(1)).deleteById(1);

    }
}