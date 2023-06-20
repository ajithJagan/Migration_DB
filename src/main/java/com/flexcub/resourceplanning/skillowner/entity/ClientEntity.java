package com.flexcub.resourceplanning.skillowner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "WorkDetails")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int clientId;

    @Column
    private int skillOwnerEntityId;

    @Column
    private String employerName;

    @Column
    private String jobTitle;

    @Column
    private String project;

    @Column
    private String projectDescription;

    @Column
    private String department;

    @Column(nullable = false)
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private Boolean currentEmployer;

    @Column
    private String location;
}
