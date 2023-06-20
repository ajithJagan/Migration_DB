package com.flexcub.resourceplanning.contracts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartnerMsaFileRequest {
    private int partnerId;
    private LocalDate msaContractStartDate;
    private LocalDate msaContractExpiryDate;

}
