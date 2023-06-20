package com.flexcub.resourceplanning.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private Long createdBy;


    @Column(name = "changed_at")
    @LastModifiedDate
    private Date changedAt;

    @Column(name = "changed_by")
    @LastModifiedBy
    private Long changedBy;
}