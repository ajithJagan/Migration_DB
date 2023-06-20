package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillLevel;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillLevelRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillLevelService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_FOUND;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.LEVEL_NOT_FOUND;

@Service
public class OwnerSkillLevelServiceImpl implements OwnerSkillLevelService {
    @Autowired
    OwnerSkillLevelRepository ownerSkillLevelRepository;

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(OwnerSkillLevelServiceImpl.class);

    public List<OwnerSkillLevel> getDatalevel() {
        logger.info("OwnerSkillLevelServiceImpl || getDatalevel || Get all datas from the OwnerSkillLevelEntity");
        Optional<List<OwnerSkillLevelEntity>> ownerSkillLevelEntities = Optional.ofNullable(ownerSkillLevelRepository.findAll());
        List<OwnerSkillLevel> ownerSkillLevelListDto = new ArrayList<>();
        if (!ownerSkillLevelEntities.get().isEmpty()) {
            for (OwnerSkillLevelEntity ownerSkillLevelEntity : ownerSkillLevelEntities.get()) {
                OwnerSkillLevel ownerSkillLevel = modelMapper.map(ownerSkillLevelEntity, OwnerSkillLevel.class);
                ownerSkillLevelListDto.add(ownerSkillLevel);
            }
            return ownerSkillLevelListDto;
        } else {
            throw new ServiceException(LEVEL_NOT_FOUND.getErrorCode(), LEVEL_NOT_FOUND.getErrorDesc());
        }

    }

    public OwnerSkillLevel insertDatalevel(OwnerSkillLevel ownerSkillLevelDto) {
        logger.info("OwnerSkillLevelServiceImpl || insertDatalevel || Data was inserted and saved in OwnerSkillLevelEntity: {}", ownerSkillLevelDto);
        try {
            OwnerSkillLevelEntity ownerSkillLevelEntity = modelMapper.map(ownerSkillLevelDto, OwnerSkillLevelEntity.class);
            ownerSkillLevelRepository.save(ownerSkillLevelEntity);
            BeanUtils.copyProperties(ownerSkillLevelEntity, ownerSkillLevelDto, NullPropertyName.getNullPropertyNames(ownerSkillLevelEntity));
            return ownerSkillLevelDto;

        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Null values are present in the data");
        }

    }


    public OwnerSkillLevel updateSkillLeveL(OwnerSkillLevel ownerSkillLevelDto) {
        OwnerSkillLevelEntity ownerSkillLevelEntity = modelMapper.map(ownerSkillLevelDto, OwnerSkillLevelEntity.class);
        logger.info("OwnerSkillLevelServiceImpl || updateSkillLeveL || Data was updated in OwnerSkillLevelEntity == {} ", ownerSkillLevelEntity);
        try {
            ownerSkillLevelRepository.saveAndFlush(ownerSkillLevelEntity);
            BeanUtils.copyProperties(ownerSkillLevelEntity, ownerSkillLevelDto, NullPropertyName.getNullPropertyNames(ownerSkillLevelEntity));
            return ownerSkillLevelDto;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Null values are present in the data");
        }
    }
}
