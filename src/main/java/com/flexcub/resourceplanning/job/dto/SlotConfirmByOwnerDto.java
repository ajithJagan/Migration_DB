package com.flexcub.resourceplanning.job.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;


@Getter
@Setter
public class SlotConfirmByOwnerDto {

    private String jobId;

    private int skillOwnerEntityId;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date dateSlotsByOwner1;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date dateSlotsByOwner2;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date dateSlotsByOwner3;

    private LocalTime timeSlotsByOwner1;
    private LocalTime endTimeSlotsByOwner1;

    private LocalTime timeSlotsByOwner2;
    private LocalTime endTimeSlotsByOwner2;

    private LocalTime timeSlotsByOwner3;
    private LocalTime endTimeSlotsByOwner3;

    private Boolean slotsConfirmedBySeeker;

}
