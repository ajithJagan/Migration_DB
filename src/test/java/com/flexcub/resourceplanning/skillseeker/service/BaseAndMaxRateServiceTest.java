package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.BaseAndMaxRate;
import com.flexcub.resourceplanning.skillseeker.entity.BaseAndMaxRateCardEntity;
import com.flexcub.resourceplanning.skillseeker.repository.BaseAndMaxRateCardRepository;
import com.flexcub.resourceplanning.skillseeker.service.impl.BaseAndMaxRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaseAndMaxRateServiceImpl.class)
class BaseAndMaxRateServiceTest {

    @Autowired
    BaseAndMaxRateServiceImpl service;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    BaseAndMaxRateCardRepository baseAndMaxRateCardRepository;

    BaseAndMaxRate baseAndMaxRateCard = new BaseAndMaxRate();

    List<BaseAndMaxRateCardEntity> baseAndMaxRateCardsEntities = new ArrayList<>();

    BaseAndMaxRateCardEntity baseAndMaxRateCardEntity = new BaseAndMaxRateCardEntity();

    @BeforeEach
    void setup() {
        baseAndMaxRateCard.setId(1);
        baseAndMaxRateCard.setBaseRate("$45");
        baseAndMaxRateCard.setMaxRate("$500");
        baseAndMaxRateCardsEntities.add(baseAndMaxRateCardEntity);
    }

    @Test
    void insertBaseAndMaxRateCard() {
        Mockito.when(modelMapper.map(baseAndMaxRateCard, BaseAndMaxRateCardEntity.class)).thenReturn(baseAndMaxRateCardEntity);
        assertEquals(baseAndMaxRateCard.getBaseRate(), service.insertData(baseAndMaxRateCard).getBaseRate());
    }

    @Test
    void insertBaseAndMaxRateCardNullTest() {
        Mockito.when(modelMapper.map(baseAndMaxRateCard, BaseAndMaxRateCardEntity.class)).thenReturn(baseAndMaxRateCardEntity);
        assertNotNull(service.insertData(baseAndMaxRateCard));
    }

    @Test
    void getBaseAndMaxRateCardTest() {
        Mockito.when(baseAndMaxRateCardRepository.findAll()).thenReturn(baseAndMaxRateCardsEntities);
        Mockito.when(modelMapper.map(baseAndMaxRateCardEntity, BaseAndMaxRate.class)).thenReturn(baseAndMaxRateCard);
        assertEquals(1, service.getData().size());
    }
}
