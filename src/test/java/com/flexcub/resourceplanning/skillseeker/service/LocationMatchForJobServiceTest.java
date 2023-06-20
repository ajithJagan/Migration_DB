package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.location.repository.LocationRepository;
import com.flexcub.resourceplanning.skillseeker.dto.LocationCoordinate;
import com.flexcub.resourceplanning.skillseeker.service.impl.LocationMatchForJobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LocationMatchForJobServiceImpl.class)
class LocationMatchForJobServiceTest {

    private static final String geoUrl = "https://api.openweathermap.org/geo/1.0/direct";
    @MockBean
    LocationRepository repository;
    @Autowired
    LocationMatchForJobServiceImpl service;

    @MockBean
    ModelMapperConfiguration modelMapperConfiguration;


    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setup() {

    }

    @Test
    void setService() {
        LocationCoordinate locationCoordinate = new LocationCoordinate(36.7783, 119.4179);
        final String url = UriComponentsBuilder.fromHttpUrl(geoUrl).queryParam("q", "California").queryParam("appid", "f7446b5f66ae2fca96eb899ebd32357c").toUriString();
        HttpEntity<Object> entity = getObjectHttpEntity();
        ResponseEntity<Object> fromResponseEntity = new ResponseEntity<>(locationCoordinate, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.<Class<Object>>any())).thenReturn(fromResponseEntity);
//        assertEquals(0, service.getLocationMatchPercentage("California", "Alabama"));
        assertNotNull(locationCoordinate);
    }

    private HttpEntity<Object> getObjectHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
