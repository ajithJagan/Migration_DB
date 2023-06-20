package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SowCreateRequest {
        private int sowEntityId;
        LocalDate contractStartDate;
        LocalDate contractEndDate;
}
