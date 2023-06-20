package com.flexcub.resourceplanning.skillpartner.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hiring {

    private int hiringID;

    private int requirementID;

    private int candidateID;

    private String hiringStartDt;

    private String interviewStage;

    private String hiringEndDt;

    private String hiringStatus;

    private String feedBack1;

    private String feedBack2;

    private String feedBack3;

}
