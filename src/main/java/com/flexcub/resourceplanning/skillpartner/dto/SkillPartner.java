package com.flexcub.resourceplanning.skillpartner.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SkillPartner {


    private int skillPartnerId;

    private String businessName;

    private String phone;

    private String businessEmail;

    private String primaryContactFullName;

    private String primaryContactEmail;

    private String primaryContactPhone;

    private String secondaryContactFullName;

    private String secondaryContactEmail;

    private String secondaryContactPhone;

    private String taxIdBusinessLicense;

    private String addressLine2;

    private String addressLine1;

    private String state;

    private String zipcode;

    private String excelId;

    private double serviceFeePercentage;
}
