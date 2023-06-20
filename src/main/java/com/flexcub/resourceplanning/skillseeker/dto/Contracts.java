package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Contracts {
    private int ownerId;

    private String name;

    private String position;

    private String seekerName;
    private String seekerContactEmail;
    private String seekerContactPhone;

    private String partner;

    private int seekerId;

    private int projectId;

    private String projectName;

    private String ownerMailId;

    private LocalDate processedOn;

    private Date expiresOn;

    private String ownerContactNumber;

    private String status;

    private String location;

    private String jobId;

    private Date onBoarding;
    private Date contractDurationStartDate;
    private Date contractDurationEndDate;
    private Boolean isMsaCreated=false;
    private int msaId;
    private String msaStatus;
    private Boolean isSowCreated=false;
    private int sowId;
    private String sowStatus;
    private Boolean isPoCreated=false;
    private int poId;
    private String poStatus;
}
