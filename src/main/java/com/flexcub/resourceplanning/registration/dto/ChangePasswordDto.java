package com.flexcub.resourceplanning.registration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private String emailId;
    private String oldPassword;
    private String newPassword;
    private String forgotPassToken;
}
