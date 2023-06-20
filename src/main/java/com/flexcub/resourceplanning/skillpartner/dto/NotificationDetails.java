package com.flexcub.resourceplanning.skillpartner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {

    private String title;

    private String content;

    private Date date;
}
