package com.flexcub.resourceplanning.job.dto;

import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class SelectionPhaseDto {

    private int selectionId;

    private Job job;

    private SkillOwnerEntity skillOwnerEntity;

    private List<RequirementPhase> requirementPhase;

    private int currentStage = 1;

    private boolean showTicksValues = true;

    private boolean showSelectionBar = true;

    private boolean accepted;

    private LocalDate dateSlotsByOwner1;

    private LocalDate dateSlotsByOwner2;

    private LocalDate dateSlotsByOwner3;

    private LocalTime timeSlotsByOwner1;

    private LocalTime timeSlotsByOwner2;

    private LocalTime timeSlotsByOwner3;

    private Boolean newSlotRequested;

    private Boolean slotConfirmed;
}
