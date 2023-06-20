package com.flexcub.resourceplanning.skillowner.repository;


import com.flexcub.resourceplanning.skillowner.entity.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaStatusRepository extends JpaRepository<VisaEntity, Integer> {
    VisaEntity findByVisaStatusIgnoreCase(String visaStatus);
}
