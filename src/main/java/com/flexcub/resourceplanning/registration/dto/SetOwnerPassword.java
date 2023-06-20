package com.flexcub.resourceplanning.registration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetOwnerPassword {

    private String emailId;
    private String token;
    private String password;

}
