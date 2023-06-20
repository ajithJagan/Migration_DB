package com.flexcub.resourceplanning.skillowner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "skill_owner_gender")
@Getter
@Setter
//@Where(clause = "deleted_at is null")
public class SkillOwnerGenderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column
    private String gender;
}
