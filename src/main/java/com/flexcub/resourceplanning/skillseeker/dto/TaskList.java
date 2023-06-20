package com.flexcub.resourceplanning.skillseeker.dto;

import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseekeradmin.dto.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskList {

    private SkillSeekerProjectEntity skillSeekerProjectEntity;
    private List<Task> skillSeekerTasks;
}
