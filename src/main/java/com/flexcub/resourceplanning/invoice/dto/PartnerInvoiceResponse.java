package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerInvoiceResponse {

    private int skillSeekerId;

    private int skillSeekerProjectId;

    private int skillOwnerId;

    private int totalHours;

    private int amount;

}
