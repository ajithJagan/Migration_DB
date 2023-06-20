package com.flexcub.resourceplanning.job.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlowLockedDto {

    private String jobId;
    private List<String> flow;
    private List<Integer> percentageExpected;
    private Boolean locked;

}
