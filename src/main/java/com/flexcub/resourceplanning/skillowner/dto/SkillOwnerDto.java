package com.flexcub.resourceplanning.skillowner.dto;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerPortfolio;
import com.flexcub.resourceplanning.skillowner.entity.VisaEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SkillOwnerDto {
    private int skillOwnerEntityId;

    private int skillPartnerId;

    private String firstName;

    private String lastName;

    private LocalDate dOB;

    private String primaryEmail;

    private String alternateEmail;

    private String phoneNumber;

    private String alternatePhoneNumber;

    private OwnerSkillStatusEntity ownerSkillStatusEntity;

    private VisaEntity visaStatus;

    private String city;

    private String state;

    private String address;

    private String linkedIn;

    private int rateCard;

    private boolean accountStatus;

    private String status;

    private SkillOwnerMaritalStatus maritalStatus;

    private SkillOwnerGender gender;

    private String aboutMe;

    private String ssn;

    private String permanentAddress;

    private List<SkillOwnerPortfolio> portfolioUrl;

    private int expMonths;

    private int expYears;

//    private String statusVisa;

    private java.util.Date visaStartDate;

    private java.util.Date visaEndDate;

    private java.util.Date federalCheckStartDate;

    private java.util.Date federalCheckEndDate;

//    private boolean USC;
//
//    private boolean usAuthorization;

    private boolean federalSecurityClearance;

    private byte[] image;

    private byte[] resume;


    private String permanentCity;

    private String permanentState;

}
