package com.flexcub.resourceplanning.skillseekeradmin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAdmin {
    private int id;

    private String fullName;

    private String businessName;

    private String phone;

    private String email;

    private String location;

    private double servicePercentage;

}
