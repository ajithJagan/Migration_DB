package com.flexcub.resourceplanning.contracts.service;

import com.flexcub.resourceplanning.contracts.dto.*;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.skillseeker.dto.PurchaseOrder;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ContractService {

    MsaFilesResponse createSeekerMsa(MultipartFile multipartFile, String msaFileDetails) throws IOException;

    MsaFilesResponse createPartnerMsa(MultipartFile multipartFile, String msaFileDetails) throws IOException;

    List<PartnerContractDetails> getPartnerMsaDetailsInSuperAdmin();

    ResponseEntity<Resource> downloadSkillPartnerMsaAgreement(int id) throws IOException;

    MsaFilesResponse updatePartnerMsaStatus(int msaId, int msaStatusId);

//    PoResponse createPO(MultipartFile multipartFile, String poDetails) throws IOException;

    PoResponse updatePOByAdmin(MultipartFile multipartFile, String updateDetails) throws IOException;

//    PurchaseOrder updateStatus(int poId, int statusId);

    Boolean seekerMSAValidation(int seekerId);

    MsaFilesResponse updateSeekerMsaStatus(int msaId, int msaStatusId);

    List<SeekerMSADetails> getSeekerMsaDetailsInSuperAdmin();

    ResponseEntity<Resource> downloadSeekerMsa(int seekerId);

    ContractFiles getMsa(int seekerId);

    ContractFiles saveContract(ContractFiles contractFiles);

    MsaInitiate msaInitiate(MsaInitiate initiate);

    PartnerContractDetails getPartnerMsa(int id);

    List<SowResponse> getSeekerDetails(int seekerId);


    SowCreateResponse sowCreation(MultipartFile multipartFile, int sowEntityId) throws IOException;

    PoCreateResponse poCreation(MultipartFile multipartFile, int poEntityId) throws IOException;

    SowPoInitiateResponse initiateSow(InitiateSowPoRequest sowRequest);

    StatementOfWorkStatus updateSowStatus(int sowId, int status);

    PurchaseOrder updatePoStatus(int poId, int status);

    Resource sowDocument(String jobId) throws IOException, SQLException;

    Resource poDocument(String jobId) throws IOException, SQLException;


    List<PartnersOwnerData> getProjectDetails(int projectId);

    List<PartnersOwnerData> getPartnersData(int partnerId);

    PartnerSowResponse initiateSowPartner(InitiatePartnerSow partnerSow);

    PartnerSowResponse createPartnerSow(MultipartFile multipartFile, int sowEntityId) throws IOException;

    PartnerSowResponse updatePartnerSowStatus(int sowId, int sowStatusId);

    Resource partnerSowDocument(String jobId, int partnerId) throws IOException, SQLException;

    List<JobDetail> getJobDetails(String jobId);

    Page<PartnerContractDetails> getPartnerContractsInAdmin(int page, int size);
    Map<Integer, String> createNew();

    List<PartnerContractDetails> getPartnerSowDetailsInSuperAdmin();

    List<PartnerContractDetails> getByPartnerIdSowDetail(int partnerId);

    SeekerMSADetails getSeekerMsa(int seekerId);

    @Transactional
    List<SeekerMSADetails> getPurchaseOrderDetails(int skillSeekerId);

    @Transactional
    List<SeekerMSADetails> getSowDetails(int seekerId);

    List<SeekerMSADetails> getAllContractsDetails(int skillSeekerId);

    @Transactional
    List<SeekerMSADetails> getAllSowDetails();

    List<SeekerMSADetails> getAllPoDetails();

    List<SeekerMSADetails> getAllContractsDetails();

    List<PartnerContractDetails> getContractByPartnerId(int partnerId);

    Page<SeekerMSADetails> getAllViewData(int page,int size);
    Map<Integer, String> getPartnersProject(int partnerId);

    Map<String, String> getJobNamesByProjectId(int projectId, int id);






}

