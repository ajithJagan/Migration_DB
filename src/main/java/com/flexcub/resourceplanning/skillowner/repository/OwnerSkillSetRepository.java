package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerSkillSetRepository extends JpaRepository<OwnerSkillSetEntity, Integer> {

    List<OwnerSkillSetEntity> findBySkillOwnerEntityId(int skillOwnerId);

    @Query(value =
            "SELECT owner_skill_set_owner_skill_technologies_entity.owner_skill_technologies_entity_id \n" +
                    "FROM owner_skill_set_owner_skill_technologies_entity WHERE owner_skill_set_entity_owner_skill_set_entity_id=?;", nativeQuery = true)
    List<Integer> findAllTechnologyIdByOwnerSkillSetId(int ownerSkillSetId);

    @Query(value = "DELETE FROM owner_skill_set_new WHERE owner_skill_set_entity_id=?;", nativeQuery = true)
    void deleteBySkillId(int id);

    @Query(value = "SELECT * FROM owner_skill_set_new WHERE owner_skill_technology_id=? AND skill_owner_id = ?;", nativeQuery = true)
    OwnerSkillSetEntity findBySKillByTechAndOwnerID(int techId, int skillOwnerId);

}
