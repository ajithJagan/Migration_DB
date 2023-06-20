package com.flexcub.resourceplanning.skillowner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "owner_skill_domain")
@Getter
@Setter
//@Where(clause = "deleted_at is null")
public class OwnerSkillDomainEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int domainId;

    @Column(nullable = false)
    private String domainValues;

    private int priority;

}
