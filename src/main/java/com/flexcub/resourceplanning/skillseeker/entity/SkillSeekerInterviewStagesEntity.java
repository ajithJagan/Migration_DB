package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "interview_stages")
@Getter
@Setter
public class SkillSeekerInterviewStagesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    private String interviewStages;

}
