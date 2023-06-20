package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerRequirement;

import java.util.List;

public interface RequirementService {


    List<SeekerRequirement> getDataById(int seekerId);

    List<JobDto> insertData(List<SeekerRequirement> requirementEntity);

    JobDto updateData(SeekerRequirement update);

    void deleteData(String id);
}
