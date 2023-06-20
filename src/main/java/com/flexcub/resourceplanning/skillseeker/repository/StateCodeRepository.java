package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateCodeRepository extends JpaRepository<StateEntity, Integer> {
}
