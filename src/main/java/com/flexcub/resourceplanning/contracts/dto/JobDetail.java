package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDetail {

    private int projectId;
    private String jobId;
    private List<PartnerDetail> partners;
}

