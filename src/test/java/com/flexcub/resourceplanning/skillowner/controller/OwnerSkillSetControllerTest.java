package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerSkillSet;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillSetService;
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

@SpringBootTest(classes = OwnerSkillSetController.class)
class OwnerSkillSetControllerTest {

    @Autowired
    OwnerSkillSetController ownerSkillSetController;

    @MockBean
    OwnerSkillSetService ownerSkillSetService;

    OwnerSkillSetEntity ownerSkillSetEntity = new OwnerSkillSetEntity();
    List<OwnerSkillSetEntity> ownerSkillSetEntities = new ArrayList<OwnerSkillSetEntity>();

     SkillOwnerSkillSet skillOwnerSkillSet= new SkillOwnerSkillSet();
    List<SkillOwnerSkillSet> skillOwnerSkillSetDtoList = new ArrayList<>();

    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    @BeforeEach
    public void setup() {
        ownerSkillSetEntity.setOwnerSkillSetId(1);
        ownerSkillSetEntity.setExperience("1+");
        ownerSkillSetEntity.setLastUsed("2022");

        skillOwnerEntity.setSkillOwnerEntityId(1);

        skillOwnerSkillSetDtoList.add(skillOwnerSkillSet);

    }

    @Test
    void getDetails() {
        Mockito.when(ownerSkillSetService.getSkillset(skillOwnerEntity.getSkillOwnerEntityId())).thenReturn(skillOwnerSkillSetDtoList);
        Assertions.assertThat(ownerSkillSetController.getDetails(skillOwnerEntity.getSkillOwnerEntityId()).getBody()).hasSize(1);
        assertEquals(200, ownerSkillSetController.getDetails(skillOwnerEntity.getSkillOwnerEntityId()).getStatusCodeValue());
    }

    @Test
    void insertDetails() {
        Mockito.when(ownerSkillSetService.insertSkillSet(skillOwnerSkillSet)).thenReturn(skillOwnerSkillSet);
        assertEquals(200, ownerSkillSetController.insertDetails(skillOwnerSkillSet).getStatusCodeValue());
        assertEquals(ownerSkillSetController.insertDetails(skillOwnerSkillSet).getBody().getOwnerSkillSetId(), skillOwnerSkillSet.getOwnerSkillSetId());
    }

    @Test
    void deleteDetails() {
        ownerSkillSetController.deleteDetails(1);
        ownerSkillSetController.deleteDetails(2);
        Mockito.verify(ownerSkillSetService, times(2)).deleteSkillset(Mockito.anyInt());
    }

    @Test
    void updateDetails() {
        Mockito.when(ownerSkillSetService.updateSkillSet(skillOwnerSkillSet)).thenReturn(skillOwnerSkillSet);
        assertEquals(200, ownerSkillSetController.updateDetails(skillOwnerSkillSet).getStatusCodeValue());
        assertEquals(skillOwnerSkillSet.getOwnerSkillSetId(), ownerSkillSetController.updateDetails(skillOwnerSkillSet).getBody().getOwnerSkillSetId());
    }


}