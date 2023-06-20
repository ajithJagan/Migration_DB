package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StageStatusDto {

    private String jobId;
    private int skillOwnerEntityId;
    private int stage;
    private String status;

    private boolean accepted;
}
