package com.flexcub.resourceplanning.skillpartner.service;

import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerRateUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerStatusUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.RateCardToSkillOwner;
import com.flexcub.resourceplanning.skillpartner.dto.SkillOwnerRateCard;
import com.flexcub.resourceplanning.skillpartner.dto.SkillPartner;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.Contracts;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SkillPartnerService {


    List<SkillPartner> getData();

    SkillPartner insertData(SkillPartner skillPartner);

    SkillPartner updateData(SkillPartner updateEntity);

    void deleteData(int id);

    void addEntryToSkillPartner(RegistrationEntity registration);

    List<SkillOwnerRateCard> addRateCard(RateCardToSkillOwner rateCardToSkillOwner);

    OwnerStatusUpdate updateSKillOwnerStatus(OwnerStatusUpdate ownerStatusUpdate);

    SkillPartner getPartnerDetails(int id);

    List<Contracts> getContractDetails(int partnerId);

    SkillPartnerEntity serviceFee(int partnerId, int percentage);

    OwnerRateUpdate updateSkillOwnerRate(OwnerRateUpdate ownerRateUpdate);

    Map<Integer,String> getAllPartnersNames();

    Optional<SkillPartnerEntity> getSkillPartnerEntity(int partnerId);



}
