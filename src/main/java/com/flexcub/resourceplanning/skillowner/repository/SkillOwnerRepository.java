package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillOwnerRepository extends JpaRepository<SkillOwnerEntity, Integer> {

    SkillOwnerEntity findByPrimaryEmail(String primaryEmail);

    SkillOwnerEntity findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner\n" +
            "\tWHERE id=?;", nativeQuery = true)
    SkillOwnerEntity findBySkillOwnerEntityId(int skillOwnerEntityId);

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner\n" +
            "\tWHERE skill_status_id = 1 AND account_status = true;", nativeQuery = true)
    List<SkillOwnerEntity> getAvailableTalents();

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner\n" +
            "\tWHERE skill_partner_id=?;", nativeQuery = true)
    Optional<List<SkillOwnerEntity>> findBySkillPartnerId(int partnerId);

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner\n" +
            "\tWHERE ssn=?;", nativeQuery = true)
    Optional<List<SkillOwnerEntity>> findBySsn(String ssn);

    @Query(value = "SELECT * FROM skill_owner WHERE id=?;",nativeQuery = true )
    Optional<SkillOwnerEntity> findBySkillOwnerId(int ownerId);
}