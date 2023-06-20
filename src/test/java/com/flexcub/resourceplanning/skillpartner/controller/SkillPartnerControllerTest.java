package com.flexcub.resourceplanning.skillpartner.controller;


import com.flexcub.resourceplanning.skillowner.dto.SkillOwnerDto;
import com.flexcub.resourceplanning.skillpartner.dto.*;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerFileDataService;
import com.flexcub.resourceplanning.skillpartner.service.SkillPartnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = SkillPartnerController.class)
class SkillPartnerControllerTest {

    @Autowired
    SkillPartnerController skillPartnerController;

    @MockBean
    SkillPartnerService skillPartnerService;

    @MockBean
    SkillPartnerFileDataService skillPartnerFileDataService;


    SkillPartner skillPartner = new SkillPartner();
    List<SkillPartner> skillPartners = new ArrayList<>();

    SkillOwnerRateCard skillOwnerRateCard = new SkillOwnerRateCard();

   List<SkillOwnerRateCard> skillOwnerRateCards =new ArrayList<>();

    RateCardToSkillOwner rateCardToSkillOwner = new RateCardToSkillOwner();
    OwnerStatusUpdate ownerStatusUpdate=new OwnerStatusUpdate();
    Map<Integer,String> map=new HashMap<>();


    @BeforeEach
    public void setup() {
        skillPartner.setSkillPartnerId(1);
        skillPartner.setPhone("8825773502");
        skillPartner.setPrimaryContactFullName("kevin");
        skillPartner.setTaxIdBusinessLicense("Tam987");
        skillPartner.setBusinessEmail("godwin.g@qbrainx.com");
        skillPartner.setBusinessName("kevin pvt ltd");
        skillPartner.setState("TamilNadu");
        skillPartner.setPrimaryContactEmail("godwin.g@qbrainx.com");
        skillPartner.setAddressLine1("123 - kev Street");
        skillPartner.setAddressLine2("134 - manu Street");
        skillPartner.setExcelId("1");
        skillPartner.setSecondaryContactEmail("godwin.g@qbrainx.com");
        skillPartner.setZipcode("638656");
        skillPartner.setPhone("9943611849");

    }

    @Test
    void insertSkillPartnerDetailsTest() {

        Mockito.when(skillPartnerService.insertData(skillPartner)).thenReturn(skillPartner);
        assertEquals(200, skillPartnerController.insertSkillPartnerDetails(skillPartner).getStatusCodeValue());
        assertEquals(skillPartnerController.insertSkillPartnerDetails(skillPartner).getBody().getSkillPartnerId(), skillPartner.getSkillPartnerId());
        assertNotNull(skillPartner.getAddressLine1());
    }

    @Test
    void insertSkillPartnerNullTest() {
        Mockito.when(skillPartnerService.insertData(skillPartner)).thenReturn(null);
        assertNull(skillPartnerController.insertSkillPartnerDetails(skillPartner).getBody());
    }


    @Test
    void updateSkillPartnerDetailsTest() {

        Mockito.when(skillPartnerService.updateData(skillPartner)).thenReturn(skillPartner);
        assertEquals(200, skillPartnerController.updateSkillPartnerDetails(skillPartner).getStatusCodeValue());
        assertThat(skillPartnerController.updateSkillPartnerDetails(skillPartner).getBody()).isEqualTo(skillPartner);
        assertNotNull(skillPartner.getBusinessName());
    }

    @Test
    void getSkillPartnerDetailsTest() {

        skillPartners.add(skillPartner);
        skillPartners.add(skillPartner);
        Mockito.when((skillPartnerService.getData())).thenReturn(skillPartners);
        Assertions.assertThat(skillPartnerController.getSkillPartnerDetails().getBody()).hasSize(2);
        assertEquals(200, skillPartnerController.getSkillPartnerDetails().getStatusCodeValue());

    }

    @Test
    void deleteDataTest() {

        skillPartnerController.deleteData(1);
        skillPartnerController.deleteData(2);
        Mockito.verify(skillPartnerService, times(2)).deleteData(Mockito.anyInt());

    }

    @Test
    void setDataInDbTest() {

        Mockito.when(skillPartnerFileDataService.readSkillPartnerExcelFile(1)).thenReturn("Saved");
        assertEquals(200, skillPartnerController.setDataInDb(1).getStatusCodeValue());
    }

    @Test
    void addRateCardTest() {

        skillOwnerRateCard.setSkillOwnerId(1);
        skillOwnerRateCard.setRate(100);
        rateCardToSkillOwner.setSkillOwnerRateCards(Collections.singletonList(skillOwnerRateCard));

        SkillOwnerDto skillOwner = new SkillOwnerDto();
        skillOwner.setRateCard(100);
        skillOwner.setSkillOwnerEntityId(1);

        Mockito.when(skillPartnerService.addRateCard(rateCardToSkillOwner)).thenReturn(skillOwnerRateCards);
        assertEquals(200, skillPartnerController.getSkillPartnerDetails().getStatusCodeValue());

    }

    @Test
    void addRateCardNullTest() {

        RateCardToSkillOwner rateCardToSkillOwner = new RateCardToSkillOwner();
        SkillOwnerRateCard skillOwnerRateCard = new SkillOwnerRateCard();
        skillOwnerRateCard.setSkillOwnerId(1);
        skillOwnerRateCard.setRate(100);
        rateCardToSkillOwner.setSkillOwnerRateCards(Collections.singletonList(skillOwnerRateCard));


        Mockito.when(skillPartnerService.addRateCard(rateCardToSkillOwner)).thenReturn(null);
        assertNull(skillPartnerController.addRateCard(rateCardToSkillOwner).getBody());

    }
    @Test
    void ownerStatusUpdateTest(){
        Mockito.when(skillPartnerService.updateSKillOwnerStatus(ownerStatusUpdate)).thenReturn(ownerStatusUpdate);
        assertEquals(200, skillPartnerController.skillOwnerStatusUpdate(ownerStatusUpdate).getStatusCodeValue());

    }
    @Test
    void getAllPartners(){
        Mockito.when(skillPartnerService.getAllPartnersNames()).thenReturn(map);
        assertEquals(200, skillPartnerController.getAllPartners().getStatusCodeValue());
    }

}
