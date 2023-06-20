package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePORequest {
    int poId;
    LocalDate contractStartDate;
    LocalDate contractEndDate;
    int rateCard;
}

