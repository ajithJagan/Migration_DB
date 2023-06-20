package com.flexcub.resourceplanning.job.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotConfirmBySeekerDto {

    private String jobId;
    private int skillOwnerEntityId;
    private Boolean slotConfirmed;

}
