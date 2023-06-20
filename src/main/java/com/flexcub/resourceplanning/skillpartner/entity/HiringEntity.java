package com.flexcub.resourceplanning.skillpartner.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "skill_partner_hiring")
@AllArgsConstructor
@NoArgsConstructor
public class HiringEntity extends BaseEntity {


    @Column(name = "hiringID", nullable = false)
    private int hiringID;

    @Id
    @Column(name = "requirementID", nullable = false)
    private int requirementID;

    @Column(name = "candidateID", nullable = false)
    private int candidateID;

    @Column(name = "hiring_Start_Dt", nullable = false)
    private String hiringStartDt;

    @Column(name = "interviewStage", nullable = false)
    private String interviewStage;

    @Column(name = "hiring_End_Dt", nullable = false)
    private String hiringEndDt;

    @Column(name = "hiring_Status", nullable = false)
    private String hiringStatus;

    @Column(name = "feedBack1", nullable = false)
    private String feedBack1;

    @Column(name = "feedBack2", nullable = false)
    private String feedBack2;

    @Column(name = "feedBack3", nullable = false)
    private String feedBack3;

}
