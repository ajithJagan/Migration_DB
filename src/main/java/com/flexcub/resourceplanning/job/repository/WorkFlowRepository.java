package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.WorkFlowComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkFlowRepository extends JpaRepository<WorkFlowComponent, Integer> {
}

