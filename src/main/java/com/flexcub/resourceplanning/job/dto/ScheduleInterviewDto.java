package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleInterviewDto {
    private String jobId;
    private int skillOwnerId;
    private int stage;
    private String interviewedBy;
    private LocalDate dateOfInterview;
    private LocalTime timeOfInterview;
    private LocalTime endTimeOfInterview;
    private String modeOfInterview;
}
