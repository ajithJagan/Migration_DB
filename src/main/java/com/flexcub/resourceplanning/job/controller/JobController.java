package com.flexcub.resourceplanning.job.controller;

import com.flexcub.resourceplanning.job.dto.JobDto;
import com.flexcub.resourceplanning.job.entity.HiringPriority;
import com.flexcub.resourceplanning.job.service.JobService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/jobCreation")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request")})

public class JobController {
    @Autowired
    JobService jobService;

    Logger logger = LoggerFactory.getLogger(JobController.class);

    @PostMapping(value = "/createJob", produces = {"application/json"})
    public ResponseEntity<JobDto> addJobDetails(@RequestBody JobDto jobDto) {
        logger.info("JobCreationController || addJobDetails || Adding the JobCreation Details");
        return new ResponseEntity<>(jobService.createJobDetails(jobDto), HttpStatus.OK);
    }

    @GetMapping(value = "/publish", produces = {"application/json"})
    public ResponseEntity<JobDto> publish(@RequestParam String jobId) {
        logger.info("JobCreationController || publish || Inserting the Publish Info");
        return new ResponseEntity<>(jobService.publish(jobId), HttpStatus.OK);
    }

    @GetMapping(value = "/retrieveJob", produces = {"application/json"})
    public ResponseEntity<List<JobDto>> getJobDetails(@RequestParam int seekerId) {
        logger.info("JobCreationController || getRetrieveJob || Getting the Job Details by TaxId");
        return new ResponseEntity<>(jobService.getAllJobDetails(seekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/hiringPriority", produces = {"application/json"})
    public ResponseEntity<List<HiringPriority>> getHiringPriority() {
        logger.info("JobCreationController || getHiringPriority || Getting the HiringPriority Info ");
        return new ResponseEntity<>(jobService.getHiringPriority(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteJob", produces = {"application/json"})
    public void deleteJob(@RequestParam String jobId) {
        logger.info("JobCreationController||deleteJob||Deleting The Job");
        jobService.deleteJob(jobId);
    }



}
