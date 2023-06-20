package com.flexcub.resourceplanning.notifications.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class Notification {

    private Integer id;

    private int contentId;

    private Date date;

    private String content;

    private String title;

    private int stage;

    private String requirementPhaseName;

    private LocalDate dateOfInterview;

    private LocalTime timeOfInterview;

    private String projectName;


}
