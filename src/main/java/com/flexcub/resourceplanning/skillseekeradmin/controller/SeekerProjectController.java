package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerProjectService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
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
@RequestMapping(value = "/v1/skillseekerProject")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})

public class SeekerProjectController {
    @Autowired
    SkillSeekerProjectService seekerProject;
    Logger logger = LoggerFactory.getLogger(SeekerProjectController.class);

    /**
     * This method is to insert skillSeeker project details.
     *
     * @param skillSeekerProjectEntityList
     * @return It returns inserted skillSeeker projects.
     */
    @PostMapping(value = "/insertData", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerProject>> insertSeekerProjectDetails(@RequestBody List<SkillSeekerProject> skillSeekerProjectEntityList) {
        logger.info("SkillSeekerProjectController || insertSeekerProjectDetails || Inserting the SkillSeekerProjectDetails Info {} // ->", skillSeekerProjectEntityList);
        return new ResponseEntity<>(seekerProject.insertData(skillSeekerProjectEntityList), HttpStatus.OK);
    }

    /**
     * This method is to update skillSeeker project details.
     *
     * @param skillSeekerprojectEntity
     * @return It returns updated data of skillSeeker projects.
     */
    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<SkillSeekerProject> updateSeekerProjectDetails(@RequestBody SkillSeekerProject skillSeekerprojectEntity) {
        logger.info("SkillSeekerProjectController || updateSeekerProjectDetails || Updating the SkillSeekerProjectDetails Info {} //->", skillSeekerprojectEntity);
        return new ResponseEntity<>(seekerProject.updateData(skillSeekerprojectEntity), HttpStatus.OK);
    }

    /**
     * This method is to delete skillSeeker project detail based on id.
     *
     * @param id
     */
    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteSeekerProjectDetails(@RequestParam int id) {
        logger.info("SkillSeekerProjectController || deleteSeekerProjectDetails || Deleting the SkillSeekerProjectDetails {} // ->", id);
        seekerProject.deleteData(id);
    }

    @GetMapping(value = "/getProjectData", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerProject>> seekerProjectDetails(@RequestParam int skillSeekerId) {
        logger.info("SkillSeekerProjectController || getProjectData || getting  the SkillSeekerProjectDetails ->");
        return new ResponseEntity<>(seekerProject.getProjectData(skillSeekerId), HttpStatus.OK);
    }

}

