package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RescheduleDto {

    private String jobId;
    private int skillOwnerId;
    private int currentStage;
    private LocalDate dateOfInterview;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;



}
