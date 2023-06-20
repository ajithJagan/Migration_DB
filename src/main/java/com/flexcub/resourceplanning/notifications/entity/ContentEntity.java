package com.flexcub.resourceplanning.notifications.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "content_notifications")
public class ContentEntity {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column
    private String title;

}
