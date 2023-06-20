package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDetail {

    private int partnerId;
    private int totalResources;
    private int totalResourcesRate;
    private List<OwnerDetail> owners;
}

