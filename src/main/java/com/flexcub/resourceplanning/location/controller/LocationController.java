package com.flexcub.resourceplanning.location.controller;

import com.flexcub.resourceplanning.location.service.LocationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/LocationController")
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class LocationController {
    @Autowired
    LocationService locationServiceImpl;

    @GetMapping(value = "/generateToken", produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> generateToken() {
        return new ResponseEntity<>(locationServiceImpl.generateToken(), HttpStatus.OK);
    }

    @GetMapping(value = "/getStates", produces = {"application/json"})
    public ResponseEntity<List<HashMap<String, String>>> getStates() {
        return new ResponseEntity<>(locationServiceImpl.getStates(), HttpStatus.OK);
    }

    @GetMapping(value = "/getCities/{state}", produces = {"application/json"})
    public ResponseEntity<List<HashMap<String, String>>> getCities(@PathVariable String state) {
        List<HashMap<String, String>> loc = locationServiceImpl.getCities(state);
        return new ResponseEntity<>(loc, HttpStatus.OK);
    }

}
