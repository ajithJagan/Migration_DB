package com.flexcub.resourceplanning.contracts.dto;

import com.flexcub.resourceplanning.job.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerSowRequest {

    private int sowEntityId;

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
}
