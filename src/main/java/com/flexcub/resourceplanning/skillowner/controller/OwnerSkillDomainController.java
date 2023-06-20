package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillDomain;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillDomainService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/OwnerSkillDomain", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillDomainController {
    @Autowired
    OwnerSkillDomainService ownerSkillDomainService;

    Logger logger = LoggerFactory.getLogger(OwnerSkillDomainController.class);

    @GetMapping(value = "/getDomainDetails")
    public ResponseEntity<List<OwnerSkillDomain>> getDetails() {
        logger.info("OwnerSkillDomainController || getDetails || Getting the Details from the OwnerSkillDomainEntity");
        return new ResponseEntity<>(ownerSkillDomainService.getDatadomain(), HttpStatus.OK);
    }


    @PostMapping(value = "/insertDomainDetails")
    public ResponseEntity<OwnerSkillDomain> insertDetails(@RequestBody OwnerSkillDomain ownerSkillDomainDto) {
        logger.info("OwnerSkillDomainController || insertDetails || Inserting the Details from the OwnerSkillDomainEntity");
        return new ResponseEntity<>(ownerSkillDomainService.insertDataDomain(ownerSkillDomainDto), HttpStatus.OK);
    }

    @PutMapping(value = "/updateSkillSet")
    public ResponseEntity<OwnerSkillDomain> updateDetails(@RequestBody OwnerSkillDomain ownerSkillDomainDto) {
        logger.info("OwnerSkillDomainController || updateDetails || Updating the Details from the OwnerSkillDomainEntity");
        return new ResponseEntity<>(ownerSkillDomainService.updateDomain(ownerSkillDomainDto), HttpStatus.OK);
    }

}
