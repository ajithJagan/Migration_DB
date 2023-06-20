package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubRole {

    int roleId;
    String taxId;
    boolean isActive;
    List<Integer> moduleId;

}
