package com.flexcub.resourceplanning.skillpartner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDetails {

    private int employeeId;

    private String employeeName;

    private String location;

    private String levelExperience;

    private Boolean status;

    private Date joinedDate;

    private Date leavingDate;
    private String ownerStatus;

    private Date dateOfShortListing;

   private Integer rate;




}
