package com.flexcub.resourceplanning.skillowner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "owner_skill_status")
@Getter
@Setter
//@Where(clause = "deleted_at is null")
public class OwnerSkillStatusEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "skill_status_id")
    private int skillOwnerStatusId;

    @Column(nullable = false)
    private String statusDescription;
}
