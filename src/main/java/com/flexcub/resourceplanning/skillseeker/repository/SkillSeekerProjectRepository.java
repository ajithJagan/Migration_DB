package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillSeekerProjectRepository extends JpaRepository<SkillSeekerProjectEntity, Integer> {

    Optional<List<SkillSeekerProjectEntity>> findBySkillSeekerId(int id);



    @Query(value ="SELECT * FROM skill_seeker_project Where department=? AND skill_seeker_id=?;", nativeQuery = true )
    Optional<List<SkillSeekerProjectEntity>> findProjectByDepartmentAndSeekerId(int departmentId,int seekerId);



    @Query(value="SELECT * FROM skill_seeker_project where skill_seeker_id=?;", nativeQuery = true)
    List<SkillSeekerProjectEntity> findBySeekerId(int seekerId);

}
