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
public class TimesheetDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "integer default 0")
    private int timesheetId;

    private int ownerId;

    private int seekerId;

    private int projectId;

    private String name;

    private String type;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @ColumnDefault("1")
    private long size;


    public TimesheetDocument(int ownerId, String name, byte[] data, String type, long size, int seekerId, int projectId, int timesheetId) {
        this.ownerId = ownerId;
        this.name = name;
        this.data = data;
        this.type = type;
        this.size = size;
        this.seekerId = seekerId;
        this.projectId = projectId;
        this.timesheetId = timesheetId;
    }

}
