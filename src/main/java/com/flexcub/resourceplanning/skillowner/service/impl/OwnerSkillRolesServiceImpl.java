package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillRolesRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillRolesService;
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

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_SAVED;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.ROLES_NOT_FOUND;

@Service
public class OwnerSkillRolesServiceImpl implements OwnerSkillRolesService {
    @Autowired
    OwnerSkillRolesRepository ownerSkillRolesRepository;

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(OwnerSkillRolesServiceImpl.class);

    public List<OwnerSkillRoles> getDataroles() {
        logger.info("OwnerSkillRolesServiceImpl || getDataroles || Get all datas from the OwnerSkillRolesEntity");
        Optional<List<OwnerSkillRolesEntity>> ownerSkillRolesEntities = Optional.ofNullable(ownerSkillRolesRepository.findAll());
        List<OwnerSkillRoles> ownerSkillRolesListDto = new ArrayList<>();
        if (!ownerSkillRolesEntities.get().isEmpty()) {
            for (OwnerSkillRolesEntity ownerSkillRolesEntity : ownerSkillRolesEntities.get()) {
                OwnerSkillRoles ownerSkillRoles = modelMapper.map(ownerSkillRolesEntity, OwnerSkillRoles.class);
                ownerSkillRolesListDto.add(ownerSkillRoles);
            }
            return ownerSkillRolesListDto;
        } else {
            throw new ServiceException(ROLES_NOT_FOUND.getErrorCode(), ROLES_NOT_FOUND.getErrorDesc());
        }
    }

    public OwnerSkillRoles insertDataroles(OwnerSkillRoles ownerSkillRolesDto) {

        try {
            OwnerSkillRolesEntity ownerSkillRolesEntity = modelMapper.map(ownerSkillRolesDto, OwnerSkillRolesEntity.class);
            logger.info("OwnerSkillRolesServiceImpl || insertDataroles || Role was inserted in OwnerSkillRoleEntity=={}", ownerSkillRolesDto);
            ownerSkillRolesRepository.save(ownerSkillRolesEntity);
            BeanUtils.copyProperties(ownerSkillRolesEntity, ownerSkillRolesDto, NullPropertyName.getNullPropertyNames(ownerSkillRolesEntity));
            return ownerSkillRolesDto;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Invalid data");
        }
    }


    public OwnerSkillRoles updateSkillRoles(OwnerSkillRoles ownerSkillRolesDto) {
        OwnerSkillRolesEntity ownerSkillRolesEntity = modelMapper.map(ownerSkillRolesDto, OwnerSkillRolesEntity.class);

        try {
            logger.info("OwnerSkillRolesServiceImpl || updateSkillRoles || Role was updated OwnerSkillRoleEntity=={}", ownerSkillRolesEntity);
            ownerSkillRolesRepository.saveAndFlush(ownerSkillRolesEntity);
            BeanUtils.copyProperties(ownerSkillRolesEntity, ownerSkillRolesDto, NullPropertyName.getNullPropertyNames(ownerSkillRolesEntity));
            return ownerSkillRolesDto;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), "Invalid Data");
        }
    }
}
