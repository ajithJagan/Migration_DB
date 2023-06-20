package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerSkillSet;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;

import java.util.List;

public interface OwnerSkillSetService {

    List<SkillOwnerSkillSet> getSkillset(int skillOwnerId);

    SkillOwnerSkillSet insertSkillSet(SkillOwnerSkillSet ownerSkillSetDto);

    String deleteSkillset(int skillId);

    SkillOwnerSkillSet updateSkillSet(SkillOwnerSkillSet ownerSkillSetDto);

    int skillSetPercentage(String jobId, List<OwnerSkillSetEntity> ownerSkillSetEntities);

}
