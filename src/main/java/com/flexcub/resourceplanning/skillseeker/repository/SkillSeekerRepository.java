package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillSeekerRepository extends JpaRepository<SkillSeekerEntity, Integer> {

    //    @Query(value = "SELECT e FROM  skill_seeker e WHERE e.is_added_by_admin=?1", nativeQuery = true)
    Optional<List<SkillSeekerEntity>> findByIsAddedByAdminTrue();

    @Query(value = "SELECT e FROM  skill_seeker e WHERE e.sub_role_id=1", nativeQuery = true)
    SkillSeekerEntity findBySubRolesId(Long subRoleId);

    @Query(value = "SELECT * FROM  skill_seeker  WHERE tax_id_business_license=? AND sub_role_id=?;", nativeQuery = true)
    List<SkillSeekerEntity> findByTaxIdBusinessLicenseAndSubRoles(String taxIdBusinessLicenseId, Long subRoles);

    Optional<List<SkillSeekerEntity>> findByTaxIdBusinessLicense(String taxIdBusinessLicense);


    @Query(value = "SELECT * FROM skill_seeker WHERE  tax_id_business_license=? AND id =?;", nativeQuery = true)
    Optional<SkillSeekerEntity> findByTaxIdBusinessLicenseAndSeekerId(String taxIdBusinessLicense, int id);

    SkillSeekerEntity findByEmail(String emailId);

    @Query(value = "SELECT * FROM public.skill_seeker where msa_end_date = current_date;", nativeQuery = true)
    List<SkillSeekerEntity> findbyMsaEndDate();
    @Query(value="SELECT * FROM public.skill_seeker WHERE msa_end_date = Current_Date + INTERVAL '30 days';", nativeQuery = true)
    List<SkillSeekerEntity> findByMsaForExpiringSoon();

    @Query(value = "SELECT * FROM  skill_seeker WHERE msa_id_id=?;", nativeQuery = true)
    Optional<SkillSeekerEntity> findByMsaId(int id);
}
