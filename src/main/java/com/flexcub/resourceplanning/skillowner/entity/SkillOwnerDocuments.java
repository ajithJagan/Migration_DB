package com.flexcub.resourceplanning.skillowner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class SkillOwnerDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int ownerId;

    private String name;

    private String type;

    private int count;

    @Lob
    private byte[] data;
    @ColumnDefault("1")
    private long size;

    public SkillOwnerDocuments(int ownerId, String name, String type, byte[] data, int count, long size) {
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.data = data;
        this.count = count;
        this.size = size;

    }


}