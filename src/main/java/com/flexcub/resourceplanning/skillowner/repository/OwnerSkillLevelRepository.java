package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerSkillLevelRepository extends JpaRepository<OwnerSkillLevelEntity, Integer> {
    OwnerSkillLevelEntity findBySkillLevelDescriptionIgnoreCase(String skillLevelDescription);
}
