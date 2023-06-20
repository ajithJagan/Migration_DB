package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeeker;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.PartnerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerStatusUpdate;
import com.flexcub.resourceplanning.skillseekeradmin.service.SeekerAdminService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/SeekerAdminController")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})
public class SeekerAdminController {

    @Autowired
    SeekerAdminService seekerAdminService;
    Logger logger = LoggerFactory.getLogger(SeekerAdminController.class);
    @Autowired
    private SkillSeekerService seekerInfo;

    @GetMapping(value = "/skillSeekerByAdmin", produces = {"application/json"})
    public List<SeekerAdmin> skillSeekerByAdmin() {
        logger.info("SeekerAdminController || skillSeekerByAdmin || skillSeekerByAdmin called");
        return seekerAdminService.getSkillSeeker();
    }

    @GetMapping(value = "/skillSeekerBasicDetail", produces = {"application/json"})
    public SkillSeeker skillSeekerBasicDetail(@RequestParam int id) {
        logger.info("SeekerAdminController || skillSeekerByAdmin || skillSeekerByAdmin called");
        return seekerInfo.getSeekerData(id);
    }

    /**
     * This method is to insert skillSeeker details.
     *
     * @param skillSeeker skill seeker details
     * @return It returns the inserted data of skillSeeker.
     */
    @PostMapping(value = "/addClientInfo", produces = {"application/json"})
    public ResponseEntity<SkillSeeker> addClientDetails(@Valid @RequestBody SkillSeeker skillSeeker) throws MessagingException {
        logger.info("SkillSeekerController || addClientDetails || Adding the SkillSeeker Info");
        return new ResponseEntity<>(seekerInfo.addClientDetails(skillSeeker), HttpStatus.OK);
    }

    @PutMapping(value = "/updateClientInfo", produces = {"application/json"})
    public ResponseEntity<SkillSeeker> updateClientDetails(@RequestBody SkillSeeker skillSeeker) {
        logger.info("SkillSeekerController || updateClientDetails || updating the SkillSeeker Info");
        return new ResponseEntity<>(seekerInfo.updateClientDetails(skillSeeker), HttpStatus.OK);
    }

    @PutMapping(value = "/updateSeekerStatus", produces = {"application/json"})
    public ResponseEntity<SeekerStatusUpdate> updateSeekerStatus(@RequestBody SeekerStatusUpdate seekerStatusUpdate) {
        logger.info("SkillSeekerController || updateSeekerStatus || updating the Seeker Status By SeekerAdmin");
        return new ResponseEntity<>(seekerAdminService.updateSeekerStatus(seekerStatusUpdate), HttpStatus.OK);
    }


    @GetMapping(value = "/GetTimeSheet", produces = {"application/json"})
    public List<TimeSheetResponse> timeSheets() {
        logger.info("SeekerAdminController || getTimeSheet || To Get all the TimesheetDetails ");
        return seekerAdminService.getTimeSheets();
    }

    @GetMapping(value = "/getAllSkillPartner", produces = {"application/json"})
    public List<PartnerAdmin> getAllSkillPartner() {
        logger.info("SeekerAdminController || getAllSkillPartner || getAllSkillPartner called");
        return seekerAdminService.getAllSkillPartner();
    }




}
