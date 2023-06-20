package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.contracts.dto.SeekerMSADetails;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseeker.service.USALocationService;
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
@RequestMapping(value = "/v1/SkillSeekerController")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class SkillSeekerController {

    Logger logger = LoggerFactory.getLogger(SkillSeekerController.class);
    @Autowired
    private SkillSeekerService seekerInfo;
    @Autowired
    private USALocationService usaLocationService;

    @Autowired
    private SkillSeekerService skillSeekerService;

    /**
     * This method is to update skillSeeker details.
     *
     * @param skillSeekerInfo info
     * @return It returns the updated data of skillSeeker.
     */
    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<SkillSeeker> updateClientDetails(@RequestBody SkillSeeker skillSeekerInfo) {
        logger.info("SkillSeekerController || updateClientDetails || Updating the SkillSeeker Info {} ", skillSeekerInfo);
        return new ResponseEntity<>(seekerInfo.updateData(skillSeekerInfo), HttpStatus.OK);
    }

    /**
     * This method is to delete skillSeeker data based on id.
     *
     * @param id client Id
     */
    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteClientDetails(@RequestParam int id) {
        logger.info("SkillSeekerController || deleteClientDetails || Deleting the Client Info {} ", id);
        seekerInfo.deleteData(id);
    }

    @GetMapping(value = "/getLocationByKeyword", produces = {"application/json"})
    public ResponseEntity<List<String>> getLocationByKeyword(@RequestParam String keyword) {
        logger.info("SkillSeekerController || getLocationByKeyword || getting Location By Keyword");
        return new ResponseEntity<>(usaLocationService.getLocationFromDatabase(keyword), HttpStatus.OK);
    }

    @GetMapping(value = "/getSeekerByTaxId", produces = {"application/json"})
    public ResponseEntity<List<SkillSeeker>> getSeekerById(@RequestParam String taxId) {
        logger.info("SkillSeekerController || getSeekerById || getting Seeker By Id");
        return new ResponseEntity<>(seekerInfo.getSkillSeeker(taxId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAccessByTaxId", produces = {"application/json"})
    public ResponseEntity<List<SeekerRoleListing>> getAccessById(@RequestParam String taxId) {
        logger.info("SkillSeekerController || getAccessById || getting Access By Id");
        return new ResponseEntity<>(seekerInfo.getAccessByTaxId(taxId), HttpStatus.OK);
    }

    @GetMapping(value = "/getModules", produces = {"application/json"})
    public ResponseEntity<List<SeekerModulesEntity>> getModules() {
        logger.info("SkillSeekerController || getModules || getting The Modules");
        return new ResponseEntity<>(seekerInfo.getModules(), HttpStatus.OK);
    }

    @GetMapping(value = "/getSubRoles", produces = {"application/json"})
    public ResponseEntity<List<SubRoles>> getSubRoles() {
        logger.info("SkillSeekerController || getSubRoles || getting The SubRoles");
        return new ResponseEntity<>(seekerInfo.getRoles(), HttpStatus.OK);
    }

    @PostMapping(value = "/addSubRoleToSeeker", produces = {"application/json"})
    public ResponseEntity<SkillSeeker> addSeekerSubRoles(@RequestParam int skillSeekerId, int role) {
        logger.info("SkillSeekerController || addSeekerSubRoles || adding Seeker SubRoles");
        return new ResponseEntity<>(seekerInfo.addSeekerSubRoles(skillSeekerId, role), HttpStatus.OK);
    }

    @PostMapping(value = "/addSubRole", produces = {"application/json"})
    public ResponseEntity<List<SeekerAccess>> addSubRole(@RequestBody SubRole role) {
        logger.info("SkillSeekerController || addSubRole || adding SubRoles");
        return new ResponseEntity<>(skillSeekerService.addSubRole(role), HttpStatus.OK);
    }

    @GetMapping(value = "getContractDetails", produces = {"application/json"})
    public ResponseEntity<List<Contracts>> getContractDetails(@RequestParam int seekerId) {
        logger.info("SkillSeekerController || getContractDetails || getting The contracts details");
        return new ResponseEntity<>(skillSeekerService.getContractDetails(seekerId), HttpStatus.OK);
    }


    @GetMapping(value = "getListsOfContractDetails", produces = {"application/json"})
    public ResponseEntity<List<ContractDetails>> getListsOfContractDetails(@RequestParam int ownerId) {
        logger.info("SkillSeekerController || getContractDetails || getting the list of contracts details");
        return new ResponseEntity<>(skillSeekerService.getListsOfContractDetails(ownerId), HttpStatus.OK);
    }

    @PostMapping(value = "onboarding", produces = {"application/json"})
    public ResponseEntity<OnBoarding> response(@RequestBody OnboardingRequest  skillOwnerDto) {
        return new ResponseEntity<>(skillSeekerService.onBoardingSkillOwner(skillOwnerDto), HttpStatus.OK);
    }

    @GetMapping(value = "getProjectTaskDetailsBySeeker", produces = {"application/json"})
    public ResponseEntity<ProjectTaskDetails> getProjectTaskDetailsBySeeker(@RequestParam int skillSeekerId) {
        logger.info("SkillSeekerController || getProjectTaskDetailsBySeeker || getting the list of project & task Details for particular SkillSeeker");
        return new ResponseEntity<>(skillSeekerService.getProjectTaskDetailsBySeeker(skillSeekerId), HttpStatus.OK);
    }

    @GetMapping(value = "getAllContractDetails", produces = {"application/json"})
    public ResponseEntity<List<Contracts>> getAllContractDetails() {
        logger.info("SkillSeekerController || getContractDetails || getting The contracts details");
        return new ResponseEntity<>(skillSeekerService.getAllContractDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "getListsOfContractDetailsInPartner", produces = {"application/json"})
    public ResponseEntity<List<ContractDetails>> getListsOfContractDetailsInPartner(@RequestParam int partnerId) {
        logger.info("SkillSeekerController || getContractDetails || getting the list of contracts details");
        return new ResponseEntity<>(skillSeekerService.getListsOfContractDetailsInPartner(partnerId), HttpStatus.OK);
    }



}