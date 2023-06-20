package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SowPoInitiateResponse {
        private int seekerId;
        private int projectId;
        private int sowEntityId;
        private String sowStatus;
        private int poEntityId;
        private String poStatus;
        private String jobId;
        private String jobTitle;
        private  String department;
        private  Integer numberOfResources;
        private  Integer amountForEachResource;
}
