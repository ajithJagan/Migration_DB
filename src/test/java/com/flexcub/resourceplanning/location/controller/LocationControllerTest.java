package com.flexcub.resourceplanning.location.controller;

import com.flexcub.resourceplanning.location.entity.Location;
import com.flexcub.resourceplanning.location.service.LocationService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LocationController.class)
class LocationControllerTest {

    @Autowired
    LocationController locationController;
    @MockBean
    LocationService locationService;
    Location location = new Location();


    @Test
    @Order(1)
    void generateToken() {
        Mockito.when(locationService.generateToken()).thenReturn(location.getToken());
        assertEquals(200, locationController.generateToken().getStatusCodeValue());
    }

    @Test
    @Order(2)
    void getStates() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "NewYork");
        List<HashMap<String, String>> list = new ArrayList<>();
        list.add(map);
        Mockito.when(locationService.getStates()).thenReturn(list);
        assertEquals(200, locationController.getStates().getStatusCodeValue());
    }

    @Test
    @Order(3)
    void getCities() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "Gadsden");
        List<HashMap<String, String>> list = new ArrayList<>();
        list.add(map);
        Mockito.when(locationService.getCities("Alabama")).thenReturn(list);
        assertEquals(200, locationController.getCities("Alabama").getStatusCodeValue());
    }
}



