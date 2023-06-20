package com.flexcub.resourceplanning.contracts.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PoCreateRequest {
        private int poEntityId;
        LocalDate contractStartDate;
        LocalDate contractEndDate;
}
