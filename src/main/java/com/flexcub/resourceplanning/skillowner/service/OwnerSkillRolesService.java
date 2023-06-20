package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;

import java.util.List;

public interface OwnerSkillRolesService {
    List<OwnerSkillRoles> getDataroles();

    OwnerSkillRoles insertDataroles(OwnerSkillRoles ownerSkillRolesDto);

    OwnerSkillRoles updateSkillRoles(OwnerSkillRoles ownerSkillRolesDto);
}
