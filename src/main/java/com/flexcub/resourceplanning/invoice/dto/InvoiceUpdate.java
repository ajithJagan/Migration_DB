package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InvoiceUpdate {

    private String invoiceId;
    private LocalDate invoiceDate;
    List<InvoiceUpdateData> invoiceUpdateList;

}

