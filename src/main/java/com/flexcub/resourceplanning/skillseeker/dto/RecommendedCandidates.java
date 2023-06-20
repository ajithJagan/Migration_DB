package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendedCandidates {

    private String jobId;
    private String jobTitle;
    private int skillOwnerId;
    private String skillOwnerName;
    private String skillOwnerExperience;
    private int skillSetMatchPercentage;
    private int locationMatchPercentage;
    private int rateMatchPercentage;
    private boolean verified;
    private boolean preScreen;
    private int overallMatchPercentage;
    private String skillOwnerEmailAddress;
    private String skillOwnerContact;
    private String skillOwnerResume;

    private boolean federalSecurityClearance;
    private boolean shortlist;
    private boolean accepted;
    private boolean isImageAvailable = false;
    private boolean isResumeAvailable = false;
}
