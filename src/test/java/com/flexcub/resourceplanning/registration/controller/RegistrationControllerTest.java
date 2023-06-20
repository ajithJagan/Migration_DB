package com.flexcub.resourceplanning.registration.controller;

import com.flexcub.resourceplanning.registration.controller.RegistrationController;
import com.flexcub.resourceplanning.registration.dto.*;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.service.ForgotPasswordService;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RegistrationController.class)
class RegistrationControllerTest {
    private final String token = "r-ugVMRYcuNxXAu2tvbS";
    private final String email = "Sukumarm111@gmail.com";
    Roles roles = new Roles();
    RegistrationEntity registration = new RegistrationEntity();
    Registration registrationDto=new Registration();
    Login login=new Login();
    Verify verify=new Verify();
    WorkForceStrength workForceStrength = new WorkForceStrength();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    ChangePasswordDto changePasswordDto = new ChangePasswordDto();

    SetOwnerPassword setOwnerPassword=new SetOwnerPassword();
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private RegistrationController registrationController;

    @BeforeEach
    void setRegistration() {
        roles.setRolesId(1L);
        workForceStrength.setId(1);
        registration.setId(1);
        registration.setRoles(roles);
        registration.setBusinessName("Business Name");
        registration.setFirstName("First");
        registration.setLastName("Last");
        registration.setEmailId("Sukumarm111@gmail.com");
        registration.setPassword("June@123");
        registration.setToken("Xz1U3jQL6aZGFeQmjuL3");
        registration.setAccountStatus(false);
        registration.setMailStatus("Sent");
        registration.setTaxIdBusinessLicense("112233");
        registration.setContactPhone("9884104947");
        registration.setContactEmail("sukumarm121@gmai.com");
        registration.setBusinessPhone("9884108899");
        registration.setWorkForceStrength(workForceStrength);
        registration.setCity("city");
        registration.setState("state");
        registration.setDomainId(1);
        registration.setTechnologyIds("1");
        registration.setAddress("Address");
        registration.setExcelId("1");


        verify.setEmailId("sukumarm111@gmail.com");
        verify.setPassword("June@123");
        verify.setToken("Xz1U3jQL6aZGFeQmjuL3");

        changePasswordDto.setEmailId("sukumarm111@gmail.com");
        changePasswordDto.setOldPassword("June@123");
        changePasswordDto.setNewPassword("123456");

        setOwnerPassword.setEmailId("sukumarm111@gmail.com");
        setOwnerPassword.setToken("Xz1U3jQL6aZGFeQmjuL3");
        setOwnerPassword.setPassword("June@123");
    }

    @Test
    void insertDetailsTest() {
        Mockito.when(registrationService.insertDetails(registration)).thenReturn(registrationDto);
        assertEquals(200, registrationController.insertDetails(registration).getStatusCodeValue());
        assertEquals(registrationController.insertDetails(registration).getBody(), registrationDto);
        assertFalse(registrationController.insertDetails(registration).getBody().isAccountStatus());
    }

    @Test
    void insertDetailsNullTest() {
        Mockito.when(registrationService.insertDetails(registration)).thenReturn(null);
        assertNull(registrationController.insertDetails(registration).getBody());
    }

    @Test
    void getLoginDetailsTest() {
        Mockito.when(registrationService.getLoginDetails(login)).thenReturn(registrationDto);
        assertEquals(200, registrationController.getLoginDetails(login).getStatusCodeValue());
        assertEquals(registrationController.getLoginDetails(login).getBody().getId(), registrationDto.getId());
    }

    @Test
    void getLoginDetailsNullTest() {
        Mockito.when(registrationService.getLoginDetails(login)).thenReturn(null);
        assertNull(registrationController.getLoginDetails(login).getBody());
    }

