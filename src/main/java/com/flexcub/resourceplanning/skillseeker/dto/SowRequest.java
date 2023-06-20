package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class SowRequest {

    private int ownerId;
    private int seekerId;
    private String jobId;
    private LocalDate sowStartDate;
    private LocalDate sowEndDate;


}
