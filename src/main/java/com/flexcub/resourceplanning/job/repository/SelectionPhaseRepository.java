package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SelectionPhaseRepository extends JpaRepository<SelectionPhase, Integer> {

    List<SelectionPhase> findByJobJobId(String jobId);


    @Query(value =
            "Select * from selection_phase where job_job_id = ? and skill_owner_entity = ?;", nativeQuery = true)
    Optional<SelectionPhase> findByJobIdAndSkillOwnerId(String jobId, int skillOwnerId);

    @Query(value =
            "Select * from selection_phase where skill_owner_entity = ?;", nativeQuery = true)
    Optional<List<SelectionPhase>> findBySkillOwnerId(int skillOwnerId);


    @Query(value = "select * from selection_phase where accepted is true;", nativeQuery = true)
    List<SelectionPhase> findByAcceptanceTrue();

    @Query(value = "Select * from selection_phase where skill_owner_entity = ?;", nativeQuery = true)
    List<SelectionPhase> findByCurrentStage(int ownerId);

    @Query(value = "SELECT * FROM selection_phase Where job_job_id =?;", nativeQuery = true)
    List<SelectionPhase> deleteByJobId(String jobId);


    @Query(value = "select * from selection_phase where job_job_id =?;", nativeQuery = true)
    Optional<List<SelectionPhase>> findByJobId(String jobId);

    @Query(value =
            "Select * from selection_phase where skill_owner_entity = ?;", nativeQuery = true)
    Optional<SelectionPhase> findByOwnerId(int skillOwnerId);

    @Query(value="SELECT * FROM selection_phase WHERE job_job_id=?;",nativeQuery = true)
    List<SelectionPhase> findOwnerByJobId(String jobId);


    @Query(value = "Select * from selection_phase where skill_owner_entity = ? and job_job_id=?;", nativeQuery = true)
    Optional<List<SelectionPhase>> findBySkillOwnerIdAndJobId(int skillOwnerId,String jobId);
}
