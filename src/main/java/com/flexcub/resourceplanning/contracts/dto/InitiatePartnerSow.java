package com.flexcub.resourceplanning.contracts.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitiatePartnerSow {

    private int partnerId;
    private int projectId;
    private String jobId;

    private Integer numberOfResources;

    private Integer totalResourcesRate;


}
