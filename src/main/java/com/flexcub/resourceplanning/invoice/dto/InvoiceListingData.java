package com.flexcub.resourceplanning.invoice.dto;

import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class InvoiceListingData {
    private String invoiceId;
    private int soCount;
    private InvoiceStatus status;
    private String to = "FlexCub";
    private LocalDate invoiceDate;
    private Date weekStartDate;
    private LocalDate paymentDueDate;
    private String comments;
}
