package com.flexcub.resourceplanning.invoice.controller;

import com.flexcub.resourceplanning.invoice.dto.*;
import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;
import com.flexcub.resourceplanning.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/invoice")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Server Error"), @ApiResponse(responseCode = "400", description = "Bad Request"), @ApiResponse(responseCode = "404", description = "Bad Request")})

public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    //2nd
    @PostMapping(value = "/savePartnerInvoice", produces = {"application/json"})
    public ResponseEntity<InvoiceResponse> saveInvoiceDetailsByPartner(@RequestBody InvoiceRequest invoiceRequest) {
        logger.info("Invoice Controller || add Invoice Details || Adding the Invoice Details By Partner");
        return new ResponseEntity<>(invoiceService.saveInvoiceByPartner(invoiceRequest), HttpStatus.OK);
    }


    @GetMapping(value = "/getOwnersByPartnerId", produces = {"application/json"})
    public ResponseEntity<List<WorkEfforts>> getOwnersByPartners(@RequestParam("partnerId") int partnerId, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        logger.info("InvoiceController || getAllInvoiceDetails || get all the Seeker Info");
        return new ResponseEntity<>(invoiceService.getOwnerByPartner(partnerId, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(value = "/submittedInvoicesByPartner", produces = {"application/json"})
    public ResponseEntity<List<InvoiceListingData>> getInvoices(int partnerId) {
        logger.info("InvoiceController || getInvoices || Get invoice listing data ");
        return new ResponseEntity<>(invoiceService.getInvoices(partnerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getInvoiceDetails", produces = {"application/json"})
    public ResponseEntity<InvoiceDetailResponse> getInvoiceByInvoiceId(@RequestParam String invoiceId, @RequestParam boolean partnerGenerated) {
        logger.info("Invoice Controller || get Invoice Details || Getting the Invoice Details by InvoiceId");
        return new ResponseEntity<>(invoiceService.getInvoice(invoiceId, partnerGenerated), HttpStatus.OK);
    }

    //1st api

    @PutMapping(value = "/updatePartnerInvoiceStatus")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(@RequestParam String invoiceId, @RequestParam int id, @RequestParam(required = false) String comments) {
        logger.info("Invoice Controller || updateInvoiceStatus || updating the invoiceDetails by invoiceId {} ->", invoiceId);
        return new ResponseEntity<>(invoiceService.updatePartnerInvoiceStatus(invoiceId, id, comments), HttpStatus.OK);
    }

//    @PutMapping(value = "/updateAdminInvoiceStatus")
//    public ResponseEntity<InvoiceResponse> updateAdminInvoiceStatus(@RequestParam String invoiceId, int id) {
//        logger.info("Invoice Controller || updateInvoiceStatus || updating the invoiceDetails by invoiceId {} ->", invoiceId);
//        return new ResponseEntity<>(invoiceService.updateAdminInvoiceStatus(invoiceId, id), HttpStatus.OK);
//    }

    @GetMapping(value = "/invoiceStatus", produces = {"application/json"})
    public ResponseEntity<List<InvoiceStatus>> getInvoiceStatus() {
        logger.info("Invoice Controller || getInvoiceStatus ||getting all invoice status ");
        return new ResponseEntity<>(invoiceService.getInvoiceStatus(), HttpStatus.OK);
    }


    @GetMapping(value = "/getAllInvoiceDetails", produces = {"application/json"})
    public ResponseEntity<List<InvoiceDetails>> getAllInvoiceDetails() {
        logger.info("SeekerAdminController || GetInvoiceDetails || To Get all invoice Details from Partner ");
        return new ResponseEntity<>(invoiceService.getAllInvoiceDetails(), HttpStatus.OK);
    }


    @GetMapping(value = "/getAdminInvoiceDetails", produces = {"application/json"})
    public ResponseEntity<List<AdminInvoice>> getAdminInvoiceData(@RequestParam int seekerId, int projectId) {
        logger.info("Invoice Controller || get Admin Invoice Details || Getting the Invoice Data by projectId and seekerId");
        return new ResponseEntity<>(invoiceService.getAdminInvoiceData(seekerId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAdminInvoiceforSeeker", produces = {"application/json"})
    public ResponseEntity<List<SeekerInvoice>> getAdminInvoiceData(@RequestParam int seekerId) {
        logger.info("Invoice Controller || get Admin Invoice Details || Getting the Invoice Data by seekerId");
        return new ResponseEntity<>(invoiceService.getAllInvoiceOfSeeker(seekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getPartnerInvoiceBySeeker", produces = {"application/json"})
    public ResponseEntity<List<AdminInvoice>> getPartnerInvoiceBySeeker(@RequestParam int seekerId, int projectId) {
        logger.info("Invoice Controller || get Admin Invoice Details || Getting the Invoice Data by projectId and seekerId");
        return new ResponseEntity<>(invoiceService.getAdminInvoiceData(seekerId, projectId), HttpStatus.OK);

    }


    @PostMapping(value = "/saveAdminInvoice", produces = {"application/json"})
    public ResponseEntity<List<InvoiceResponse>> saveInvoiceDetailsByAdmin(@RequestBody List<AdminInvoiceRequest> invoiceRequest) {
        logger.info("Invoice Controller || add Invoice Details || Adding the Invoice Details By Admin");
        return new ResponseEntity<>(invoiceService.saveInvoiceDetailsByAdmin(invoiceRequest), HttpStatus.OK);
    }


    //    @PostMapping(value = "/saveSeekerInvoice", produces = {"application/json"})
//    public ResponseEntity<List<SkillSeekerInvoice>> saveSeekerInvoiceDetails(@RequestBody List<SeekerInvoice> seekerInvoices) {
//        logger.info("Invoice Controller || save seeker Invoice Details || Saving the Seeker Invoice Details");
//        return new ResponseEntity<>(invoiceService.saveSeekerInvoice( seekerInvoices), HttpStatus.OK);
//    }
    @PutMapping(value = "/updateAdminInvoiceStatus")
    public ResponseEntity<SeekerInvoiceStatus> updateSeekerInvoiceStatus(@RequestParam String invoiceId, @RequestParam int statusId, @RequestParam(required = false) String comments) {
        logger.info("Invoice Controller || update Seeker InvoiceStatus || updating the seeker invoice status {} ->", invoiceId, statusId);
        return new ResponseEntity<>(invoiceService.updateSeekerInvoiceStatus(invoiceId, statusId, comments), HttpStatus.OK);
    }

    @GetMapping(value = "/clientDetailInInvoice", produces = {"application/json"})
    public ResponseEntity<List<ClientInvoiceDetails>> invoiceClientDetails() {
        logger.info("Invoice Controller || invoiceClientDetails || Getting The Client Details From Invoice");
        return new ResponseEntity<>(invoiceService.invoiceClientDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllAdminInvoices", produces = {"application/json"})
    public ResponseEntity<List<InvoiceDetails>> getAllInvoiceDetailAdmin() {
        logger.info("Invoice Controller || GetInvoiceDetails || To Get all invoice Details from Admin ");
        return new ResponseEntity<>(invoiceService.getAllInvoiceDetailAdmin(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAdminInvoiceBySeeker", produces = {"application/json"})
    public ResponseEntity<List<InvoiceDetails>> getAdminInvoiceBySeeker(@RequestParam int seekerId) {
        logger.info("Invoice Controller || getAdminInvoiceBySeeker || Getting the InvoiceAdmin By seekerId");
        return new ResponseEntity<>(invoiceService.getAdminInvoiceBySeeker(seekerId), HttpStatus.OK);

    }

    @PutMapping(value = "/updatePartnerInvoice", produces = {"application/json"})
    public ResponseEntity<InvoiceUpdate> updateInvoiceDetailsByPartner(@RequestBody InvoiceUpdate invoiceUpdateRequest) {
        logger.info("Invoice Controller || update Invoice Details || updating the Invoice Details By Partner");
        return new ResponseEntity<>(invoiceService.updateInvoiceDetailsByPartner(invoiceUpdateRequest), HttpStatus.OK);

    }

    @PutMapping(value = "/updateAdminInvoice", produces = {"application/json"})
    public ResponseEntity<InvoiceUpdate> updateInvoiceDetailsByAdmin(@RequestBody InvoiceUpdate invoiceUpdateRequest) {
        logger.info("Invoice Controller || update Admin Details || updating the Invoice Details By Admin");
        return new ResponseEntity<>(invoiceService.updateInvoiceDetailsByAdmin(invoiceUpdateRequest), HttpStatus.OK);
    }

}