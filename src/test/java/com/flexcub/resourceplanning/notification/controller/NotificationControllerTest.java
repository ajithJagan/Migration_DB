package com.flexcub.resourceplanning.notification.controller;

import com.flexcub.resourceplanning.notifications.controller.NotificationController;
import com.flexcub.resourceplanning.notifications.entity.OwnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.PartnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.SeekerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillpartner.dto.JobHistory;
import com.flexcub.resourceplanning.skillpartner.dto.NotificationDetails;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerDetails;
import com.flexcub.resourceplanning.skillseeker.dto.NotificationSeekerDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = NotificationController.class)
class NotificationControllerTest {

    @MockBean
    NotificationService notificationService;

    @Autowired
    NotificationController notificationController;

    OwnerNotificationsEntity ownerNotificationsEntity = new OwnerNotificationsEntity();
    SeekerNotificationsEntity seekerNotificationsEntity = new SeekerNotificationsEntity();
    PartnerNotificationsEntity partnerNotificationsEntity = new PartnerNotificationsEntity();
    OwnerDetails ownerDetails = new OwnerDetails();
    JobHistory jobHistory = new JobHistory();
    NotificationDetails notificationDetails = new NotificationDetails();

    NotificationSeekerDetails notificationSeekerDetails = new NotificationSeekerDetails();
    List<OwnerNotificationsEntity> ownerNotificationsEntityList = new ArrayList<>();
    List<SeekerNotificationsEntity> seekerNotificationsEntityList = new ArrayList<>();
    List<PartnerNotificationsEntity> partnerNotificationsEntityList = new ArrayList<>();
    List<OwnerDetails> details = new ArrayList<>();
    List<JobHistory> historyList = new ArrayList<>();
    List<NotificationDetails> notificationDetailsList = new ArrayList<>();
    List<NotificationSeekerDetails> notificationSeekerDetailsList = new ArrayList<>();

    @BeforeEach
    void setDetails() {

        ownerDetails.setEmployeeId(1);
        ownerDetails.setEmployeeName("Hema");
        ownerDetails.setLocation("Alabama");
        ownerDetails.setJoinedDate(new Date(2015, Calendar.FEBRUARY, 12));
        ownerDetails.setLeavingDate(new Date(2022, Calendar.DECEMBER, 9));
        ownerDetails.setStatus(true);
        details.add(ownerDetails);

        ownerNotificationsEntity.setId(1);
        ownerNotificationsEntity.setSkillOwnerEntityId(3);
        ownerNotificationsEntity.setTitle("Reinitiated Interview  - Owner");
        ownerNotificationsEntity.setContentId(9);
        ownerNotificationsEntity.setJobId("FJB-00030");
        ownerNotificationsEntity.setDate(new Date(2022, Calendar.FEBRUARY, 12));
        ownerNotificationsEntity.setMarkAsRead(false);
        ownerNotificationsEntity.setContent("Interview for web developement has been Re-initiated");
        ownerNotificationsEntity.setStage(2);
        ownerNotificationsEntity.setRequirementPhaseName("HR ROUND");
        ownerNotificationsEntity.setDateOfInterview(LocalDate.now());
        ownerNotificationsEntity.setTimeOfInterview(LocalTime.now());
        ownerNotificationsEntityList.add(ownerNotificationsEntity);

        partnerNotificationsEntity.setId(1);
        partnerNotificationsEntity.setSkillPartnerEntityId(3);
        partnerNotificationsEntity.setTitle("Reinitiated Interview  - Owner");
        partnerNotificationsEntity.setContentId(9);
        partnerNotificationsEntity.setJobId("FJB-00030");
        partnerNotificationsEntity.setDate(new Date(2022, Calendar.FEBRUARY, 12));
        partnerNotificationsEntity.setMarkAsRead(false);
        partnerNotificationsEntity.setContent("Interview for web developement has been Re-initiated");
        partnerNotificationsEntity.setStage(2);
        partnerNotificationsEntity.setRequirementPhaseName("HR ROUND");
        partnerNotificationsEntity.setDateOfInterview(LocalDate.now());
        partnerNotificationsEntity.setTimeOfInterview(LocalTime.now());
        partnerNotificationsEntity.setOwnerId(3);
        partnerNotificationsEntityList.add(partnerNotificationsEntity);

        seekerNotificationsEntity.setId(1);
        seekerNotificationsEntity.setSkillSeekerEntityId(3);
        seekerNotificationsEntity.setTitle("Reinitiated Interview  - Owner");
        seekerNotificationsEntity.setContentId(9);
        seekerNotificationsEntity.setJobId("FJB-00030");
        seekerNotificationsEntity.setDate(new Date(2022, Calendar.FEBRUARY, 12));
        seekerNotificationsEntity.setMarkAsRead(false);
        seekerNotificationsEntity.setContent("Interview for web developement has been Re-initiated");
        seekerNotificationsEntity.setStage(2);
        seekerNotificationsEntity.setOwnerId(3);
        seekerNotificationsEntityList.add(seekerNotificationsEntity);

        jobHistory.setJobId("FJB-00025");
        jobHistory.setBusinessName("Praba");
        jobHistory.setJobTitle("Flexcub");
        jobHistory.setLocation("Alabama");
        jobHistory.setHiringStatus("Available");
        historyList.add(jobHistory);

        notificationDetails.setTitle("Reinitiated Interview  - Owner");
        notificationDetails.setDate(new Date(2022, Calendar.FEBRUARY, 12));
        notificationDetails.setContent("Interview for web developement has been Re-initiated");
        notificationDetailsList.add(notificationDetails);

        notificationSeekerDetails.setTitle("Reinitiated Interview  - Owner");
        notificationSeekerDetails.setDate(new Date(2022, Calendar.FEBRUARY, 12));
        notificationSeekerDetails.setContent("Interview for web developement has been Re-initiated");
        notificationSeekerDetailsList.add(notificationSeekerDetails);

    }

