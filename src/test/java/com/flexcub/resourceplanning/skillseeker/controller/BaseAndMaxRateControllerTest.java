package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillseeker.controller.BaseAndMaxRateController;
import com.flexcub.resourceplanning.skillseeker.dto.BaseAndMaxRate;

import com.flexcub.resourceplanning.skillseeker.service.BaseAndMaxRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = BaseAndMaxRateController.class)
class BaseAndMaxRateControllerTest {

    @Autowired
    BaseAndMaxRateController baseAndMaxRateController;
    @MockBean
    BaseAndMaxRateService baseAndMaxRateService;

    BaseAndMaxRate baseAndMaxRateCard = new BaseAndMaxRate();

    List<BaseAndMaxRate> baseAndMaxRateCards = new ArrayList<>();

    @BeforeEach
    void setup() {
        baseAndMaxRateCard.setId(1);
        baseAndMaxRateCard.setBaseRate("100");
        baseAndMaxRateCard.setMaxRate("490");
    }

    @Test
    void insertBaseAndMaxRateCardTest() {
        Mockito.when(baseAndMaxRateService.insertData(baseAndMaxRateCard)).thenReturn(baseAndMaxRateCard);
        assertEquals(200, baseAndMaxRateController.insertBaseAndMaxRateCard(baseAndMaxRateCard).getStatusCodeValue());
        assertEquals(baseAndMaxRateController.insertBaseAndMaxRateCard(baseAndMaxRateCard).getBody().getId(), baseAndMaxRateCard.getId());
    }

    @Test
    void insertBaseAndMaxRateCardNullTest() {
        Mockito.when(baseAndMaxRateService.insertData(baseAndMaxRateCard)).thenReturn(null);
        assertNull(baseAndMaxRateController.insertBaseAndMaxRateCard(baseAndMaxRateCard).getBody());
    }

    @Test
    void getBaseAndMaxRateCardTest() {
        baseAndMaxRateCards.add(baseAndMaxRateCard);
        baseAndMaxRateCards.add(baseAndMaxRateCard);
        Mockito.when((baseAndMaxRateService.getData())).thenReturn(baseAndMaxRateCards);
        assertEquals(200, baseAndMaxRateController.getBaseAndMaxRateCardData().getStatusCodeValue());

    }
}
