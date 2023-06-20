package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.skillseeker.service.RequirementService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerRequirement;
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
@RequestMapping(value = "/v1/SeekerRequirementController")
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})
public class SeekerRequirementController {

    @Autowired
    RequirementService requirementService;
    Logger logger = LoggerFactory.getLogger(SeekerRequirementController.class);

    /**
     * Request to get the list of all SkillSeekerRequirement Details
     *
     * @return list of all details
     */

    @GetMapping(value = "/getRequirementData", produces = {"application/json"})
    public ResponseEntity<List<SeekerRequirement>> getRequirementDetails(@RequestParam int skillSeekerId) {
        logger.info("RequirementController || getRequirementDetails || Getting the SkillPartnerRequirementDetails Info");
        return new ResponseEntity<>(requirementService.getDataById(skillSeekerId), HttpStatus.OK);
    }


    /**
     * Request to insert new data into SkillSeekerRequirement
     *
     * @param requirementEntity
     * @return newly inserted data list
     */
    @PostMapping(value = "/insertData", produces = {"application/json"})
    public ResponseEntity<List<JobDto>> insertRequirementDetailsData(@RequestBody List<SeekerRequirement> requirementEntity) {
        logger.info("RequirementController || insertRequirementDetailsData || Inserting the SkillPartnerRequirementDetails Info {} //-> ", requirementEntity);
        return new ResponseEntity<>(requirementService.insertData(requirementEntity), HttpStatus.OK);
    }

    /**
     * Request to update the existing data in SkillSeekerRequirement
     *
     * @param update
     * @return updated data
     */
    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<JobDto> updateRequirementDetailsData(@RequestBody SeekerRequirement update) {
        logger.info("RequirementController || updateRequirementDetailsData || Updating the SkillPartnerRequirementDetails Info {} // -> ", update);
        return new ResponseEntity<>(requirementService.updateData(update), HttpStatus.OK);
    }

    /**
     * Request to delete the data from SkillSeekerRequirement based on Id
     *
     * @param id
     */
    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteData(@RequestParam String id) {
        logger.info("RequirementController || deleteData || Deleting the SkillPartnerRequirementDetails Info {} //-> ", id);
        requirementService.deleteData(id);
    }
}