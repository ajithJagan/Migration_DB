package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OwnerSkillYearOfExperienceDetails {

    private String levelId;
    private List<String> listOfExp;

}