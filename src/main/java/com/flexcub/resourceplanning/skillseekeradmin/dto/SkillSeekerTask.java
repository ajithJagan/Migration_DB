package com.flexcub.resourceplanning.skillseekeradmin.dto;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillSeekerTask {

    private int taskId;

    private SkillSeekerProjectEntity skillSeekerProjectEntity;

    private String taskTitle;

    private String taskDescription;

    private int skillSeekerId;
}