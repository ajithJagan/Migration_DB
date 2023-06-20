package com.flexcub.resourceplanning.job.dto;

import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SelectionPhaseResponse {

    private int selectionId;

    private String jobId;

    private String jobTitle;

    private int skillOwnerId;

    private String skillOwnerName;

    private Integer rate;

    private int currentStage;

    private boolean showTicksValues;

    private boolean showSelectionBar;

    private String experience;

    private List<RequirementPhase> requirementPhaseList;

    private boolean accepted;

    private LocalDate dateSlotsByOwner1;

    private LocalDate dateSlotsByOwner2;

    private LocalDate dateSlotsByOwner3;

    private LocalTime timeSlotsByOwner1;

    private LocalTime timeSlotsByOwner2;

    private LocalTime timeSlotsByOwner3;

    private Boolean newSlotRequested;

    private Boolean slotConfirmed;

    private boolean isImageAvailable = false;

    private boolean isResumeAvailable = false;

    private boolean interviewAccepted = false;

    private boolean msaCreated = false;

    private boolean sowCreated = false;

    private boolean poCreated = false;

}
