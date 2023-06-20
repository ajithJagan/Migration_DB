package com.flexcub.resourceplanning.skillseekeradmin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeekerAdmin {

    private int id; //regId

    private String skillSeekerName;

    private String primaryContactFullName;

    private String phone;

    private String email;

    private String location;

    private Date startDate;

    private Date endDate;

    private String status;

    public SeekerAdmin(int id, String skillSeekerName, String primaryContactFullName, String phone, String email, String location, String status) {
        this.id = id;
        this.skillSeekerName = skillSeekerName;
        this.primaryContactFullName = primaryContactFullName;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.status = status;
    }
}
