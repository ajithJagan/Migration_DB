package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerTaskRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTaskService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SkillSeekerTask;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.TASK_ALREADY_DEFINED;

@Service
public class SkillSeekerTaskServiceImpl implements SkillSeekerTaskService {

    @Autowired
    SkillSeekerTaskRepository taskRepository;

    Logger logger = LoggerFactory.getLogger(SkillSeekerTaskServiceImpl.class);

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<SkillSeekerTask> insertData(List<SkillSeekerTask> skillSeekerTaskList) {

        List<SkillSeekerTaskEntity> skillSeekerTaskEntities = new ArrayList<>();
        for (SkillSeekerTask skillSeekerTask1 : skillSeekerTaskList) {
            Optional<List<SkillSeekerTaskEntity>> taskList = taskRepository.findBySkillSeekerProjectId(skillSeekerTask1.getSkillSeekerProjectEntity().getId());
            if (taskList.get().size() > 0) {
                for (SkillSeekerTaskEntity task : taskList.get()) {
                    if (task.getTaskTitle().equalsIgnoreCase(skillSeekerTask1.getTaskTitle())) {
                        throw new ServiceException(TASK_ALREADY_DEFINED.getErrorCode(), TASK_ALREADY_DEFINED.getErrorDesc());
                    }
                }
            }
            SkillSeekerTaskEntity skillSeekerTaskEntity1 = modelMapper.map(skillSeekerTask1, SkillSeekerTaskEntity.class);
            if (null == skillSeekerTask1.getSkillSeekerProjectEntity() || skillSeekerTask1.getSkillSeekerProjectEntity().getId() == 0) {
                List<SkillSeekerTaskEntity> defaultProjectTask = taskRepository.findByDefaultProject();
                if (defaultProjectTask.size() > 0 ) {
                    for (SkillSeekerTaskEntity defaultTask : defaultProjectTask) {
                        if (defaultTask.getTaskTitle().equalsIgnoreCase(skillSeekerTask1.getTaskTitle()) && defaultTask.getSkillSeekerId()==skillSeekerTask1.getSkillSeekerId()) {
                            throw new ServiceException(TASK_ALREADY_DEFINED.getErrorCode(), TASK_ALREADY_DEFINED.getErrorDesc());
                        }
                    }
                }
                skillSeekerTaskEntity1.setSkillSeekerId(skillSeekerTask1.getSkillSeekerId());
                skillSeekerTaskEntity1.setSkillSeekerProject(null);
            }
            skillSeekerTaskEntities.add(skillSeekerTaskEntity1);
//                skillSeekerTaskEntities.forEach(skillSeekerProject -> skillSeekerProject.setSkillSeekerTechnologyData(skillSeekerTechnologyService.insertMultipleData(skillSeekerProject.getSkillSeekerTechnologyData())));
            taskRepository.saveAll(skillSeekerTaskEntities);
        }
        logger.info("SkillSeekerTaskServiceImpl || insertData || Inserting the SeekerTask list: {}", skillSeekerTaskList);
        List<SkillSeekerTask> skillSeekerTaskLists = new ArrayList<>();
        for (SkillSeekerTaskEntity skillSeekerTaskEntity : skillSeekerTaskEntities) {
            SkillSeekerTask skillSeekerTask = modelMapper.map(skillSeekerTaskEntity, SkillSeekerTask.class);
            skillSeekerTaskLists.add(skillSeekerTask);
        }
        return skillSeekerTaskLists;
    }


    @Override
    public List<SkillSeekerTask> getTaskData(int id, int skillSeekerId) {
        List<SkillSeekerTask> skillSeekerTaskList = new ArrayList<>();
        Optional<List<SkillSeekerTaskEntity>> skillSeekerTask;
        if (id == 0) {
            skillSeekerTask = taskRepository.findBySkillSeekerProjectIdAndSkillSeekerId(skillSeekerId);
        } else {
            skillSeekerTask = taskRepository.findBySkillSeekerProjectId(id);
        }
        if (!skillSeekerTask.get().isEmpty()) {
            for (SkillSeekerTaskEntity skillSeekerTaskEntity : skillSeekerTask.get()) {
                SkillSeekerTask skillSeekerTask1 = modelMapper.map(skillSeekerTaskEntity, SkillSeekerTask.class);
                skillSeekerTaskList.add(skillSeekerTask1);
            }
            return skillSeekerTaskList;
        } else {
            logger.info("SkillSeekerTaskServiceImpl || getTaskData || No tasks found for given project id");
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteData(int id) {

        Optional<SkillSeekerTaskEntity> skillSeekerTaskData = taskRepository.findById(id);

        if (skillSeekerTaskData.isPresent()) {
            logger.info("SkillSeekerTaskServiceImpl || deleteData || Deleted the SeekerTask id: {}", id);
            taskRepository.deleteById(id);
        } else {
            throw new ServiceException(INVALID_TASK_ID.getErrorCode(), INVALID_TASK_ID.getErrorDesc());
        }

    }

    @Override
    public SkillSeekerTask updateData(SkillSeekerTask skillSeekerTask) {

        Optional<SkillSeekerTaskEntity> skillSeekerTaskData = taskRepository.findById(skillSeekerTask.getTaskId());
        SkillSeekerTask seekerTask = modelMapper.map(skillSeekerTaskData, SkillSeekerTask.class);
        if (skillSeekerTaskData.isPresent()) {
            seekerTask.setTaskTitle(skillSeekerTask.getTaskTitle());
            seekerTask.setTaskDescription(skillSeekerTask.getTaskDescription());
            if (skillSeekerTaskData.get().getSkillSeekerProject() != null) {
                Optional<List<SkillSeekerTaskEntity>> taskList = taskRepository.findBySkillSeekerProjectId(skillSeekerTaskData.get().getSkillSeekerProject().getId());
                if (taskList.get().size() > 0) {
                    for (SkillSeekerTaskEntity task : taskList.get()) {
                        if (task.getId() != skillSeekerTask.getTaskId() && task.getTaskTitle().equalsIgnoreCase(seekerTask.getTaskTitle())) {
                            throw new ServiceException(TASK_ALREADY_DEFINED.getErrorCode(), TASK_ALREADY_DEFINED.getErrorDesc());
                        }
                    }
                }
            }
            if (skillSeekerTaskData.get().getSkillSeekerProject() == null) {
                List<SkillSeekerTaskEntity> defaultProjectTask = taskRepository.findByDefaultProject();
                if (defaultProjectTask.size() > 0) {
                    for (SkillSeekerTaskEntity defaultTask : defaultProjectTask) {
                        if (defaultTask.getId() != skillSeekerTask.getTaskId() && defaultTask.getTaskTitle().equalsIgnoreCase(seekerTask.getTaskTitle())) {
                            throw new ServiceException(TASK_ALREADY_DEFINED.getErrorCode(), TASK_ALREADY_DEFINED.getErrorDesc());
                        }
                    }
                }
            }
            BeanUtils.copyProperties(seekerTask, skillSeekerTaskData.get(), NullPropertyName.getNullPropertyNames(seekerTask));
            taskRepository.saveAndFlush(skillSeekerTaskData.get());
        }
        return seekerTask;
    }
}





