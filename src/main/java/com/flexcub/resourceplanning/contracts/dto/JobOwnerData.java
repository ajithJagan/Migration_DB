package com.flexcub.resourceplanning.contracts.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobOwnerData {
    private int ownerId;
    private int rate;
    private int partnerId;

}
