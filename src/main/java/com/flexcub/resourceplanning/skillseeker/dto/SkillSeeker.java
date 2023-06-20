package com.flexcub.resourceplanning.skillseeker.dto;

import com.flexcub.resourceplanning.contracts.dto.ContractsFilesDto;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SkillSeeker {

    List<SeekerModulesEntity> moduleAccess;
    private int id;
    private String skillSeekerName;
    private OwnerSkillDomainEntity ownerSkillDomainEntity;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private int zipCode;
    private String phone;
    private String email;
    private String primaryContactFullName;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String secondaryContactFullName;
    private String secondaryContactEmail;
    private String secondaryContactPhone;
    private String status;
    private boolean isAddedByAdmin;
    private SubRoles subRoles;
    private String accessModule;
    private String taxIdBusinessLicense;
    private boolean isActive;
    private boolean registrationAccess;
    private ContractsFilesDto contractFiles;

}
