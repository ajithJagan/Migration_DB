package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillStatus;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillStatusRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillStatusService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.STATUS_NOT_SAVED;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.STATUS_VALUES_EXIST;


@Service
public class OwnerSkillStatusServiceImpl implements OwnerSkillStatusService {

    @Autowired
    OwnerSkillStatusRepository ownerSkillStatusRepository;

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(OwnerSkillStatusServiceImpl.class);

    public List<OwnerSkillStatus> getDataStatus() {
        logger.info("OwnerSkillStatusServiceImpl || getDataStatus || Get all dataStatus from the OwnerSkillStatusEntity");
        Optional<List<OwnerSkillStatusEntity>> ownerSkillStatusEntities = Optional.ofNullable(ownerSkillStatusRepository.findAll());
        List<OwnerSkillStatus> ownerSkillStatusDtoList = new ArrayList<>();
        if (!ownerSkillStatusEntities.get().isEmpty()) {
            for (OwnerSkillStatusEntity ownerSkillStatusEntity : ownerSkillStatusEntities.get()) {
                OwnerSkillStatus ownerSkillStatus = modelMapper.map(ownerSkillStatusEntity, OwnerSkillStatus.class);
                ownerSkillStatusDtoList.add(ownerSkillStatus);
            }
            return ownerSkillStatusDtoList;
        } else {
            throw new ServiceException(STATUS_NOT_SAVED.getErrorCode(), STATUS_NOT_SAVED.getErrorDesc());
        }
    }

    public OwnerSkillStatus insertDataStatus(OwnerSkillStatus ownerSkillStatusDto) {
        Optional<OwnerSkillStatusEntity> existingOwnerStatusInfo = ownerSkillStatusRepository.findByStatusDescriptionIgnoreCase(ownerSkillStatusDto.getStatusDescription());
        if (!ObjectUtils.isEmpty(existingOwnerStatusInfo)) {
            logger.error("OwnerSkillStatusServiceImpl || insertDataStatus ||  Status is already exist");
            throw new ServiceException(STATUS_VALUES_EXIST.getErrorCode(), STATUS_VALUES_EXIST.getErrorDesc());
        } else {
            try {
                OwnerSkillStatusEntity ownerSkillStatusEntity = modelMapper.map(ownerSkillStatusDto, OwnerSkillStatusEntity.class);
                ownerSkillStatusRepository.save(ownerSkillStatusEntity);
                logger.info("OwnerSkillStatusServiceImpl || insertDataStatus || DataStatus was inserted in OwnerSkillStatusEntity=={}", ownerSkillStatusEntity);
                BeanUtils.copyProperties(ownerSkillStatusEntity, ownerSkillStatusDto, NullPropertyName.getNullPropertyNames(ownerSkillStatusEntity));
                return ownerSkillStatusDto;
            } catch (Exception e) {
                throw new ServiceException(STATUS_NOT_SAVED.getErrorCode(), STATUS_NOT_SAVED.getErrorDesc());
            }
        }
    }

    public OwnerSkillStatus updateOwnerDetails(OwnerSkillStatus ownerSkillStatusDto) {
        OwnerSkillStatusEntity ownerSkillStatusEntity = modelMapper.map(ownerSkillStatusDto, OwnerSkillStatusEntity.class);
        try {
            logger.info("OwnerSkillStatusServiceImpl || updateOwnerDetails || DataStatus was updated in OwnerSkillStatusEntity=={}", ownerSkillStatusEntity);
            ownerSkillStatusRepository.saveAndFlush(ownerSkillStatusEntity);
            BeanUtils.copyProperties(ownerSkillStatusEntity, ownerSkillStatusDto, NullPropertyName.getNullPropertyNames(ownerSkillStatusEntity));
            return ownerSkillStatusDto;
        } catch (Exception e) {
            throw new ServiceException(STATUS_NOT_SAVED.getErrorCode(), STATUS_NOT_SAVED.getErrorDesc());
        }

    }
}
