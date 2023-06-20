package com.flexcub.resourceplanning.contracts.dto;

import com.flexcub.resourceplanning.job.entity.Job;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SowResponse {
        private int projectId;
        private String ProjectName;
        private int seekerId;
        private String seekerName;
        private  Integer numberOfResources;
        private  Integer amountForEachResource;
        private String jobId;
        private  String jobTitle;
}
