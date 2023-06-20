package com.flexcub.resourceplanning.skillpartner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "work_force_strength")
public class WorkForceStrength {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(nullable = false)
    private String range;
}
