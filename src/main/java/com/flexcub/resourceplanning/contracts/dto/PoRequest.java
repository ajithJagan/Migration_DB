package com.flexcub.resourceplanning.contracts.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PoRequest {
    int seekerId;
    int ownerId;
    String jobId;
    LocalDate contractStartDate;
    LocalDate contractEndDate;
}

