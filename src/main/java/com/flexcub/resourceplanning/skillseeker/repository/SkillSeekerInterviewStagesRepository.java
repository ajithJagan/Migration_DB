package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerInterviewStagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillSeekerInterviewStagesRepository extends JpaRepository<SkillSeekerInterviewStagesEntity, Integer> {

}
