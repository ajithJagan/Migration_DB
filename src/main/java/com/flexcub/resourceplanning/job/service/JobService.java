package com.flexcub.resourceplanning.job.service;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.job.entity.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface JobService {

    List<Job> getJobDetails();

    JobDto publish(String jobId);

    JobDto createJobDetails(JobDto jobDto);

    List<JobDto> getAllJobDetails(int seekerId);

    List<HiringPriority> getHiringPriority();

    void deleteJob(String jobId);

    Optional<Job> getJob(String jobId);

    Job getById(String jobId);

    Job findByJobId(String id);

    Job saveAndFlush(Job job);
    List<Job> getBySeekerProject(int projectId);


    List<Job> getBySeekerIdAndProjectId(int seekerId, int id);

    Optional<Job> getJobByProjectId(int projectId);

    Optional<Job> getByJobId(String jobId);
}


