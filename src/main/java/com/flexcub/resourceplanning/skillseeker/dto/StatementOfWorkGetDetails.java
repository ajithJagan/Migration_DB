package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatementOfWorkGetDetails {

    private int id;
    private int ownerId;
    private String skillOwnerName;
    private String jobId;
    private String role;
    private String project;
    private String department;
    private String email;
    private String phone;
    private String status;

}

