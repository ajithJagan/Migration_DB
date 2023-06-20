//package com.flexcub.resourceplanning.skillseeker.controller;
//
//import com.flexcub.resourceplanning.skillseeker.dto.CityUS;
//import com.flexcub.resourceplanning.skillseeker.service.CityService;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping
//@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
//        @ApiResponse(responseCode = "500", description = "Server Error"),
//        @ApiResponse(responseCode = "400", description = "Bad Request")})
//public class CityController {
//
//    @Autowired
//    CityService cityService;
//    Logger logger = LoggerFactory.getLogger(CityController.class);
//
//    /**
//     * This method is to get city details
//     *
//     * @return It returns the list of cities
//     */
//    @GetMapping(value = "/getData", produces = {"application/json"})
//    public ResponseEntity<List<CityUS>> getCityDetails() {
//        logger.info("CityController || getCityDetails || Getting the City Details");
//        return new ResponseEntity<>(cityService.getData(), HttpStatus.OK);
//    }
//
//    /**
//     * This method loads City and State data
//     */
//    @GetMapping(value = "/getCity", produces = {"application/json"})
//    public void loadCityData() {
//        logger.info("CityController || loadCityData || Loading the City Details");
//        cityService.loadData();
//    }
//
//
//}
