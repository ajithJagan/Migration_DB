package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OnboardingRequest {
    private int skillOwnerEntityId;
    private int seekerId;
    private String jobId;
    private Date startDate;
    private Date endDate;

}
