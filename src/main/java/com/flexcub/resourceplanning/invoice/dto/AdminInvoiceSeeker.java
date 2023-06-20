package com.flexcub.resourceplanning.invoice.dto;

import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminInvoiceSeeker {

    private String invoiceId;
    private InvoiceStatus contractStatus;
    private List<AdminInvoiceResponseData> adminInvoiceData;

}
