package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerSlotsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillOwnerSlotsRepository extends JpaRepository<SkillOwnerSlotsEntity, Integer> {

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner_slots\n" +
            "\tWHERE skill_owner_entity_id=?;", nativeQuery = true)
    SkillOwnerSlotsEntity findBySkillOwnerEntityId(int skillOwnerEntityId);

    @Query(value = "SELECT date_slots_by_owner1,date_slots_by_owner2,date_slots_by_owner3\n" +
            "FROM public.skill_owner_slots WHERE skill_owner_entity=?;", nativeQuery = true)
    List<Object> findDatesBySkillOwnerId(int skillOwnerId);

    @Query(value = "SELECT time_slots_by_owner1,time_slots_by_owner2,time_slots_by_owner3\n" +
            "FROM public.skill_owner_slots WHERE skill_owner_entity=?;", nativeQuery = true)
    List<Object> findTimeBySkillOwnerId(int skillOwnerId);

}