package com.flexcub.resourceplanning.invoice.dto;

import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceResponse {

    private String invoiceId;
    private int totalAmount;
    private InvoiceStatus invoiceStatus;
}