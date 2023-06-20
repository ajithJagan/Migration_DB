package com.flexcub.resourceplanning.skillowner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "owner_skill_technologies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerSkillTechnologiesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int technologyId;

    @Column(nullable = false)
    private String technologyValues;

    @Column
    private int priority;
}
