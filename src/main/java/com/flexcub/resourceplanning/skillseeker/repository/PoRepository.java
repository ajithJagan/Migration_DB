package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import liquibase.pro.packaged.P;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoRepository extends JpaRepository<PoEntity, Integer> {

    Optional<PoEntity> findById(int id);

    Optional<List<PoEntity>> findBySkillSeekerId(int skillSeekerId);
    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_owner_id=? AND job_id=? ", nativeQuery = true)
    Optional<PoEntity> findBySkillOwnerIdAndJobId(int SkillOwnerId, String JobId);


    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_owner_id=? ", nativeQuery = true)
    Optional<PoEntity> findByOwnerId(int ownerId);


    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_domain_id=? AND skill_owner_id=? " +
            "AND skill_seeker_id=? AND skill_seeker_project_id=? AND job_id=? ", nativeQuery = true)
    Optional<PoEntity> findByAllFields(int domainId, int ownerId, int seekerId, int projectID, String jobId);

    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_owner_id=? ", nativeQuery = true)
    PoEntity findByOwner(int ownerId);

    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_owner_id=? ", nativeQuery = true)
    Optional<PoEntity> findBySkillOwnerId(int SkillOwnerId);

    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_owner_id=? ", nativeQuery = true)
    Optional<List<PoEntity>> findListBySkillOwnerId(int SkillOwnerId);

    @Query(value = "SELECT * FROM public.purchase_order WHERE skill_seeker_project_id=? ", nativeQuery = true)
    Optional<PoEntity> findByProjectId(int SkillSeekerProjectId);


    @Query(value = "SELECT  count(skill_owner_id) FROM purchase_order where skill_seeker_project_id =?;", nativeQuery = true)
    int findResource(int id);


    @Query(value = " Select * FROM public.purchase_order WHERE purchase_order.date_of_release < CURRENT_DATE- interval '14 days'; ", nativeQuery = true)
    List<PoEntity> findByDateOfRelease();


    @Query(value = "select * from purchase_order where skill_owner_id =? and deleted_at is not null ;",nativeQuery = true)
    Optional<PoEntity> findByDeletedAt(int ownerId);

    @Query(value="select * from purchase_order where skill_owner_id =? and deleted_at is null ;",nativeQuery = true)
    Optional<PoEntity> findByDeleteAtNull(int ownerId);


    @Query(value = "SELECT * FROM public.purchase_order WHERE job_id=? ", nativeQuery = true)
    Optional<PoEntity> findByJobId(String jobId);

    @Query(value="SELECT * FROM public.purchase_order WHERE po_id_id=?", nativeQuery = true)
    Optional<PoEntity> findByPoId(int poId);
}
