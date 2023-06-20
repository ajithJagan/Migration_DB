package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OwnerSkillDomainRepository extends JpaRepository<OwnerSkillDomainEntity, Integer> {

    OwnerSkillDomainEntity findByDomainValuesIgnoreCase(String domainValues);
}