    @Test
    void markAsReadSeekerTest() {
        Mockito.when(notificationService.markAsReadSeeker(1)).thenReturn(true);
        assertEquals(200, notificationController.seekerMarkAsRead(1).getStatusCodeValue());

    }

    @Test
    void markAsReadOwnerTest() {
        Mockito.when(notificationService.markAsReadOwner(2)).thenReturn(true);
        assertEquals(200, notificationController.ownerMarkAsRead(2).getStatusCodeValue());
    }

    @Test
    void markAsReadPartnerTest() {
        Mockito.when(notificationService.markAsReadPartner(3)).thenReturn(true);
        assertEquals(200, notificationController.ownerMarkAsRead(3).getStatusCodeValue());
    }

    @Test
    void getAllNotificationByOwnerId() {
        Mockito.when(notificationService.getOwnerNotification(3)).thenReturn(ownerNotificationsEntityList);
        assertEquals(200, notificationController.ownerNotification(3).getStatusCodeValue());
    }

    @Test
    void getAllNotificationBySeekerId() {
        Mockito.when(notificationService.getSeekerNotification(1)).thenReturn(seekerNotificationsEntityList);
        assertEquals(200, notificationController.seekerNotification(1).getStatusCodeValue());
    }

    @Test
    void getAllNotificationByPartnerId() {
        Mockito.when(notificationService.getPartnerNotification(1)).thenReturn(partnerNotificationsEntityList);
        assertEquals(200, notificationController.partnerNotification(1).getStatusCodeValue());
    }

    @Test
    void getLastFiveNotificationOfOwner() {
        Mockito.when(notificationService.getLastFiveNotificationOfOwner(1)).thenReturn(ownerNotificationsEntityList);
        assertEquals(200, notificationController.getLastFiveNotificationOfOwner(1).getStatusCodeValue());
    }

    @Test
    void getLastFiveNotificationOfPartner() {
        Mockito.when(notificationService.getLastFiveNotificationOfPartner(1)).thenReturn(partnerNotificationsEntityList);
        assertEquals(200, notificationController.getLastFiveNotificationOfPartner(1).getStatusCodeValue());

    }

    @Test
    void getSeekerLastFiveNotification() {
        Mockito.when(notificationService.getSeekerLastFiveNotification(1)).thenReturn(seekerNotificationsEntityList);
        assertEquals(200, notificationController.getSeekerLastFiveNotification(1).getStatusCodeValue());

    }

    @Test
    void getJobHistoryInPartner() {
        Mockito.when(notificationService.getJobHistoryInPartner(3)).thenReturn(historyList);
        assertEquals(200, notificationController.getJobHistoryInSeeker(3).getStatusCodeValue());

    }

    @Test
    void getPartnerNotificationForParticularOwner() {
        Mockito.when(notificationService.getNotificationForParticularOwner(3, jobHistory.getJobId())).thenReturn(partnerNotificationsEntityList);
        assertEquals(200, notificationController.getNotificationForParticularOwner(3, jobHistory.getJobId()).getStatusCodeValue());

    }

    @Test
    void getSeekerNotificationForOwner() {
        Mockito.when(notificationService.getSeekerNotificationByOwner(3, jobHistory.getJobId())).thenReturn(seekerNotificationsEntityList);
        assertEquals(200, notificationController.getSeekerNotificationByOwner(3, jobHistory.getJobId()).getStatusCodeValue());

    }

    @Test
    void getOwnerDetailsInPartner() {
        Mockito.when(notificationService.getOwnerDetailsInPartner(1)).thenReturn(details);
        assertEquals(200, notificationController.getOwnerDetailsInPartner(1).getStatusCodeValue());


    }

}
