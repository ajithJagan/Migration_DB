package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillRoles;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillRolesService;
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
@RequestMapping(value = "/v1/OwnerSkillRoles", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillRolesController {

    @Autowired
    OwnerSkillRolesService ownerSkillRolesService;

    Logger logger = LoggerFactory.getLogger(OwnerSkillRolesController.class);

    @GetMapping(value = "/getlevel", produces = {"application/json"})
    public ResponseEntity<List<OwnerSkillRoles>> getDetailsroles() {
        logger.info("OwnerSkillRolesController || getDetails || Getting the Details from the OwnerSkillRolesEntity");
        return new ResponseEntity<>(ownerSkillRolesService.getDataroles(), HttpStatus.OK);
    }

}
