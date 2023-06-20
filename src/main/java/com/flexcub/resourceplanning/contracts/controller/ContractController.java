package com.flexcub.resourceplanning.contracts.controller;

import com.flexcub.resourceplanning.contracts.dto.*;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.contracts.service.ContractService;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import liquibase.pro.packaged.R;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/contracts")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Server Error"), @ApiResponse(responseCode = "400", description = "Bad Request"), @ApiResponse(responseCode = "400", description = "Bad Request")})

public class ContractController {
    Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    ContractService contractService;

    @Value("${flexcub.downloadURLSeekerMSA}")
    private String downloadURLSeekerMSA;

    @PostMapping(value = "/createSeekerMsaContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MsaFilesResponse> createSeekerMSA(@RequestParam("file") MultipartFile file, @RequestPart String msaContractDetails) throws IOException {
        logger.info("ContractController || createSeekerMSA || New MSA agreement for SkillSeeker ");
        return new ResponseEntity<>(contractService.createSeekerMsa(file, msaContractDetails), HttpStatus.OK);
    }

    @PostMapping(value = "/createPartnerMsaContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MsaFilesResponse> createPartnerMSA(@RequestParam("file") MultipartFile file, @RequestPart String msaContractDetails) throws IOException {
        logger.info("ContractController || createPartnerMSA || New MSA agreement for SkillPartner");
        return new ResponseEntity<>(contractService.createPartnerMsa(file, msaContractDetails), HttpStatus.OK);
    }

    @PutMapping(value = "/updatePartnerMsaStatus", produces = {"application/json"})
    public ResponseEntity<MsaFilesResponse> updatePartner(@RequestParam int partnerId, @RequestParam int msaStatusId) {
        logger.info("ContractController || updatePartnerMsaStatus || update msa status");
        return new ResponseEntity<>(contractService.updatePartnerMsaStatus(partnerId, msaStatusId), HttpStatus.OK);
    }

    @PutMapping(value = "/updateSeekerMsaStatus", produces = {"application/json"})
    public ResponseEntity<MsaFilesResponse> updateSeeker(@RequestParam int msaId, @RequestParam int msaStatusId) {
        logger.info("ContractController || updateSeekerMsaStatus || update msa status");
        return new ResponseEntity<>(contractService.updateSeekerMsaStatus(msaId, msaStatusId), HttpStatus.OK);
    }

