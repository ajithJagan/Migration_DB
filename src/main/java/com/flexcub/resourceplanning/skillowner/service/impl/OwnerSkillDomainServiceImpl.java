package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillDomain;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillDomainService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;


@Service
@RequiredArgsConstructor
@Component

public class OwnerSkillDomainServiceImpl implements OwnerSkillDomainService {

    @Autowired
    OwnerSkillDomainRepository ownerSkillDomainRepository;

    @Autowired
    ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(OwnerSkillDomainServiceImpl.class);

    @Override
    public List<OwnerSkillDomain> getDatadomain() {
        logger.info("OwnerSkillDomainServiceImpl || getDatadomain || Get all datas from the OwnerSkillDomainEntity");
        Optional<List<OwnerSkillDomainEntity>> ownerSkillDomainEntities = Optional.ofNullable(ownerSkillDomainRepository.findAll(Sort.by(Sort.Direction.ASC, "priority")));
        List<OwnerSkillDomain> ownerSkillDomainsDtoList = new ArrayList<>();
        if (!ownerSkillDomainEntities.get().isEmpty()) {
            for (OwnerSkillDomainEntity ownerSkillDomainEntity : ownerSkillDomainEntities.get()) {
                OwnerSkillDomain ownerSkillDomain = modelMapper.map(ownerSkillDomainEntity, OwnerSkillDomain.class);
                ownerSkillDomainsDtoList.add(ownerSkillDomain);
            }
            return ownerSkillDomainsDtoList;
        } else {
            throw new ServiceException(DOMAIN_NOT_FOUND.getErrorCode(), DOMAIN_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    public OwnerSkillDomain insertDataDomain(OwnerSkillDomain ownerSkillDomainDto) {

        Optional<OwnerSkillDomainEntity> existingOwnerInfo = Optional.ofNullable(ownerSkillDomainRepository.
                findByDomainValuesIgnoreCase(ownerSkillDomainDto.getDomainValues()));
        if (existingOwnerInfo.isPresent()) {

            logger.error("OwnerSkillDomainServiceImpl || insertDataDomain ||  Domain is already exist");
            throw new ServiceException(DOMAIN_VALUES_EXIST.getErrorCode(), DOMAIN_VALUES_EXIST.getErrorDesc());
        } else {
            logger.info("OwnerSkillDomainServiceImpl || insertDataDomain || Domain was inserted in OwnerSkillDomainEntity");
            try {
                OwnerSkillDomainEntity ownerSkillDomainEntity1 = modelMapper.map(ownerSkillDomainDto, OwnerSkillDomainEntity.class);
                ownerSkillDomainRepository.save(ownerSkillDomainEntity1);
                BeanUtils.copyProperties(ownerSkillDomainEntity1, ownerSkillDomainDto, NullPropertyName.getNullPropertyNames(ownerSkillDomainEntity1));
                return ownerSkillDomainDto;
            } catch (Exception e) {
                throw new ServiceException(DOMAIN_DATA_NOT_SAVED.getErrorCode(), DOMAIN_DATA_NOT_SAVED.getErrorDesc());
            }
        }
    }

    @Override
    public OwnerSkillDomain updateDomain(OwnerSkillDomain ownerSkillDomainDto) {
        try {
            OwnerSkillDomainEntity ownerSkillDomainEntity = modelMapper.map(ownerSkillDomainDto, OwnerSkillDomainEntity.class);
            ownerSkillDomainRepository.saveAndFlush(ownerSkillDomainEntity);
            BeanUtils.copyProperties(ownerSkillDomainEntity, ownerSkillDomainDto, NullPropertyName.getNullPropertyNames(ownerSkillDomainEntity));
        } catch (Exception e) {
            throw new ServiceException(DOMAIN_DATA_NOT_SAVED.getErrorCode(), DOMAIN_DATA_NOT_SAVED.getErrorDesc());
        }
        logger.info("OwnerSkillDomainServiceImpl || updateDomain || Domain was updated in OwnerSkillDomainEntity");
        return ownerSkillDomainDto;
    }
@Override
    public OwnerSkillDomainEntity getById(int id) {
        OwnerSkillDomainEntity ownerSkillDomainEntity = ownerSkillDomainRepository.getById(id);
        return ownerSkillDomainEntity;
    }
}
