package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillStatus;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillStatusService;
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

@SpringBootTest(classes = OwnerSkillStatusController.class)
class OwnerSkillStatusControllerTest {

    @Autowired
    OwnerSkillStatusController ownerSkillStatusController;

    @MockBean
    OwnerSkillStatusService ownerSkillStatusService;

    OwnerSkillStatusEntity ownerSkillStatusEntity = new OwnerSkillStatusEntity();

    List<OwnerSkillStatusEntity> ownerSkillStatusEntities = new ArrayList<OwnerSkillStatusEntity>();

    OwnerSkillStatus ownerSkillStatus=new OwnerSkillStatus();
    List<OwnerSkillStatus>ownerSkillStatuses=new ArrayList<>();


    @BeforeEach
    public void setup() {
        ownerSkillStatusEntity.setSkillOwnerStatusId(1);
        ownerSkillStatusEntity.setStatusDescription("Available");
        ownerSkillStatus.setSkillOwnerStatusId(1);
        ownerSkillStatus.setStatusDescription("available");
    }


    @Test
    void getDetailsStatus() {
        ownerSkillStatuses.add(ownerSkillStatus);

        Mockito.when((ownerSkillStatusService.getDataStatus())).thenReturn(ownerSkillStatuses);
        Assertions.assertThat(ownerSkillStatusController.getDetailsStatus().getBody()).hasSize(1);
        assertEquals(200, ownerSkillStatusController.getDetailsStatus().getStatusCodeValue());
    }

    @Test
    void insertDetailsStatus() {
        Mockito.when(ownerSkillStatusService.insertDataStatus(ownerSkillStatus)).thenReturn(ownerSkillStatus);
        Assertions.assertThat(ownerSkillStatusService.insertDataStatus(ownerSkillStatus).getSkillOwnerStatusId()).isEqualTo(1);

        assertEquals(200, ownerSkillStatusController.insertDetailsStatus(ownerSkillStatus).getStatusCodeValue());
    }

    @Test
    void updateOwnerDetails() {
        Mockito.when(ownerSkillStatusService.updateOwnerDetails(ownerSkillStatus)).thenReturn(ownerSkillStatus);
        assertEquals(200, ownerSkillStatusController.updateOwnerDetails(ownerSkillStatus).getStatusCodeValue());
      //  assertEquals(ownerSkillStatusController.updateOwnerDetails(ownerSkillStatus).getBody().getSkillOwnerStatusId(), ownerSkillStatusEntity.getSkillOwnerStatusId());
    }


//    @Test
//    void deleteDetailsStatus() {
//        ownerSkillStatusController.deleteDetailsStatus(1);
//        ownerSkillStatusController.deleteDetailsStatus(2);
//        Mockito.verify(ownerSkillStatusService, times(2)).deleteDataStatus(Mockito.anyInt());
//    }


}