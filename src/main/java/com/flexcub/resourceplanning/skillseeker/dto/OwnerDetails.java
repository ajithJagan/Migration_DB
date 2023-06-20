package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDetails {

    private int employeeId;

    private String employeeName;

    private String location;

    private String status;

    private Date joinedDate;

    private Date leavingDate;


}
