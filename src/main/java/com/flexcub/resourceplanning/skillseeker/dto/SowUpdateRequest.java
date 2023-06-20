package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class SowUpdateRequest {

    private int sowId;
    private LocalDate sowStartDate;
    private LocalDate sowEndDate;
}
