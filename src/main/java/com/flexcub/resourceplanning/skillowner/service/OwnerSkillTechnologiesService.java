package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.OwnerSkillTechnologies;

import java.util.List;

public interface OwnerSkillTechnologiesService {
    List<OwnerSkillTechnologies> getDataTech();

    OwnerSkillTechnologies insertDataTech(OwnerSkillTechnologies ownerSkillTechnologiesDto);

    OwnerSkillTechnologies updateOwnerDetails(OwnerSkillTechnologies ownerSkillTechnologiesDto);

//    void deleteDataTech(int id);
}
