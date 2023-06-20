package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OnBoarding {
    private int skillOwnerEntityId;
    private int seekerId;
    private int projectId;
    private String projectName;
    private String jobId;
    private Date startDate;
    private Date endDate;
    private String status;


}
