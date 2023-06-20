package com.flexcub.resourceplanning.skillowner.repository;


import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerMaritalStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerMaritalStatusRepository extends JpaRepository<SkillOwnerMaritalStatusEntity, Integer> {
}
