package com.flexcub.resourceplanning.notifications.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "owner_notifications")

public class OwnerNotificationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private int skillOwnerEntityId;

    @Column
    private String title;

    @Column
    private int contentId;

    @ManyToOne
    @JoinColumn(name = "content_obj")
    private ContentEntity contentObj;

    @Column
    private String jobId;

    @Column
    private Date date;

    @Column
    private Boolean markAsRead;

    @Column
    private String content;

    @Column
    private int stage;

    @Column
    private String requirementPhaseName;

    @Column(name = "date_Of_Interview")
    private LocalDate dateOfInterview;

    private LocalTime timeOfInterview;

    @Column(columnDefinition = "integer default 0")
    private Integer seekerId;

}
