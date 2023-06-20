package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillseeker.dto.SeekerPurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.service.PoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/purchaseOrder")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})

public class PoController {

    Logger logger = LoggerFactory.getLogger(PoController.class);

    @Autowired
    private PoService poService;

    @Value("${flexcub.downloadURLPO}")
    private String downloadURLPO;


    @GetMapping(value = "/getPurchaseOrder", produces = {"application/json"})
    public ResponseEntity<List<SeekerPurchaseOrder>> getPoDetails(@RequestParam int skillSeekerId) {
        logger.info("PurchaseOrderController|| getMsaDetails || getting PurchaseOrder Details");
        return new ResponseEntity<>(poService.getPurchaseOrderDetails(skillSeekerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllPurchaseOrder", produces = {"application/json"})
    public ResponseEntity<List<SeekerPurchaseOrder>> getAllPoDetails() {
        logger.info("PurchaseOrderController|| getMsaDetails || getting All PurchaseOrder Details");
        return new ResponseEntity<>(poService.getAllPurchaseOrderDetails(), HttpStatus.OK);
    }
//
//    @PostMapping(value = "/uploadPO", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<PurchaseOrder> uploadFile(@RequestParam(value = "document") List<MultipartFile> multipartFile,
//                                                    @RequestParam("skillSeekerId") int seekerId,
//                                                    @RequestParam("skillSeekerProjectId") int projectID,
//                                                    @RequestParam("skillOwnerId") int ownerId,
//                                                    @RequestParam("Role") String role,
//                                                    @RequestParam("Domain") int domainId,
//                                                    @RequestParam("JobId") String jobId) {
//        logger.info("PurchaseOrderController|| uploadFile || ProductOwner upload File Attached");
//        return new ResponseEntity<>(poService.addData(multipartFile, role, domainId, ownerId, seekerId, projectID, jobId), HttpStatus.OK);
//    }

//    @PutMapping(value = "/uploadPO", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<PurchaseOrder> updateFile(@RequestParam(value = "document") List<MultipartFile> multipartFile,
//                                                    @RequestParam("skillSeekerId") int seekerId,
//                                                    @RequestParam("skillSeekerProjectId") int projectID,
//                                                    @RequestParam("skillOwnerId") int ownerId,
//                                                    @RequestParam("Role") String role,
//                                                    @RequestParam("Domain") int domainId,
//                                                    @RequestParam("JobId") String jobId) {
//        logger.info("PurchaseOrderController|| updateFile || Updating ProductOwner File ");
//        return new ResponseEntity<>(poService.addData(multipartFile, role, domainId, ownerId, seekerId, projectID, jobId), HttpStatus.OK);
//    }

//    @PutMapping(value = "/updateStatus", produces = {"application/json"})
//    public ResponseEntity<PurchaseOrder> updatePoStatus(@RequestParam int id, @RequestParam int status) {
//        logger.info("PurchaseOrderController|| updatePoStatus || updating The PoStatus");
//        return new ResponseEntity<>(poService.updateStatus(id, status), HttpStatus.OK);
//    }

//    @GetMapping(value = "/downloadOwnerAgreementPO", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
//    public ResponseEntity<Resource> downloadAgreement(@RequestParam int id) throws IOException {
//
//        try {
//            Optional<PoEntity> poEntity = this.poService.downloadAgreement(id);
//            if (poEntity.isPresent()) {
//                String file = String.valueOf(poEntity.get().getPoId().getFileName());
//                file = file.substring(file.indexOf("."));
//                logger.info("PurchaseOrderController|| downloadAgreement || PurchaseOrder downloadTemplate called");
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(poEntity.get().getPoId().getMimeType()))
//                        .header("Content-disposition", "attachment; filename=" + poEntity.get().getSkillOwnerEntity().getFirstName() + "_PO" + file)
//                        .body(new ByteArrayResource(poEntity.get().getPoId().getData()));
//            } else {
//                throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
//            }
//        } catch (ServiceException e) {
//            throw new ServiceException(INVALID_PURCHASEORDER_ID.getErrorCode(), INVALID_PURCHASEORDER_ID.getErrorDesc());
//        }
//    }


    @GetMapping(value = "/downloadAgreementPO", produces = {"application/json"})
    public FileResponse downloadAgreementPO(int id) throws FileNotFoundException {
        PoEntity po = poService.getPo(id);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLPO + id).
                toUriString();
        String file = String.valueOf(po.getPoId().getFileName());
        file = file.substring(file.indexOf("."));
        logger.info("PurchaseOrderController|| downloadAgreementPO || /downloadAgreementPO response URL for download");
        return new FileResponse(po.getSkillOwnerEntity().getFirstName() + "_PO" + file, fileDownloadUri, po.getPoId().getMimeType(), po.getPoId().getSize(), HttpStatus.OK);
    }

    @GetMapping(value = "/getPurchaseOrderTemplate", produces = {"APPLICATION/PDF"})
    public ResponseEntity<Resource> getProductOwnerTemplate() throws IOException {
        logger.info("PurchaseOrderController|| downloadTemplate || download Owner Purchase Order Template");
        return poService.getPurchaseOrderTemplateDownload();
    }

}


