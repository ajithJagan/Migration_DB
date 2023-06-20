package com.flexcub.resourceplanning.job.controller;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = JobController.class)
class JobControllerTest {
    @MockBean
    JobService jobService;
    @Autowired
    JobController jobController;
    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity;
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    OwnerSkillYearOfExperience ownerSkillYearOfExperience;
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    SkillSeeker skillSeekerDto = new SkillSeeker();
    JobDto job1 = new JobDto();

    List<String> jobList = new ArrayList<>();

    List<HiringPriority> hiringPriorities = new ArrayList<>();

    HiringPriority hiringPriority = new HiringPriority();



    @BeforeEach
    void setJobDetail() {

        job1.setJobId("FJB_001");
        job1.setJobTitle("Java Developer");
        job1.setJobLocation("US");
        job1.setJobDescription("Developer");
        job1.setProject("flex");
        job1.setNumberOfPositions(2);
        job1.setRemote(56);
        job1.setTravel(67);
        job1.setBaseRate(56);
        job1.setMaxRate(67);
        job1.setFederalSecurityClearance(true);
        job1.setScreeningQuestions(true);
        job1.setStatus("Draft");
        job1.setHiringPriority(hiringPriority);
        job1.setSkillSeeker(skillSeekerDto);
        job1.setOwnerSkillYearOfExperience(ownerSkillYearOfExperience);
        job1.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        job1.setOwnerSkillDomainEntity(ownerSkillDomainEntity);

        jobList.add("Medium");
        hiringPriorities.add(hiringPriority);
    }

    @Test
    void publishTest() {
        when(jobService.publish(Mockito.anyString())).thenReturn(job1);
        assertEquals(200, jobController.publish(job1.getJobId()).getStatusCodeValue());
    }

    @Test
    void publishNullTest() {
        when(jobService.publish(String.valueOf(job1))).thenReturn(null);
        assertNull(jobController.publish(String.valueOf(job1)).getBody());
    }

    @Test
    void addJobDetailsTest() {
        when(jobService.createJobDetails(job1)).thenReturn(job1);
        assertEquals(200, jobController.addJobDetails(job1).getStatusCodeValue());
        assertEquals(jobController.addJobDetails(job1).getBody(), job1);
    }

    @Test
    void getHiringPriorityTest() {
        when(jobService.getHiringPriority()).thenReturn(hiringPriorities);
        assertEquals(200, jobController.getHiringPriority().getStatusCodeValue());
    }

    @Test
    void getHiringPriorityNullTest() {
        when(jobService.getHiringPriority()).thenReturn(null);
        assertNull(jobController.getHiringPriority().getBody());
    }

//    @Test
//    void getJobDetails() {
//        when(jobService.getJobDetailsBySeekerId(job1.getSkillSeeker().getId())).thenReturn(Collections.singletonList(job1));
//        assertEquals(200, jobController.getRetrieveJob(job1.getSkillSeeker().getId()).getStatusCodeValue());
//    }

//    @Test
//    void getJobDetailsNullTest() {
//        when(jobService.getJobDetailsBySeekerId(job1.getSkillSeeker().getId())).thenReturn(null);
//        assertNull(jobController.getRetrieveJob(job1.getSkillSeeker().getId()).getBody());
//    }

    @Test
    void deleteJobTest() {
        jobController.deleteJob(job1.getJobId());
        Mockito.verify(jobService, times(1)).deleteJob(Mockito.anyString());
    }

}


