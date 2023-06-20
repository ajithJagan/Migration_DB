package com.flexcub.resourceplanning.job.entity;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "SelectionPhase")
public class SelectionPhase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selection_Id")
    private int selectionId;

    @OneToOne(targetEntity = Job.class, cascade = CascadeType.DETACH)
    @JoinColumn
    private Job job;

    @OneToOne(targetEntity = SkillOwnerEntity.class)
    @JoinColumn(name = "skill_owner_entity")
    private SkillOwnerEntity skillOwnerEntity;

    @OneToMany(targetEntity = RequirementPhase.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_phase")
    private List<RequirementPhase> requirementPhase;

    //TODO : Need to remove constant value from here once edit screen is received
    private int currentStage = 1;

    private boolean showTicksValues = true;

    private boolean showSelectionBar = true;

    private Boolean accepted;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner1;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner2;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner3;

    private LocalTime timeSlotsByOwner1;

    private LocalTime timeSlotsByOwner2;

    private LocalTime timeSlotsByOwner3;

    private LocalTime endTimeSlotsByOwner1;

    private LocalTime endTimeSlotsByOwner2;

    private LocalTime endTimeSlotsByOwner3;

    private Boolean newSlotRequested;

    private Boolean slotConfirmed;

    private Boolean mailSent = false;

    private Boolean interviewAccepted = false;

    private LocalDate joinedOn = null;

    private LocalDate rejectedOn;

    private Integer rate;
}
