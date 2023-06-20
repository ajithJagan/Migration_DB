package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillSeekerMsaRepository extends JpaRepository<SkillSeekerMSAEntity, Integer> {


    Optional<SkillSeekerMSAEntity> findById(int id);

    Optional<List<SkillSeekerMSAEntity>> findBySkillSeekerId(int id);


    @Query(value = "SELECT * FROM public.msa_files WHERE skill_seeker_id=? ", nativeQuery = true)
    List<SkillSeekerMSAEntity> findBySeekerId(int seekerID);


    @Query(value = "SELECT * FROM public.msa_files WHERE skill_owner_id=? ", nativeQuery = true)
    SkillSeekerMSAEntity findByOwnerId(int ownerID);

    @Query(value =
            "Select * from msa_files where job_id = ? and skill_owner_id = ?;", nativeQuery = true)
    Optional<SkillSeekerMSAEntity> findByJobIdAndSkillOwnerId(String jobId, int skillOwnerId);


}
