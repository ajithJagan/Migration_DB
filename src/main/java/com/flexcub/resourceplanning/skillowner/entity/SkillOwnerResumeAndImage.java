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
public class SkillOwnerResumeAndImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int ownerId;

    private String imageName;

    private String resumeName;

    private String federalName;

    private String imageType;

    private String resumeType;

    private String federalType;

    @Lob
    @Column(name = "imageData")
    private byte[] imageData;

    @Lob
    @Column(name = "resumeData")
    private byte[] resumeData;

    @Lob
    @Column(name = "federalData")
    private byte[] federalData;

    @ColumnDefault("1")
    private long imageSize;

    @ColumnDefault("1")
    private long resumeSize;

    @ColumnDefault("1")
    private long federalSize;

    private boolean image = false;

    private boolean resume = false;

    @Column(columnDefinition = "boolean default false")
    private boolean federal = false;

//    public SkillOwnerResumeAndImage(int ownerId, String name, String type, long size) {
//        this.ownerId = ownerId;
//        this.name = name;
//        this.type = type;
//        this.size = size;
//
//    }


}