//package com.flexcub.resourceplanning.skillseeker.controller;
//
//import com.flexcub.resourceplanning.skillseeker.dto.StateUS;
//import com.flexcub.resourceplanning.skillseeker.service.StateCodeService;
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
//public class StateCodeController {
//
//
//    @Autowired
//    StateCodeService service;
//    Logger logger = LoggerFactory.getLogger(StateCodeController.class);
//
//    /**
//     * This method is to get data of state code.
//     *
//     * @return It returns list of state code.
//     */
//    @GetMapping(value = "/getstatecode", produces = {"application/json"})
//    public ResponseEntity<List<StateUS>> getstateDetails() {
//        logger.info("StateCodeController || getstateDetails || Getting the SkillSeekerStateDetails Info");
//        return new ResponseEntity<>(service.getData(), HttpStatus.OK);
//    }
//}
