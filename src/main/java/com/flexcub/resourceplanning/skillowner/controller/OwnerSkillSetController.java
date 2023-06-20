package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerSkillSet;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillSetService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/OwnerSkillSet", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerSkillSetController {
    @Autowired
    OwnerSkillSetService ownerSkillSetService;

    Logger logger = LoggerFactory.getLogger(OwnerSkillSetController.class);

    @GetMapping(value = "/getSkillSet")
    public ResponseEntity<List<SkillOwnerSkillSet>> getDetails(@RequestParam int skillOwnerId) {
        logger.info("OwnerSkillSetController || getDetails || Getting the Details from the OwnerSkillSetEntity ");
        return new ResponseEntity<>(ownerSkillSetService.getSkillset(skillOwnerId), HttpStatus.OK);
    }

    @PostMapping(value = "/insertSkillset")
    public ResponseEntity<SkillOwnerSkillSet> insertDetails(@Valid @RequestBody SkillOwnerSkillSet ownerSkillSetDto) {
        logger.info("OwnerSkillSetController || insertDetails || Inserting the Details from the OwnerSkillSetEntity");
        return new ResponseEntity<>(ownerSkillSetService.insertSkillSet(ownerSkillSetDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteDataset", produces = {"application/json"})
    public String deleteDetails(@RequestParam int skillId) {
        logger.info("OwnerSkillSetController || deleteDetails || Deleting the Details from the OwnerSkillSetEntity");
        String s = ownerSkillSetService.deleteSkillset(skillId);
//        return new ResponseEntity("Deleted Successfully", HttpStatus.OK);
        return s;
    }


    @PutMapping(value = "/updateSkillSet")
    public ResponseEntity<SkillOwnerSkillSet> updateDetails(@RequestBody SkillOwnerSkillSet ownerSkillSetDto) {
        logger.info("OwnerSkillSetController || updateDetails || Updating the Details from the OwnerSkillSetEntity");
        return new ResponseEntity<>(ownerSkillSetService.updateSkillSet(ownerSkillSetDto), HttpStatus.OK);

    }

}
