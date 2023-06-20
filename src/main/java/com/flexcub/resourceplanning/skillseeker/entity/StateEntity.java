package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "state_code")
public class StateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(nullable = false)
    private String stateCode;


}
