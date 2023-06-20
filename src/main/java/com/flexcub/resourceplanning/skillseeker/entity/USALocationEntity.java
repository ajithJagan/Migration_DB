package com.flexcub.resourceplanning.skillseeker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "usa_location")
public class USALocationEntity {

    @Id
    @Column(name = "id", unique = true)
    private int id;

    @Column
    private String cityAndState;

}


