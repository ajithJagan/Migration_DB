package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class JobData {

    private String jobId;
    private int noOfResources;
    private int resourcesRate;

    private int partnerId;

    private List<OwnerData> owners;
    public JobData() {
        owners = new ArrayList<>();
    }

}

