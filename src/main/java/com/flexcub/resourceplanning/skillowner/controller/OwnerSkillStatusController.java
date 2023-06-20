package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillStatus;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillStatusService;
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
@RequestMapping(value = "/v1/OwnerSkillStatus", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillStatusController {
    @Autowired
    OwnerSkillStatusService ownerSkillStatusService;

    Logger logger = LoggerFactory.getLogger(OwnerSkillStatusController.class);

    @GetMapping(value = "/get")
    public ResponseEntity<List<OwnerSkillStatus>> getDetailsStatus() {
        logger.info("OwnerSkillStatusController || getDetails || Getting the Details from the OwnerSkillStatusEntity");
        return new ResponseEntity<>(ownerSkillStatusService.getDataStatus(), HttpStatus.OK);
    }

    @PostMapping(value = "/insertData")
    public ResponseEntity<OwnerSkillStatus> insertDetailsStatus(@RequestBody OwnerSkillStatus ownerSkillStatusDto) {
        logger.info("OwnerSkillStatusController || insertDetailsStatus || Inserting the Details from the OwnerSkillStatusEntity");
        return new ResponseEntity<>(ownerSkillStatusService.insertDataStatus(ownerSkillStatusDto), HttpStatus.OK);
    }

    @PutMapping(value = "/updateData")
    public ResponseEntity<OwnerSkillStatus> updateOwnerDetails(@RequestBody OwnerSkillStatus ownerSkillStatusDto) {
        logger.info("OwnerSkillStatusController || updateOwnerDetails || Updating the Details from the OwnerSkillStatusEntity");
        return new ResponseEntity<>(ownerSkillStatusService.updateOwnerDetails(ownerSkillStatusDto), HttpStatus.OK);
    }

}
