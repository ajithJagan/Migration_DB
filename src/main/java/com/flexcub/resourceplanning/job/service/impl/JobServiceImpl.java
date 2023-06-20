package com.flexcub.resourceplanning.job.service.impl;

import com.flexcub.resourceplanning.contracts.dto.ContractsFilesDto;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.repository.ContractFileRepository;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.repository.HiringPriorityRepository;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.job.service.JobService;
import com.flexcub.resourceplanning.job.service.SelectionPhaseService;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillTechnologiesRepository;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;

    @Autowired
    RequirementPhaseRepository requirementPhaseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    SelectionPhaseService selectionPhaseService;

    @Autowired
    HiringPriorityRepository hiringPriorityRepository;

    @Autowired
    OwnerSkillTechnologiesRepository ownerSkillTechnologiesRepository;

    @Autowired
    SkillSeekerRepository skillSeekerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ContractFileRepository contractFileRepository;

    Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);


    @Value("${job.status}")
    private String status;

    @Transactional
    @Override
    public List<Job> getJobDetails() {
        List<Job> jobList;
        try {
            jobList = jobRepository.findAll();
        } catch (Exception e) {
            logger.info("JobServiceImpl || getData || Data not Found ");
            throw new ServiceException(JOB_NOT_FOUND.getErrorCode(), JOB_NOT_FOUND.getErrorDesc());
        }
        logger.info("JobServiceImpl || getData || Get all the retrieve job Details ");
        return jobList;
    }

    @Override
    @Transactional
    public JobDto publish(String jobId) {
        Optional<Job> job = jobRepository.findByJobId(jobId);
        Job jobFromDb;
        if (job.isPresent()) {
            jobFromDb = job.get();
            jobFromDb.setStatus(status);
            jobFromDb = jobRepository.saveAndFlush(jobFromDb);
        } else {
            logger.info("JobServiceImpl || publish || Invalid JobId {}", jobId);
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }
        logger.info("JobServiceImpl || publish || Publishing the Job {}", jobId);
        return modelMapper.map(jobFromDb, JobDto.class);
    }


    @Override
    public JobDto createJobDetails(JobDto jobDto) {
        Job job;
        try {
            job = modelMapper.map(jobDto, Job.class);

            jobDto.getOwnerSkillTechnologiesEntity().forEach(k -> {
                Optional<OwnerSkillTechnologiesEntity> byId = ownerSkillTechnologiesRepository.findById(k.getTechnologyId());
                job.setCoreTechnology(byId.get().getTechnologyValues());
            });
            if (null != job.getSeekerProject() && job.getSeekerProject().getId() == 0 && null != job.getTaxIdBusinessLicense()) {
                job.setSeekerProject(null);
                job.setProject("Default");
            }
            Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(jobDto.getSkillSeeker().getId());
            if (skillSeeker.isPresent()) {
                job.setTaxIdBusinessLicense(skillSeeker.get().getTaxIdBusinessLicense());
            }
            jobRepository.save(job);

        } catch (Exception e) {
            logger.info("JobServiceImpl || insertData || Invalid Seeker Id or  Invalid TaxIdBusinessLicense or Invalid Job Details {}", jobDto);
            throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
        }
        logger.info("JobServiceImpl || insertData || Inserting the jobDetails  {}", jobDto);
        return modelMapper.map(job, JobDto.class);
    }


    @Override
    @Transactional
    public List<JobDto> getAllJobDetails(int seekerId) {
        try {
            Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerRepository.findById(seekerId);
            if (skillSeekerEntity.isPresent()) {
                Optional<List<Job>> jobList = jobRepository.findByTaxIdBusinessLicense(skillSeekerEntity.get().getTaxIdBusinessLicense());
                if (!jobList.get().isEmpty()) {
                    List<JobDto> jobDto = new ArrayList<>();
                    ContractsFilesDto contractsFilesDto = new ContractsFilesDto();

                    for (Job job : jobList.get()) {
                        if (null  != skillSeekerEntity.get().getMsaId()) {
                            Optional<ContractFiles> contractFiles = contractFileRepository.findById(skillSeekerEntity.get().getMsaId().getId());
                            contractsFilesDto.setId(contractFiles.get().getId());
                            contractsFilesDto.setFileVersion(contractFiles.get().getFileVersion());
                            contractsFilesDto.setMimeType(contractFiles.get().getMimeType());
                            contractsFilesDto.setSize(contractFiles.get().getSize());
                            contractsFilesDto.setFileName(contractFiles.get().getFileName());
                        }
                        JobDto dto = new JobDto();
                        modelMapper.map(job, dto);
                        if (null  != skillSeekerEntity.get().getMsaId()) {
                            dto.getSkillSeeker().setContractFiles(contractsFilesDto);
                            SkillSeeker map = modelMapper.map(skillSeekerEntity, SkillSeeker.class);
                            dto.setSkillSeeker(map);
                            dto.getSkillSeeker().setContractFiles(contractsFilesDto);
                        }else{
                            dto.getSkillSeeker().setContractFiles(null);
                        }
                        jobDto.add(dto);
                    }
                    logger.info("JobServiceImpl || getJobDetails By TaxIdBusinessLicense || Get JobDetails By TaxIdBusinessLicense");
                    return jobDto;
                } else {
                    logger.info("JobServiceImpl || getJobDetails By TaxIdBusinessLicense || Invalid TaxIdBusinessLicense {}", skillSeekerEntity.get().getTaxIdBusinessLicense());
                    throw new ServiceException(INVALID_TAX_ID.getErrorCode(), INVALID_TAX_ID.getErrorDesc());
                }
            } else {
                throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

    }




    public List<HiringPriority> getHiringPriority() {
        logger.info("JobServiceImpl || getHiringPriority || Get HiringPriority Details");
        return hiringPriorityRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteJob(String jobId) {

        Optional<Job> job = jobRepository.findByJobId(jobId);
        if (job.isPresent()) {
            logger.info("JobServiceImpl || deleteJob || deleting Job Details");
            requirementPhaseRepository.deleteAll(requirementPhaseRepository.deleteByJobId(jobId));
            selectionPhaseRepository.deleteAll(selectionPhaseRepository.deleteByJobId(jobId));
            jobRepository.deleteById(jobId);
        } else {
            logger.info("JobServiceImpl || deleteJob || Invalid JobId {}", jobId);
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }

    }

    @Override
    public Job findByJobId(String id) {
        Optional<Job> byJobJobId = Optional.ofNullable(Optional.ofNullable(jobRepository.findByJobJobId(id)).orElseThrow(() -> new ServiceException(JOB_NOT_FOUND.getErrorCode(), JOB_NOT_FOUND.getErrorDesc())));
        return byJobJobId.get();
    }

    @Override
    public Job saveAndFlush(Job id) {
        Job job = jobRepository.saveAndFlush(id);
        return job;
    }

    @Override

    public List<Job> getBySeekerProject(int projectId) {
        return jobRepository.findBySeekerProjectId(projectId);
    }

    @Override
    @Transactional
    public List<Job> getBySeekerIdAndProjectId(int seekerId, int projectId) {
        List<Job> jobs = jobRepository.findBySeekerIdAndProjectId(seekerId, projectId);
        return jobs;


    }

    @Override
    @Transactional
    public Optional<Job> getJobByProjectId(int projectId) {
        Optional<Job> job = jobRepository.findJobByProjectId(projectId);
        return job;
    }

    @Override
    @Transactional
    public Optional<Job> getByJobId(String jobId) {
        Optional<Job> job = jobRepository.findByJobId(jobId);
        return job;
    }

    @Override
    @Transactional
    public Optional<Job> getJob(String jobId) {
        return jobRepository.findByJobId(jobId);
    }

    @Override
    public Job getById(String jobId) {
        return jobRepository.findByJobJobId(jobId);
    }

}

