package com.flexcub.resourceplanning.template.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "template_table")
@Data
public class TemplateTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String templateName;

    @Column
    private String templateType;
    @Column
    private String templateMimeType;
    @Column
    private long size;
    @Column(columnDefinition = "integer default 0")
    private Integer templateVersion;

    @JsonIgnore
    @Lob
    private byte[] data;
}
