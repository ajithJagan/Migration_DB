package com.flexcub.resourceplanning.job.dto;

import com.flexcub.resourceplanning.job.entity.FeedbackRate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsertRequirementPhaseDto {

    private String jobId;
    private List<String> requirementPhases;
//    private List<FeedbackRate> percentageRequired;

}
