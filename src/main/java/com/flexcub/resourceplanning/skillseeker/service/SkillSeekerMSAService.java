package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.MsaStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.SkillSeekerMsaDto;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdminMsa;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SkillSeekerMSAService {
    SkillSeekerMsaDto addDocuments(List<MultipartFile> multipartFiles, int seekerId, int projectID, String jobId, int ownerId);

    Optional<SkillSeekerMSAEntity> downloadAgreement(int id) throws IOException;

    List<SeekerAdminMsa> getMsaDetails();

    List<SeekerAdminMsa> getMsaDetailsBySeeker(int id);

    ResponseEntity<Resource> getSkillSeekerMsaTemplateDownload() throws IOException;


    List<ContractStatus> getMsaStatus();

    MsaStatusDto updateMsaStatus(int msaId, int msaStatusId);

    @Transactional
    SkillSeekerMSAEntity getMSA(int id);
}
