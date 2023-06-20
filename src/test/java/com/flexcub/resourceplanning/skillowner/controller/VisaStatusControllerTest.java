package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.Visa;
import com.flexcub.resourceplanning.skillowner.entity.VisaEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.VisaStatusServiceImpl;
import org.assertj.core.api.Assertions;
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

@SpringBootTest(classes = VisaStatusController.class)
class VisaStatusControllerTest {

    @Autowired
    VisaStatusController visaStatusController;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    VisaStatusServiceImpl visaStatusService;

    VisaEntity visa = new VisaEntity();
    Visa visadto=new Visa();
    List<Visa> visaDtos=new ArrayList<>();

    List<VisaEntity> visas = new ArrayList<VisaEntity>();

    @BeforeEach
    public void setup() {
        visa.setVisaId(1);
        visa.setVisaStatus("H1b");
    }
    @Test
    void getVisa() {

        Mockito.when(visaStatusService.getVisa()).thenReturn(visaDtos);
        visaDtos.add(visadto);
       Assertions.assertThat(visaStatusController.getVisa().getBody()).hasSize(1);
        assertEquals(200, visaStatusController.getVisa().getStatusCodeValue());

    }
}