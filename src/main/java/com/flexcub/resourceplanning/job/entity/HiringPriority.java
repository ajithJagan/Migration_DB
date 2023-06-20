package com.flexcub.resourceplanning.job.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "hiring_priority")
public class HiringPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "hiring_priority")
    private String hiringPriority;
}
