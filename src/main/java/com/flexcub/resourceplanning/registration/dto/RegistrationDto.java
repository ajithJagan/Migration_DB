package com.flexcub.resourceplanning.registration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

    private Long id;

    private String businessName;

    private String firstName;

    private String lastName;

    private String password;

    private RolesDto rolesDto;

}
