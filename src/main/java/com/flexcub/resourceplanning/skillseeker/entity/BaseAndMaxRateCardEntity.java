package com.flexcub.resourceplanning.skillseeker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "ratecard")
public class BaseAndMaxRateCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(nullable = false, columnDefinition = "text")
    private String baseRate = "$45";

    @Column(nullable = false, columnDefinition = "text")
    private String maxRate = "$500";
}
