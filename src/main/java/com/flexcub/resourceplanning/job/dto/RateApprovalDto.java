package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateApprovalDto {


    private int skillOwnerId;

    private String jobId;

    private Integer rate;
}
