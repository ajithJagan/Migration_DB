package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerInterviewStages;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerInterviewStagesService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/SeekerInterviewStages")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class SkillSeekerInterviewStagesController {

    @Autowired
    SkillSeekerInterviewStagesService seekerInterviewStagesService;
    Logger logger = LoggerFactory.getLogger(SkillSeekerInterviewStagesController.class);

    /**
     * This method is to get interviewStages data of SkillSeeker
     *
     * @return It returns list of SkillSeeker interviewStages
     */
    @GetMapping(value = "/getInterviewStages", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerInterviewStages>> getInterviewStages() {
        logger.info("SkillSeekerInterviewStagesController || getInterviewStages || Getting the SkillSeekerInterviewStages Info");
        return new ResponseEntity<>(seekerInterviewStagesService.getInterviewStages(), HttpStatus.OK);
    }
}
