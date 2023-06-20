package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.BaseAndMaxRate;

import java.util.List;

public interface BaseAndMaxRateService {

    BaseAndMaxRate insertData(BaseAndMaxRate baseAndMaxRateCard);

    List<BaseAndMaxRate> getData();
}
