package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerSkillStatusRepository extends JpaRepository<OwnerSkillStatusEntity, Integer> {

    Optional<OwnerSkillStatusEntity> findByStatusDescriptionIgnoreCase(String statusDescription);
}
