package com.flexcub.resourceplanning.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnersOwnerData {

    private int projectId;

    private List<JobData> jobs;
    public void addJob(JobData jobData) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        jobs.add(jobData);
    }
}

