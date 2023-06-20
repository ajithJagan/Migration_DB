package com.flexcub.resourceplanning.skillowner.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkillOwnerGender {

    private int id;

    private String gender;

    public SkillOwnerGender(int id) {
        this.id=id;
    }

}
