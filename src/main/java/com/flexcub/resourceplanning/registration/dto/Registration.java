package com.flexcub.resourceplanning.registration.dto;

import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Registration {

    private int id;

    private String businessName;

    private String firstName;

    private String lastName;

    private Roles roles;

    private String emailId;

    private String password;

    private WorkForceStrength workForceStrength;

    private String city;

    private String state;

    private String mailStatus;

    private boolean accountStatus = false;

    private String contactPhone;

    private int subRoles;

    private List<SeekerModulesEntity> modulesAccess;

    private String taxIdBusinessLicense;

    private Long loginCount;

    private boolean timeSheetAccess;
}
