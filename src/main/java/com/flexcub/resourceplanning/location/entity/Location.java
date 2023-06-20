package com.flexcub.resourceplanning.location.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "token", length = 300)
    private String token;

    @Column(name = "apiToken", nullable = false)
    private String apiToken;


}
