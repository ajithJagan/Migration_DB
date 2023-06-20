package com.flexcub.resourceplanning.skillpartner.repository;

import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiringRepository extends JpaRepository<HiringEntity, Integer> {
}
