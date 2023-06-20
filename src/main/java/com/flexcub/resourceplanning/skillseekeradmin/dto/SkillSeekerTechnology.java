package com.flexcub.resourceplanning.skillseekeradmin.dto;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class SkillSeekerTechnology {


    private int id;


    private OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity;


    private OwnerSkillRolesEntity ownerSkillRolesEntity;


    private OwnerSkillLevelEntity ownerSkillLevelEntity;


    private int baseRate;


    private int maxRate;


    private Date expiresOn;


    private String status;


}
