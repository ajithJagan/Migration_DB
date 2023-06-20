package com.flexcub.resourceplanning.location.repository;

import com.flexcub.resourceplanning.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    Location findByEmailId(String emailId);
}
