package com.flexcub.resourceplanning.skillseekeradmin.service;


import com.flexcub.resourceplanning.invoice.repository.InvoiceDataRepository;
import com.flexcub.resourceplanning.invoice.repository.InvoiceRepository;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerStatusUpdate;
import com.flexcub.resourceplanning.skillseekeradmin.service.impl.SeekerAdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SeekerAdminServiceImpl.class)
class SeekerAdminServiceTest {

    @MockBean
    SkillSeekerRepository skillSeekerRepository;
    @MockBean
    InvoiceRepository invoiceRepository;

    @MockBean
    RegistrationRepository registrationRepository;

    @MockBean
    OwnerTimeSheetRepository ownerTimeSheetRepository;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    SkillPartnerRepository skillPartnerRepository;

    @Autowired
    SeekerAdminServiceImpl seekerAdminService;
    SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
    RegistrationEntity registration = new RegistrationEntity();
    SeekerAdmin seekerAdmin = new SeekerAdmin();
    SeekerStatusUpdate seekerStatusUpdate = new SeekerStatusUpdate();
    TimeSheetResponse timeSheetResponse = new TimeSheetResponse();

    OwnerTimeSheetEntity ownerTimeSheetEntity = new OwnerTimeSheetEntity();
    List<SkillSeekerEntity> seekerEntities = new ArrayList<>();
    List<TimeSheetResponse> timeSheetResponseList = new ArrayList<>();
    List<OwnerTimeSheetEntity> ownerTimeSheetEntityList = new ArrayList<>();
    java.util.Date date = java.sql.Date.valueOf(LocalDate.now());

    @BeforeEach
    void beforeTest() {
        skillSeeker.setId(1);
        skillSeeker.setSkillSeekerName("Hema");
        skillSeeker.setPhone("8825773502");
        skillSeeker.setEmail("kevinranig@gmail.com");
        skillSeeker.setPrimaryContactFullName("Godwin Kevin Raj G");
        skillSeeker.setStatus("Available");
        skillSeeker.setCity("Chicago");
        skillSeeker.setStartDate(date);
        skillSeeker.setEndDate(date);
        seekerEntities.add(skillSeeker);

        seekerAdmin.setId(1);
        seekerAdmin.setSkillSeekerName("Kevin");
        seekerAdmin.setPhone("8825773502");
        seekerAdmin.setEmail("kevinranig@gmail.com");
        seekerAdmin.setPrimaryContactFullName("raj");
        seekerAdmin.setLocation("Alabama");
        seekerAdmin.setStartDate(new Date());
        seekerAdmin.setEndDate(new Date());

        seekerStatusUpdate.setSkillSeekerId(1);
        seekerStatusUpdate.setIsAccountActive(true);

        timeSheetResponse.setTimeSheetId(1);
        timeSheetResponse.setSkillOwnerEntityId(1);
        timeSheetResponse.setSkillSeekerEntityId(1);
        timeSheetResponse.setSkillSeekerProjectEntityId(2);
        timeSheetResponse.setSkillSeekerTaskEntityId(1);
        timeSheetResponse.setTaskTitle("Infocus");
        timeSheetResponse.setTaskDescription("Infocus Implementation");
        timeSheetResponse.setStartDate(new java.sql.Date(2022, 12, 28));
        timeSheetResponse.setEndDate(new java.sql.Date(2022, 12, 30));
        timeSheetResponse.setApproved(true);
        timeSheetResponseList.add(timeSheetResponse);

        registration.setId(1);
        registration.setBusinessName("Business Name");
        registration.setFirstName("First");
        registration.setLastName("Last");
        registration.setEmailId("sukumarm121@gmail.com");
        registration.setPassword("1234567");
        registration.setToken("Xz1U3jQL6aZGFeQmjuL3");
        registration.setAccountStatus(false);
        registration.setMailStatus("Sent");
        registration.setTaxIdBusinessLicense("112233");
        registration.setContactPhone("9884104947");
        registration.setContactEmail("sukumarm121@gmail.com");
        registration.setBusinessPhone("9884108899");
        registration.setCity("city");
        registration.setState("state");
        registration.setDomainId(1);
        registration.setTechnologyIds("1");
        registration.setAddress("Address");
        registration.setExcelId("1");
        registration.setEmailId("sukumarm121@gmail.com");
        registration.setPassword("NKPwitKwMi6OIo4QbYSFpw==");
        registration.setIsAccountActive(true);

        ownerTimeSheetEntity.setTimeSheetId(1);
        ownerTimeSheetEntity.setSkillOwnerEntity(ownerTimeSheetEntity.getSkillOwnerEntity());
        ownerTimeSheetEntity.setSkillSeekerEntity(ownerTimeSheetEntity.getSkillSeekerEntity());
        ownerTimeSheetEntity.setSkillSeekerProjectEntity(ownerTimeSheetEntity.getSkillSeekerProjectEntity());
        ownerTimeSheetEntity.setSkillSeekerTaskEntity(ownerTimeSheetEntity.getSkillSeekerTaskEntity());
        ownerTimeSheetEntity.setStartDate(new java.sql.Date(2022, 12, 28));
        ownerTimeSheetEntity.setEndDate(new java.sql.Date(2022, 12, 30));
        ownerTimeSheetEntity.setTimesheetStatus("Available");
        ownerTimeSheetEntity.setInvoiceGenerated(true);
        ownerTimeSheetEntity.setHours("3:00");
        ownerTimeSheetEntityList.add(ownerTimeSheetEntity);
    }

    @Test
    void getSkillSeekerTest() {
        when(skillSeekerRepository.findAll()).thenReturn(seekerEntities);
        assertEquals(1, seekerAdminService.getSkillSeeker().size());
        assertNotNull(seekerAdminService.getSkillSeeker());
    }

    @Test
    void updateStatusTest() {
        Mockito.when(registrationRepository.findById(1)).thenReturn(Optional.ofNullable(registration));
        Mockito.when(registrationRepository.save(registration)).thenReturn(registration);
        Mockito.when(skillSeekerRepository.findById(1)).thenReturn(Optional.ofNullable(skillSeeker));
        Mockito.when(skillSeekerRepository.save(skillSeeker)).thenReturn(skillSeeker);
        assertEquals(skillSeeker.getIsActive(), seekerAdminService.updateSeekerStatus(seekerStatusUpdate).getIsAccountActive());
    }

    @Test
    void getTimeSheetTest() {
        Mockito.when(ownerTimeSheetRepository.findAll()).thenReturn(ownerTimeSheetEntityList);
        Mockito.when(modelMapper.map(ownerTimeSheetEntity, TimeSheetResponse.class)).thenReturn(timeSheetResponse);
        assertEquals(1, seekerAdminService.getTimeSheets().size());
    }

}
