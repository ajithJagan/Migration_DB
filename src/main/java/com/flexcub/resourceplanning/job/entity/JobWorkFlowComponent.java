package com.flexcub.resourceplanning.job.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "JobWorkflowComponent")
public class JobWorkFlowComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String jobId;

    private String flow; //comma separated

    private Boolean locked;

}
