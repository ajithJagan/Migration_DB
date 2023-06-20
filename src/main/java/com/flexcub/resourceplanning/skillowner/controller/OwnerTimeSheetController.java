package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.FileResponse;
import com.flexcub.resourceplanning.skillowner.dto.OwnerTimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.TimesheetDocument;
import com.flexcub.resourceplanning.skillowner.service.OwnerTimeSheetService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.INVALID_TIMESHEETID;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.INVALID_TIMESHEET_ID;


@RestController
@RequestMapping(value = "/v1/OwnerSkillTimeSheet", produces = {"application/json"})
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "404", description = "Bad Request"),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
public class OwnerTimeSheetController {

    @Autowired
    OwnerTimeSheetService ownerTimeSheetService;

    @Value("${flexcub.downloadURLTS}")
    private String downloadURLTS;

    Logger logger = LoggerFactory.getLogger(OwnerTimeSheetController.class);

    @PostMapping(value = "/insertTimeSheet", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TimeSheetResponse>> insertTimeSheet(@RequestBody List<OwnerTimeSheet> ownerTimeSheet) {
        logger.info("OwnerTimeSheetController || insertTimeSheet || Inserting the Details for theOwnerTimeSheet");
        return new ResponseEntity<>(ownerTimeSheetService.insertTimeSheet(ownerTimeSheet), HttpStatus.OK);
    }

    @PostMapping(value = "/timesheetDocuments", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<TimesheetDocument> timesheetDocuments(@RequestParam(value = "document") MultipartFile multipartFile,
                                                                @RequestParam("timesheetId") int timesheetId) throws IOException {
        logger.info("OwnerTimeSheetController|| timesheetDocuments || timesheet upload File Attached");
        return new ResponseEntity<>(ownerTimeSheetService.timesheetDocuments(multipartFile, timesheetId), HttpStatus.OK);
    }

    @GetMapping(value = "/downloadTimesheetDocuments", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Resource> downloadTimesheetDocuments(@RequestParam int id) throws IOException {
        Optional<TimesheetDocument> timesheetDocument = this.ownerTimeSheetService.downloadTimesheetDocuments(id);
        TimesheetDocument doc = timesheetDocument.orElseThrow(() -> new ServiceException(INVALID_TIMESHEETID.getErrorCode(), INVALID_TIMESHEETID.getErrorDesc()));
        String fileExtension = FilenameUtils.getExtension(doc.getName());
        logger.info("OwnerTimeSheetController|| downloadTimesheetDocuments || timesheet downloadTemplate called");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getType()))
                .header("Content-disposition", "attachment; filename=" + doc.getName() + "_TS." + fileExtension)
                .body(new ByteArrayResource(doc.getData()));
    }

    @GetMapping(value = "/urlDownloadTimesheetDocument", produces = {"application/json"})
    public FileResponse urlDownloadTimesheetDocuments(int id) throws FileNotFoundException {
        TimesheetDocument timesheetDocument = ownerTimeSheetService.urlDownloadTimesheetDocuments(id);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                fromUriString(downloadURLTS + id).
                toUriString();
        String file = String.valueOf(timesheetDocument.getName());
        file = file.substring(file.indexOf("."));
        logger.info("OwnerTimeSheetController|| urlDownloadTimesheetDocuments || timesheetDocument response URL for download");
        return new FileResponse(timesheetDocument.getName() + "_TS" + file, fileDownloadUri, timesheetDocument.getType(), timesheetDocument.getSize(), HttpStatus.OK);
    }
    @PutMapping(value = "/updateTimeSheet")
    public ResponseEntity<TimeSheetResponse> updateTimeSheet(@RequestBody TimeSheetResponse ownerTimeSheetDto) {
        logger.info("OwnerTimeSheetController||updateTimeSheet||Updating the details for OwnerTimeSheet");
        return new ResponseEntity<>(ownerTimeSheetService.updateTimeSheet(ownerTimeSheetDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteData")
    public void deleteTimeSheetData(@RequestParam int id) {
        logger.info("SkillOwnerTimeSheetController || deleteTimeSheetData || Deleting the Details from the SkillOwnerTimeSheet");
        ownerTimeSheetService.deleteTimeSheetById(id);
    }

    @GetMapping(value = "/getTimeSheet")
    public ResponseEntity<List<TimeSheetResponse>> getTimeSheet(@RequestParam("weekStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam("ownerId") int ownerId) {
        logger.info("OwnerTimeSheetController || getTimeSheetHours || Getting the date for last specific week ");
        return new ResponseEntity<>(ownerTimeSheetService.getTimeSheetHours(startDate, ownerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getSkillOwnerProjectDetails")
    public ResponseEntity<List<SkillSeekerProject>> getProjectDetails(@RequestParam int skillOwnerId) {
        logger.info("OwnerTimeSheetController || getProjectDetails || Getting the skillOwnerProjects ");
        return new ResponseEntity<>(ownerTimeSheetService.getProjectDetails(skillOwnerId), HttpStatus.OK);
    }

    @GetMapping(value = "/getOwnerTimeSheetDetails")
    public ResponseEntity<List<TimeSheet>> getOwnerTimeSheetDetails(@RequestParam int skillOwnerId) {
        logger.info("OwnerTimeSheetController || getOwnerTimeSheetDetails || Getting the OwnerTimeSheetDetails ");
        return new ResponseEntity<>(ownerTimeSheetService.getOwnerTimeSheetDetails(skillOwnerId), HttpStatus.OK);
    }

}
