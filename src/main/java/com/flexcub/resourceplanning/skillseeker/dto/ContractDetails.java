package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContractDetails {

    private String nameOfOwner;

    private String position;

    private String experience;

    private int currentStage;

    private String jobId;

    private List<RequirementPhases> requirementPhaseList;

    private Boolean isMsaCreated=false;
    private int msaId;
    private String msaStatus;
    private Boolean isSowCreated=false;
    private int sowId;
    private String sowStatus;
    private Boolean isPoCreated=false;
    private int poId;
    private String poStatus;

//    private String contractWritingMSA;
//
//    private String contractWritingSOW;
//
//    private String contractWritingPO;


}
