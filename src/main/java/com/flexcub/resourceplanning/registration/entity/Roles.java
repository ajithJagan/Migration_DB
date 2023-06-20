package com.flexcub.resourceplanning.registration.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Roles {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "roles_id")
    private Long rolesId;

    @Column(name = "roleDescription")
    private String roleDescription;


}
