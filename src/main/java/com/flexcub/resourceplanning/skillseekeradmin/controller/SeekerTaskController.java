package com.flexcub.resourceplanning.skillseekeradmin.controller;

import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTaskService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;
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
@RequestMapping(value = "/v1/skillseekerTask")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})
public class SeekerTaskController {

    @Autowired
    SkillSeekerTaskService seekerTaskService;

    Logger logger = LoggerFactory.getLogger(SeekerTaskController.class);

    @PostMapping(value = "/insertTask", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerTask>> insertSeekerTaskDetails(@RequestBody List<SkillSeekerTask> skillSeekerTaskList) {
        logger.info("SeekerTaskController || insertSeekerTaskDetails || Inserting the SkillSeekerTaskDetails Info {} // ->", skillSeekerTaskList);
        return new ResponseEntity<>(seekerTaskService.insertData(skillSeekerTaskList), HttpStatus.OK);
    }

    @PutMapping(value = "/updateTask", produces = {"application/json"})
    public ResponseEntity<SkillSeekerTask> updateSeekerTaskDetails(@RequestBody SkillSeekerTask skillSeekerTask) {
        logger.info("SeekerTaskController || updateSeekerTaskDetails || Updating the SkillSeekerTaskDetails Info {} //->", skillSeekerTask);
        return new ResponseEntity<>(seekerTaskService.updateData(skillSeekerTask), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteTask", produces = {"application/json"})
    public void deleteSeekerTaskDetails(@RequestParam int id) {
        logger.info("SeekerTaskController || deleteSeekerTaskDetails || Deleting the SkillSeekerTaskDetails {} // ->", id);
        seekerTaskService.deleteData(id);
    }

    @GetMapping(value = "/getTaskData", produces = {"application/json"})
    public ResponseEntity<List<SkillSeekerTask>> seekerTaskDetails(@RequestParam int projectId,int skillSeekerId) {
        logger.info("SkillSeekerTaskController || getSeekerTaskDetails || Getting the SkillSeekerTaskDetails ->");
        return new ResponseEntity<>(seekerTaskService.getTaskData(projectId,skillSeekerId), HttpStatus.OK);
    }
}

