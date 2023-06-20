package com.flexcub.resourceplanning.invoice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientInvoiceDetails {

    private int skillSeekerId;
    private String skillSeekerName;
    private List<ClientProjects> clientProjects;
}
