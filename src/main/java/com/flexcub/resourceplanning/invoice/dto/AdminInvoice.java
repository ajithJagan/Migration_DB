package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class AdminInvoice {
    private int invoiceDataId;
    private int timesheetId;
    private int ownerId;
    private String ownerName;
    private String position;
    private String client;
    private int totalHours;
    private double actualAmount;
    private int amount;
    private LocalDate invoiceCreationDate;
    private LocalDate invoiceDueDate;
    private int rateCard;
    private Date startDate;
    private Date endDate;

}
