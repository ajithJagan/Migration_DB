//package com.flexcub.resourceplanning.skillseeker.controller;
//
//import com.flexcub.resourceplanning.skillseeker.entity.City;
//import com.flexcub.resourceplanning.skillseeker.service.CityService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(classes = CityController.class)
//class CityControllerTest {
//
//    @Autowired
//    CityController cityController;
//
//    @MockBean
//    CityService cityService;
//
//    City city = new City();
//
//    List<City> cities = new ArrayList<>();
//
//    @BeforeEach
//    void setup() {
//        city.setId(1);
//        city.setCityNames("alabama");
//        city.setStateCode("al");
//    }
//
//    @Test
//    void getCityTest() {
//
//        cities.add(city);
//        cities.add(city);
//        Mockito.when((cityService.getData())).thenReturn(cities);
//        assertEquals(200, cityController.getCityDetails().getStatusCodeValue());
//
//    }
//
//    @Test
//    void loadCityDataTest() {
//        cityController.loadCityData();
//        Mockito.verify(cityService, Mockito.times(1)).loadData();
//    }
//
//}