package com.flexcub.resourceplanning.skillpartner.repository;

import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkForceStrengthRepo extends JpaRepository<WorkForceStrength, Integer> {
}
