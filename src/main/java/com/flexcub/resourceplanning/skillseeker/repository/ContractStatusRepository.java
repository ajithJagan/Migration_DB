package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus,Integer> {
    @Query(value = "SELECT * FROM public.contract_status WHERE id=? ", nativeQuery = true)
    ContractStatus findByStatusId(int id);
}
