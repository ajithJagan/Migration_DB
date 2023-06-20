package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerProject;

import java.util.List;
import java.util.Optional;

public interface SkillSeekerProjectService {

    List<SkillSeekerProject> insertData(List<SkillSeekerProject> skillSeekerProjectEntityList);

    List<SkillSeekerProject> getProjectData(int id);

    void deleteData(int id);

    SkillSeekerProject updateData(SkillSeekerProject skillSeekerprojectEntity);

    Optional<SkillSeekerProjectEntity> getById(int projectId);

    Optional<List<SkillSeekerProjectEntity>> getBySeekerId(int seekerId);

    SkillSeekerProjectEntity getByProjectId(int id);
}
