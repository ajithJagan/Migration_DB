package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;

import java.util.List;

public interface SkillSeekerTaskService {

    List<SkillSeekerTask> insertData(List<SkillSeekerTask> skillSeekerTaskList);

    List<SkillSeekerTask> getTaskData(int id,int skillSeekerId);

    void deleteData(int id);

    SkillSeekerTask updateData(SkillSeekerTask skillSeekerTask);
}


