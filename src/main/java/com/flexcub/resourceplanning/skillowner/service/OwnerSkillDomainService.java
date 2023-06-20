package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillDomain;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;

import java.util.List;

public interface OwnerSkillDomainService {
    List<OwnerSkillDomain> getDatadomain();

    OwnerSkillDomain insertDataDomain(OwnerSkillDomain ownerSkillDomainDto);

    OwnerSkillDomain updateDomain(OwnerSkillDomain ownerSkillDomainDto);

    OwnerSkillDomainEntity getById(int id);
}
