package com.flexcub.resourceplanning.skillowner.controller;


import com.flexcub.resourceplanning.skillowner.dto.Visa;
import com.flexcub.resourceplanning.skillowner.service.VisaStatusService;
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
@RequestMapping(value = "/v1/VisaStatusController", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class VisaStatusController {
    @Autowired
    VisaStatusService visaStatusService;

    Logger logger = LoggerFactory.getLogger(VisaStatusController.class);

    @GetMapping(value = "/getVisa")
    public ResponseEntity<List<Visa>> getVisa() {
        logger.info("VisaStatusController || getVisa || Getting the Visa Details from the VisaStatus");
        return new ResponseEntity<>(visaStatusService.getVisa(), HttpStatus.OK);
    }

}
