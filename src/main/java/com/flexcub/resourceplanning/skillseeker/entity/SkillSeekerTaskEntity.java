package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "skill_seeker_task")
public class SkillSeekerTaskEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "skill_seeker_project_id", referencedColumnName = "id")
    private SkillSeekerProjectEntity skillSeekerProject;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_description")
    private String taskDescription;

    //Storing skillSeeker if there is no project , otherwise skillSeeker can be retrieved from project
    @ColumnDefault("0")
    private int skillSeekerId;
}


