package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "skill_seeker")
@Where(clause = "deleted_at is null")
public class SkillSeekerEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private int id; //regId

    @Column
    private String skillSeekerName;

    @OneToOne(targetEntity = OwnerSkillDomainEntity.class)
    @JoinColumn(name = "owner_skill_domain_entity_id")
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @Column(columnDefinition = "text")
    private String addressLine1;

    @Column(columnDefinition = "text")
    private String addressLine2;

    @Column
    private String state;

    @Column
    private String city;

    @Column(length = 20)
    private int zipCode;

    @Column(length = 20, columnDefinition = "text")
    private String phone;

    @Column(columnDefinition = "text")
    private String email;

    @Column
    private String primaryContactFullName;

    @Column(columnDefinition = "text")
    private String primaryContactEmail;

    @Column(length = 10, columnDefinition = "text")
    private String primaryContactPhone;

    private String secondaryContactFullName;

    @Column(columnDefinition = "text")
    private String secondaryContactEmail;

    @Column(length = 10, columnDefinition = "text")
    private String secondaryContactPhone;

    @Column
    private String status;

    @Column
    private boolean isAddedByAdmin;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private String taxIdBusinessLicense;

    @OneToOne(targetEntity = SubRoles.class)
    @JoinColumn(name = "sub_role")
    private SubRoles subRoles;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Transient
    @Column
    private boolean registrationAccess;
    @OneToOne(targetEntity = ContractFiles.class,fetch = FetchType.EAGER)
    private ContractFiles msaId;
    @Column
    private LocalDate msaStartDate;
    @Column
    private LocalDate msaEndDate;
    @OneToOne(targetEntity = ContractStatus.class)
    private ContractStatus msaStatusId;

}
