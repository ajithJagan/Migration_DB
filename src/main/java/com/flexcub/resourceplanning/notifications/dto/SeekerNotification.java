package com.flexcub.resourceplanning.notifications.dto;

import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SeekerNotification {


    private int id;

    private int skillSeekerEntityId;

    private int skillOwnerId;

    private String title;

    private int contentId;

    private ContentEntity contentObj;

    private String jobId;

    private String taxIdBusinessLicense;

    private Date date;

    private boolean markAsRead;

    private String content;

    private int stage;

    private String projectName;

}


