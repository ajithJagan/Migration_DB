package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.HiringPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiringPriorityRepository extends JpaRepository<HiringPriority, Integer> {
}
