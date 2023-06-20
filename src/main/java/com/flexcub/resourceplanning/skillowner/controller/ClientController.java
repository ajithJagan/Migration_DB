package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.Client;
import com.flexcub.resourceplanning.skillowner.dto.ClientDetails;
import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import com.flexcub.resourceplanning.skillowner.service.ClientService;
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
@RequestMapping(value = "/v1/ClientController", produces = {"application/json"})
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class
ClientController {
    @Autowired
    ClientService clientService;

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping(value = "/getClientDetails")
    public ResponseEntity<ClientDetails> getSkillOwnerId(@RequestParam int ownerId) {
        logger.info("ClientEntity || getClient || Getting the Client Details from the ClientEntity");
        return new ResponseEntity<>(clientService.getClient(ownerId), HttpStatus.OK);
    }

    @PostMapping(value = "/insertClient")
    public ResponseEntity<List<Client>> insertClient(@RequestBody List<Client> clientEntity) {
        logger.info("ClientEntity || insertClient || Inserting the Client Details from the ClientEntity");
        return new ResponseEntity<>(clientService.insertClient(clientEntity), HttpStatus.OK);
    }

    @PutMapping(value = "/updateClient")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity clientEntity) {
        logger.info("ClientEntity || updateClient || Updating the Client Details from the ClientEntity");
        return new ResponseEntity<>(clientService.updateClient(clientEntity), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteClient")
    public void deleteClient(@RequestParam int clientId) {
        logger.info("ClientEntity || deleteClient || Deleting the Client Details from the ClientEntity");
        clientService.deleteClient(clientId);
    }
}





