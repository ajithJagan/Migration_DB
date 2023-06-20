package com.flexcub.resourceplanning.registration.service;

import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import org.springframework.stereotype.Service;

@Service
public interface ForgotPasswordService {

    ForgotPasswordToken createForgotPasswordToken();

    void saveToken(ForgotPasswordToken forgotToken);

    ForgotPasswordToken findByForgotToken(String forgotToken);

    void removeForgotToken(ForgotPasswordToken forgotToken);
}
