package com.flexcub.resourceplanning.job.entity;


import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@Table(name = "RequirementPhase")
public class RequirementPhase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_Id")
    private int requirementId;

    @Column
    private String jobId;

    @Column
    private int skillOwnerId;

    @Column
    private int stage;

    @Column(name = "requirement_Phase_Name")
    private String requirementPhaseName;

    @Column(name = "date_Of_Interview")
    private LocalDate dateOfInterview;

    @Column(name = "time_Of_Interview")
    private LocalTime timeOfInterview;

    @Column
    private LocalTime endTimeOfInterview;

    @Column(name = "status")
    private String status;

    @Column(name = "interviewed_By")
    private String interviewedBy;

    @Column(columnDefinition = "text")
    private String feedback;

    @Column(columnDefinition = "text")
    private String comments;

//    @Column(name = "percentage_Expected")
//    @OneToOne(targetEntity = FeedbackRate.class)
//    private FeedbackRate expectedRate;

    @Column
    private String modeOfInterview;

//    @Column(name = "candidate_Percentage")
    @OneToOne(targetEntity = FeedbackRate.class)
    private FeedbackRate CandidateRate;

    @Column(columnDefinition = "boolean default false")
    private boolean isRescheduled;


}
