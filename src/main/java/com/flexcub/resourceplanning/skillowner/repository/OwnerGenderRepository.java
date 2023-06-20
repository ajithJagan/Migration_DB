package com.flexcub.resourceplanning.skillowner.repository;


import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerGenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerGenderRepository extends JpaRepository<SkillOwnerGenderEntity, Integer> {

}
