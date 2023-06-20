package com.flexcub.resourceplanning.skillowner.repository;


import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    @Query(value = "Select * from work_details", nativeQuery = true)
    List<ClientEntity> findAllData();

    List<ClientEntity> findBySkillOwnerEntityId(int ownerd);
}
