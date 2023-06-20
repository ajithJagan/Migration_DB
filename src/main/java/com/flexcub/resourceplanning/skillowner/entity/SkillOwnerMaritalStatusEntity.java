package com.flexcub.resourceplanning.skillowner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "skill_owner_marital_status")
@Getter
@Setter

public class SkillOwnerMaritalStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column
    private String status;
}
