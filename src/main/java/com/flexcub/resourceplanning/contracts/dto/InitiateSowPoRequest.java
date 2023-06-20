package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitiateSowPoRequest {
    private int projectId;
    private int seekerId;
    private  Integer numberOfResources;
    private  Integer amountForEachResource;
    private String jobId;

}
