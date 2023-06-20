package com.flexcub.resourceplanning.skillpartner.dto;

import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobHistory {

    private String jobId;

    private String businessName;

    private String jobTitle;

    private String location;

    private String levelExperience;

    private String hiringStatus;

    private String expByName;

    private int currentStage;

    private List<RequirementPhase> requirementPhases;

}
