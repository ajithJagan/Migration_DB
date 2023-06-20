package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillseeker.dto.BaseAndMaxRate;
import com.flexcub.resourceplanning.skillseeker.entity.BaseAndMaxRateCardEntity;
import com.flexcub.resourceplanning.skillseeker.repository.BaseAndMaxRateCardRepository;
import com.flexcub.resourceplanning.skillseeker.service.BaseAndMaxRateService;
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

@Service
public class BaseAndMaxRateServiceImpl implements BaseAndMaxRateService {


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BaseAndMaxRateCardRepository baseAndMaxRateCardRepository;

    Logger logger = LoggerFactory.getLogger(BaseAndMaxRateServiceImpl.class);

    @Override
    public BaseAndMaxRate insertData(BaseAndMaxRate baseAndMaxRateCard) {
        BaseAndMaxRateCardEntity baseAndMaxRate = modelMapper.map(baseAndMaxRateCard, BaseAndMaxRateCardEntity.class);
        logger.info("BaseRateAndMaxRateServiceImpl || insertData || Updating the RateCard Details {} //-> ", baseAndMaxRateCard);
        baseAndMaxRateCardRepository.save(baseAndMaxRate);
        BeanUtils.copyProperties(baseAndMaxRate, baseAndMaxRateCard, NullPropertyName.getNullPropertyNames(baseAndMaxRate));
        return baseAndMaxRateCard;
    }

    @Override
    public List<BaseAndMaxRate> getData() {

        Optional<List<BaseAndMaxRateCardEntity>> baseAndMaxRateCardList = Optional.of(baseAndMaxRateCardRepository.findAll());
        List<BaseAndMaxRate> baseAndMaxRateDtoList = new ArrayList<>();

        try {
            for (BaseAndMaxRateCardEntity rateCard : baseAndMaxRateCardList.get()) {
                BaseAndMaxRate baseAndMaxRateDto = modelMapper.map(rateCard, BaseAndMaxRate.class);
                baseAndMaxRateDtoList.add(baseAndMaxRateDto);
            }
            logger.info("BaseRateAndMaxRateServiceImpl || getData || Get the all RateCard Details....");
            return baseAndMaxRateDtoList;
        } catch (ServiceException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
        }
    }
}
