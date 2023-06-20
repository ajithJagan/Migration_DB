package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.SowStatusDto;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWork;
import com.flexcub.resourceplanning.skillseeker.dto.StatementOfWorkGetDetails;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StatementOfWorkService {

//    StatementOfWork addDocument(MultipartFile multipartFile,  String sowRequestDto) throws IOException;

//    List<StatementOfWorkGetDetails> getSowDetails(int skillSeekerId);

//    List<StatementOfWorkGetDetails> getAllSowDetails();

//    StatementOfWork upDateSow(MultipartFile multipartFile,String sowUpDateRequest) throws IOException;

//    SowStatusDto updateSowStatus(int id, int sowStatusId);

    StatementOfWorkEntity downloadAgreementSOW(int id);

    ResponseEntity<Resource> templateDownload() throws IOException;

    StatementOfWorkEntity getSow(int id);

    Optional<StatementOfWorkEntity> findByOwnerId(int ownerId);


    Optional<StatementOfWorkEntity> getBySowId(int sowId);

    Optional<StatementOfWorkEntity> getByProjectId(int projectId);

    StatementOfWorkEntity  save(StatementOfWorkEntity sowEntity);

    Optional<StatementOfWorkEntity> getById(int sowEntityId);

    Optional<StatementOfWorkEntity> getStatementofWorkEntity(int id);

    Optional <StatementOfWorkEntity> getByPartnerJob(int partnerId,  String jobId);

    Optional<StatementOfWorkEntity> getByJobId(String jobId);

    Optional<List<StatementOfWorkEntity>> findBySkillSeekerId(int seekerId);

    List<StatementOfWorkEntity> findAll();
}
