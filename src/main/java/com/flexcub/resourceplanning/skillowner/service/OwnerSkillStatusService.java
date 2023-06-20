package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillStatus;

import java.util.List;

public interface OwnerSkillStatusService {
    List<OwnerSkillStatus> getDataStatus();

    OwnerSkillStatus insertDataStatus(OwnerSkillStatus ownerSkillStatusDto);

    OwnerSkillStatus updateOwnerDetails(OwnerSkillStatus ownerSkillStatusDto);


}
