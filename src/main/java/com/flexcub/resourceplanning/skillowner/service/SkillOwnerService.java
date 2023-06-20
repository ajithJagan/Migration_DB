package com.flexcub.resourceplanning.skillowner.service;


import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerGender;
import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerMaritalStatus;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerDocuments;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerSlotsEntity;
import com.flexcub.resourceplanning.skillseeker.dto.Contracts;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SkillOwnerService {

    SkillOwnerDto getSkillOwner(int id);

    Optional<SkillOwnerEntity> getSkillOwnerEntity(int id);

    List<SkillOwnerEntity> insertData(List<SkillOwnerEntity> skillOwnerEntity) throws IOException;

    SkillOwnerEntity updateOwnerDetails(SkillOwnerEntity skillOwnerEntity);

//    void deleteData(int id);

    void deletePortfolioUrl(int id);

    List<SkillOwnerGender> genderList();

    List<SkillOwnerMaritalStatus> maritalStatus();

    SkillOwnerDto updateOwnerProfile(SkillOwnerEntity skillOwnerEntity);

    SkillOwnerEntity getOwnerId(int id);

    Boolean insertAttachment(Optional<MultipartFile> resume, List<MultipartFile> documents, Optional<MultipartFile> image, Optional<MultipartFile> federal, int ownerId) throws IOException;

    ResponseEntity<Resource> downloadImage(int ownerId) throws IOException;

    ResponseEntity<Resource> downloadResume(int id) throws IOException;

    ResponseEntity<Resource> fileDownloadFederal(int id);


    @Transactional
    Contracts ownerContractDetails(SkillSeekerMSAEntity skillSeekerMSAEntity);


    @Transactional
    ResponseEntity<Resource> downloadOtherDocuments(int ownerId, int count);

    @Transactional
    void deleteOtherDocuments(int id, int count);

    @Transactional
    SkillOwnerResumeAndImage getResume(int ownerId) throws FileNotFoundException;

    @Transactional
    SkillOwnerResumeAndImage getImage(int ownerId) throws FileNotFoundException;

    @Transactional
    SkillOwnerResumeAndImage getFederal(int ownerId) throws FileNotFoundException;

    SkillOwnerResumeAndImage storeResume(Optional<MultipartFile> resume, int ownerId) throws IOException;

    SkillOwnerResumeAndImage storeImage(Optional<MultipartFile> image, int ownerId) throws IOException;

    SkillOwnerDocuments getOtherDocuments(int ownerId, int count);

    SkillOwnerDocuments documentUpdates(MultipartFile documents, int ownerId, int count) throws IOException;

    SkillOwnerSlotsEntity findBySkillOwnerEntityId(int skillOwnerId);
  
    SkillOwnerEntity save (SkillOwnerEntity skillOwnerEntity);

}
