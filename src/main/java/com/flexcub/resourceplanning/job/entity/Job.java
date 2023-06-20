package com.flexcub.resourceplanning.job.entity;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import com.flexcub.resourceplanning.utils.FlexcubConstants;
import com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Job")
@Where(clause = "deleted_at is null")
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "add_id")
    @GenericGenerator(name = "add_id", strategy = "com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "FJB-"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")})
    @Column(name = "job_Id", unique = true)
    private String jobId;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "job_location")
    private String jobLocation;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = OwnerSkillTechnologiesEntity.class)
    @JoinColumn(name = "owner_skill_technology_id")
    private List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity;

    @Column(name = "description", length = 1265, columnDefinition = "text")
    private String jobDescription;

    @OneToOne(targetEntity = OwnerSkillDomainEntity.class)
    @JoinColumn(name = "department")
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private SkillSeekerProjectEntity seekerProject;

    private String project;

    @OneToOne(targetEntity = OwnerSkillYearOfExperience.class)
    @JoinColumn(name = "experience_Id")
    private OwnerSkillYearOfExperience ownerSkillYearOfExperience;

    @Column(name = "number_of_positions")
    private int numberOfPositions;

    private int originalNumberOfPosition;

    @Column(name = "remote")
    private int remote;

    @Column(name = "travel")
    private int travel;

    @Column(name = "base_rate")
    private int baseRate = FlexcubConstants.BASE_RATE;

    @Column(name = "max_rate")
    private int maxRate = FlexcubConstants.MAX_RATE;

    @Column(name = "federal_security_clearance")
    private Boolean federalSecurityClearance;

    @Column(name = "screening_questions")
    private Boolean screeningQuestions;

    @Column(name = "status")
    private String status = FlexcubConstants.DRAFT;

//    @Column(name = "hiring_priority")
//    private String hiringPriority;

    @OneToOne(targetEntity = HiringPriority.class)
    @JoinColumn(name = "hiring_priority_id")
    private HiringPriority hiringPriority;

    private String coreTechnology;

    @OneToOne
    @JoinColumn(nullable = false)
    private SkillSeekerEntity skillSeeker;

    @Column
    private String taxIdBusinessLicense;

    @Column
    private Date expiryDate;

    @Column
    private String customTech;

}
