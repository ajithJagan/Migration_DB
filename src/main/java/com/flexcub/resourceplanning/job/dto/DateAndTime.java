package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class DateAndTime {

    private LocalDate dateOfInterview;
    private LocalTime timeOfInterview;
    private LocalTime endTimeOfInterview;
}
