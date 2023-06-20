package com.flexcub.resourceplanning.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkEfforts {

    private int skillOwnerEntityId;
    private String ownerName;
    private String designation;
    private int skillSeekerEntityId;
    private String clientName;
    private int skillSeekerProjectEntityId;
    private String projectName;
    private int totalHours;
    private Double actualAmount;
    private double serviceFeesAmount;
    private int amount;
    private int rate;
    private int timesheetId;
    private LocalDate timesheetStartDate;
    private LocalDate timesheetEndDate;

}
