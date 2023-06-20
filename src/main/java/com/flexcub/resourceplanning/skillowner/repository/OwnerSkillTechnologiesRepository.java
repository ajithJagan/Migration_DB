package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerSkillTechnologiesRepository extends JpaRepository<OwnerSkillTechnologiesEntity, Integer> {


    OwnerSkillTechnologiesEntity findByTechnologyValuesIgnoreCase(String values);

}
