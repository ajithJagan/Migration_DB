package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSeekerDetails {

    private String title;

    private String content;

    private Date date;
}
