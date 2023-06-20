package com.flexcub.resourceplanning.notifications.controller;

import com.flexcub.resourceplanning.notifications.entity.OwnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.PartnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.SeekerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.SuperAdminNotifications;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillpartner.dto.HistoryOfJobs;
import com.flexcub.resourceplanning.skillpartner.dto.JobHistory;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerDetails;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/notifications")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "InternalServer Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})
public class NotificationController {

    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    NotificationService notificationService;

    @GetMapping(value = "/getAllNotificationByOwnerId", produces = {"application/json"})
    public ResponseEntity<List<OwnerNotificationsEntity>> ownerNotification(@RequestParam int id) {
        logger.info("NotificationController || ownerNotification || get all notification by SkillOwnerID");
        return new ResponseEntity<>(notificationService.getOwnerNotification(id), HttpStatus.OK);

    }

    @GetMapping(value = "/getAllNotificationBySeekerId", produces = {"application/json"})
    public ResponseEntity<List<SeekerNotificationsEntity>> seekerNotification(@RequestParam int id) {
        logger.info("NotificationController || seekerNotification || get all notification by SkillSeekerID");
        return new ResponseEntity<>(notificationService.getSeekerNotification(id), HttpStatus.OK);

    }

    @GetMapping(value = "/getAllNotificationByPartnerId", produces = {"application/json"})
    public ResponseEntity<List<PartnerNotificationsEntity>> partnerNotification(@RequestParam int id) {
        logger.info("NotificationController || partnerNotification || get all notification by SkillPartnerID");
        return new ResponseEntity<>(notificationService.getPartnerNotification(id), HttpStatus.OK);

    }


    @GetMapping(value = "/getLastFiveNotificationOfOwner", produces = {"application/json"})
    public ResponseEntity<List<OwnerNotificationsEntity>> getLastFiveNotificationOfOwner(int ownerId) {
        logger.info("SkillOwnerController || getLastFiveNotificationOfOwner || get Last Five notification Owner");
        return new ResponseEntity<>(notificationService.getLastFiveNotificationOfOwner(ownerId), HttpStatus.OK);
    }


    @GetMapping(value = "/getLastFiveNotificationOfPartner", produces = {"application/json"})
    public ResponseEntity<List<PartnerNotificationsEntity>> getLastFiveNotificationOfPartner(int partnerId) {
        logger.info("SkillPartnerController || getLastFiveNotificationOfPartner || get Last Five notification Partner");
        return new ResponseEntity<>(notificationService.getLastFiveNotificationOfPartner(partnerId), HttpStatus.OK);
    }


    @GetMapping(value = "/getSeekerLastFiveNotification", produces = {"application/json"})
    public ResponseEntity<List<SeekerNotificationsEntity>> getSeekerLastFiveNotification(int seekerId) {
        logger.info("SkillSeekerController || getSeekerLastFiveNotification || get Seeker's last five Notification ");
        return new ResponseEntity<>(notificationService.getSeekerLastFiveNotification(seekerId), HttpStatus.OK);
    }

    @PutMapping(value = "/markAsReadSeeker", produces = {"application/json"})
    public ResponseEntity<Boolean> seekerMarkAsRead(@RequestParam int id) {
        logger.info("NotificationController || seekerMarkAsRead || mark notification by SkillSeekerID");
        return new ResponseEntity<>(notificationService.markAsReadSeeker(id), HttpStatus.OK);

    }

    @PutMapping(value = "/markAsReadOwner", produces = {"application/json"})
    public ResponseEntity<Boolean> ownerMarkAsRead(@RequestParam int id) {
        logger.info("NotificationController || ownerMarkAsRead || mark notification by SkillOwnerID");
        return new ResponseEntity<>(notificationService.markAsReadOwner(id), HttpStatus.OK);

    }

