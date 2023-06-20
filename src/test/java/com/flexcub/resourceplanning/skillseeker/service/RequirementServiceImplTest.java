package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillYearOfExperienceRepository;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.RequirementServiceImpl;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerRequirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RequirementServiceImpl.class)
class RequirementServiceImplTest {

    @Autowired
    RequirementServiceImpl requirementService;

    @MockBean
    JobRepository jobRepository;

    @MockBean
    OwnerSkillYearOfExperienceRepository yearOfExperienceRepository;

    @MockBean
    SkillSeekerProjectRepository skillSeekerProjectRepository;
    @MockBean
    SkillSeekerProjectService skillSeekerProjectService;

    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    @MockBean
    ModelMapper modelMapper;

    List<Job> jobs = new ArrayList<>();

    Job job = new Job();


    SeekerRequirement seekerRequirement = new SeekerRequirement();
    SkillSeeker skillSeeker = new SkillSeeker();
    SkillSeekerEntity skillSeekerEntity = new SkillSeekerEntity();
    List<SeekerRequirement> seekerRequirementList = new ArrayList<>();
    List<JobDto> jobDtoList = new ArrayList<>();
    JobDto jobDto = new JobDto();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();

    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = new OwnerSkillTechnologiesEntity();

    List<OwnerSkillTechnologiesEntity> ownerSkillTechnologiesEntityList = new ArrayList<>();

    List<SkillSeekerTechnologyData> skillSeekerTechnologyDataList = new ArrayList<>();
    SkillSeekerTechnologyData skillSeekerTechnologyData = new SkillSeekerTechnologyData();

    OwnerSkillLevelEntity ownerSkillLevelEntity = new OwnerSkillLevelEntity();

    @BeforeEach
    void setup() {
        skillSeeker.setId(1);

        seekerRequirementList.add(seekerRequirement);

        seekerRequirement.setJobId(job.getJobId());
        seekerRequirement.setSkillSeekerProjectEntity(skillSeekerProjectEntity);
        seekerRequirement.setExpiryDate(new Date(22 - 11 - 2022));
        seekerRequirement.setSkillSeeker(skillSeekerEntity);
        skillSeekerEntity.setId(1);

        jobs.add(job);
        jobDto.setJobId(job.getJobId());
        jobDtoList.add(jobDto);

        skillSeekerTechnologyDataList.add(skillSeekerTechnologyData);

        job.setJobId("FJB-00001");
        job.setExpiryDate(new Date(22 - 11 - 2022));
        job.setSeekerProject(skillSeekerProjectEntity);
        job.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntityList);

        skillSeekerProjectEntity.setId(1);
        skillSeekerProjectEntity.setOwnerSkillDomainEntity(ownerSkillDomainEntity);
        skillSeekerProjectEntity.setTitle("Development UI");
        skillSeekerProjectEntity.setSkillSeeker(skillSeekerEntity);
        skillSeekerProjectEntity.setTitle("Scala");
        skillSeekerProjectEntity.setSkillSeekerTechnologyData(skillSeekerTechnologyDataList);

        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setExpYears(2);
        skillOwnerEntity.setExpMonths(3);

        ownerSkillTechnologiesEntityList.add(ownerSkillTechnologiesEntity);

        skillSeekerTechnologyData.setId(1);
        skillSeekerTechnologyData.setOwnerSkillLevelEntity(ownerSkillLevelEntity);
        skillSeekerTechnologyData.setOwnerSkillTechnologiesEntity(ownerSkillTechnologiesEntity);


    }

    @Test
    void getDataById() {
        when(jobRepository.findBySkillSeekerId(skillSeeker.getId())).thenReturn(jobs);
        assertEquals(1, requirementService.getDataById(skillSeeker.getId()).size());
    }

    @Test
    void insertData() {
        when(modelMapper.map(jobDto, JobDto.class)).thenReturn(jobDto);
        when(skillSeekerProjectRepository.findById(seekerRequirement.getSkillSeekerProjectEntity().getId())).thenReturn(Optional.of(skillSeekerProjectEntity));
        when(skillSeekerProjectRepository.findById(seekerRequirement.getSkillSeekerProjectEntity().getId())).thenReturn(Optional.of(skillSeekerProjectEntity));
        assertEquals(1, requirementService.insertData(seekerRequirementList).size());
    }

    @Test
    void updateData() {
        when(modelMapper.map(seekerRequirement, JobDto.class)).thenReturn(jobDto);
        when(jobRepository.findByJobId(seekerRequirement.getJobId())).thenReturn(Optional.of(job));
        when(skillSeekerProjectRepository.findById(seekerRequirement.getSkillSeekerProjectEntity().getId())).thenReturn(Optional.of(skillSeekerProjectEntity));
        assertEquals(seekerRequirement.getJobId(), requirementService.updateData(seekerRequirement).getJobId());
    }

    @Test
    void deleteData() {
        when(jobRepository.findByJobId(Mockito.anyString())).thenReturn(Optional.of(job));
        requirementService.deleteData(job.getJobId());
        Mockito.verify(jobRepository, times(1)).findByJobId(Mockito.anyString());
    }
}