package com.flexcub.resourceplanning.skillpartner.service;


import com.flexcub.resourceplanning.invoice.repository.InvoiceDataRepository;
import com.flexcub.resourceplanning.invoice.repository.InvoiceRepository;
import com.flexcub.resourceplanning.job.entity.Job;
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
import com.flexcub.resourceplanning.skillpartner.dto.*;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillpartner.service.impl.SkillPartnerServiceImpl;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = SkillPartnerServiceImpl.class)
class SkillPartnerServiceImplTest {

    @Autowired
    SkillPartnerServiceImpl skillPartnerService;
    @MockBean
    OwnerNotificationsRepository ownerNotificationsRepository;
    @MockBean
    RequirementPhaseRepository requirementPhaseRepository;
    @MockBean
    PoRepository poRepository;
    @MockBean
    PartnerNotificationsRepository partnerNotificationsRepository;
    @MockBean
    SelectionPhaseRepository selectionPhaseRepository;
    @MockBean
    SkillSeekerMsaRepository skillSeekerMsaRepository;
    @MockBean
    RegistrationRepository registrationRepository;
    @MockBean
    SkillPartnerRepository skillPartnerRepository;
    @MockBean
    SkillOwnerRepository skillOwnerRepository;
    @MockBean
    ModelMapper modelMapper;

    @MockBean
    InvoiceDataRepository invoiceDataRepository;
    @MockBean
    InvoiceRepository invoiceRepository;
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();

    List<SkillPartnerEntity> skillPartnerEntities = new ArrayList<SkillPartnerEntity>();
    RegistrationEntity registration = new RegistrationEntity();
    SkillPartner skillPartner = new SkillPartner();
    List<SkillOwnerRateCard> skillOwnerRateCards = new ArrayList<>();
    List<RequirementPhase> requirementPhases=new ArrayList<>();
    List<SelectionPhase> selectionPhaseList=new ArrayList<>();
    PoEntity poEntity=new PoEntity();
    OwnerStatusUpdate ownerStatusUpdate=new OwnerStatusUpdate();

    Job job=new Job();

    RateCardToSkillOwner rateCardToSkillOwner = new RateCardToSkillOwner();
    SkillOwnerRateCard skillOwnerRateCard = new SkillOwnerRateCard();

    RequirementPhase requirementPhase=new RequirementPhase();
    SelectionPhase selectionPhase=new SelectionPhase();
    Map<Integer,String> map=new HashMap<>();


    List<SkillOwnerEntity> skillOwnerEntities = new ArrayList<>();
    @BeforeEach
    public void setup() {
        skillPartnerEntity.setSkillPartnerId(1);
        skillPartnerEntity.setPhone("8825773502");
        skillPartnerEntity.setPrimaryContactFullName("kevin");
        skillPartnerEntity.setTaxIdBusinessLicense("Tam987");
        skillPartnerEntity.setBusinessEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setBusinessName("");
        skillPartnerEntity.setState("TamilNadu");
        skillPartnerEntity.setPrimaryContactEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setAddressLine1("123 - kev Street");
        skillPartnerEntity.setAddressLine2("134 - manu Street");
        skillPartnerEntity.setExcelId("1");
        skillPartnerEntity.setSecondaryContactEmail("godwin.g@qbrainx.com");
        skillPartnerEntity.setZipcode("638656");
        skillPartnerEntities.add(skillPartnerEntity);


        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setRateCard(skillOwnerRateCard.getRate());
        skillOwnerEntities.add(skillOwnerEntity);
        job.setJobId("FJB-00001");

        requirementPhase.setSkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());
        selectionPhase.setJob(job);
        selectionPhase.setRejectedOn(LocalDate.now());

        skillOwnerRateCard.setSkillOwnerId(1);
        skillOwnerRateCard.setRate(1);
        skillOwnerRateCards.add(skillOwnerRateCard);


        rateCardToSkillOwner.setSkillPartnerId(1);
        rateCardToSkillOwner.setSkillOwnerRateCards(skillOwnerRateCards);

        ownerStatusUpdate.setSkillOwnerId(1);
        ownerStatusUpdate.setIsAccountActive(true);
        poEntity.setSkillOwnerEntity(skillOwnerEntity);
        poEntity.setExpiryDate(new Date());

