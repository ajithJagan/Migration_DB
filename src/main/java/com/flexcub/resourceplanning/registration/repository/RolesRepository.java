package com.flexcub.resourceplanning.registration.repository;

import com.flexcub.resourceplanning.registration.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
}
