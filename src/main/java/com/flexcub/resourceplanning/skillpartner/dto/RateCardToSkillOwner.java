package com.flexcub.resourceplanning.skillpartner.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RateCardToSkillOwner {

    private int skillPartnerId;
    private List<SkillOwnerRateCard> skillOwnerRateCards;

}
