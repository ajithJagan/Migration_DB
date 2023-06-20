package com.flexcub.resourceplanning.location.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.location.entity.Location;
import com.flexcub.resourceplanning.location.repository.LocationRepository;
import com.flexcub.resourceplanning.location.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Component
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Value("${location.api.token}")
    private String apiToken;
    @Value("${token.url}")
    private String tokenUrl;
    @Value("${location.emailId}")
    private String emailId;
    @Value("${state.url}")
    private String stateUrl;
    @Value("${city.url}")
    private String cityUrl;

    public LocationServiceImpl(LocationRepository locationRepository, RestTemplate restTemplate) {
        this.locationRepository = locationRepository;
        this.restTemplate = restTemplate;
    }

    private MultiValueMap<String, String> getObjectHttpEntity() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        headers.add("api-token", apiToken);
        headers.add("user-email", emailId);
        Location location = locationRepository.findByEmailId(emailId);
        try {
            headers.add("Authorization", "Bearer " + location.getToken());
        } catch (Exception e) {
            throw new ServiceException(TOKEN_NOT_FOUND.getErrorCode(), TOKEN_NOT_FOUND.getErrorDesc());
        }
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }


    @Override
    public String generateToken() {
        ResponseEntity<HashMap> response = null;
        try {
            response = restTemplate.exchange(tokenUrl, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), HashMap.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(response)) {
            HashMap jsonToken = response.getBody();
            String token = "";
            if (Objects.nonNull(jsonToken)) {
                token = (String) jsonToken.get("auth_token");
            }
            try {
                Optional<Location> oldToken = oldToken = Optional.ofNullable(locationRepository.findByEmailId(emailId));
                Location newToken;
                if (oldToken.isPresent()) {
                    newToken = oldToken.get();
                    newToken.setToken(token);
                } else {
                    newToken = new Location();
                    newToken.setToken(token);
                    newToken.setEmailId(emailId);
                    newToken.setApiToken(apiToken);
                }
                locationRepository.save(newToken);
            } catch (Exception e) {
                logger.error("LocationServiceImpl || generateToken || Exception while saving token in DB", e);
            }

            return token;

        } else {
            throw new RestClientException("Token is null");
        }
    }

    @Override
    public List<HashMap<String, String>> getStates() {

        HttpEntity<List> response = null;
        try {
            response = restTemplate.exchange(stateUrl, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), List.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        logger.debug("LocationServiceImpl || getStates || Response: {}", response);
        if (Objects.nonNull(response)) {
            try {
                return response.getBody();
            } catch (Exception e) {
                throw new ServiceException(STATES_NOT_FOUND.getErrorCode(), STATES_NOT_FOUND.getErrorDesc());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<HashMap<String, String>> getCities(String state) {
        try {
            HttpEntity<List> response = restTemplate.exchange(cityUrl + state, HttpMethod.GET, new HttpEntity<>(getObjectHttpEntity()), List.class);
            logger.debug("LocationServiceImpl || getCities || Response: {}", response);
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException(CITIES_NOT_FOUND.getErrorCode(), CITIES_NOT_FOUND.getErrorDesc());
        }
    }

    @Scheduled(fixedRateString = "PT23H")
    private Object tokenScheduler() {
        logger.debug("LocationServiceImpl || tokenScheduler || The token getting from database at time: {}", LocalDateTime.now());
        return generateToken();

    }
}

