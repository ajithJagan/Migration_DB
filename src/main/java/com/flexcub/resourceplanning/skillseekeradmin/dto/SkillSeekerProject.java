package com.flexcub.resourceplanning.skillseekeradmin.dto;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
public class SkillSeekerProject {


    List<SkillSeekerTask> taskData;
    private int id;
    private SkillSeeker skillSeeker;
    private OwnerSkillDomainEntity ownerSkillDomainEntity;
    private String title;
    private String summary;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String secondaryContactEmail;
    private String secondaryContactPhone;
    private Date startDate;
    private Date endDate;
    private List<SkillSeekerTechnologyData> skillSeekerTechnologyData;

}
