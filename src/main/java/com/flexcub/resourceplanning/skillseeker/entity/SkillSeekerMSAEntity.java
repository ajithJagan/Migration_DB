package com.flexcub.resourceplanning.skillseeker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "msa_files")
@Getter
@Setter
public class SkillSeekerMSAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column
    private int skillSeekerId;

    @ManyToOne(targetEntity = SkillSeekerProjectEntity.class)
    @JsonIgnoreProperties("skill_seeker_project")
    @JoinColumn(name = "skill_seeker_project_id", referencedColumnName = "id", updatable = false)
    private SkillSeekerProjectEntity skillSeekerProject;

    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "job_id", referencedColumnName = "job_Id", updatable = false)
    private Job jobId;

    @ManyToOne(targetEntity = SkillOwnerEntity.class)
    @JsonIgnoreProperties("skill_owner_entity")
    @JoinColumn(name = "skill_owner_id", referencedColumnName = "id", updatable = false)
    private SkillOwnerEntity skillOwnerEntity;


    @Column
    private LocalDate dateSigned;

    @Column
    private Date expiryDate;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private long size = 0;

    @JsonIgnore
    @Lob
    private byte[] data;

    @OneToOne(targetEntity = ContractStatus.class)
    private ContractStatus msaStatus;

    @Column
    private Boolean msaCreated;

}
