package com.flexcub.resourceplanning.registration.controller;

import com.flexcub.resourceplanning.exceptions.ControllerException;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.registration.dto.*;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.service.ForgotPasswordService;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.utils.FlexcubErrorCodes;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.VERIFY_OWNER;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.VERIFY_USER;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_SAVED;


@RestController
@RequestMapping(value = "/v1/registration")
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "InternalServer Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})

public class RegistrationController {

    RegistrationEntity registration = new RegistrationEntity();
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping(value = "/createAccount", produces = {"application/json"})
    public ResponseEntity<Registration> insertDetails(@RequestBody RegistrationEntity registration) {
        return new ResponseEntity<>(registrationService.insertDetails(registration), HttpStatus.OK);
    }

    @PostMapping(value = "/loginScreen", produces = {"application/json"})
    public ResponseEntity<Registration> getLoginDetails(@RequestBody Login registration) {
        return new ResponseEntity(registrationService.getLoginDetails(registration), HttpStatus.OK);
    }

    @PostMapping(value = "/superAdminLoginScreen", produces = {"application/json"})
    public ResponseEntity<Registration> superAdminLoginScreen(@RequestBody Login registration) {
        return new ResponseEntity(registrationService.superAdminLoginScreen(registration), HttpStatus.OK);
    }

    @PostMapping(value = VERIFY_USER, produces = {"application/json"})
    public ResponseEntity<?> verifyCandidate(@RequestBody Verify registration) {
        if (!registration.getToken().isEmpty()) {
            return ResponseEntity.ok(registrationService.verifyRegistration(registration));
        }

        ControllerException controllerException = new ControllerException(FlexcubErrorCodes.PARAM_NOT_PROVIDED.getErrorCode()
                , FlexcubErrorCodes.PARAM_NOT_PROVIDED.getErrorDesc());
        return new ResponseEntity<>(controllerException, HttpStatus.BAD_REQUEST);

    }

    @PostMapping(value = "/verifyForgotPassword", produces = {"application/json"})
    public ResponseEntity<?> verifyForgotPass(@RequestBody ChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getForgotPassToken().isEmpty() && !changePasswordDto.getNewPassword().isEmpty()) {
            return ResponseEntity.ok(registrationService.verifyForgottenPassword(changePasswordDto));
        }
        throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
    }

    @GetMapping(value = "/getStrengthTable", produces = {"application/json"})
    public ResponseEntity<List<WorkForceStrength>> getStrength() {
        return new ResponseEntity<>(registrationService.getData(), HttpStatus.OK);
    }

    @GetMapping(value = VERIFY_OWNER, produces = {"application/json"})
    public ResponseEntity<SkillOwnerEntity> verifyRegistrationForOwner(@RequestParam String token) {
        return new ResponseEntity<>(registrationService.verifyRegistrationForOwner(token), HttpStatus.OK);
    }

    @PutMapping(value = "/setOwnerPassword", produces = {"application/json"})
    public ResponseEntity<Registration> setPasswordForOwner(@RequestBody SetOwnerPassword skillOwnerRegistrationEntity) {
        return new ResponseEntity<>(registrationService.setPasswordForOwner(skillOwnerRegistrationEntity), HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword", produces = {"application/json"})
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {

        return new ResponseEntity<Boolean>(registrationService.changePassword(changePasswordDto), HttpStatus.OK);
    }

    @GetMapping(value = "/forgotPassword", produces = {"application/json"})
    public ResponseEntity<Boolean> setForgotPassword(@RequestParam String emailId) {
        return new ResponseEntity<Boolean>(registrationService.forgotPassword(emailId), HttpStatus.OK);
    }


    @PostMapping(value = "/failedOwnerRegistration", produces = {"application/json"})
    public void sendMailForFailedOwnerRegistrations(@RequestParam Map<Integer, List<String>> failedMap) {
        try {
            registrationService.sendMailForFailedOwnerRegistrations(failedMap);
            new ResponseEntity<>("OwnerRegistration Failed ", HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {

            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    @PutMapping(value = "/reSendMail",produces = {"application/json"})
    public void resendMail(@RequestParam int id){
        registrationService.resendMail(id);

    }



}
