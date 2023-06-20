package com.flexcub.resourceplanning.skillseeker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
public class PoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column
    private int skillSeekerId;

    @ManyToOne(targetEntity = SkillSeekerProjectEntity.class)
    @JsonIgnoreProperties("skill_seeker_project")
    @JoinColumn(name = "skill_seeker_project_id", referencedColumnName = "id", updatable = false)
    private SkillSeekerProjectEntity skillSeekerProject;

    @ManyToOne(targetEntity = SkillOwnerEntity.class)
    @JoinColumn(name = "skill_owner_id", referencedColumnName = "id", updatable = false)
    private SkillOwnerEntity skillOwnerEntity;

    @ManyToOne(targetEntity = OwnerSkillDomainEntity.class)
    @JoinColumn(name = "skill_domain_id", referencedColumnName = "id", updatable = false)
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "job_id", referencedColumnName = "job_Id", updatable = false)
    private Job jobId;

    @Column
    private String role;

    @OneToOne(targetEntity = ContractStatus.class)
    private ContractStatus poStatus;
    @Column
    private Date dateOfRelease;
    @Column
    private Date expiryDate;
    @Column
    private Date onBoarding;
    private LocalDate startDate;
    @Column
    private LocalDate endDate;

    @OneToOne(targetEntity = ContractFiles.class, fetch = FetchType.LAZY)
    private ContractFiles poId;

    @Column
    private LocalDate contractStartDate;

    @Column
    private LocalDate contractEndDate;

    @Column
    private String priceRange;

    @Column(columnDefinition = "integer default 0")
    private int rateCard;

    @Column
    private Integer numberOfResources;

    @Column
    private Integer amountForEachResource;


}
