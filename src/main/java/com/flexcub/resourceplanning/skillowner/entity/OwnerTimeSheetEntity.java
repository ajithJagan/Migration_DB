package com.flexcub.resourceplanning.skillowner.entity;


import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "skill_owner_timesheet")
@Getter
@Setter
@Where(clause = "deleted_at is null")
public class OwnerTimeSheetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int timeSheetId;

    @ManyToOne
    @JoinColumn(name = "skill_owner_id", referencedColumnName = "id")
    private SkillOwnerEntity skillOwnerEntity;

    @ManyToOne
    @JoinColumn(name = "skill_seeker_id", referencedColumnName = "id")
    private SkillSeekerEntity skillSeekerEntity;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private SkillSeekerProjectEntity skillSeekerProjectEntity;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private SkillSeekerTaskEntity skillSeekerTaskEntity;

    @NotNull
    @Column
    private Date startDate;

    @NotNull
    @Column
    private Date endDate;

    @NotNull
    @Column
    private String hours;

    @Column
    private boolean invoiceGenerated = false;

    @Column
    private String timesheetStatus;

    @Column(columnDefinition = "integer default 0")
    private Integer partnerId;

}



