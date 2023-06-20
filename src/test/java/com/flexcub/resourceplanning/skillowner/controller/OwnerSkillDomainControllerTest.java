package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillDomain;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillDomainServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = OwnerSkillDomainController.class)
class OwnerSkillDomainControllerTest {

    @Autowired
    OwnerSkillDomainController ownerSkillDomainController;

    @MockBean
    OwnerSkillDomainServiceImpl ownerSkillDomainService;



    OwnerSkillDomain ownerSkillDomain=new OwnerSkillDomain();
    List<OwnerSkillDomain> ownerSkillDomains=new ArrayList<>();

    @BeforeEach
    public void setup() {
        ownerSkillDomain.setDomainId(1);
        ownerSkillDomain.setDomainValues("Java");
        ownerSkillDomains.add(ownerSkillDomain);
    }

    @Test
    void getDetailsTest() {

        Mockito.when((ownerSkillDomainService.getDatadomain())).thenReturn(ownerSkillDomains);
        Assertions.assertThat(ownerSkillDomainController.getDetails().getBody()).hasSize(1);
        assertEquals(200, ownerSkillDomainController.getDetails().getStatusCodeValue());
    }

    @Test
    void insertDetailsTest() {
        Mockito.when(ownerSkillDomainService.insertDataDomain(ownerSkillDomain)).thenReturn(ownerSkillDomain);
        assertEquals(200, ownerSkillDomainController.insertDetails(ownerSkillDomain).getStatusCodeValue());
    }

    @Test
    void updateDetailsTest() {
        Mockito.when(ownerSkillDomainService.updateDomain(ownerSkillDomain)).thenReturn(ownerSkillDomain);
        assertEquals(200, ownerSkillDomainController.updateDetails(ownerSkillDomain).getStatusCodeValue());

    }


}
