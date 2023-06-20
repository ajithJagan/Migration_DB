package com.flexcub.resourceplanning.job.repository;

import com.flexcub.resourceplanning.job.entity.FeedbackRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackRate,Integer> {
}
