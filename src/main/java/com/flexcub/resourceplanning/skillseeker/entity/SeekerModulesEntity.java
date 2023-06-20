package com.flexcub.resourceplanning.skillseeker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "seeker_module")
@Getter
@Setter
public class SeekerModulesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "seeker_module")
    private String seekerModule;

}
