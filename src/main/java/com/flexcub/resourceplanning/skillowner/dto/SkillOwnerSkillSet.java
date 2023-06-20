package com.flexcub.resourceplanning.skillowner.dto;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillOwnerSkillSet {

    private int ownerSkillSetId;

    private int skillOwnerEntityId;

    private OwnerSkillLevelEntity ownerSkillLevelEntity;

    private OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity;

    private OwnerSkillRolesEntity ownerSkillRolesEntity;

    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    private String experience;

    private String lastUsed;
}
