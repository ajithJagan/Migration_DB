package com.flexcub.resourceplanning.skillowner.entity;


import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "skill_owner")
@Getter
@Setter
@Where(clause = "deleted_at is null")
public class SkillOwnerEntity extends BaseEntity {

    @Id
    @Column(unique = true, name = "id")
    private int skillOwnerEntityId; //regId

    @OneToOne
    @JoinColumn(name = "skill_partner_id")
    private SkillPartnerEntity skillPartnerEntity;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column
    private LocalDate dOB;

    @NotNull
    @Column(columnDefinition = "text")
    private String primaryEmail;

    @Column(columnDefinition = "text")
    private String alternateEmail;

    @Column(length = 10, columnDefinition = "text")
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String alternatePhoneNumber;

    @OneToOne
    @JoinColumn(name = "skill_status_id")
    private OwnerSkillStatusEntity ownerSkillStatusEntity;

    @OneToOne
    @JoinColumn(name = "visa_status_id")
    private VisaEntity visaStatus;

    @Column
    private String city;

    @Column
    private String state;


    @Column(columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "text")
    private String linkedIn;

    @Column
    private Integer rateCard;

    @Column
    private boolean accountStatus = false;

    //New Fields
    @Column
    private String status;

    @OneToOne
    @JoinColumn
    private SkillOwnerMaritalStatusEntity maritalStatus;

    @OneToOne
    @JoinColumn
    private SkillOwnerGenderEntity gender;

    @Column(columnDefinition = "text")
    private String aboutMe;

    @Column
    private String ssn;

    @Column
    private String permanentAddress;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<SkillOwnerPortfolio> portfolioUrl;

    @Column
    private Integer expMonths;

    @Column
    private Integer expYears;

//    @Column(columnDefinition = "boolean default false")
//    private boolean usAuthorization;
//
//    @Column(columnDefinition = "boolean default false")
//    private boolean USC;
//
//    @Column(columnDefinition = "varchar(255) default 'Active' ")
//    private String statusVisa;

    @Column(name = "visa_start_date")
    private java.util.Date visaStartDate;

    @Column(name = "visa_end_date")
    private java.util.Date visaEndDate;

    @Column(name = "federal_start_date")
    private java.util.Date federalStartDate;

    @Column(name = "federal_end_date")
    private java.util.Date federalEndDate;

    @Column(columnDefinition = "boolean default false")
    private boolean federalSecurityClearance;

    @Column
    private java.util.Date startDate;

    @Column
    private java.util.Date endDate;

    @Column
    private java.util.Date onBoardingDate;

    @Column
    private boolean resumeAvailable = false;

    @Column
    private boolean imageAvailable = false;

    @Column
    private String permanentCity;
    @Column
    private String permanentState;
//    @Column
//    private String zipcode;
//
//    @Column
//    private String permanentZipcode;

}



