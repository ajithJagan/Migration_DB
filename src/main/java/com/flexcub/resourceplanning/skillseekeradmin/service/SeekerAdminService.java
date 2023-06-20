package com.flexcub.resourceplanning.skillseekeradmin.service;

import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillseekeradmin.dto.PartnerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerStatusUpdate;

import java.util.List;

public interface SeekerAdminService {

    List<SeekerAdmin> getSkillSeeker();

    SeekerStatusUpdate updateSeekerStatus(SeekerStatusUpdate seekerStatusUpdate);

    List<TimeSheetResponse> getTimeSheets();

    List<PartnerAdmin> getAllSkillPartner();



}

