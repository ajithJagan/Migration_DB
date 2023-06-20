package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerTimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheet;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.TimesheetDocument;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OwnerTimeSheetService {
    List<TimeSheetResponse> insertTimeSheet(List<OwnerTimeSheet> ownerTimeSheet);

    void deleteTimeSheetById(int id);


    TimeSheetResponse updateTimeSheet(TimeSheetResponse ownerTimeSheet);


    List<TimeSheetResponse> getTimeSheetHours(Date startDate, int ownerId);

    List<SkillSeekerProject> getProjectDetails(int skillOwnerId);

    List<TimeSheet> getOwnerTimeSheetDetails(int skillOwnerId);

    TimesheetDocument timesheetDocuments(MultipartFile multipartFile, int timesheetId) throws IOException;

    Optional<TimesheetDocument> downloadTimesheetDocuments(int id);

    TimesheetDocument urlDownloadTimesheetDocuments(int id);
}
