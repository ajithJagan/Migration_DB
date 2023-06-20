package com.flexcub.resourceplanning.location.service;

import com.flexcub.resourceplanning.location.entity.Location;
import com.flexcub.resourceplanning.location.repository.LocationRepository;
import com.flexcub.resourceplanning.location.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LocationServiceTest {

    private static final String apiToken = "123";
    private static final String tokenUrl = "http://tokenurl.com";
    private static final String emailId = "test@gmail.com";
    private static final String stateUrl = "http://stateurl.com";
    private static final String cityUrl = "http://cityurl.com";
    @InjectMocks
    LocationServiceImpl locationService;
    MockRestServiceServer mockRestServiceServer;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private RestTemplate restTemplate;
    private Location location = new Location();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(locationService, "emailId", emailId);
        ReflectionTestUtils.setField(locationService, "apiToken", apiToken);
        ReflectionTestUtils.setField(locationService, "tokenUrl", tokenUrl);
        ReflectionTestUtils.setField(locationService, "stateUrl", stateUrl);
        ReflectionTestUtils.setField(locationService, "cityUrl", cityUrl);
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

        location.setToken("123");
        location.setApiToken("123");
        location.setEmailId("test@gmail.com");
        location.setId(1);
    }

    @Test
    void generateToken() {
        HashMap<String, String> jsonMap = new HashMap<>();
        jsonMap.put("auth_token", "123");
        when(locationRepository.findByEmailId(emailId)).thenReturn(location);
        when(restTemplate.exchange(tokenUrl, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), HashMap.class)).thenReturn(new ResponseEntity<>(jsonMap, HttpStatus.OK));
        when(locationRepository.save(Mockito.any())).thenReturn(location);
        assertEquals(locationService.generateToken(), jsonMap.get("auth_token"));
    }

    @Test
    void getStates() {
        HashMap<String, String> jsonMap = new HashMap<>();
        jsonMap.put("USA", "NY");
        List<HashMap<String, String>> myList = Collections.singletonList(jsonMap);
        when(locationRepository.findByEmailId(emailId)).thenReturn(location);
        when(restTemplate.exchange(stateUrl, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), List.class)).thenReturn(new ResponseEntity<>(myList, HttpStatus.OK));
        assertEquals(locationService.getStates().get(0), jsonMap);
    }

    @Test
    void getCities() {
        String state = "NY";
        HashMap<String, String> jsonMap = new HashMap<>();
        jsonMap.put("NY", "NY");
        List<HashMap<String, String>> myList = Collections.singletonList(jsonMap);
        when(locationRepository.findByEmailId(emailId)).thenReturn(location);
        when(restTemplate.exchange(cityUrl + state, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), List.class)).thenReturn(new ResponseEntity<>(myList, HttpStatus.OK));
        assertEquals(locationService.getCities(state).get(0), jsonMap);
    }

    private MultiValueMap<String, String> getObjectHttpEntity() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        headers.add("api-token", apiToken);
        headers.add("user-email", emailId);
        headers.add("Authorization", "Bearer " + location.getToken());
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
