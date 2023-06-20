package com.flexcub.resourceplanning.skillpartner.dto;

import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HistoryOfJobs {
    private String jobId;

    private String seekerName;

    private String jobTitle;

    private String location;

    private String levelExperience;

    private String contractStatus;

    private Date fromDate;

    private Date toDate;

    private String project;



}
