package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeekerMSADetails {
    private int skillSeekerId;
    private String businessName;
    private LocalDate msaContractStartDate;
    private LocalDate msaContractExpiryDate;
    private String status;
    private int statusId;
    private int contractId;
    private int projectId;

    private String projectName;
    private String jobId;
    private String department;

    private int noOfResource;

    private String phoneNumber;

    private String email;


    public SeekerMSADetails(int skillSeekerId, String businessName, String jobId, LocalDate msaContractExpiryDate, LocalDate msaContractStartDate,
                            int projectId, String projectName, String status, int statusId, String department, int noOfResource,
                            String email, String phoneNumber) {
        this.skillSeekerId = skillSeekerId;
        this.businessName = businessName;
        this.jobId = jobId;
        this.msaContractExpiryDate = msaContractExpiryDate;
        this.msaContractStartDate = msaContractStartDate;
        this.projectId = projectId;
        this.projectName = projectName;
        this.status = status;
        this.statusId = statusId;
        this.department = department;
        this.noOfResource = noOfResource;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
