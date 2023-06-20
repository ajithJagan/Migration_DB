package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SeekerMsaFileRequest {

    private int seekerId;
    private LocalDate contractStartDate;
    private LocalDate contractExpiryDate;
    private String name;
    private String email;
    private String phoneNumber;



}
