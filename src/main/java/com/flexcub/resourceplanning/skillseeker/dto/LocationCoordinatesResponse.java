package com.flexcub.resourceplanning.skillseeker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LocationCoordinatesResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("local_names")
    private List<String> localNames;

    @JsonProperty("lat")
    private double lat;

    @JsonProperty("lon")
    private double lon;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;
}
