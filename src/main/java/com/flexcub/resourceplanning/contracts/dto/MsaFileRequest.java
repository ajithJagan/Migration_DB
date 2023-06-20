package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MsaFileRequest {

    private int seekerId;
    private LocalDate contractStartDate;
    private LocalDate contractExpiryDate;

}
