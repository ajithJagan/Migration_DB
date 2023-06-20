package com.flexcub.resourceplanning.skillseeker.controller;


import com.flexcub.resourceplanning.skillseeker.dto.RecommendedCandidates;
import com.flexcub.resourceplanning.skillseeker.service.TalentRecommendationService;
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
@RequestMapping(value = "/v1/Recommendation")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class TalentRecommendationController {

    Logger logger = LoggerFactory.getLogger(TalentRecommendationController.class);

    @Autowired
    TalentRecommendationService talentRecommendationService;

    @GetMapping(value = "/talents", produces = {"application/json"})
    public ResponseEntity<List<RecommendedCandidates>> talentRecommendation(String jobId) {
        logger.info("TalentRecommendationController || talentRecommendation || talentRecommendation called !!");
        return new ResponseEntity<>(talentRecommendationService.getTalentRecommendation(jobId), HttpStatus.OK);
    }
}
