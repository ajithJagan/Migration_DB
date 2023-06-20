package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillseeker.dto.BaseAndMaxRate;
import com.flexcub.resourceplanning.skillseeker.service.BaseAndMaxRateService;
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
@RequestMapping(value = "v1/baseAndMaxRateCard")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class BaseAndMaxRateController {

    @Autowired
    BaseAndMaxRateService baseAndMaxRateService;

    Logger logger = LoggerFactory.getLogger(BaseAndMaxRateController.class);

    @PostMapping(value = "/InsertData", produces = {"application/json"})
    public ResponseEntity<BaseAndMaxRate> insertBaseAndMaxRateCard(@RequestBody BaseAndMaxRate baseAndMaxRateCard) {
        logger.info("BaseAndMaxRateController || insertBaseAndMaxRateCardDetails || Inserting the BaseAndMaxRateCardDetails Info {} //->", baseAndMaxRateCard);
        return new ResponseEntity<>(baseAndMaxRateService.insertData(baseAndMaxRateCard), HttpStatus.OK);
    }

    @GetMapping(value = "/getData", produces = {"application/json"})
    public ResponseEntity<List<BaseAndMaxRate>> getBaseAndMaxRateCardData() {
        logger.info("BaseAndMaxRateController || getBaseAndMaxRateCardDetails || Getting the BaseAndMaxRateCardDetails Info");
        return new ResponseEntity<>(baseAndMaxRateService.getData(), HttpStatus.OK);
    }
}
