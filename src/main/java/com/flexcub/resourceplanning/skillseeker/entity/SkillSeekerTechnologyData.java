package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table
public class SkillSeekerTechnologyData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @OneToOne(targetEntity = OwnerSkillTechnologiesEntity.class)
    @JoinColumn(name = "owner_skill_technology_id")
    private OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity;

    @OneToOne(targetEntity = OwnerSkillRolesEntity.class)
    @JoinColumn(name = "owner_skill_role_id")
    private OwnerSkillRolesEntity ownerSkillRolesEntity;

    @OneToOne(targetEntity = OwnerSkillLevelEntity.class)
    @JoinColumn(name = "owner_skill_level_id")
    private OwnerSkillLevelEntity ownerSkillLevelEntity;

    @Column(length = 20)
    private int baseRate;

    @Column(length = 20)
    private int maxRate;

    @Column
    private Date expiresOn;

    @Column
    private String status;

}
