package com.flexcub.resourceplanning.registration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verify {

    private String emailId;
    private String password;
    private String token;
}
