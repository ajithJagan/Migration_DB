package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequirementPhaseRepository extends JpaRepository<RequirementPhase, Integer> {
    @Query(value =
            "Select * from requirement_phase where job_id = ? AND " +
                    "skill_owner_id = ? AND stage = ?;", nativeQuery = true)
    Optional<RequirementPhase> findByJobIdSkillOwnerIdAndStage(String jobId, int skillOwnerId, int stage);

    @Query(value =
            "Select * from requirement_phase where job_id = ? AND " +
                    "skill_owner_id = ?;", nativeQuery = true)
    Optional<List<RequirementPhase>> findByJobIdSkillOwnerId(String jobId, int skillOwnerId);

    Optional<List<RequirementPhase>> findByJobId(String id);

    RequirementPhase findByJobIdAndSkillOwnerId(String jobId, int skillOwnerId);

    List<RequirementPhase> deleteByJobId(String jobId);


    @Query(value =
            "SELECT EXISTS ( SELECT * FROM requirement_phase WHERE skill_owner_id = ? AND job_id = ?);", nativeQuery = true)
    Boolean ifRecordExistByOwnerId(int skillOwnerId, String jobId);

    @Query(value =
            "Select * from requirement_phase where skill_owner_id = ?;", nativeQuery = true)
    Optional<List<RequirementPhase>> findBySkillOwnerId(int ownerId);

    @Query(value = "SELECT *\n" +
            "\tFROM public.requirement_phase\n" +
            "\tWHERE skill_owner_id=?", nativeQuery = true)
    List<RequirementPhase> findByDateAndTime(int skillOwnerId);

    @Query(value="Select * from requirement_phase where skill_owner_id=? and job_id=? ;",nativeQuery = true)
    List<RequirementPhase> findBySkillOwnerIdAndJobIdOrderByStageDesc(int ownerId, String jobId);



//    @Query(value = "SELECT date_of_interview, time_of_interview,end_time_of_interview\n" +
//            "\tFROM public.requirement_phase where skill_owner_id=?;",nativeQuery = true)
//    List<RequirementPhase> findInterviewBySkillOwner(int skillOwner);

}
