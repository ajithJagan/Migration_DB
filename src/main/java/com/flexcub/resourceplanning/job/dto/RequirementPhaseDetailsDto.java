package com.flexcub.resourceplanning.job.dto;

import com.flexcub.resourceplanning.job.entity.FeedbackRate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RequirementPhaseDetailsDto {

    private String jobId;
    private int skillOwnerId;
    private LocalDate dateOfInterview;
    private LocalTime timeOfInterview;
    private int stage;
    private String status;
    private String interviewedBy;
    private String feedback;
    private String modeOfInterview;
    private FeedbackRate candidatePercentage;
}
