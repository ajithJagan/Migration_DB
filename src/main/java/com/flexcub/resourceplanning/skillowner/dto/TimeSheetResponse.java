package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class TimeSheetResponse {

    private int timeSheetId;

    private int skillOwnerEntityId;

    private String firstName;

    private int skillSeekerEntityId;

    private int skillSeekerProjectEntityId;
    private String title;

    private int skillSeekerTaskEntityId;

    private String taskTitle;

    private String taskDescription;

    private Date startDate;

    private Date endDate;

    private String hours;

    private int totalHours;

    private boolean approved;

}
