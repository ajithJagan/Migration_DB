package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceUpdateData {
    private int skillOwnerEntityId;
    private String firstName;
    private String ownerDesignation;
    private int skillSeekerEntityId;
    private String skillSeekerName;
    private int skillSeekerProjectEntityId;
    private String title;
    private  int totalHours;
    private int amount;
}
