package com.flexcub.resourceplanning.skillseekeradmin.dto;


import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.utils.FlexcubConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SeekerRequirement {
    private String jobId;
    private String jobTitle;
    private SkillSeekerEntity skillSeeker;
    private SkillSeekerProjectEntity skillSeekerProjectEntity;
    private String jobDescription;
    private String expYears;
    private String expMonths;
    private String jobLocation;
    private Date expiryDate;
    private int originalOfPositions;
    private int positionsAvailable;
    private int remote;
    private int travel;
    private int baseRate = FlexcubConstants.BASE_RATE;
    private int maxRate = FlexcubConstants.MAX_RATE;
    private Boolean federalSecurityClearance;
    private Boolean screeningQuestions;
    private HiringPriority hiringPriority;
    private String coreTechnology;
    private String Status;
    private String taxIdBusinessLicense;
}
