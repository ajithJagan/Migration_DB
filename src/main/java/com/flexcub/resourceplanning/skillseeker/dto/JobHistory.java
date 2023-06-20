package com.flexcub.resourceplanning.skillseeker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobHistory {

    private String jobId;

    private String skillSeekerName;

    private String jobTitle;

    private String location;

    private int experience;

    private String status;

}
