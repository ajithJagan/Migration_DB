package com.flexcub.resourceplanning.registration.service;


import com.flexcub.resourceplanning.registration.dto.*;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;

import java.util.List;
import java.util.Map;

public interface RegistrationService {

    Registration getLoginDetails(Login registration);

    Registration insertDetails(RegistrationEntity registration);


    Registration verifyRegistration(Verify registration);

    List<WorkForceStrength> getData();

    void sendMailForFailedOwnerRegistrations(Map<Integer, List<String>> failedMap);

    SkillOwnerEntity verifyRegistrationForOwner(String token);

    Registration setPasswordForOwner(SetOwnerPassword skillOwnerRegistrationEntity);

    boolean changePassword(ChangePasswordDto changePasswordDto);

    boolean forgotPassword(String emailId);

    boolean verifyForgottenPassword(ChangePasswordDto changePasswordDto);


    void resendMail(int id);

    RegistrationEntity sendRegistrationConfirmationEmail(RegistrationEntity  registration);

    Registration superAdminLoginScreen(Login registration);
}
