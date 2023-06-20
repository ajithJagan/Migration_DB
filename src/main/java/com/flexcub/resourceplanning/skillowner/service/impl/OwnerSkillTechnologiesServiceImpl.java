package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillTechnologies;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillTechnologiesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillTechnologiesRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillTechnologiesService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

@Service
public class OwnerSkillTechnologiesServiceImpl implements OwnerSkillTechnologiesService {

    @Autowired
    OwnerSkillTechnologiesRepository ownerSkillTechnologiesRepository;

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(OwnerSkillTechnologiesServiceImpl.class);

    public List<OwnerSkillTechnologies> getDataTech() {
        logger.info("OwnerSkillTechnologiesServiceImpl || getDataTech || Get all datas from the OwnerSkillTechnologyEntity");

        Optional<List<OwnerSkillTechnologiesEntity>> ownerSkillTechnologiesEntities = Optional.ofNullable(ownerSkillTechnologiesRepository.findAll(Sort.by(Sort.Direction.ASC, "priority")));
        List<OwnerSkillTechnologies> ownerSkillTechnologiesDto = new ArrayList<>();

        if (!ownerSkillTechnologiesEntities.get().isEmpty()) {
            for (OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity : ownerSkillTechnologiesEntities.get()) {
                OwnerSkillTechnologies ownerSkillTechnologies = modelMapper.map(ownerSkillTechnologiesEntity, OwnerSkillTechnologies.class);
                ownerSkillTechnologiesDto.add(ownerSkillTechnologies);
            }
            return ownerSkillTechnologiesDto;
        } else {
            throw new ServiceException(TECHNOLOGY_NOT_FOUND.getErrorCode(), TECHNOLOGY_NOT_FOUND.getErrorDesc());
        }
    }


    public OwnerSkillTechnologies insertDataTech(OwnerSkillTechnologies ownerSkillTechnologiesDto) {
        String technologyToBeSaved = capitalizeFully(ownerSkillTechnologiesDto.getTechnologyValues());

        OwnerSkillTechnologiesEntity existingOwnerInfo = ownerSkillTechnologiesRepository.findByTechnologyValuesIgnoreCase(technologyToBeSaved);
        if (!ObjectUtils.isEmpty(existingOwnerInfo)) {
            logger.error("OwnerSkillTechnologiesServiceImpl || insertDataTech || Technology is already exist in OwnerSkillTechnologiesEntity");
            throw new ServiceException(TECHNOLOGY_EXIST.getErrorCode(), TECHNOLOGY_EXIST.getErrorDesc());
        } else {
            try {
                logger.info("OwnerSkillTechnologiesServiceImpl || insertDataTech || Technology was inserted OwnerSkillTechnologiesEntity=={}", ownerSkillTechnologiesDto);
                ownerSkillTechnologiesDto.setTechnologyValues(technologyToBeSaved);
                OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = modelMapper.map(ownerSkillTechnologiesDto, OwnerSkillTechnologiesEntity.class);
                ownerSkillTechnologiesRepository.save(ownerSkillTechnologiesEntity);
                BeanUtils.copyProperties(ownerSkillTechnologiesEntity, ownerSkillTechnologiesDto, NullPropertyName.getNullPropertyNames(ownerSkillTechnologiesEntity));
                return ownerSkillTechnologiesDto;
            } catch (NullPointerException e) {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid data");
            } catch (Exception e) {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Failed to save data");
            }
        }
    }

//    public void deleteDataTech(int id) {
//        try {
//            logger.info("OwnerSkillTechnologiesServiceImpl || deleteDataTech || Technology Data is deleted by particular Id=={}", id);
//            ownerSkillTechnologiesRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid Id");
//        }
//    }

    public OwnerSkillTechnologies updateOwnerDetails(OwnerSkillTechnologies ownerSkillTechnologiesDto) {
        try {
            OwnerSkillTechnologiesEntity ownerSkillTechnologiesEntity = modelMapper.map(ownerSkillTechnologiesDto, OwnerSkillTechnologiesEntity.class);
            logger.info("OwnerSkillTechnologiesServiceImpl || updateOwnerDetails || Technology detail is inserted and saved in OwnerSkillTechnologiesEntity=={}", ownerSkillTechnologiesEntity);
            ownerSkillTechnologiesRepository.saveAndFlush(ownerSkillTechnologiesEntity);
            BeanUtils.copyProperties(ownerSkillTechnologiesEntity, ownerSkillTechnologiesDto, NullPropertyName.getNullPropertyNames(ownerSkillTechnologiesDto));
            return ownerSkillTechnologiesDto;
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), "Invalid Id");
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Failed to save data");
        }
    }

}

