package com.flexcub.resourceplanning.skillowner.controller;


import com.flexcub.resourceplanning.skillowner.service.OwnerSkillYearOfExperienceService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillLevelAndExperienceController {

    @Autowired
    OwnerSkillYearOfExperienceService service;

    @GetMapping(value = "/levelAndExperience", produces = {"application/json"})
    public ResponseEntity<List<Object[]>> getOwnerSkillYearOfExperienceDetails() {
        return new ResponseEntity<>(service.getData(), HttpStatus.OK);

    }
}
