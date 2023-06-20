package com.flexcub.resourceplanning.registration.repository;

import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, Integer> {
    ForgotPasswordToken findByForgotToken(String forgotToken);

}
