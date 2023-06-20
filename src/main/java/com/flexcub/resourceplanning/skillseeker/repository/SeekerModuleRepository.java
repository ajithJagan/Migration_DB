package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeekerModuleRepository extends JpaRepository<SeekerModulesEntity, Integer> {

    @Query(value = "SELECT seeker_modules_entity_id FROM seeker_access WHERE sub_role_id = ? ", nativeQuery = true)
    List<Integer> getAllModule(int id);
}