    @GetMapping(value = "/getPartnerContractsInAdmin", produces = {"application/json"})
    public ResponseEntity<Page<PartnerContractDetails>> getPartnerContractDetailsInSuperAdmin(@RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("ContractController || getPartnerContractsInAdmin || Getting the PartnerContractDetails in SuperAdmin");
        return new ResponseEntity<>(contractService.getPartnerContractsInAdmin(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/downloadSkillPartnerMsaAgreement", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Resource> downloadSkillPartnerMsaAgreement(@RequestParam int id) throws IOException {
        logger.info("ContractController|| downloadSkillPartnerMsaAgreement || downloading Msa Agreement for SkillPartner");
        return contractService.downloadSkillPartnerMsaAgreement(id);
    }

    //    @PostMapping(value = "/createPoContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<PoResponse> createPO(@RequestParam("file") MultipartFile multipartFile, @RequestPart String poDetails) throws IOException {
//        logger.info("ContractController || createPo || New PO agreement for SkillOwner");
//        return new ResponseEntity<>(contractService.createPO(multipartFile, poDetails), HttpStatus.OK);
//    }
    @PutMapping(value = "/updatePoContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PoResponse> updatePOByAdmin(@RequestParam(value = "file", required = false) MultipartFile multipartFile, @RequestPart String updatePODetails) throws IOException {
        logger.info("ContractController || createPo || New PO agreement for SkillOwner");
        return new ResponseEntity<>(contractService.updatePOByAdmin(multipartFile, updatePODetails), HttpStatus.OK);
    }

//    @PutMapping(value = "/updatePOStatus", produces = {"application/json"})
//    public ResponseEntity<PurchaseOrder> updatePoStatus(@RequestParam int id, @RequestParam int status) {
//        logger.info("PurchaseOrderController|| updatePoStatus || updating The PoStatus");
//        return new ResponseEntity<>(contractService.updateStatus(id, status), HttpStatus.OK);
//    }

    @GetMapping(value = "/fileDownloadMSASeeker", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> downloadSeekerMsa(@RequestParam int seekerId) {
        logger.info("ContractController|| downloadSeekerMsa || download MSA Agreement for seeker");
        return contractService.downloadSeekerMsa(seekerId);
    }

    @GetMapping(value = "/uriDownloadMSASeeker", produces = {"application/json"})
    public FileResponse downloadResume(int seekerId) {
        ContractFiles contractFile = contractService.getMsa(seekerId);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLSeekerMSA + seekerId).
                toUriString();
        logger.info("ContractController|| fileDownloadMSASeeker || /fileDownloadMSASeeker response URL for download");
        return new FileResponse(contractFile.getFileName(), fileDownloadUri, contractFile.getMimeType(), contractFile.getSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/seekerMSAValidation", produces = {"application/json"})
    public ResponseEntity<Boolean> seekerMSAValidation(int seekerId) {
        logger.info("ContractController || seekerMSAValidation || check the MSA Validation ");
        return new ResponseEntity<>(contractService.seekerMSAValidation(seekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getSeekerMsaDetailsInSuperAdmin", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getSeekerMsaDetailsInSuperAdmin() {
        logger.info("ContractController || getSeekerMsaDetailsInSuperAdmin || get the SeekerMSADetails");
        return new ResponseEntity<>(contractService.getSeekerMsaDetailsInSuperAdmin(), HttpStatus.OK);
    }

    @PostMapping(value = "/msaInitiate", produces = {"application/json"})
    public ResponseEntity<MsaInitiate> msaInitiate(@RequestBody MsaInitiate initiate) {
        logger.info("ContractController || msaInitiate || To initiate Master service Agreement");
        return new ResponseEntity<>(contractService.msaInitiate(initiate), HttpStatus.OK);
    }

    @GetMapping(value = "/getProjectDetails", produces = "application/json")
    public ResponseEntity<List<PartnersOwnerData>> getProjectDetails(@RequestParam int projectId) {
        logger.info("Contract Controller || getProjectDetailsCount || get Project List with count for PartnerSow");
        return new ResponseEntity<>(contractService.getProjectDetails(projectId), HttpStatus.OK);
    }

    @GetMapping(value = "/getSeekerDetails", produces = {"application/json"})
    public ResponseEntity<List<SowResponse>> getSeekerDetails(int seekerId) {
        logger.info("ContractController || getSeekerDetails || get  Seeker's Data List to create New SOW/PO agreement");
        return new ResponseEntity<>(contractService.getSeekerDetails(seekerId), HttpStatus.OK);

    }

    @GetMapping("/download-SOWTemplate")
    public ResponseEntity<Resource> generateSowDocument(@RequestParam String jobId) {
        try {
            Resource sowBlob = contractService.sowDocument(jobId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "SOW.pdf");

            return new ResponseEntity<>(sowBlob, headers, HttpStatus.OK);
        } catch (IOException | SQLException e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download-POTemplate")
    public ResponseEntity<Resource> generatePODocument(@RequestParam String jobId) {
        try {
            Resource sowBlob = contractService.poDocument(jobId);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "PO.pdf");

            return new ResponseEntity<>(sowBlob, headers, HttpStatus.OK);
        } catch (IOException | SQLException e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping(value = "/createSowContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SowCreateResponse> createSowContract(@RequestParam("file") MultipartFile multipartFile, int sowEntityId) throws IOException {
        logger.info("ContractController || create Sow || New SOW agreement for SkillOwner for a project");
        return new ResponseEntity<>(contractService.sowCreation(multipartFile, sowEntityId), HttpStatus.OK);
    }

    @PostMapping(value = "/createPoContract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PoCreateResponse> createPoContract(@RequestParam("file") MultipartFile multipartFile, int poEntityId) throws IOException {
        logger.info("ContractController || create PO || New PO agreement for SkillOwner for a project");
        return new ResponseEntity<>(contractService.poCreation(multipartFile, poEntityId), HttpStatus.OK);
    }

    @PostMapping(value = "/initiateSowOrPoContract", produces = {"application/json"})
    public ResponseEntity<SowPoInitiateResponse> initiateSow(@RequestBody InitiateSowPoRequest sowRequest) {
        logger.info("ContractController || Initiate Sow || New SOW agreement for SkillOwner for a project");
        return new ResponseEntity<>(contractService.initiateSow(sowRequest), HttpStatus.OK);
    }

    @PutMapping(value = "/updateSowStatus", produces = {"application/json"})
    public ResponseEntity<StatementOfWorkStatus> updateSowStatus(@RequestParam int sowId, @RequestParam int status) {
        logger.info("ContractController|| updateSowStatus || updating The SowStatus");
        return new ResponseEntity<>(contractService.updateSowStatus(sowId, status), HttpStatus.OK);
    }

    @PutMapping(value = "/updatePOStatus", produces = {"application/json"})
    public ResponseEntity<PurchaseOrder> updatePoStatus(@RequestParam int poId, @RequestParam int status) {
        logger.info("PurchaseOrderController|| updatePoStatus || updating The PoStatus");
        return new ResponseEntity<>(contractService.updatePoStatus(poId, status), HttpStatus.OK);
    }

    @PostMapping(value = "/initiatePartnerSow", produces = "application/json")
    public ResponseEntity<PartnerSowResponse> initiatePartnerSow(@RequestBody InitiatePartnerSow partnerSow) {
        logger.info("Contract Controller || initiatePartnerSow || Initiated the Partner Sow");
        return new ResponseEntity<>(contractService.initiateSowPartner(partnerSow), HttpStatus.OK);

    }

    @PostMapping(value = "/createPartnerSOW", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PartnerSowResponse> createPartnerSOW(@RequestParam("file") MultipartFile file, @RequestParam int sowEntityId) throws IOException {
        logger.info("ContractController || createPartnerSOW || New SOW agreement for SkillPartner ");
        return new ResponseEntity<>(contractService.createPartnerSow(file, sowEntityId), HttpStatus.OK);
    }

    @GetMapping(value = "/getPartnerSowDetails", produces = "application/json")
    public ResponseEntity<List<PartnersOwnerData>> getPartnerSowDetails(@RequestParam int partnerId) {
        logger.info("Contract Controller || getPartnersData || get Partner Details with Projects ");
        return new ResponseEntity<>(contractService.getPartnersData(partnerId), HttpStatus.OK);
    }

    @PutMapping(value = "/updatePartnerSowStatus", produces = {"application/json"})
    public ResponseEntity<PartnerSowResponse> updatePartnerSowStatus(@RequestParam int sowId, @RequestParam int sowStatus) {
        logger.info("ContractController|| updatePartnerSowStatus || updating Partner Sow Status");
        return new ResponseEntity<>(contractService.updatePartnerSowStatus(sowId, sowStatus), HttpStatus.OK);
    }


    @GetMapping(value = "/getSeekerMsa", produces = {"application/json"})
    public ResponseEntity<SeekerMSADetails> getSeekerMsa(int seekerId) {
        logger.info("ContractController || getSeekerMsaDetailsInSuperAdmin || get the SeekerMSADetails");
        return new ResponseEntity<>(contractService.getSeekerMsa(seekerId), HttpStatus.OK);
    }


       @GetMapping("/downloadPartnerSow")
       public ResponseEntity<Resource> downloadPartnerSow(@RequestParam String jobId, @RequestParam int partnerId) {

               try {
                   Resource sowBlob = contractService.partnerSowDocument(jobId,partnerId);


                   HttpHeaders headers = new HttpHeaders();
                   headers.setContentType(MediaType.APPLICATION_PDF);
                   headers.setContentDispositionFormData("attachment", "SOW.pdf");

                   return new ResponseEntity<>(sowBlob, headers, HttpStatus.OK);
               } catch (IOException | SQLException e) {
                   // Handle exceptions and return an appropriate response
                   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
               }
}



    @GetMapping(value = "/getJobDetails", produces = "application/json")
    public ResponseEntity<List<JobDetail>> getJobDetails(@RequestParam String jobId) {
        logger.info("Contract Controller || getJobDetailsWithCount || get Job List with count for PartnerSow");
        return new ResponseEntity<>(contractService.getJobDetails(jobId), HttpStatus.OK);
    }

    @GetMapping(value = "/getCreateNew", produces = {"application/json"})
    public ResponseEntity<Map<Integer, String>> getCreatenew() {
        logger.info("ContractController || getCreatenew || Getting the list to create agreement");
        return new ResponseEntity<>(contractService.createNew(), HttpStatus.OK);
    }

    @GetMapping(value = "/getSowDetails")
    public ResponseEntity<List<SeekerMSADetails>> getSowDetails(@RequestParam int seekerId) {
        logger.info("StatementOfWorkController|| getSowDetails || getSowDetails");
        return new ResponseEntity<>(contractService.getSowDetails(seekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getPurchaseOrder", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getPoDetails(@RequestParam int skillSeekerId) {
        logger.info("PurchaseOrderController|| getMsaDetails || getting PurchaseOrder Details");
        return new ResponseEntity<>(contractService.getPurchaseOrderDetails(skillSeekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllContractsOfSeeker", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getAllContractsOfSeeker(@RequestParam int skillSeekerId) {
        logger.info("SkillSeekerController|| getAllContractsDetails || getting All contracts Details");
        return new ResponseEntity<>(contractService.getAllContractsDetails(skillSeekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllSowDetails", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getAllSowDetails() {
        logger.info("ContractController|| getAllSowDetails in superAdmin || get AllSowDetails");
        return new ResponseEntity<>(contractService.getAllSowDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllPurchaseOrder", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getAllPoDetails() {
        logger.info("ContractController|| getAll PO Details in superAdmin  || getting All PO Details");
        return new ResponseEntity<>(contractService.getAllPoDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllContracts", produces = {"application/json"})
    public ResponseEntity<List<SeekerMSADetails>> getAllContractDetailsInSuperAdmin() {
        logger.info("ContractController|| getAll PO Details in superAdmin  || getting All PO Details");
        return new ResponseEntity<>(contractService.getAllContractsDetails(), HttpStatus.OK);
    }

    @GetMapping(value = "/getContractByPartnerId", produces = {"application/json"})
    public ResponseEntity<List<PartnerContractDetails>> getContractByPartnerId(int partnerId) {
        logger.info("ContractController || getContractByPartnerId || Getting the list of Contracts of SkillPartners");
        return new ResponseEntity<>(contractService.getContractByPartnerId(partnerId), HttpStatus.OK);

    }
    @GetMapping(value = "/allSeekerContractInAdmin/view-data", produces = {"application/json"})
    public ResponseEntity<Page<SeekerMSADetails>> getViewData(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        logger.info("ContractController || getViewData || Getting the list of Contracts of skillSeekers in superAdmin");
        return new ResponseEntity<>(contractService.getAllViewData(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/getPartnersProject", produces = "application/json")
    public ResponseEntity <Map<Integer, String>>getPartnersProject(@RequestParam int partnerId) {
        logger.info("Contract Controller || getPartnersData || get Partner Details with Projects ");
        return new ResponseEntity<>(contractService.getPartnersProject(partnerId), HttpStatus.OK);
    }

    @GetMapping(value="/getJobs", produces = {"application/json"})
    public ResponseEntity<Map<String,String>> getAllJobs(@RequestParam int projectId, @RequestParam int id){
        return new ResponseEntity<>(contractService.getJobNamesByProjectId(projectId,id),HttpStatus.OK);
    }



}

