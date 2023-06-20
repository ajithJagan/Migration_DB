package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.Visa;
import com.flexcub.resourceplanning.skillowner.entity.VisaEntity;
import com.flexcub.resourceplanning.skillowner.repository.VisaStatusRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.VisaStatusServiceImpl;
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

@SpringBootTest(classes = VisaStatusServiceImpl.class)
class VisaStatusServiceImplTest {

    @MockBean
    VisaStatusRepository visaStatusRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    VisaStatusServiceImpl visaStatusService;

    VisaEntity visaEntity = new VisaEntity();
    List<VisaEntity> visaEntities = new ArrayList<>();

    Visa visaDto = new Visa();

    @BeforeEach
    public void setup() {
        visaEntity.setVisaId(1);
        visaEntity.setVisaStatus("H1b");
        visaEntities.add(visaEntity);
    }

    @Test
    void getVisa() {

        Mockito.when(visaStatusRepository.findAll()).thenReturn(visaEntities);
        Mockito.when(modelMapper.map(visaEntity,Visa.class)).thenReturn(visaDto);
        assertEquals(1, visaStatusService.getVisa().size());
        assertNotNull(visaStatusService.getVisa());
    }
}

