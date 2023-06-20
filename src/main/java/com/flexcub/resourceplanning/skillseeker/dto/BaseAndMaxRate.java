package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseAndMaxRate {

    private int id;

    private String baseRate = "$45";

    private String maxRate = "$500";
}