        map.put(skillPartnerEntity.getSkillPartnerId(),skillPartnerEntity.getBusinessName());
    }

    @Test
    void insertDataTest() {
        Mockito.when(modelMapper.map(skillPartner, SkillPartnerEntity.class)).thenReturn(skillPartnerEntity);
        skillPartnerRepository.save(skillPartnerEntity);
        Mockito.when(modelMapper.map(skillPartnerEntity, SkillPartner.class)).thenReturn(skillPartner);
        assertNotEquals(skillPartner.getSkillPartnerId(), skillPartnerService.insertData(skillPartner).getSkillPartnerId());
    }

    @Test
    void insertDataNullTest() {
        Mockito.when(modelMapper.map(skillPartner, SkillPartnerEntity.class)).thenReturn(skillPartnerEntity);
        skillPartnerRepository.save(skillPartnerEntity);
        Mockito.when(modelMapper.map(skillPartnerEntity, SkillPartner.class)).thenReturn(skillPartner);
        assertNotNull(skillPartner, skillPartnerService.insertData(skillPartner).getBusinessName());
    }

    @Test
    void getDataTest() {
        Mockito.when(skillPartnerRepository.findAll()).thenReturn(skillPartnerEntities);
        Mockito.when(modelMapper.map(skillPartnerEntities, SkillPartner.class)).thenReturn(skillPartner);
        assertEquals(1, skillPartnerService.getData().size());
        assertNotNull(skillPartnerService.getData());
    }

    @Test
    void getPartnerDetailsTest() {
        Optional<SkillPartnerEntity> partner = Optional.of(skillPartnerEntity);
        Mockito.when(skillPartnerRepository.findById(skillPartnerEntity.getSkillPartnerId())).thenReturn(Optional.of(skillPartnerEntity));
        Mockito.when(modelMapper.map(partner, SkillPartner.class)).thenReturn(skillPartner);
        assertEquals(skillPartner.getSkillPartnerId(), skillPartnerService.getPartnerDetails(skillPartnerEntity.getSkillPartnerId()).getSkillPartnerId());

    }


    @Test
    void updateDataTest() {

        Optional<SkillPartnerEntity> skillPartnerEntity1 = Optional.of(skillPartnerEntity);
        when(modelMapper.map(skillPartner, SkillPartnerEntity.class)).thenReturn(skillPartnerEntity);
        when(skillPartnerRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(skillPartnerEntity));
        when(modelMapper.map(skillPartnerEntity1, SkillPartner.class)).thenReturn(skillPartner);
        assertEquals(skillPartner.getSkillPartnerId(), skillPartnerService.updateData(skillPartner).getSkillPartnerId());
    }

    @Test
    void deleteDataTest() {

        Mockito.when(skillPartnerRepository.findById(skillPartnerEntity.getSkillPartnerId())).thenReturn(Optional.of(skillPartnerEntity));
        skillPartnerService.deleteData(skillPartnerEntity.getSkillPartnerId());
        Mockito.verify(skillPartnerRepository, times(1)).save(Mockito.any());

    }

    @Test
    void addEntryToSkillPartnerTest() {

        skillPartnerService.addEntryToSkillPartner(registration);
        Mockito.verify(skillPartnerRepository, times(1)).save(Mockito.any());
    }

    @Test
    void addRateCardTest() {

        Mockito.when(skillOwnerRepository.findBySkillOwnerEntityId(skillOwnerRateCard.getSkillOwnerId())).thenReturn(skillOwnerEntity);
        Mockito.when(skillOwnerRepository.saveAndFlush(Mockito.any())).thenReturn(skillOwnerEntity);
        assertEquals(1, skillPartnerService.addRateCard(rateCardToSkillOwner).size());
    }
    @Test
    void updateSkillOwnerStatus(){
        Mockito.when(registrationRepository.findById(1)).thenReturn(Optional.ofNullable(registration));
        Mockito.when(skillOwnerRepository.findById(1)).thenReturn(Optional.ofNullable(skillOwnerEntity));
        Mockito.when(requirementPhaseRepository.findBySkillOwnerId(1)).thenReturn(Optional.ofNullable(requirementPhases));
        Mockito.when(selectionPhaseRepository.findBySkillOwnerId(1)).thenReturn(Optional.ofNullable(selectionPhaseList));
        Mockito.when(poRepository.findByOwnerId(1)).thenReturn(Optional.ofNullable(poEntity));
        assertEquals(ownerStatusUpdate.getSkillOwnerId(),skillPartnerService.updateSKillOwnerStatus(ownerStatusUpdate).getSkillOwnerId());
    }
    @Test
    void getPartnersNames(){
        Mockito.when(skillPartnerRepository.findAll()).thenReturn(skillPartnerEntities);
        assertEquals(map.size(),skillPartnerService.getAllPartnersNames().size());

    }



}


