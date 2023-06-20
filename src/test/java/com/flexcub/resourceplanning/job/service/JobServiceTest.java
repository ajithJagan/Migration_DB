package com.flexcub.resourceplanning.job.service;

import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.repository.HiringPriorityRepository;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.job.service.impl.JobServiceImpl;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillYearOfExperience;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillTechnologiesRepository;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = JobServiceImpl.class)
class JobServiceTest {

    @Autowired
    JobServiceImpl jobService;
    @MockBean
    OwnerSkillTechnologiesRepository ownerSkillTechnologiesRepository;


    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;


    @MockBean
    SelectionPhaseService service;

    @MockBean
    RequirementPhaseRepository requirementPhaseRepository;
    @MockBean
    JobRepository jobRepository;
    @MockBean
    RegistrationRepository registrationRepository;

    @MockBean
    HiringPriorityRepository hiringPriorityRepository;
    @MockBean
    ModelMapper modelMapper;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;

    @MockBean
    ContractFileRepository contractFileRepository;
    Job job1 = new Job();
    List<Job> job = new ArrayList<>();


    JobDto jobDto = new JobDto();
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    SkillSeeker skillSeekerDto = new SkillSeeker();
    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntity = new ArrayList<>();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();
    OwnerSkillYearOfExperience ownerSkillYearOfExperience = new OwnerSkillYearOfExperience();

    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();

    RegistrationEntity registrationEntity = new RegistrationEntity();

    List<HiringPriority> hiringPriorities = new ArrayList<>();

    HiringPriority hiringPriority = new HiringPriority();


    @BeforeEach
    void setJobDetails() {

        job1.setJobId("FJB-00001");
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
        job1.setCoreTechnology("Java");
        job1.setSkillSeeker(skillSeeker);
        job1.setOwnerSkillYearOfExperience(ownerSkillYearOfExperience);
        job1.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        job1.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        job1.setSeekerProject(skillSeekerProjectEntity);
        job1.setTaxIdBusinessLicense("acv-123");
        job.add(job1);

        skillSeeker.setId(1);

        jobDto.setJobId("FJB-00001");
        jobDto.setSkillSeeker(skillSeekerDto);
        jobDto.setCoreTechnology("Java");
        jobDto.setTaxIdBusinessLicense("acs-121");
        jobDto.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);
        jobDto.setJobDescription("testing");
        jobDto.setJobLocation("US");
        jobDto.setJobTitle("App Development");
        jobDto.setBaseRate(40);
        jobDto.setCreatedTime("2023-05-03");
        jobDto.setCustomTech("Java");
        jobDto.setFederalSecurityClearance(true);
        jobDto.setHiringPriority(hiringPriority);
        jobDto.setMaxRate(150);
        jobDto.setProject("Testing");
        jobDto.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        jobDto.setScreeningQuestions(true);
        jobDto.setRemote(50);
        jobDto.setTravel(50);
        jobDto.setOwnerSkillYearOfExperience(ownerSkillYearOfExperience);
        jobDto.setStatus("Active");

        hiringPriorities.add(hiringPriority);

    }

    @Test
    void getJobDetailsServiceTest() {
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.singletonList(job1));
        assertEquals(jobService.getJobDetails(), Collections.singletonList(job1));
    }

    @Test
    void createJobDetailsServiceTest() {
        Mockito.when(modelMapper.map(jobDto, Job.class)).thenReturn(job1);
        Mockito.when(registrationRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(registrationEntity));
        Mockito.when(modelMapper.map(job1, JobDto.class)).thenReturn(jobDto);
        assertEquals(jobDto.getJobId(), jobService.createJobDetails(jobDto).getJobId());

    }

    @Test
    void getHiringPriorityServiceTest() {
        Mockito.when(hiringPriorityRepository.findAll()).thenReturn(hiringPriorities);
        assertEquals(1, jobService.getHiringPriority().size());
    }

    @Test
    void publishServiceTest() {
        when(jobRepository.findByJobId(job1.getJobId())).thenReturn(Optional.ofNullable(job1));
        when(jobRepository.saveAndFlush(job1)).thenReturn(job1);
        when(modelMapper.map(job1, JobDto.class)).thenReturn(jobDto);
        assertEquals(jobDto.getJobId(), jobService.publish(job1.getJobId()).getJobId());
    }

    @Test
    void deleteJobTest() {
        when(jobRepository.findByJobId(job1.getJobId())).thenReturn(Optional.of(job1));
        jobService.deleteJob(job1.getJobId());
        Mockito.verify(jobRepository, Mockito.times(1)).deleteById(job1.getJobId());
    }

}