    @PutMapping(value = "/markAsReadPartner", produces = {"application/json"})
    public ResponseEntity<Boolean> partnerMarkAsRead(@RequestParam int id) {
        logger.info("NotificationController || partnerMarkAsRead || mark notification by SkillPartnerID");
        return new ResponseEntity<>(notificationService.markAsReadPartner(id), HttpStatus.OK);

    }

    @PutMapping(value = "/markAsReadAdmin", produces = {"application/json"})
    public ResponseEntity<Boolean> adminMarkAsRead(@RequestParam int id) {
        logger.info("NotificationController || adminMarkAsRead || mark Admin notification as read");
        return new ResponseEntity<>(notificationService.markAsReaderAdmin(id), HttpStatus.OK);
    }

    @GetMapping(value = "/getOwnerDetailsInPartner", produces = {"application/json"})
    public ResponseEntity<List<OwnerDetails>> getOwnerDetailsInPartner(int partnerId) {
        logger.info("SkillSeekerController || getOwnerDetailsInPartner || get all owner by Partner");
        return new ResponseEntity<>(notificationService.getOwnerDetailsInPartner(partnerId), HttpStatus.OK);

    }

    @GetMapping(value = "/getJobHistoryInPartner", produces = {"application/json"})
    public ResponseEntity<List<JobHistory>> getJobHistoryInSeeker(int ownerId) {
        logger.info("SkillSeekerController || getJobHistoryInPartner || get job history from owner in Partner");
        return new ResponseEntity<>(notificationService.getJobHistoryInPartner(ownerId), HttpStatus.OK);

    }

    @GetMapping(value = "/getPartnerNotificationForParticularOwner", produces = {"application/json"})
    public ResponseEntity<List<PartnerNotificationsEntity>> getNotificationForParticularOwner(int ownerId, String jobId) {
        logger.info("SkillPartnerController || getNotificationForParticularOwner || get notification for particular owner in Partner");
        return new ResponseEntity<>(notificationService.getNotificationForParticularOwner(ownerId, jobId), HttpStatus.OK);
    }

    @GetMapping(value = "/getSeekerNotificationForOwner", produces = {"application/json"})
    public ResponseEntity<List<SeekerNotificationsEntity>> getSeekerNotificationByOwner(int ownerId, String jobId) {
        logger.info("SkillSeekerController || getSeekerNotification || get Notification in SelectionPhase using ownerId");
        return new ResponseEntity<>(notificationService.getSeekerNotificationByOwner(ownerId, jobId), HttpStatus.OK);

    }

    @GetMapping(value = "/getJobSpecificNotificationForOwner", produces = {"application/json"})
    public ResponseEntity<List<OwnerNotificationsEntity>> getJobSpecificNotificationForOwner(@RequestParam int skillOwnerId, String jobId) {
        logger.info("SkillOwnerController || getOwnerNotification || get Notification in SelectionPhase using ownerId and jobid");
        return new ResponseEntity<>(notificationService.getJobSpecificNotificationForOwner(skillOwnerId, jobId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllAdminNotifications", produces = {"application/json"})
    public ResponseEntity<List<SuperAdminNotifications>> getAdminNotifications() {
        logger.info("NotificationController || getAllAdminNotifications || getAdminNotifications");
        return new ResponseEntity<>(notificationService.getSuperAdminNotification(), HttpStatus.OK);
    }

    @GetMapping(value = "/getLastFiveSuperAdminNotification", produces = {"application/json"})
    public ResponseEntity<List<SuperAdminNotifications>> getLastFiveSuperAdminNotication() {
        logger.info("NotificationController || getLastFiveSuperAdminNotification || get LastFiveNotification of SuperAdmin");
        return new ResponseEntity<>(notificationService.getLastFiveAdminNotification(), HttpStatus.OK);

    }
    @GetMapping(value = "/getHistoryOfJobs", produces = {"application/json"})
        public List<HistoryOfJobs> historyOfJobs(@RequestParam int ownerId){
        return notificationService.getHistoryOfJobs(ownerId);

    }

}