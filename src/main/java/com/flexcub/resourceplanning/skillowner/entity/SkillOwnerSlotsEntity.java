package com.flexcub.resourceplanning.skillowner.entity;


import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "skill_owner_slots")
@Getter
@Setter
@RequiredArgsConstructor
@Where(clause = "deleted_at is null")
public class SkillOwnerSlotsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column
    private int skillOwnerEntityId;

    @Column
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner1;

    @Column
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner2;

    @Column
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateSlotsByOwner3;

    @Column
    private LocalTime timeSlotsByOwner1;

    @Column
    private LocalTime endTimeSlotsByOwner1;

    @Column
    private LocalTime timeSlotsByOwner2;

    @Column
    private LocalTime endTimeSlotsByOwner2;

    @Column
    private LocalTime timeSlotsByOwner3;

    @Column
    private LocalTime endTimeSlotsByOwner3;

}


