package com.flexcub.resourceplanning.skillseeker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contract_status")
@Getter
@Setter
public class ContractStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column
    private String status;
}

