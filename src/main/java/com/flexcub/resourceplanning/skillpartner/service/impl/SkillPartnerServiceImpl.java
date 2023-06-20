package com.flexcub.resourceplanning.skillpartner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.invoice.repository.InvoiceDataRepository;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.RequirementPhaseRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.notifications.repository.OwnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.repository.PartnerNotificationsRepository;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerRateUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.OwnerStatusUpdate;
import com.flexcub.resourceplanning.skillpartner.dto.RateCardToSkillOwner;
import com.flexcub.resourceplanning.skillpartner.dto.SkillOwnerRateCard;
import com.flexcub.resourceplanning.skillpartner.dto.SkillPartner;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import com.flexcub.resourceplanning.skillseeker.dto.Contracts;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Log4j2
public class SkillPartnerServiceImpl implements SkillPartnerService {

    @Autowired
    SkillPartnerRepository skillPartnerRepository;
    Logger logger = LoggerFactory.getLogger(SkillPartnerServiceImpl.class);

    @Autowired
    SkillOwnerRepository skillOwnerRepository;

    @Autowired
    PoRepository poRepository;
    @Autowired
    RequirementPhaseRepository requirementPhaseRepository;
    @Lazy
    @Autowired
    SkillOwnerService skillOwnerService;

    @Autowired
    InvoiceDataRepository invoiceDataRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OwnerNotificationsRepository ownerNotificationsRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    PartnerNotificationsRepository partnerNotificationsRepository;

    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;

    @Autowired
    SkillSeekerMsaRepository skillSeekerMsaRepository;

    /**
     * Method to get a list of all SkillPartnerEntity details
     *
     * @return list of all details
     */

