package com.flexcub.resourceplanning.skillseekeradmin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeekerAdminMsa {

    private int id;
    private String skillSeekerProjectName;
    private String skillSeekerProjectDept;
    private int skillSeekerId;
    private LocalDate dateSigned;
    private Date expiryDate;
    private String jobId;

}
