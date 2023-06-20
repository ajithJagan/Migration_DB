package com.flexcub.resourceplanning.skillseeker.dto;

import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeekerRoleListing {

    private String roleId;
    private String roleName;
    private List<SeekerModulesEntity> accessList;
    private boolean status;
}
