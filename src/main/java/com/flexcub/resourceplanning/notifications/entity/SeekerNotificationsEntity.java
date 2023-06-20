package com.flexcub.resourceplanning.notifications.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "seeker_notifications")

public class SeekerNotificationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column
    private int skillSeekerEntityId;

    private int skillOwnerId;

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
    private String taxIdBusinessLicense;

    @Column
    private Date date;

    @Column
    private Boolean markAsRead;

    @Column
    private String content;

    @Column
    private int stage;

    private int ownerId;

    private String projectName;

    @Column(columnDefinition = "integer default 0")
    private int msaId;

    @Column(columnDefinition = "varchar(255) default null")
    private String msaStatus;

    @Column(columnDefinition = "integer default 0")
    private int sowId;

    @Column(columnDefinition = "varchar(255) default null")
    private String sowStatus;

    @Column(columnDefinition = "integer default 0")
    private int poId;

    @Column(columnDefinition = "varchar(255) default null")
    private String poStatus;


}
