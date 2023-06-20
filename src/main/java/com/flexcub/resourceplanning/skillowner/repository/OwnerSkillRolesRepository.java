package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerSkillRolesRepository extends JpaRepository<OwnerSkillRolesEntity, Integer> {
    OwnerSkillRolesEntity findByRolesDescriptionIgnoreCase(String rolesDescription);
}
