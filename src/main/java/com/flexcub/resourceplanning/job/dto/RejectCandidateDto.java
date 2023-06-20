package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectCandidateDto {

    private String jobId;
    private int skillOwnerId;
    private int stage;
}
