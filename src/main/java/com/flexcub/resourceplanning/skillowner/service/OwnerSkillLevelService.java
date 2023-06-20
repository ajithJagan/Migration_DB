package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillLevel;

import java.util.List;

public interface OwnerSkillLevelService {
    List<OwnerSkillLevel> getDatalevel();

    OwnerSkillLevel insertDatalevel(OwnerSkillLevel ownerSkillLevelDto);

    OwnerSkillLevel updateSkillLeveL(OwnerSkillLevel ownerSkillLevelDto);
}
