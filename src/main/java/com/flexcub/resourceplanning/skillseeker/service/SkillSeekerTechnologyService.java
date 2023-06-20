package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTechnologyData;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTechnology;

import java.util.List;

public interface SkillSeekerTechnologyService {

    List<SkillSeekerTechnologyData> insertMultipleData(List<SkillSeekerTechnologyData> skillSeekerRateCardList);

    SkillSeekerTechnology updateData(SkillSeekerTechnology skillSeekerRateCard);

    void deleteData(int id);

    List<SkillSeekerTechnology> getDataByProjectId(int id);
}
