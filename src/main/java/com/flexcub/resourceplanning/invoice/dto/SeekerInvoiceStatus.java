package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeekerInvoiceStatus {



    private int statusId;

    private String adminInvoiceId;

    public String comment;
}
