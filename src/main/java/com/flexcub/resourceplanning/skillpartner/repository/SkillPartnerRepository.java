package com.flexcub.resourceplanning.skillpartner.repository;

import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillPartnerRepository extends JpaRepository<SkillPartnerEntity, Integer> {

    SkillPartnerEntity findByExcelId(String excelId);
    @Query(value = "SELECT * FROM  skill_partner WHERE msa_id_id=?;", nativeQuery = true)
    Optional<SkillPartnerEntity> findByMsaId(int msaId);

    @Query(value = "SELECT * FROM public.skill_partner where msa_end_date = current_date;", nativeQuery = true)
    List<SkillPartnerEntity> findbyMsaEndDate();
    @Query(value="SELECT * FROM public.skill_partner WHERE msa_end_date = Current_Date + INTERVAL '90 days';", nativeQuery = true)
    List<SkillPartnerEntity> findByMsaForExpiringSoon();

    @Query(value="SELECT * FROM public.skill_partner WHERE id=? AND msa_status_id_id=17;",nativeQuery = true )
    Optional<SkillPartnerEntity> findByIdAndMsaStatusId(int skillPartnerId, int msaStatusId);
}