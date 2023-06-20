package com.flexcub.resourceplanning.skillpartner.controller;

import com.flexcub.resourceplanning.skillpartner.dto.OwnerRateUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerStatusUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.RateCardToSkillOwner;
import com.flexcub.resourceplanning.skillpartner.dto.SkillOwnerRateCard;
import com.flexcub.resourceplanning.skillpartner.dto.SkillPartner;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerFileDataService;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.dto.Contracts;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/SkillPartnerController")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server Error"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class SkillPartnerController {

    @Autowired
    SkillPartnerService skillPartnerService;

    @Autowired
    SkillPartnerFileDataService skillPartnerFileDataService;

    Logger logger = LoggerFactory.getLogger(SkillPartnerController.class);

    /**
     * Request to get the list of all SkillPartnerEntity
     *
     * @return list of all details
     */
    @GetMapping(value = "/getData", produces = {"application/json"})
    public ResponseEntity<List<SkillPartner>> getSkillPartnerDetails() {
        logger.info("SkillPartnerController || getSkillPartnerDetails || Getting the SkillPartnerDetails Info");
        return new ResponseEntity<>(skillPartnerService.getData(), HttpStatus.OK);
    }

    @GetMapping(value = "/getPartnerDetails", produces = {"application/json"})
    public ResponseEntity<SkillPartner> getPartnerDetails(int id) {
        logger.info("SkillPartnerController || getSkillPartnerDetails || Getting the SkillPartnerDetail Info");
        return new ResponseEntity<>(skillPartnerService.getPartnerDetails(id), HttpStatus.OK);
    }

    /**
     * Request to insert new data into SkillPartnerEntity
     *
     * @param skillPartner
     * @return newly inserted data
     */
    @PostMapping(value = "/insertData", produces = {"application/json"})
    public ResponseEntity<SkillPartner> insertSkillPartnerDetails(@RequestBody SkillPartner skillPartner) {
        logger.info("SkillPartnerController || insertSkillPartnerDetails || Inserting the SkillPartnerDetails Info {} //-> ", skillPartner);
        return new ResponseEntity<>(skillPartnerService.insertData(skillPartner), HttpStatus.OK);
    }

    /**
     * Request to update the existing data from SkillPartnerEntity
     *
     * @param updateEntity
     * @return updated data
     */
    @PutMapping(value = "/updateData", produces = {"application/json"})
    public ResponseEntity<SkillPartner> updateSkillPartnerDetails(@RequestBody SkillPartner updateEntity) {
        logger.info("SkillPartnerController || updateSkillPartnerDetails || Updating the SkillPartnerDetails Info {} // ->", updateEntity);
        return new ResponseEntity<>(skillPartnerService.updateData(updateEntity), HttpStatus.OK);
    }

    /**
     * Request to delete data from SkillPartnerEntity based on Id
     *
     * @param id
     */
    @DeleteMapping(value = "/deleteData", produces = {"application/json"})
    public void deleteData(@RequestParam int id) {
        logger.info("SkillPartnerController || deleteData || Deleting the SkillPartnerDetails Info {} //-> ", id);
        skillPartnerService.deleteData(id);
    }

    /**
     * @param id
     * @return String
     * @throws IOException
     */
    @GetMapping(path = "/syncPartnerExcel/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setDataInDb(@PathVariable int id) {
        logger.info("SkillPartnerController|| setDataInDb || /syncPartnerExcel called");
        try {
            return new ResponseEntity<>(skillPartnerFileDataService.readSkillPartnerExcelFile(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Sync Failure");
        }
    }

    @PutMapping(value = "/updateRateCardDetails", produces = {"application/json"})
    public ResponseEntity<List<SkillOwnerRateCard>> addRateCard(@RequestBody RateCardToSkillOwner rateCardToSkillOwner) {
        logger.info("SkillOwnerEntity || addRateCard || Added the rateCard in to the skillOwner by using SkillPartner");
        return new ResponseEntity<>(skillPartnerService.addRateCard(rateCardToSkillOwner), HttpStatus.OK);

    }

    @PutMapping(value = "/updateSkillOwnerStatus", produces = {"application/json"})
    public ResponseEntity<OwnerStatusUpdate> skillOwnerStatusUpdate(@RequestBody OwnerStatusUpdate ownerStatusUpdate) {
        logger.info("SkillPartnerController || skillOwnerStatusUpdate || update SkillOwner Status by SkillPartner");
        return new ResponseEntity<>(skillPartnerService.updateSKillOwnerStatus(ownerStatusUpdate), HttpStatus.OK);

    }
    @GetMapping(value = "getContractDetails", produces = {"application/json"})
    public ResponseEntity<List<Contracts>> getContractDetails(@RequestParam int partnerId) {
        logger.info("SkillSeekerController || getContractDetails || getting The contracts details");
        return new ResponseEntity<>(skillPartnerService.getContractDetails(partnerId), HttpStatus.OK);
    }


    @PutMapping(value = "/serviceFeeInPercentage", produces = {"application/json"})
    public ResponseEntity<SkillPartnerEntity> serviceFee(@RequestParam int partnerId, int percentage) {
        logger.info("SkillPartnerController || serviceFee || Inserting the serviceFee percentage into partnerTable ");
        return new ResponseEntity<>(skillPartnerService.serviceFee(partnerId, percentage), HttpStatus.OK);
    }

    @PutMapping(value="/updateSkillOwnerRate", produces={"application/json"})
    public ResponseEntity<OwnerRateUpdate> skillOwnerRateUpdate(@RequestBody OwnerRateUpdate ownerRateUpdate) {
        logger.info("SkillPartnerController || skillOwnerRateUpdate || update SkillOwner Rate by SkillPartner");
        return new ResponseEntity<>(skillPartnerService.updateSkillOwnerRate(ownerRateUpdate), HttpStatus.OK);
    }
    @GetMapping(value = "/getPartnerBusinessNames", produces = {"application/json"})
    public ResponseEntity<Map<Integer,String>> getAllPartners() {
        logger.info("SkillPartnerController || getPartnerBusinessName || getting the partner Business names");
        return new ResponseEntity<>(skillPartnerService.getAllPartnersNames(), HttpStatus.OK);
    }

}