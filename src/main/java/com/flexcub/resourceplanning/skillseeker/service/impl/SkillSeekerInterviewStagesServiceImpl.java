package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerInterviewStages;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerInterviewStagesEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerInterviewStagesRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerInterviewStagesService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_FOUND;

@Service
public class SkillSeekerInterviewStagesServiceImpl implements SkillSeekerInterviewStagesService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SkillSeekerInterviewStagesRepository seekerInterviewStagesRepo;
    Logger logger = LoggerFactory.getLogger(SkillSeekerInterviewStagesServiceImpl.class);

    /**
     * This method is to get interviewStages data of SkillSeeker
     *
     * @return It returns list of SkillSeeker interviewStages
     */

    @Override
    public List<SkillSeekerInterviewStages> getInterviewStages() {

        Optional<List<SkillSeekerInterviewStagesEntity>> interviewStagesEntities = Optional.ofNullable(seekerInterviewStagesRepo.findAll());
        List<SkillSeekerInterviewStages> interviewStages = new ArrayList<>();

        try {
            if (!interviewStagesEntities.get().isEmpty()) {
                for (SkillSeekerInterviewStagesEntity entity : interviewStagesEntities.get()) {
                    SkillSeekerInterviewStages stages = modelMapper.map(entity, SkillSeekerInterviewStages.class);
                    interviewStages.add(stages);
                }
                logger.info("SkillSeekerInterviewStagesServiceImpl || getInterviewStages || Get the Interview stages....");
                return interviewStages;
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
            }
        } catch (ServiceException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
        }
    }
}
