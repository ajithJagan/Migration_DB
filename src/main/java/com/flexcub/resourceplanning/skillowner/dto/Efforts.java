package com.flexcub.resourceplanning.skillowner.dto;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Efforts {

    private SkillSeekerTaskEntity skillSeekerTaskEntity;

    private String hours;
}
