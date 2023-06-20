package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillRolesEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillRolesServiceImpl;
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

@SpringBootTest(classes = OwnerSkillRolesController.class)
class OwnerSkillRolesControllerTest {
    @Autowired
    OwnerSkillRolesController ownerSkillRolesController;

    @MockBean
    OwnerSkillRolesServiceImpl ownerSkillRolesService;

    OwnerSkillRoles ownerSkillRole= new OwnerSkillRoles();

    List<OwnerSkillRoles> ownerSkillRoles = new ArrayList<OwnerSkillRoles>();



    @BeforeEach
    public void setup() {
        ownerSkillRole.setRolesId(1);
        ownerSkillRole.setRolesDescription("Java");
        ownerSkillRoles.add(ownerSkillRole);
    }

    @Test
    void getDetailsroles(){
        Mockito.when(ownerSkillRolesService.getDataroles()).thenReturn(ownerSkillRoles);
        Assertions.assertThat(ownerSkillRolesController.getDetailsroles().getBody()).hasSize(1);

        assertEquals(200,ownerSkillRolesController.getDetailsroles().getStatusCodeValue());
    }
}

