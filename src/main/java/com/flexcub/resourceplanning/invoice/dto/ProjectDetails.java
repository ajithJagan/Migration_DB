package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProjectDetails {

    private String projectName;
    private String seekerName;
    private String job;
    private int noOfResources;
    private Date start_date;
    private Date end_date;
    private String address;

}
