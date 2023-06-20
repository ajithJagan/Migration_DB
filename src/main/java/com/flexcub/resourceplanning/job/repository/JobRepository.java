package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    Optional<Job> findByJobId(String jobId);


    List<Job> findBySkillSeekerId(int skillSeekerId);

    @Query(value =
            "SELECT job_owner_skill_technologies_entity.owner_skill_technologies_entity_id \n" +
                    "FROM job_owner_skill_technologies_entity WHERE job_job_id = ?;", nativeQuery = true)
    List<Integer> findAllTechnologyIdByJobId(String jobId);

    @Query(value =
            "Select * from job where job_id = ?;", nativeQuery = true)
    Job findByJobJobId(String jobId);

    Optional<List<Job>> findByTaxIdBusinessLicense(String taxId);


    List<Job> findBySeekerProjectId(int projectId);

    @Query(value = "SELECT * FROM job WHERE skill_seeker_id=? AND project_id=?;",nativeQuery = true)
    List<Job> findBySeekerIdAndProjectId(int seekerId, int projectId);


    @Query(value = "SELECT * FROM job WHERE project_id=?;",nativeQuery = true)
    Optional<Job> findJobByProjectId(int projectId);
}

