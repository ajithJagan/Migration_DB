package com.flexcub.resourceplanning.registration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flexcub.resourceplanning.skillpartner.entity.WorkForceStrength;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registration")
@Getter
@Setter
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties("roles")
    @JoinColumn(name = "role_id", referencedColumnName = "roles_id", updatable = false)
    private Roles roles;

    @Column(name = "businessName")
    private String businessName;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "emailId", unique = true)
    private String emailId;

    @Column(name = "password")
    private String password;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "status")
    private boolean accountStatus = false;

    @Column(name = "mail_status")
    private String mailStatus;

    @Column(name = "taxIdBusinessLicense", columnDefinition = "text")
    private String taxIdBusinessLicense;

    @Column(name = "contactPhone", columnDefinition = "text")
    private String contactPhone;

    @Column
    private String contactEmail;

    @Column
    private String businessPhone;

    @OneToOne(targetEntity = WorkForceStrength.class)
    @JoinColumn(name = "work_force_id")
    private WorkForceStrength workForceStrength;

    @Column
    private String city;

    @Column
    private String state;

    @Column(name = "domainId", nullable = false)
    private int domainId; //domainId - 2

    @Column
    private String technologyIds; //technologiesId - "2,4,6"

    @Column
    private String address;

    @Column
    private String excelId;

    private String customTech;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDate createdAt;

    @Column(name = "isTrial")
    private boolean isTrial = false;

    @Column
    private LocalDate trialExpiredOn;

    @Column(name = "User_loginCount")
    private Long loginCount = 0L;

    @Column
    private Boolean isAccountActive = true;

}
