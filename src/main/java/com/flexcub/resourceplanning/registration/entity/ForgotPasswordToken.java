package com.flexcub.resourceplanning.registration.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(unique = true)
    private String forgotToken;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp timeStamp;

    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expireAt;

    @OneToOne(targetEntity = RegistrationEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private RegistrationEntity registration;

    @Column
    private boolean isExpired = false;


}
