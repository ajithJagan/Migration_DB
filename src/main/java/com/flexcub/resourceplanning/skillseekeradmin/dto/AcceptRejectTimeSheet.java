package com.flexcub.resourceplanning.skillseekeradmin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptRejectTimeSheet {

    private int skillOwnerEntityId;
    private boolean approved;
}
