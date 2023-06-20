package com.flexcub.resourceplanning.invoice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class InvoiceRequest {

    private Date startDate;

    private Date endDate;

    private Date dueDate;

    private int skillPartnerId;

    private List<PartnerInvoiceResponse> partnerInvoiceResponseList;
}
