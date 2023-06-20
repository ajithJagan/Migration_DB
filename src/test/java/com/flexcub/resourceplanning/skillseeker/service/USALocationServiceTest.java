package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.skillseeker.entity.USALocationEntity;
import com.flexcub.resourceplanning.skillseeker.repository.USALocationRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.USALocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = USALocationServiceImpl.class)
class USALocationServiceTest {

    @MockBean
    USALocationRepository repository;

    @Autowired
    USALocationServiceImpl service;

    @MockBean
    ModelMapperConfiguration modelMapperConfiguration;

    USALocationEntity usaLocation = new USALocationEntity();
    List<USALocationEntity> locationList = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    String add = "Cal";


    @BeforeEach
    void setup() {
        usaLocation.setId(1);
        usaLocation.setCityAndState("Los Angeles, California");
        locationList.add(usaLocation);
        locationList.add(usaLocation);
        strings.add(add);
    }

    @Test
    void getLocationFromDatabaseTest() {
        Mockito.when(repository.findByCityAndStateLike(Mockito.anyString())).thenReturn(strings);
        assertEquals(1, service.getLocationFromDatabase("Cal").size());
    }

//    @Test
//    void loadDataForUSLocationTest() {
//        Mockito.when(repository.saveAll(locationList)).thenReturn(locationList);
//        assertEquals(true, service.loadDataForUSLocation());
//    }
}
