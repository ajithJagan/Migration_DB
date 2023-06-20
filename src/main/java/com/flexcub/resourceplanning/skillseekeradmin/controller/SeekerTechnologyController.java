package com.flexcub.resourceplanning.skillseekeradmin.controller;


import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTechnologyService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTechnology;
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
@RequestMapping(value = "v1/skillSeekerTechCard")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})
public class SeekerTechnologyController {
    @Autowired
    SkillSeekerTechnologyService techCardService;
    Logger logger = LoggerFactory.getLogger(SeekerTechnologyController.class);

    /**
     * This method is to update data of skillSeekerTechCard.
     *
     * @param seekerTechData
     * @return It returns updated data of skillSeeker techCard.
     */
    @PutMapping(value = "updateData", produces = {"application/json"})
    public ResponseEntity<SkillSeekerTechnology> updateSeekerTechnologyDetails(@RequestBody SkillSeekerTechnology seekerTechData) {
        logger.info("SkillSeekerRateCardController || updateSeekerTechnologyDetails || Updating the SeekerTechnologyDetails Info {} //->", seekerTechData);
        return new ResponseEntity<>(techCardService.updateData(seekerTechData), HttpStatus.OK);
    }

    /**
     * This method is to delete skillSeeker rateCard detail based on id.
     *
     * @param id
     */
    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteSeekerTechDetails(@RequestParam int id) {
        logger.info("SkillSeekerRateCardController || deleteSeekerTechDetails || Deleting the SeekerTechDetails {} //->", id);
        techCardService.deleteData(id);
    }

    /**
     * This method is to get data of skillSeekerRateCard based on id.
     *
     * @param id
     * @return It returns the skillSeeker rateCard for the given id.
     */
    @GetMapping(value = "/getDataByProjectId", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerTechnology>> getDataByProjectId(@RequestParam("id") int id) {
        List<SkillSeekerTechnology> skillSeekerTechCard = techCardService.getDataByProjectId(id);
        logger.info("SkillSeekerRateCardController || getDataByProjectId || Getting DataByProjectId  SkillSeekerRateCardDetails Info {} //->", id);
        return new ResponseEntity<>(skillSeekerTechCard, HttpStatus.OK);
    }
}
