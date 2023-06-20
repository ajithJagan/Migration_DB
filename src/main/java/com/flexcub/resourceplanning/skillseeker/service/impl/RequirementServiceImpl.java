package com.flexcub.resourceplanning.skillseeker.service.impl;


import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillYearOfExperienceRepository;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.service.RequirementService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerRequirement;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class RequirementServiceImpl implements RequirementService {

    Logger logger = LoggerFactory.getLogger(RequirementServiceImpl.class);

    @Autowired
    JobRepository jobRepository;
    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;

    @Autowired
    OwnerSkillYearOfExperienceRepository yearOfExperienceRepository;

    @Autowired
    SkillSeekerRepository skillSeekerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Value("${job.status}")
    private String status;

    /**
     * Method to get the List of all SkillSeekerRequirement details
     *
     * @return list of all details
     */

    public List<SeekerRequirement> getDataById(int skillSeekerId) {
        try {
            Optional<List<Job>> jobList = Optional.of(jobRepository.findBySkillSeekerId(skillSeekerId));
            List<SeekerRequirement> requirementDtos = new ArrayList<>();
            if (!jobList.get().isEmpty()) {
                logger.info("RequirementServiceImpl || getData || Displays the Skill-partnerRequirements");
                for (Job job : jobList.get()) {
                    SeekerRequirement seekerRequirementDto = jobEntityToRequirementEntity(job);
                    requirementDtos.add(seekerRequirementDto);
                }
                return requirementDtos;
            } else {
                throw new ServiceException(INVALID_SEEKER.getErrorCode(), INVALID_SEEKER.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_SEEKER_DATA.getErrorCode(), INVALID_SEEKER_DATA.getErrorDesc());
        }
    }

    /**
     * Method to insert new data into SkillSeekerRequirement
     *
     * @param skillSeekerRequirement add data
     * @return newly inserted data
     */


    public List<JobDto> insertData(List<SeekerRequirement> skillSeekerRequirement) {
        try {
            List<Job> jobList = new ArrayList<>();
            for (SeekerRequirement seekerRequirement : skillSeekerRequirement) {
                jobList.add(jobRepository.save(requirementEntityToJob(seekerRequirement)));
            }
            List<JobDto> jobDtoList1 = new ArrayList<>();
            for (Job jobDto1 : jobList) {
                JobDto jobDto = modelMapper.map(jobDto1, JobDto.class);
                jobDtoList1.add(jobDto);
            }
            logger.info("RequirementServiceImpl || insertData ||  Successfully inserted requirement details {} // ->", skillSeekerRequirement);
            return jobDtoList1;
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_SEEKER.getErrorCode(), INVALID_SEEKER.getErrorDesc());
        }
    }

    /**
     * Method to update the existing data in SkillSeekerRequirement
     *
     * @param update skillseekerRequirement data
     * @return updated data
     */
    public JobDto updateData(SeekerRequirement update) {
        JobDto jobDto = modelMapper.map(update, JobDto.class);
        try {
            Optional<Job> seekerRequirement = jobRepository.findByJobId(update.getJobId());
            logger.info("RequirementServiceImpl || updateData || Successfully updated  requirement details");
            if (seekerRequirement.isPresent()) {
                Job job = requirementEntityToJob(update);
                BeanUtils.copyProperties(job, seekerRequirement.get(), NullPropertyName.getNullPropertyNames(job));
                jobRepository.save(seekerRequirement.get());
                logger.info("RequirementServiceImpl || updateData || Successfully updated  requirement details");
                return jobDto;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    /**
     * Method to delete the data from SkillSeekerRequirement Based on Id
     *
     * @param id deleting method
     */

    public void deleteData(String id) {
        Optional<Job> skillRequirementData = jobRepository.findByJobId(id);
        if (skillRequirementData.isPresent()) {
            skillRequirementData.get().setDeletedAt(LocalDateTime.now());
            logger.info("RequirementServiceImpl || deleteData || Data has been deleted");
            jobRepository.save(skillRequirementData.get());
        } else {
            throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
        }
    }

    private List<OwnerSkillTechnologiesEntity> addTechToEntity(SeekerRequirement seekerRequirement) {
        if (null != seekerRequirement.getSkillSeekerProjectEntity()) {
            List<OwnerSkillTechnologiesEntity> technologiesEntities = new ArrayList<>();
            Optional<SkillSeekerProjectEntity> skillSeekerProject = skillSeekerProjectRepository.findById(seekerRequirement.getSkillSeekerProjectEntity().getId());
            if (skillSeekerProject.isPresent()) {
                for (SkillSeekerTechnologyData seekerTechnologyData : skillSeekerProject.get().getSkillSeekerTechnologyData()) {
                    OwnerSkillTechnologiesEntity skillTechnologies = seekerTechnologyData.getOwnerSkillTechnologiesEntity();
                    technologiesEntities.add(skillTechnologies);
                }
            }
            return technologiesEntities;
        }
        return Collections.emptyList();
    }

    private SeekerRequirement jobEntityToRequirementEntity(Job job) {
        SeekerRequirement seekerRequirement = new SeekerRequirement();
        seekerRequirement.setJobId(job.getJobId());
        seekerRequirement.setJobTitle(job.getJobTitle());
        seekerRequirement.setJobDescription(job.getJobDescription());
        seekerRequirement.setSkillSeeker(job.getSkillSeeker());
        seekerRequirement.setJobLocation(job.getJobLocation());
        seekerRequirement.setRemote(job.getRemote());
        seekerRequirement.setTravel(job.getTravel());
        seekerRequirement.setOriginalOfPositions(job.getOriginalNumberOfPosition());
        seekerRequirement.setBaseRate(job.getBaseRate());
        seekerRequirement.setMaxRate(job.getMaxRate());
        seekerRequirement.setStatus(job.getStatus());
        seekerRequirement.setPositionsAvailable(job.getNumberOfPositions());
        seekerRequirement.setSkillSeekerProjectEntity(job.getSeekerProject());
        seekerRequirement.setFederalSecurityClearance(job.getFederalSecurityClearance());
        seekerRequirement.setScreeningQuestions(job.getScreeningQuestions());
        seekerRequirement.setHiringPriority(job.getHiringPriority());
        seekerRequirement.setCoreTechnology(job.getCoreTechnology());
        seekerRequirement.setExpiryDate(job.getExpiryDate());
        return seekerRequirement;
    }

    private Job requirementEntityToJob(SeekerRequirement seekerRequirement) {
        Job job = new Job();
        Optional<SkillSeekerProjectEntity> projectEntity = Optional.empty();
        if (null != seekerRequirement.getSkillSeekerProjectEntity()) {
            projectEntity = skillSeekerProjectRepository.findById(seekerRequirement.getSkillSeekerProjectEntity().getId());
        }
        try {
            Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(seekerRequirement.getSkillSeeker().getId());
            if (skillSeeker.isPresent()) {
                job.setTaxIdBusinessLicense(skillSeeker.get().getTaxIdBusinessLicense());
            }
            job.setBaseRate(Optional.ofNullable(seekerRequirement.getBaseRate()).orElse(null));
            job.setMaxRate(Optional.ofNullable(seekerRequirement.getMaxRate()).orElse(null));
            job.setCoreTechnology(Optional.ofNullable(seekerRequirement.getCoreTechnology()).orElse(null));
            job.setFederalSecurityClearance(Optional.ofNullable(seekerRequirement.getFederalSecurityClearance()).orElse(null));
            job.setHiringPriority(Optional.ofNullable(seekerRequirement.getHiringPriority()).orElse(null));
            job.setScreeningQuestions(Optional.ofNullable(seekerRequirement.getScreeningQuestions()).orElse(null));
            job.setJobDescription(Optional.ofNullable(seekerRequirement.getJobDescription()).orElse(null));
            job.setJobLocation(Optional.ofNullable(seekerRequirement.getJobLocation()).orElse(null));
            job.setJobTitle(Optional.ofNullable(seekerRequirement.getJobTitle()).orElse(null));
            job.setNumberOfPositions(Optional.ofNullable(seekerRequirement.getPositionsAvailable()).orElse(null));
            job.setOriginalNumberOfPosition(Optional.ofNullable(seekerRequirement.getOriginalOfPositions()).orElse(null));
            job.setStatus("New");
            if (projectEntity.isPresent()) {
                job.setOwnerSkillDomainEntity(Optional.ofNullable(projectEntity.get().getOwnerSkillDomainEntity()).orElse(null));
            }
            if (projectEntity.isPresent()) {
                job.setProject(Optional.ofNullable(projectEntity.get().getTitle()).orElse(null));
            }
            if (projectEntity.isPresent()) {
                job.setSeekerProject(Optional.of(projectEntity.get()).orElse(null));
            }
        } catch (NullPointerException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid Req/Id is not found");
        }
        job.setOwnerSkillTechnologiesEntity(Optional.ofNullable(addTechToEntity(seekerRequirement)).orElse(null));


        job.setExpiryDate(Optional.ofNullable(seekerRequirement.getExpiryDate()).orElse(null));
        if (null != seekerRequirement.getExpYears()) {
            job.setOwnerSkillYearOfExperience(yearOfExperienceRepository.findByExperience((seekerRequirement.getExpYears()) + "+"));
        }
        if (null != seekerRequirement.getSkillSeeker()) {
            job.setSkillSeeker(seekerRequirement.getSkillSeeker());
        }
        job.setTravel(seekerRequirement.getTravel());
        job.setRemote(seekerRequirement.getRemote());

        return job;

    }
}
