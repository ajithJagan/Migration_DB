package com.flexcub.resourceplanning.skillseekeradmin.controller;


import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillDomainEntity;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerStatusUpdate;
import com.flexcub.resourceplanning.skillseekeradmin.service.SeekerAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeekerAdminController.class)
class SeekerAdminControllerTest {

    @MockBean
    SeekerAdminService seekerAdminService;
    @Autowired
    SeekerAdminController seekerAdminController;
    SkillSeeker skillSeeker = new SkillSeeker();

    SeekerAdmin seekerAdmin = new SeekerAdmin();

    TimeSheetResponse timeSheetResponse = new TimeSheetResponse();

    SeekerStatusUpdate seekerStatusUpdate = new SeekerStatusUpdate();
    OwnerSkillDomainEntity ownerSkillDomainEntity = new OwnerSkillDomainEntity();

    List<SeekerAdmin> skillSeekerList = new ArrayList<>();

    List<SkillSeeker> seekerList = new ArrayList<>();
    List<TimeSheetResponse> timeSheetResponseList = new ArrayList<>();

    @MockBean
    SkillSeekerService seekerInfo;

    @BeforeEach
    void beforeTest() {
        seekerAdmin.setId(1);
        seekerAdmin.setSkillSeekerName("Kevin");
        seekerAdmin.setPhone("8825773502");
        seekerAdmin.setEmail("kevinranig@gmail.com");
        seekerAdmin.setPrimaryContactFullName("raj");
        seekerAdmin.setLocation("Alabama");
        seekerAdmin.setStartDate(new Date());
        seekerAdmin.setEndDate(new Date());


        skillSeeker.setId(1);
        skillSeeker.setSkillSeekerName("Kevin");
        skillSeeker.setAddressLine1("no-2,6th street,alabama");
        skillSeeker.setAddressLine2("2nd floor,buildingNumber-4,alabama");
        skillSeeker.setState("alabama");
        skillSeeker.setZipCode(1234);
        skillSeeker.setPhone("8825773502");
        skillSeeker.setEmail("kevinranig@gmail.com");
        skillSeeker.setPrimaryContactFullName("kevin");
        skillSeeker.setPrimaryContactEmail("kevinranig@gmail.com");
        skillSeeker.setPrimaryContactPhone("9894765211");
        skillSeeker.setSecondaryContactFullName("raj");
        skillSeeker.setSecondaryContactEmail("raj@gmail.com");
        skillSeeker.setSecondaryContactPhone("9597154772");

        seekerStatusUpdate.setSkillSeekerId(1);
        seekerStatusUpdate.setIsAccountActive(true);

        timeSheetResponse.setTimeSheetId(1);
        timeSheetResponse.setSkillOwnerEntityId(1);
        timeSheetResponse.setSkillSeekerEntityId(1);
        timeSheetResponse.setSkillSeekerProjectEntityId(2);
        timeSheetResponse.setSkillSeekerTaskEntityId(1);
        timeSheetResponse.setTaskTitle("Infocus");
        timeSheetResponse.setTaskDescription("Infocus Implementation");
        timeSheetResponse.setStartDate(new java.sql.Date(2022, 12, 28));
        timeSheetResponse.setEndDate(new java.sql.Date(2022, 12, 30));
        timeSheetResponse.setApproved(true);

    }

    @Test
    void addClientDetailsTest() throws MessagingException {
        Mockito.when((seekerInfo.addClientDetails(skillSeeker))).thenReturn(skillSeeker);
        assertEquals(200, seekerAdminController.addClientDetails(skillSeeker).getStatusCodeValue());
        assertEquals(seekerAdminController.addClientDetails(skillSeeker).getBody().getId(), skillSeeker.getId());
        assertNotNull(skillSeeker.getAddressLine1());
    }

    @Test
    void updateClientDetailsTest() {
        Mockito.when((seekerInfo.updateClientDetails(skillSeeker))).thenReturn(skillSeeker);
        assertEquals(200, seekerAdminController.updateClientDetails(skillSeeker).getStatusCodeValue());
        assertNotNull(seekerAdminController.updateClientDetails(skillSeeker).getBody().getEmail());
    }

    @Test
    void getSkillSeekerByAdminTest() {
        skillSeekerList.add(seekerAdmin);
        Mockito.when(seekerAdminService.getSkillSeeker()).thenReturn(skillSeekerList);
        assertEquals(1, seekerAdminController.skillSeekerByAdmin().size());

    }

    @Test
    void getSkillSeekeerBasicDetailTest() {
        Mockito.when(seekerInfo.getSeekerData(1)).thenReturn(skillSeeker);
        assertEquals("Kevin", seekerAdminController.skillSeekerBasicDetail(1).getSkillSeekerName());

    }

    @Test
    void updateSeekerStatusTest() {
        Mockito.when(seekerAdminService.updateSeekerStatus(seekerStatusUpdate)).thenReturn(seekerStatusUpdate);
        assertEquals(200, seekerAdminController.updateSeekerStatus(seekerStatusUpdate).getStatusCodeValue());
        assertNotNull(seekerAdminController.updateSeekerStatus(seekerStatusUpdate).getBody().getIsAccountActive());

    }

    @Test
    void getTimeSheetTest() {
        timeSheetResponseList.add(timeSheetResponse);
        Mockito.when(seekerAdminService.getTimeSheets()).thenReturn(timeSheetResponseList);
        assertEquals(1, seekerAdminController.timeSheets().size());
    }

}
