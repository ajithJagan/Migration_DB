package com.flexcub.resourceplanning.skillseekeradmin.service.impl;


import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.invoice.repository.InvoiceRepository;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.skillowner.dto.TimeSheetResponse;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseekeradmin.dto.PartnerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerAdmin;
import com.flexcub.resourceplanning.skillseekeradmin.dto.SeekerStatusUpdate;
import com.flexcub.resourceplanning.skillseekeradmin.service.SeekerAdminService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Component
public class SeekerAdminServiceImpl implements SeekerAdminService {

    Logger logger = LoggerFactory.getLogger(SeekerAdminServiceImpl.class);
    @Autowired
    SkillSeekerRepository skillSeekerRepository;

    @Autowired
    SkillPartnerRepository skillPartnerRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    OwnerTimeSheetRepository ownerTimeSheetRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ModelMapper modelMapper;





    /**
     * @return
     */
    @Override
    public List<SeekerAdmin> getSkillSeeker() {
        try {
            Optional<List<SkillSeekerEntity>> skillSeekerList = Optional.of(skillSeekerRepository.findAll());
            List<SeekerAdmin> seekerAdminList = new ArrayList<>();
            if (!skillSeekerList.get().isEmpty()) {
                for (SkillSeekerEntity skillSeeker : skillSeekerList.get()) {
                    SeekerAdmin seekerAdmin = new SeekerAdmin(skillSeeker.getId(), skillSeeker.getSkillSeekerName(), skillSeeker.getPrimaryContactFullName(), skillSeeker.getPhone(), skillSeeker.getEmail(), skillSeeker.getCity() + "," + skillSeeker.getState(), skillSeeker.getStatus());
                    seekerAdmin.setStartDate(skillSeeker.getCreatedAt());
                    seekerAdminList.add(seekerAdmin);
                }
                logger.info("SeekerAdminServiceImpl || getSkillSeeker || Getting the SkillSeeker Info");
                seekerAdminList.sort(Comparator.comparing(SeekerAdmin::getStartDate).reversed());
                return seekerAdminList;
            } else {
                throw new ServiceException(DATA_NOT_FOUND.getErrorCode());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_ADMIN_DATA.getErrorCode(), INVALID_ADMIN_DATA.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(EXPECTATION_FAILED.getErrorCode(), EXPECTATION_FAILED.getErrorDesc());
        }
    }

    @Override
    public SeekerStatusUpdate updateSeekerStatus(SeekerStatusUpdate seekerStatusUpdate) {
        Optional<RegistrationEntity> registration = registrationRepository.findById(seekerStatusUpdate.getSkillSeekerId());
        if (registration.isPresent()) {
            registration.get().setIsAccountActive(seekerStatusUpdate.getIsAccountActive());
            Optional<SkillSeekerEntity> skillSeekerEntity = skillSeekerRepository.findById(registration.get().getId());
            skillSeekerEntity.get().setIsActive(seekerStatusUpdate.getIsAccountActive());
            skillSeekerRepository.save(skillSeekerEntity.get());
            registrationRepository.save(registration.get());
        } else {
            throw new ServiceException(INVALID_SKILL_SEEKER_ID.getErrorCode(), INVALID_SKILL_SEEKER_ID.getErrorDesc());
        }

        logger.info("SeekerAdminServiceImpl || updateSeekerStatus || Updating the SkillSeeker Info");
        return seekerStatusUpdate;
    }


    @Override
    public List<TimeSheetResponse> getTimeSheets() {
        List<TimeSheetResponse> timeSheetsDto = new ArrayList<>();
        List<OwnerTimeSheetEntity> timeSheets = ownerTimeSheetRepository.findAll();
        if (!timeSheets.isEmpty()) timeSheets.forEach(timeSheet -> {
            TimeSheetResponse sheet = modelMapper.map(timeSheet, TimeSheetResponse.class);
            timeSheetsDto.add(sheet);
        });
        else {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
        logger.info("SeekerAdminServiceImpl || getTimeSheets || To get all TimeSheet Details");
        return timeSheetsDto;
    }

    @Override
    public List<PartnerAdmin> getAllSkillPartner() {
        try {
            List<SkillPartnerEntity> allPartners = skillPartnerRepository.findAll();
            List<PartnerAdmin> partnerAdmins = new ArrayList<>(allPartners.size());
            for (SkillPartnerEntity k : allPartners) {
                partnerAdmins.add(new PartnerAdmin(k.getSkillPartnerId(), k.getPrimaryContactFullName(), k.getBusinessName(), k.getPhone(), k.getBusinessEmail(), k.getState(), k.getServiceFeePercentage()));
            }
            logger.info("SeekerAdminServiceImpl || getAllSkillPartner || getAllSkillPartner called and fetched all partner data's");
            return partnerAdmins;
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
        }
    }


}



