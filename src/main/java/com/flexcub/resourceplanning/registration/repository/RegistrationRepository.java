package com.flexcub.resourceplanning.registration.repository;

import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {

    RegistrationEntity findByEmailIdIgnoreCase(String emailId);

    RegistrationEntity findByToken(String token);

    RegistrationEntity findById(Long rolesId);

}
