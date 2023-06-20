package com.flexcub.resourceplanning.skillowner.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerSkillSet;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillSetService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;


@Service
public class OwnerSkillSetServiceImpl implements OwnerSkillSetService {

    Logger logger = LoggerFactory.getLogger(OwnerSkillSetServiceImpl.class);

    @Autowired
    OwnerSkillSetRepository ownerSkillSetRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ModelMapper modelMapper;

//    @Autowired
//    OwnerSkillSetRepository ownerSkillSetRepository;

    @Transactional
    @Override
    public List<SkillOwnerSkillSet> getSkillset(int skillOwnerId) {
        logger.info("OwnerSkillSetServiceImpl || getSkillset || Get data from the OwnerSkillSetEntity");
        List<SkillOwnerSkillSet> ownerSkillSetDto = new ArrayList<>();
        List<OwnerSkillSetEntity> ownerSkillSetEntities = ownerSkillSetRepository.findBySkillOwnerEntityId(skillOwnerId);
        if (!ownerSkillSetEntities.isEmpty()) {
            List<OwnerSkillSetEntity> ownerSkillSetEntity = ownerSkillSetEntities;
            for (OwnerSkillSetEntity ownerSkillSet : ownerSkillSetEntity) {
                SkillOwnerSkillSet ownerDto = new SkillOwnerSkillSet();
                BeanUtils.copyProperties(ownerSkillSet, ownerDto, NullPropertyName.getNullPropertyNames(ownerSkillSet));
                ownerSkillSetDto.add(ownerDto);
            }
            return ownerSkillSetDto;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public SkillOwnerSkillSet insertSkillSet(SkillOwnerSkillSet ownerSkillSetDto) {

        AtomicReference<Boolean> value = new AtomicReference<>(false);
        List<OwnerSkillSetEntity> ownerSkillSetEntities = ownerSkillSetRepository.findBySkillOwnerEntityId(ownerSkillSetDto.getSkillOwnerEntityId());

        ownerSkillSetEntities.forEach(ownerSkillSetEntity -> {

            if (ownerSkillSetEntity.getOwnerSkillTechnologiesEntity().getTechnologyId() == ownerSkillSetDto.getOwnerSkillTechnologiesEntity().getTechnologyId()
                    && ownerSkillSetEntity.getOwnerSkillDomainEntity().getDomainId() == ownerSkillSetDto.getOwnerSkillDomainEntity().getDomainId()) {
                value.set(true);
            }
        });

        if (!value.get()) {
            LocalDate date;
            OwnerSkillSetEntity ownerSkillSetEntity = modelMapper.map(ownerSkillSetDto, OwnerSkillSetEntity.class);

            try {
                Date dateInput = Date.valueOf(ownerSkillSetEntity.getLastUsed());
                date = dateInput.toLocalDate();
            } catch (Exception e) {
                date = LocalDate.now();
            }
            LocalDate curDate = LocalDate.now();
            if (date.isAfter(curDate)) {
                logger.error(INVALID_LASTUSED.getErrorDesc());
                throw new ServiceException(INVALID_LASTUSED.getErrorCode(), INVALID_LASTUSED.getErrorDesc());
            }
            try {
                ownerSkillSetRepository.save(ownerSkillSetEntity);
                SkillOwnerSkillSet ownerDto = modelMapper.map(ownerSkillSetEntity, SkillOwnerSkillSet.class);

                logger.info("OwnerSkillSetServiceImpl || insertDataset || DataSet was inserted in OwnerSkillSetEntity=={}", ownerSkillSetEntity);
                return ownerDto;
            } catch (Exception e) {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
            }
        } else {
            throw new ServiceException(TECHNOLOGY_EXIST_IN_OWNER.getErrorCode(), TECHNOLOGY_EXIST_IN_OWNER.getErrorDesc());
        }
    }


    @Transactional
    @Override
    public String deleteSkillset(int skillId) {
        logger.info("OwnerSkillSetServiceImpl || deleteDataset || DataSet was deleted ");
        try {
            ownerSkillSetRepository.deleteById(skillId);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            response.put("success", true);
            response.put("message", "Deleted successfully");

            return response.toString();

        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(SKILLSET_NOT_FOUND.getErrorCode(), SKILLSET_NOT_FOUND.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public SkillOwnerSkillSet updateSkillSet(SkillOwnerSkillSet ownerSkillSetDto) {
        try {
            OwnerSkillSetEntity ownerSkillSetEntity = modelMapper.map(ownerSkillSetDto, OwnerSkillSetEntity.class);

            Optional<OwnerSkillSetEntity> ownerSkillSetEntities = ownerSkillSetRepository.findById(ownerSkillSetEntity.getOwnerSkillSetId());
            if (ownerSkillSetEntities.isPresent()) {
                BeanUtils.copyProperties(ownerSkillSetEntity, ownerSkillSetEntities.get(), NullPropertyName.getNullPropertyNames(ownerSkillSetEntity));
                logger.info("OwnerSkillSetServiceImpl || updateSkillSet || DataSet was updated in OwnerSkillSetEntity=={}", ownerSkillSetEntity);
                ownerSkillSetRepository.save(ownerSkillSetEntities.get());

                return modelMapper.map(ownerSkillSetEntities, SkillOwnerSkillSet.class);
            } else {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public int skillSetPercentage(String jobId, List<OwnerSkillSetEntity> ownerSkillSetEntities) {

        double percentage;
        AtomicInteger matchingCount = new AtomicInteger(0);
        List<Integer> jobTechnologies = jobRepository.findAllTechnologyIdByJobId(jobId);
        List<Integer> skillOwnerTechnologies = new ArrayList<>();
        for (OwnerSkillSetEntity ownerSkillSetEntity : ownerSkillSetEntities) {
            skillOwnerTechnologies.add(ownerSkillSetEntity.getOwnerSkillTechnologiesEntity().getTechnologyId());
        }
        int requiredTechSize = jobTechnologies.size();

        jobTechnologies.forEach(technologyId -> {
            if (skillOwnerTechnologies.contains(technologyId))
                matchingCount.getAndIncrement();
        });

        percentage = (double) (100 * matchingCount.get()) / requiredTechSize;
        return (int) percentage;
    }

}
