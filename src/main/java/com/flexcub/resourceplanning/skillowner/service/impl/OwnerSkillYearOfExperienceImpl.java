package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillYearOfExperienceRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillYearOfExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerSkillYearOfExperienceImpl implements OwnerSkillYearOfExperienceService {
    @Autowired
    OwnerSkillYearOfExperienceRepository ownerSkillYearOfExperienceRepository;


    public List<Object[]> getData() {
        return ownerSkillYearOfExperienceRepository.getAll();

    }

}