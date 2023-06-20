package com.flexcub.resourceplanning.invoice.dto;

import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class InvoiceDetails {

    private String invoiceId;
    private String seekerProjectName;
    private String clientName;
    private LocalDate date;
    private InvoiceStatus status;
    private LocalDate paymentDueDate;
    private int partnerId;
    private String partnerName;
    private Date weekStartDate;
    private String comments;
}
