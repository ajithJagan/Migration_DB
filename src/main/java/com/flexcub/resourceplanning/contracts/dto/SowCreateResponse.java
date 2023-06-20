package com.flexcub.resourceplanning.contracts.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SowCreateResponse {
        int seekerId;
        int projectId;
        int sowId;
        String sowStatus;

}
