//package com.flexcub.resourceplanning.skillseeker.service;
//
//import com.flexcub.resourceplanning.skillseeker.entity.CityEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.CityRepository;
//import com.flexcub.resourceplanning.skillseeker.service.impl.CityServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//@SpringBootTest(classes = CityServiceImpl.class)
//class CityServiceTest {
//
//    @MockBean
//    CityRepository cityRepository;
//
//    @Autowired
//    CityServiceImpl cityService;
//
//    CityEntity city = new CityEntity();
//
//    List<CityEntity> cities = new ArrayList<>();
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
//        cities.add(city);
//        cities.add(city);
//        Mockito.when(cityRepository.findAll()).thenReturn(cities);
//        assertThat(cityService.getData()).isEqualTo(cities);
//    }
//
//    @Test
//    void testLoadData() {
//        Optional<CityEntity> optionalCity = Optional.empty();
//        Mockito.when(cityRepository.findByCityNamesAndStateCode(Mockito.any(), Mockito.any())).thenReturn(optionalCity);
//        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(new CityEntity());
//        assertDoesNotThrow(() -> cityService.loadData());
//
//
//    }
//}
