package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Client {

    private int clientId;

    private int skillOwnerEntityId;

    private String employerName;

    private String jobTitle;

    private String project;

    private String projectDescription;

    private String department;

    private Date startDate;

    private Date endDate;

    private Boolean currentEmployer;

    private String location;
}
