package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRateCardRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTechnologyService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTechnology;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class SkillSeekerTechnologyServiceImpl implements SkillSeekerTechnologyService {
    @Autowired
    SkillSeekerRateCardRepository seekerTechRepo;
    Logger logger = LoggerFactory.getLogger(SkillSeekerTechnologyServiceImpl.class);

    @Autowired
    ModelMapper modelMapper;


    /**
     * This method is to insert multiple data of skillSeekerRateCard.
     *
     * @param skillSeekerTechList list
     * @return It returns list of inserted data of skillSeeker rateCard.
     */
    @Override
    public List<SkillSeekerTechnologyData> insertMultipleData(List<SkillSeekerTechnologyData> skillSeekerTechList) {
        try {
            logger.info("SkillSeekerRateCardServiceImpl || insertMultipleData || Inserting the RateCard Details");
            return seekerTechRepo.saveAllAndFlush(skillSeekerTechList);
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    /**
     * This method is to update data of skillSeekerRateCard.
     *
     * @param skillSeekerTechnology data
     * @return It returns updated data of skillSeeker rateCard.
     */
    @Override
    public SkillSeekerTechnology updateData(SkillSeekerTechnology skillSeekerTechnology) {
        SkillSeekerTechnologyData seekerTechnologyData = modelMapper.map(skillSeekerTechnology, SkillSeekerTechnologyData.class);
        try {

            Optional<SkillSeekerTechnologyData> skillSeekerTechnologyData = seekerTechRepo.findById(seekerTechnologyData.getId());
            if (skillSeekerTechnologyData.isPresent()) {
                logger.info("SkillSeekerRateCardServiceImpl || updateData ||  Updating the RateCard Detail {} // ->", skillSeekerTechnology.getId());
                SkillSeekerTechnology skillSeekerTechnology1 = modelMapper.map(seekerTechnologyData, SkillSeekerTechnology.class);
                BeanUtils.copyProperties(skillSeekerTechnology, skillSeekerTechnologyData.get(), NullPropertyName.getNullPropertyNames(skillSeekerTechnology));
                seekerTechRepo.save(skillSeekerTechnologyData.get());
                return skillSeekerTechnology1;
            } else {
                throw new ServiceException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_TECH_DATA_ID.getErrorCode(), INVALID_TECH_DATA_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    /**
     * This method is to delete skillSeeker rateCard detail based on id.
     *
     * @param id of skillSeeker
     */
    @Override
    public void deleteData(int id) {

        Optional<SkillSeekerTechnologyData> skillSeekerTechnologyData;
        try {
            skillSeekerTechnologyData = seekerTechRepo.findById(id);
        } catch (Exception e) {
            throw new ServiceException(INVALID_TECH_DATA_ID.getErrorCode(), INVALID_TECH_DATA_ID.getErrorDesc());
        }
        if (skillSeekerTechnologyData.isPresent()) {
            logger.info("SkillSeekerRateCardServiceImpl || deleteData || Deleting the RateCard id=={} // ->", id);
            seekerTechRepo.deleteById(id);
        } else {
            throw new ServiceException(INVALID_TECH_DATA_ID.getErrorCode(), INVALID_TECH_DATA_ID.getErrorDesc());
        }
    }


    /**
     * This method is to get data of skillSeekerRateCard based on id.
     *
     * @param id of skillSeeker
     * @return It returns the skillSeeker rateCard for the given id.
     */


    @Transactional
    @Override
    public List<SkillSeekerTechnology> getDataByProjectId(int id) {
        List<SkillSeekerTechnology> skillSeekerTechnologies = new ArrayList<>();

        List<SkillSeekerTechnologyData> skillSeekerTechnology = null;
        try {
            skillSeekerTechnology = seekerTechRepo.findByProjectId(id);
        } catch (Exception e) {
            throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
        }
        if (!skillSeekerTechnology.isEmpty()) {
            for (SkillSeekerTechnologyData seekerTechnologyData : skillSeekerTechnology) {
                Hibernate.initialize(skillSeekerTechnology);
                SkillSeekerTechnology seekerTechnology = modelMapper.map(seekerTechnologyData, SkillSeekerTechnology.class);
                skillSeekerTechnologies.add(seekerTechnology);
            }
            logger.info("SkillSeekerRateCardServiceImpl || getDataByProjectId || Getting the RateCard Project id== {} // ->", id);
            return skillSeekerTechnologies;
        } else {
            throw new ServiceException(INVALID_PROJECT_ID.getErrorCode(), INVALID_PROJECT_ID.getErrorDesc());
        }
    }

}