    public List<SkillPartner> getData() {

        Optional<List<SkillPartnerEntity>> skillPartnerList = Optional.of(skillPartnerRepository.findAll());
        List<SkillPartner> skillPartnerDtoList = new ArrayList<>();
        try {
            if (!skillPartnerList.get().isEmpty()) {
                for (SkillPartnerEntity skillPartner : skillPartnerList.get()) {
                    SkillPartner skillPartnerDto = modelMapper.map(skillPartner, SkillPartner.class);
                    skillPartnerDtoList.add(skillPartnerDto);
                }
                logger.info("SkillPartnerServiceImpl || getData || Displays all skill partner details");
                return skillPartnerDtoList;
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }

    }

    public SkillPartner getPartnerDetails(int id) {

        Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(id);
        try {
            if (skillPartnerEntity.isPresent()) {
                SkillPartner skillPartnerDto = modelMapper.map(skillPartnerEntity, SkillPartner.class);
                logger.info("SkillPartnerServiceImpl || getPartnerDetails || Displays the skill partner details with id: {}", id);
                return skillPartnerDto;
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc());
        }
    }

    /**
     * @param partnerId
     * @return
     */
    @Override
    @Transactional
    public List<Contracts> getContractDetails(int partnerId) {
        try {
            Optional<List<SkillOwnerEntity>> skillOwner = skillOwnerRepository.findBySkillPartnerId(partnerId);
            List<Contracts> contracts = new ArrayList<>();
            if (skillOwner.get().size() > 0) {
                for (SkillOwnerEntity skillOwnerEntity : skillOwner.get()) {
                    Optional<SkillSeekerMSAEntity> seekerMSA = Optional.ofNullable(skillSeekerMsaRepository.findByOwnerId(skillOwnerEntity.getSkillOwnerEntityId()));
                    if (seekerMSA.isPresent()) {
                        Contracts contracts1 = skillOwnerService.ownerContractDetails(seekerMSA.get());
                        if (null != contracts1) {
                            contracts.add(contracts1);
                        }
                    }
                }
                return contracts;
            } else {
                throw new ServiceException(MSA_ID_NOT_FOUND.getErrorCode(), MSA_ID_NOT_FOUND.getErrorDesc());
            }
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public SkillPartnerEntity serviceFee(int partnerId, int percentage) {
        Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(partnerId);
        if (skillPartnerEntity.isPresent()) {
            skillPartnerEntity.get().setServiceFeePercentage(percentage / 100.0);
            skillPartnerRepository.saveAndFlush(skillPartnerEntity.get());
        } else {
            throw new ServiceException(INVALID_ID.getErrorCode(), INVALID_ID.getErrorDesc());
        }
        return skillPartnerEntity.get();
    }


    /**
     * Method to insert new data into SkillPartnerEntity
     *
     * @param skillPartner obj
     * @return newly inserted data
     */

    public SkillPartner insertData(SkillPartner skillPartner) {
        try {
            SkillPartnerEntity partnerEntity = modelMapper.map(skillPartner, SkillPartnerEntity.class);
            partnerEntity.setServiceFeePercentage(0.10);
            skillPartnerRepository.save(partnerEntity);
            BeanUtils.copyProperties(partnerEntity, skillPartner, NullPropertyName.getNullPropertyNames(partnerEntity));
            logger.info("SkillPartnerServiceImpl || insertData ||  successfully inserted skill partner data {} // ->", skillPartner);
            return skillPartner;
        } catch (Exception e) {
            throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
        }
    }

    /**
     * Method to delete the data from SkillPartnerEntity Based on Id
     *
     * @param id
     */
    public void deleteData(int id) {
        Optional<SkillPartnerEntity> skillPartnerData = skillPartnerRepository.findById(id);
        if (skillPartnerData.isPresent()) {
            skillPartnerData.get().setDeletedAt(LocalDateTime.now());
            logger.info("SkillPartnerServiceImpl || deleteData || Data has been deleted for id: {}", id);
            skillPartnerRepository.save(skillPartnerData.get());
        } else {
            throw new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc());
        }
    }


    /**
     * Method to update the existing data in SkillPartnerEntity
     *
     * @param updateEntity
     * @return updated data
     */
    public SkillPartner updateData(SkillPartner updateEntity) {
        SkillPartnerEntity skillPartner = modelMapper.map(updateEntity, SkillPartnerEntity.class);

        try {
            Optional<SkillPartnerEntity> skillPartnerEntity = skillPartnerRepository.findById(skillPartner.getSkillPartnerId());

            if (skillPartnerEntity.isPresent()) {
                BeanUtils.copyProperties(skillPartner, skillPartnerEntity.get(), NullPropertyName.getNullPropertyNames(skillPartner));
                skillPartnerEntity.get().setServiceFeePercentage(updateEntity.getServiceFeePercentage() / 100);
                skillPartnerRepository.save(skillPartnerEntity.get());
                SkillPartner skillPartnerDto = modelMapper.map(skillPartnerEntity, SkillPartner.class);
                logger.info("SkillPartnerServiceImpl || updateData || successfully updated skill partner data {} //->", updateEntity);
                return skillPartnerDto;
            } else {
                throw new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());
        }
    }

    public OwnerRateUpdate updateSkillOwnerRate(OwnerRateUpdate ownerRateUpdate) {
        try {
            Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(ownerRateUpdate.getSkillOwnerId());
            if (skillOwnerEntity.isPresent()) {
                skillOwnerEntity.get().setRateCard(ownerRateUpdate.getRate());
                skillOwnerRepository.save(skillOwnerEntity.get());
                OwnerRateUpdate rateUpdate = modelMapper.map(skillOwnerEntity, OwnerRateUpdate.class);
                rateUpdate.setRate(skillOwnerEntity.get().getRateCard());
                return rateUpdate;
            } else {
                throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
            }
        } catch (Exception e) {

            throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());

        }

    }


    @Override
    public void addEntryToSkillPartner(RegistrationEntity registration) {
        SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
        skillPartnerEntity.setBusinessName(registration.getBusinessName());
        skillPartnerEntity.setTaxIdBusinessLicense(registration.getTaxIdBusinessLicense());
        skillPartnerEntity.setPhone(registration.getBusinessPhone());
        skillPartnerEntity.setSkillPartnerId(registration.getId());
        if (null != registration.getLastName()) {
            skillPartnerEntity.setPrimaryContactFullName(registration.getFirstName() + " " + registration.getLastName());
        } else {
            skillPartnerEntity.setPrimaryContactFullName(registration.getFirstName());
        }
        skillPartnerEntity.setBusinessEmail(registration.getEmailId());
        skillPartnerEntity.setState(registration.getState());
        skillPartnerEntity.setAddressLine1(registration.getAddress());
        skillPartnerEntity.setPrimaryContactEmail(registration.getContactEmail());
        skillPartnerEntity.setPrimaryContactPhone(registration.getContactPhone());
        skillPartnerEntity.setExcelId(registration.getExcelId());
        skillPartnerRepository.save(skillPartnerEntity);
    }


    //    @Override
//    public List<SkillOwnerEntity> addRateCard(RateCardToSkillOwner rateCardToSkillOwner) {
//        try {
//            List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();
//            rateCardToSkillOwner.getSkillOwnerRateCards().forEach(skillOwnerRateCard -> {
//                SkillOwnerEntity savedSkillOwner = skillOwnerRepository.findBySkillOwnerEntityId(skillOwnerRateCard.getSkillOwnerId());
//                if (savedSkillOwner.getSkillPartnerEntity().getSkillPartnerId() == rateCardToSkillOwner.getSkillPartnerId()) {
//                    savedSkillOwner.setRateCard(skillOwnerRateCard.getRate());
//                    skillOwnerEntities.add(skillOwnerRepository.saveAndFlush(savedSkillOwner));
//                } else {
//                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//
//                }
//            });
//            return skillOwnerEntities;
//        } catch (Exception e) {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//        }
//    }

    @Transactional
    @Override
    public List<SkillOwnerRateCard> addRateCard(RateCardToSkillOwner rateCardToSkillOwner) {

        List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();
        List<SkillOwnerRateCard> skillOwnerRateCards = new ArrayList<>();

        rateCardToSkillOwner.getSkillOwnerRateCards().forEach(skillOwnerRateCard -> {
            try {
                SkillOwnerEntity savedSkillOwner = skillOwnerRepository.findBySkillOwnerEntityId(skillOwnerRateCard.getSkillOwnerId());

                if (savedSkillOwner.getSkillPartnerEntity().getSkillPartnerId() == rateCardToSkillOwner.getSkillPartnerId()) {
                    Hibernate.initialize(savedSkillOwner.getPortfolioUrl());
                    savedSkillOwner.setRateCard(skillOwnerRateCard.getRate());
                    skillOwnerEntities.add(skillOwnerRepository.saveAndFlush(savedSkillOwner));
                    SkillOwnerRateCard skillOwnerDto = modelMapper.map(savedSkillOwner, SkillOwnerRateCard.class);
                    skillOwnerRateCards.add(skillOwnerDto);
                } else {
                    throw new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc());
                }
            } catch (NullPointerException e) {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
            } catch (ServiceException e) {
                throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
            }
        });

        return skillOwnerRateCards;
    }

