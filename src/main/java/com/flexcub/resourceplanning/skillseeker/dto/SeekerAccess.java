package com.flexcub.resourceplanning.skillseeker.dto;

import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeekerAccess {

    private String accessId;

    private String taxIdBusinessLicense;

    private SubRoles subRoles;

    private SeekerModulesEntity seekerModulesEntity;

    private boolean isActive;

}
