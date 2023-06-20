package com.flexcub.resourceplanning.job.dto;


import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import com.flexcub.resourceplanning.utils.FlexcubConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobDto {

    private String jobId;

    private String jobTitle;

    private String jobLocation;

    private List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity;

    private String coreTechnology;

    private String jobDescription;

    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    private SkillSeekerProject seekerProject;

    private String project;

    private OwnerSkillYearOfExperience ownerSkillYearOfExperience;

    private int numberOfPositions;

    private int remote;

    private int travel;

    private int baseRate = FlexcubConstants.BASE_RATE;

    private int maxRate = FlexcubConstants.MAX_RATE;

    private Boolean federalSecurityClearance;

    private Boolean screeningQuestions;

    private String status = FlexcubConstants.DRAFT;

    private HiringPriority hiringPriority;

    private String createdTime;

    private SkillSeeker skillSeeker;

    private String customTech;

    private String taxIdBusinessLicense;


}