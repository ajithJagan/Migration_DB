package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerSkillYearOfExperienceRepository extends JpaRepository<OwnerSkillYearOfExperience, Integer> {
    @Query(value = "select ownerSkill.skill_level_description,array_to_string(array_agg(yearExp.experience), ',')\n" +
            "\tfrom owner_skill_level ownerSkill, years_of_exp yearExp \n" +
            "where\n" +
            "   ownerSkill.id = yearExp.owner_skill_level_id \n" +
            "\tgroup by ownerSkill.skill_level_description;", nativeQuery = true)
    public List<Object[]> getAll();

    OwnerSkillYearOfExperience findByExperience(String exp);
}
