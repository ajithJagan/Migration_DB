package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillSeekerRateCardRepository extends JpaRepository<SkillSeekerTechnologyData, Integer> {

    @Query(value = "SELECT s.* FROM skill_seeker_technology_data s WHERE s.skill_seeker_technology_data_id=  ?1", nativeQuery = true)
    List<SkillSeekerTechnologyData> findByProjectId(int id);

}
