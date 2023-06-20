package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SeekerInvoice {

    private String invoiceId;

    private String Details;

    private String sender;

    private LocalDate Date;

    private String status;
    private String comments;
}
