package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillTechnologies;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillTechnologiesService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/OwnerSkillTechnologies", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillTechnologiesController {
    @Autowired
    OwnerSkillTechnologiesService ownerSkillTechnologiesService;

    Logger logger = LoggerFactory.getLogger(OwnerSkillTechnologiesController.class);

    @GetMapping(value = "/getData")
    public List<OwnerSkillTechnologies> getDetailsTech() {
        logger.info("OwnerSkillTechnologiesController || getDetails || Getting the Details from the OwnerSkillTechnologiesEntity");
        return ownerSkillTechnologiesService.getDataTech();
    }

}
