package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartnerContractDetails {

    private int skillPartnerId;
    private String project;
    private String department;
    private String jobId;
    private String noOfResource;
    private  String businessName;
    private String email;
    private String phone;
    private LocalDate contractStartDate;
    private LocalDate contractExpiryDate;
    private String status;
    private int statusId;
}
