package com.flexcub.resourceplanning.skillowner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "owner_skill_roles")
@Getter
@Setter
public class OwnerSkillRolesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int rolesId;

    @Column(nullable = false)
    private String rolesDescription;

}
