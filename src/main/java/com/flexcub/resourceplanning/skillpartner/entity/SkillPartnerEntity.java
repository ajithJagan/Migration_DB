package com.flexcub.resourceplanning.skillpartner.entity;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "skill_partner")
@Entity
@Where(clause = "deleted_at is null")
public class SkillPartnerEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private int skillPartnerId; // regId

    @Column(name = "businessName")
    private String businessName;

    @Column(name = "phone", columnDefinition = "text")
    private String phone;

    @Column(name = "businessEmail", columnDefinition = "text")
    private String businessEmail;

    @Column(name = "primaryContactFullName")
    private String primaryContactFullName;

    @Column(name = "primaryContactEmail", columnDefinition = "text")
    private String primaryContactEmail;

    @Column(name = "primaryContactPhone", columnDefinition = "text")
    private String primaryContactPhone;

    @Column(name = "secondaryContactFullName")
    private String secondaryContactFullName;

    @Column(name = "secondaryContactEmail", columnDefinition = "text")
    private String secondaryContactEmail;

    @Column(name = "secondaryContactPhone", columnDefinition = "text")
    private String secondaryContactPhone;

    @Column(name = "taxIdBusinessLicense", columnDefinition = "text")
    private String taxIdBusinessLicense;

    @Column(name = "addressLine1", columnDefinition = "text")
    private String addressLine2;

    @Column(name = "addressLine2", columnDefinition = "text")
    private String addressLine1;

    @Column(name = "state", columnDefinition = "text")
    private String state;

    @Column(name = "zipcode", columnDefinition = "text")
    private String zipcode;

    @Column
    private String excelId; //fresh_data_1

    @Column(columnDefinition = "Decimal(10,2) default '0.10'")
    private Double serviceFeePercentage=0.10;
    @OneToOne(targetEntity = ContractFiles.class)
    private ContractFiles msaId;
    @Column
    private LocalDate msaStartDate;
    @Column
    private LocalDate msaEndDate;
    @OneToOne(targetEntity = ContractStatus.class)
    private ContractStatus msaStatusId;

}


