package com.flexcub.resourceplanning.skillowner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "owner_skill_set_new")
@Getter
@Setter
public class OwnerSkillSetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "owner_skill_set_entity_id")
    private int ownerSkillSetId;

    @Column(name = "skill_owner_id")
    private int skillOwnerEntityId;

    @OneToOne(targetEntity = OwnerSkillLevelEntity.class)
    @JoinColumn(name = "owner_skill_level_entity_id")
    private OwnerSkillLevelEntity ownerSkillLevelEntity;

    @OneToOne
    @JoinColumn(name = "owner_skill_technology_id")
    private OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity;

    @OneToOne
    @JoinColumn(name = "owner_skill_role_entity_id")
    private OwnerSkillRolesEntity ownerSkillRolesEntity;

    @OneToOne
    @JoinColumn(name = "owner_skill_domain_entity_id")
    private OwnerSkillDomainEntity ownerSkillDomainEntity;

    @Column
    private String experience;

    @Column
    private String lastUsed;
}
