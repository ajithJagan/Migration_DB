package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.SeekerPurchaseOrder;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PoService {

//    PurchaseOrder addData(List<MultipartFile> multipartFiles, String role, int domainId, int ownerId, int seekerId, int projectID, String jobId);

//    PurchaseOrder updateStatus(int id, String status);

//    PurchaseOrder updateStatus(int poId, int statusId);
    Optional<PoEntity> getBySkillOwnerIdAndJobId(int ownerId,String jobId);

    Optional<PoEntity> downloadAgreement(int id) throws IOException;

    List<SeekerPurchaseOrder> getPurchaseOrderDetails(int seekerId);

    List<SeekerPurchaseOrder> getAllPurchaseOrderDetails();

    ResponseEntity<Resource> getPurchaseOrderTemplateDownload() throws IOException;

    @Transactional
    PoEntity getPo(int id) throws FileNotFoundException;

    Optional<PoEntity> getById(int po);

    boolean findByOwnerId(int id);

    PoEntity save(PoEntity poEntity);

    PoEntity saveAndFlush(PoEntity poEntity);

    void removeCandidates(int OwnerId ,String jobId);

    Optional<PoEntity> findByDeleteAtNull(int OwnerId);

    Optional<PoEntity> getByProjectId(int projectId);

    List<PoEntity> findAll();

    Optional<PoEntity> getByJobId(String jobId);

    Optional<PoEntity> getByPoId(int poId);
}
