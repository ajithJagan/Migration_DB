package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "skill_seeker_project")
@Where(clause = "deleted_at is null")
public class SkillSeekerProjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "skill_seeker_id")
    private SkillSeekerEntity skillSeeker;

    @OneToOne(targetEntity = OwnerSkillDomainEntity.class)
    @JoinColumn(name = "department")
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(nullable = false)
    private String primaryContactEmail;

    @Column
    private String primaryContactPhone;

    @Column
    private String secondaryContactEmail;

    @Column
    private String secondaryContactPhone;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn
    private List<SkillSeekerTechnologyData> skillSeekerTechnologyData;

}
