package com.flexcub.resourceplanning.skillpartner.controller;

import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;
import com.flexcub.resourceplanning.skillpartner.service.HiringService;
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
@RequestMapping(value = "/v1/HiringController")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class HiringController {

    @Autowired
    HiringService service;
    Logger logger = LoggerFactory.getLogger(HiringController.class);

    /**
     * Request to get all HiringEntity Details
     *
     * @return List of all HiringEntity Details
     */

    @GetMapping(value = "/getData", produces = {"application/json"})
    public ResponseEntity<List<HiringEntity>> getHiringDetails() {
        logger.info("HiringController || getHiringDetails || Getting the SkillPartnerHiringDetails Info");
        return new ResponseEntity<>(service.getData(), HttpStatus.OK);
    }

    /**
     * Request to insert new data into HiringEntity
     *
     * @param hiringEntity
     * @return newly inserted data
     */

    @PostMapping(value = "/insertData", produces = {"application/json"})
    public ResponseEntity<HiringEntity> insertHiringDetailsData(@RequestBody HiringEntity hiringEntity) {
        logger.info("HiringController || insertHiringDetailsData || Inserting the SkillPartnerHiringDetails Info {} // -> ", hiringEntity);
        return new ResponseEntity<>(service.insertData(hiringEntity), HttpStatus.OK);
    }

    /**
     * Request to update the existing data in HiringEntity
     *
     * @param update
     * @return updated data in HiringEntity
     */
    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<HiringEntity> updateHiringDetailsData(@RequestBody HiringEntity update) {
        logger.info("HiringController || updateHiringDetailsData || Updating the SkillPartnerHiringDetails Info {}", update);
        return new ResponseEntity<>(service.updateData(update), HttpStatus.OK);
    }

    /**
     * Request to delete data from HiringEntity based on id
     *
     * @param id
     */

    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteData(@RequestParam int id) {
        logger.info("HiringController || deleteData || Deleting the SkillPartnerHiringDetails Info {}", id);
        service.deleteData(id);
    }
}
