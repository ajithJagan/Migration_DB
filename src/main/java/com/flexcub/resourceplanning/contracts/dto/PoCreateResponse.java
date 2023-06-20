package com.flexcub.resourceplanning.contracts.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoCreateResponse {
        int seekerId;
        int projectId;
        int poId;
        String poStatus;
}

