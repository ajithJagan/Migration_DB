package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.skillseeker.dto.LocationCoordinate;
import com.flexcub.resourceplanning.skillseeker.service.LocationMatchForJobService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
public class LocationMatchForJobServiceImpl implements LocationMatchForJobService {

    @Autowired
    ModelMapperConfiguration modelMapperConfiguration;
    RestTemplate restTemplate = new RestTemplate();
    @Value("${geo.url}")
    private String geoUrl;

    @Override
    public int getLocationMatchPercentage(String jobLocation, String ownerLocation) {
        log.info("TalentRecommendationServiceImpl || getLocationMatchPercentage ||Finding Match percentage for location");
        LocationCoordinate jobLocationCoordinateDto = getLatitudeAndLongitude(jobLocation);
        LocationCoordinate ownerLocationCoordinateDto = getLatitudeAndLongitude(ownerLocation);

        if (null == jobLocationCoordinateDto || null == ownerLocationCoordinateDto) {
            return 0;
        }

        double conversionFactor = 1.609344;
        double currentMiles = haversine(jobLocationCoordinateDto.getLatitude(), jobLocationCoordinateDto.getLongitude()
                , ownerLocationCoordinateDto.getLatitude(), ownerLocationCoordinateDto.getLongitude()) / conversionFactor;

        if (currentMiles < 10) {
            return 100;
        } else if (currentMiles > 4000) {
            return 5;
        }
        double result = 95.226 - currentMiles * (0.0226);
        return (int) Math.round(result);

    }

    private LocationCoordinate getLatitudeAndLongitude(String locationName) {
        log.info("TalentRecommendationServiceImpl || getLatitudeAndLongitude ||getting coordinate for location {}", locationName);

        HttpEntity<Object> entity = getObjectHttpEntity();
        final String url = UriComponentsBuilder.fromHttpUrl(geoUrl).queryParam("q", locationName)
                .queryParam("appid", "f7446b5f66ae2fca96eb899ebd32357c").toUriString();
        ResponseEntity<Object> fromResponse;
        try {
            fromResponse = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            log.error("Unable to connect to API");
            return null;
        }
        JSONObject fromJsonObject = new JSONObject(fromResponse);
        JSONArray fromJsonArray = fromJsonObject.getJSONArray("body");

        if (fromJsonArray.isEmpty()) {
            log.error("Unable to find coordinate of {} from API", locationName);
            return null;
        }
        JSONObject fromJsonObject1 = fromJsonArray.getJSONObject(0);
        Double fromLatitude = (Double) fromJsonObject1.get("lat");
        Double fromLongitude = (Double) fromJsonObject1.get("lon");

        return new LocationCoordinate(fromLongitude, fromLatitude);

    }

    private Double haversine(double latit1, double long1, double latit2, double long2) {
        double dLat = Math.toRadians(latit2 - latit1);
        double dLon = Math.toRadians(long2 - long1);

        latit1 = Math.toRadians(latit1);
        latit2 = Math.toRadians(latit2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(latit1) * Math.cos(latit2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    private HttpEntity<Object> getObjectHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

}
