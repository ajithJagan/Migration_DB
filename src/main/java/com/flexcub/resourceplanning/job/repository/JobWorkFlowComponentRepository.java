package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.JobWorkFlowComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobWorkFlowComponentRepository extends JpaRepository<JobWorkFlowComponent, Integer> {
    Optional<JobWorkFlowComponent> findByJobId(String jobId);
}
