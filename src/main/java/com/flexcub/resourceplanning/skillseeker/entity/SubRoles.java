package com.flexcub.resourceplanning.skillseeker.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sub_roles")
@Getter
@Setter
public class SubRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "sub_role_description")
    private String subRoleDescription;

}
