package com.flexcub.resourceplanning.verificationmail.entity;//package com.flexcub.resourceplanning.verificationmail.entity;

import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String token;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp timeStamp;

    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expireAt;

    @OneToOne(targetEntity = RegistrationEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private RegistrationEntity registration;

    @Column
    private boolean isExpired = false;
}
