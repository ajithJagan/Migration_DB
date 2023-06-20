package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class OwnerTimeSheet {

    private int timeSheetId;

    private int skillOwnerEntityId;

    private int skillSeekerEntityId;

    private int skillSeekerProjectEntityId;

    private List<Efforts> efforts;

    private Date startDate;

    private Date endDate;

    private String hours;

    private String totalHours;

    private boolean approved;

}
