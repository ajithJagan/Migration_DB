package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class TimeSheet {

    private String firstName;
    private int skillSeekerProjectEntityId;
    private String title;
    private int skillSeekerTaskEntityId;
    private String taskTitle;
    private String taskDescription;
    private String hours;
    private int totalHours;
    private Date startDate;
    private Date endDate;
    private boolean approved;
}
