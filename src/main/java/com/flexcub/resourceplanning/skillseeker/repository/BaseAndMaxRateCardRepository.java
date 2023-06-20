package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.BaseAndMaxRateCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAndMaxRateCardRepository extends JpaRepository<BaseAndMaxRateCardEntity, Integer> {

}