    @Override
    @Transactional
    public OwnerStatusUpdate updateSKillOwnerStatus(OwnerStatusUpdate ownerStatusUpdate) {

        RegistrationEntity registration = registrationRepository.findById(ownerStatusUpdate.getSkillOwnerId()).orElseThrow(() -> new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc()));
        Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(ownerStatusUpdate.getSkillOwnerId());
        Optional<List<RequirementPhase>> requirementPhaseList = requirementPhaseRepository.findBySkillOwnerId(ownerStatusUpdate.getSkillOwnerId());
        Optional<List<SelectionPhase>> selectionPhaseList = selectionPhaseRepository.findBySkillOwnerId(ownerStatusUpdate.getSkillOwnerId());
        Optional<PoEntity> po = poRepository.findByOwnerId(ownerStatusUpdate.getSkillOwnerId());
        if (selectionPhaseList.get().size() > 0) {
            selectionPhaseList.get().forEach(selectionPhase -> {
                if (selectionPhase.getRejectedOn() != null || (po.isPresent() && po.get().getExpiryDate().before(new Date()))) {
                    registration.setIsAccountActive(ownerStatusUpdate.getIsAccountActive());
                    skillOwnerEntity.get().setAccountStatus(ownerStatusUpdate.getIsAccountActive());
                } else {
                    throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());
                }
            });
        } else if (skillOwnerEntity.isPresent() && requirementPhaseList.get().isEmpty()) {
            registration.setIsAccountActive(ownerStatusUpdate.getIsAccountActive());
            skillOwnerEntity.get().setAccountStatus(ownerStatusUpdate.getIsAccountActive());
        } else {
            throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());
        }
        return ownerStatusUpdate;
    }

    @Override
    public Map<Integer, String> getAllPartnersNames() {
        List<SkillPartnerEntity> skillPartnerEntities = skillPartnerRepository.findAll();
        logger.info("SkillPartnerService || getAllPartnersNames || getting the partner Business names");
        if (!skillPartnerEntities.isEmpty()) {
            return skillPartnerEntities.stream()
                    .collect(Collectors.toMap(SkillPartnerEntity::getSkillPartnerId, SkillPartnerEntity::getBusinessName));
        } else {
            throw new ServiceException(NO_ACTIVE_PARTNERS.getErrorCode(), NO_ACTIVE_PARTNERS.getErrorDesc());
        }
    }

    @Override
    public Optional<SkillPartnerEntity> getSkillPartnerEntity(int partnerId) {
        return Optional.ofNullable(skillPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ServiceException(PARTNER_ID_NOT_FOUND.getErrorCode(), PARTNER_ID_NOT_FOUND.getErrorDesc())));
    }
}






