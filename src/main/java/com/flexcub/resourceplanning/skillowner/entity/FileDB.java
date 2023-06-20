package com.flexcub.resourceplanning.skillowner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Files")
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    private String type;

    @Lob
    private byte[] data;

    private boolean synced;

    private String skillPartnerId;


    public FileDB() {
    }

    public FileDB(String name, String type, byte[] data, boolean synced, String skillPartnerId) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.synced = synced;
        this.skillPartnerId = skillPartnerId;
    }

    public FileDB(String name, String type, byte[] data, boolean synced) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.synced = synced;
    }
}