package com.flexcub.resourceplanning.skillowner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "years_of_exp")
@Getter
@Setter
public class OwnerSkillYearOfExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @OneToOne(targetEntity = OwnerSkillLevelEntity.class)
    @JoinColumn(name = "owner_skill_level_id", referencedColumnName = "id")
    private OwnerSkillLevelEntity ownerSkillLevelEntity;

    @Column(name = "experience")
    private String experience;
}
