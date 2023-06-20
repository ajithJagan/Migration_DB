package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class RequirementPhases {

    private String requirementPhaseName;

    private int stage;

    private LocalDate interviewDate;
}
