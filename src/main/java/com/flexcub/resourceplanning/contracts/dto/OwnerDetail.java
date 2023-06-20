package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDetail {
    private int ownerId;
    private int rate;
}
