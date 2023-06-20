package com.flexcub.resourceplanning.skillseeker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "statement_of_work")
@Getter
@Setter
public class StatementOfWorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(targetEntity = SkillOwnerEntity.class)
    @JoinColumn(name = "skill_owner_id", referencedColumnName = "id", updatable = false)
    private SkillOwnerEntity skillOwnerEntity;

    @Column
    private int skillSeekerId;

    @ManyToOne(targetEntity = SkillPartnerEntity.class)
    @JoinColumn(name = "partner_Id", referencedColumnName = "id", updatable = false)
    private SkillPartnerEntity skillPartnerEntity;

    @ManyToOne(targetEntity = SkillSeekerProjectEntity.class)
    @JsonIgnoreProperties("skill_seeker_project")
    @JoinColumn(name = "skill_seeker_project_id", referencedColumnName = "id", updatable = false)
    private SkillSeekerProjectEntity skillSeekerProject;

    @ManyToOne(targetEntity = OwnerSkillDomainEntity.class)
    @JoinColumn(name = "domain_id", referencedColumnName = "id")
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "job_id", referencedColumnName = "job_Id", updatable = false)
    private Job jobId;

    @Column
    private String roles;

    @Column
    private LocalDate sowStartDate;
    @Column
    private LocalDate sowEndDate;

    @OneToOne(targetEntity = ContractStatus.class)
    @JoinColumn
    private ContractStatus sowStatus;

    @Column
    private LocalDate dateOfRelease;

    @OneToOne(targetEntity = ContractFiles.class,fetch = FetchType.LAZY)
    private ContractFiles sowId;

    @Column
    private Integer numberOfResources;

    @Column
    private Integer amountForEachResource;

    @Column
    private Integer totalResourcesRate;
}
