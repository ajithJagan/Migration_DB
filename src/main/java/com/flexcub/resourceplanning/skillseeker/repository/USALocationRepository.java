package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.USALocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface USALocationRepository extends JpaRepository<USALocationEntity, Integer> {

    @Query(value = "Select city_and_state from usa_location where city_and_state ILIKE ? ", nativeQuery = true)
    List<String> findByCityAndStateLike(String keyword);
}
