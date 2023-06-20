package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillSeekerTaskRepository extends JpaRepository<SkillSeekerTaskEntity, Integer> {

    Optional<List<SkillSeekerTaskEntity>> findBySkillSeekerProjectId(int id);

    @Query(value = "SELECT * from public.skill_seeker_task Where skill_seeker_id =? and skill_seeker_project_id IS NULL;", nativeQuery = true)
    Optional<List<SkillSeekerTaskEntity>> findBySkillSeekerProjectIdAndSkillSeekerId(int skillSeekerId);
    @Query(value = "SELECT * from public.skill_seeker_task Where skill_seeker_project_id IS NULL;", nativeQuery = true)

    List<SkillSeekerTaskEntity> findByDefaultProject();
}