    @Test
    void verifyCandidateTest() {
        registration.setToken("Xz1U3jQL6aZGFeQmjuL3");
        Mockito.when(registrationService.verifyRegistration(verify)).thenReturn(registrationDto);
        assertEquals(200, registrationController.verifyCandidate(verify).getStatusCodeValue());
    }

    @Test
    void verifyCandidateNullTest() {
        Mockito.when(registrationService.verifyRegistration(verify)).thenReturn(null);
        assertNull(registrationController.verifyCandidate(verify).getBody());
    }

    @Test
    void verifyForgotPassTest() {
        changePasswordDto.setForgotPassToken("Xz1U3jQL6aZGFeQmjuL3");
        changePasswordDto.setNewPassword("password");
        Mockito.when(registrationService.verifyForgottenPassword(changePasswordDto)).thenReturn(true);
        assertEquals(200, registrationController.verifyForgotPass(changePasswordDto).getStatusCodeValue());
    }

    @Test
    void getStrengthTest() {
        List<WorkForceStrength> workForceStrengths = new ArrayList<>();
        workForceStrengths.add(workForceStrength);
        workForceStrengths.add(workForceStrength);
        Mockito.when(registrationService.getData()).thenReturn(workForceStrengths);
        assertEquals(200, registrationController.getStrength().getStatusCodeValue());
        assertEquals(2, registrationController.getStrength().getBody().size());
    }

    @Test
    void getStrengthNullTest() {
        Mockito.when(registrationService.getData()).thenReturn(null);
        assertNull(registrationController.getStrength().getBody());
    }

    @Test
    void verifyRegistrationForOwnerTest() {
        Mockito.when(registrationService.verifyRegistrationForOwner(token)).thenReturn(skillOwnerEntity);
        assertEquals(200, registrationController.verifyRegistrationForOwner(token).getStatusCodeValue());
        assertEquals(registrationController.verifyRegistrationForOwner(token).getBody().getAddress(), skillOwnerEntity.getAddress());
    }

    @Test
    void verifyRegistrationForOwnerNullTest() {
        Mockito.when(registrationService.verifyRegistrationForOwner(token)).thenReturn(null);
        assertNull(registrationController.verifyRegistrationForOwner(token).getBody());
    }

    @Test
    void setPasswordForOwnerTest() {
        Mockito.when(registrationService.setPasswordForOwner(setOwnerPassword)).thenReturn(registrationDto);
        assertEquals(200, registrationController.setPasswordForOwner(setOwnerPassword).getStatusCodeValue());
        assertEquals(registrationController.setPasswordForOwner(setOwnerPassword).getBody().getPassword(), registrationDto.getPassword());
        assertFalse(registrationController.setPasswordForOwner(setOwnerPassword).getBody().isAccountStatus());
    }

    @Test
    void setPasswordForOwnerNullTest() {
        Mockito.when(registrationService.setPasswordForOwner(setOwnerPassword)).thenReturn(null);
        assertNull(registrationController.setPasswordForOwner(setOwnerPassword).getBody());
    }

    @Test
    void changePasswordTest() {
        Mockito.when(registrationService.changePassword(changePasswordDto)).thenReturn(true);
        assertEquals(200, registrationController.changePassword(changePasswordDto).getStatusCodeValue());
        assertEquals(Boolean.TRUE, registrationController.changePassword(changePasswordDto).getBody());
    }

    @Test
    void setForgotPasswordTest() {
        Mockito.when(registrationService.forgotPassword(email)).thenReturn(true);
        assertEquals(200, registrationController.setForgotPassword(email).getStatusCodeValue());
        assertEquals(Boolean.TRUE, registrationController.setForgotPassword(email).getBody());

    }

    @Test
    void sendMailForFailedOwnerRegistrationsTest() {
        registrationController.sendMailForFailedOwnerRegistrations(Mockito.any());
        Mockito.verify(registrationService, Mockito.times(1)).sendMailForFailedOwnerRegistrations(Mockito.any());

    }
}
