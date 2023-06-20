package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillLevel;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillLevelEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.OwnerSkillLevelServiceImpl;
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

@SpringBootTest(classes = OwnerSkillLevelController.class)
class OwnerSkillLevelControllerTest {
    @Autowired
    OwnerSkillLevelController ownerSkillLevelController;

    @MockBean
    OwnerSkillLevelServiceImpl ownerSkillLevelService;

    List<OwnerSkillLevel> ownerSkillLevels = new ArrayList<OwnerSkillLevel>();
    OwnerSkillLevel ownerSkillLevel=new OwnerSkillLevel();
    @BeforeEach
    public void setup() {
        ownerSkillLevel.setSkillSetLevelId(1);
        ownerSkillLevel.setSkillLevelDescription("Java");
        ownerSkillLevels.add(ownerSkillLevel);
    }

    @Test
    void getDetails() {

        Mockito.when((ownerSkillLevelService.getDatalevel())).thenReturn(ownerSkillLevels);
        Assertions.assertThat(ownerSkillLevelController.getDetails().getBody()).hasSize(1);
        assertEquals(200, ownerSkillLevelController.getDetails().getStatusCodeValue());
    }


}
