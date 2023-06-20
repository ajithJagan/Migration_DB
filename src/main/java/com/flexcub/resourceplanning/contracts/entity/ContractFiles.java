package com.flexcub.resourceplanning.contracts.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "contract_files")
@Data
public class ContractFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    private byte[] data;

    private String fileName;

    private String mimeType;

    private long size;

    @Column(columnDefinition = "integer default 0")
    private int fileVersion;

}


