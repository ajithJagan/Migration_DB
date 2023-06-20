package com.flexcub.resourceplanning.skillowner.service.impl;


import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.Visa;
import com.flexcub.resourceplanning.skillowner.entity.VisaEntity;
import com.flexcub.resourceplanning.skillowner.repository.VisaStatusRepository;
import com.flexcub.resourceplanning.skillowner.service.VisaStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.VISA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Component
public class VisaStatusServiceImpl implements VisaStatusService {

    @Autowired
    VisaStatusRepository visaStatusRepository;

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(VisaStatusServiceImpl.class);

    @Override
    public List<Visa> getVisa() {
        logger.info("VisaStatusServiceImpl || getVisa || Get all VisaStatus ");
        Optional<List<VisaEntity>> visaEntities = Optional.ofNullable(visaStatusRepository.findAll());
        List<Visa> visaDtoList = new ArrayList<>();
        if (!visaEntities.get().isEmpty()) {
            for (VisaEntity visaEntity : visaEntities.get()) {
                Visa visa = modelMapper.map(visaEntity, Visa.class);
                visaDtoList.add(visa);
            }
            return visaDtoList;
        } else {
            throw new ServiceException(VISA_NOT_FOUND.getErrorCode(), VISA_NOT_FOUND.getErrorDesc());
        }
    }
}
