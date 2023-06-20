package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.contracts.dto.SeekerMSADetails;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.registration.dto.Registration;
import com.flexcub.resourceplanning.registration.entity.RegistrationEntity;
import com.flexcub.resourceplanning.registration.entity.Roles;
import com.flexcub.resourceplanning.registration.repository.RegistrationRepository;
import com.flexcub.resourceplanning.registration.service.RegistrationService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillStatusEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillDomainRepository;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillStatusRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.SkillOwnerService;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.dto.*;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerAccessEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SeekerModulesEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerMSAEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerTaskEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SubRoles;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SeekerAccessRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SeekerModuleRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerMsaRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerTaskRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SubRolesRepository;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerService;
import com.flexcub.resourceplanning.skillseeker.service.SkillSeekerTaskService;
import com.flexcub.resourceplanning.skillseeker.service.StatementOfWorkService;
import com.flexcub.resourceplanning.skillseekeradmin.dto.Task;
import com.flexcub.resourceplanning.template.repository.TemplateRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.NO_VALID_PO_OR_SOW;

@Service
public class SkillSeekerServiceImpl implements SkillSeekerService {

    @Autowired
    OwnerSkillStatusRepository ownerSkillStatusRepository;

    @Autowired
    StatementOfWorkService statementOfWorkService;

    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;
    @Autowired
    SkillOwnerService skillOwnerService;

    @Autowired
    SkillSeekerTaskService skillSeekerTaskService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SkillSeekerRepository seekerRepo;

    @Autowired
    RegistrationRepository repositoryRepo;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    SeekerAccessRepository seekerAccessRepository;
    @Autowired
    SeekerModuleRepository seekerModuleRepository;
    @Autowired
    SubRolesRepository subRolesRepository;
    @Autowired
    PoRepository repository;
    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    @Autowired
    StatementOfWorkRepository statementOfWorkRepository;
    @Autowired
    SkillSeekerMsaRepository skillSeekerMsaRepository;
    @Autowired
    SkillSeekerRepository skillSeekerRepository;
    @Autowired
    PoRepository poRepository;
    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;

    @Autowired
    SkillSeekerTaskRepository skillSeekerTaskRepository;

    @Autowired
    SkillPartnerRepository skillPartnerRepository;


    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    OwnerSkillDomainRepository ownerSkillDomainRepository;

    Logger logger = LoggerFactory.getLogger(SkillSeekerServiceImpl.class);
    @Value("${owner.poReleased}")
    private String poReleased;
    @Value("${owner.sowReleased}")
    private String sowReleased;


    @Value("${flexcub.baseurl}")
    private String baseURL;

    /**
     * This method is to insert skillSeeker details.
     *
     * @param skillSeeker data
     * @return It returns the inserted data of skillSeeker.
     */
    @Override
    public SkillSeeker addClientDetails(SkillSeeker skillSeeker) {
        boolean isActive = skillSeeker.isActive();

        SkillSeekerEntity skillSeekerEntity = modelMapper.map(skillSeeker, SkillSeekerEntity.class);
        if (isActive) {
            if (null == skillSeeker.getPrimaryContactFullName()) {
                throw new ServiceException(INVALID_NAME.getErrorCode(), INVALID_NAME.getErrorDesc());
            }
            Registration registration = registrationService.insertDetails(seekerToRegistration(skillSeeker));
            skillSeekerEntity.setId(registration.getId());
            skillSeekerEntity.setTaxIdBusinessLicense(registration.getTaxIdBusinessLicense());

            Optional<SkillSeekerEntity> skillSeekerEntity1 = skillSeekerRepository.findById(registration.getId());
            skillSeekerEntity.setSubRoles(skillSeekerEntity1.get().getSubRoles());
            skillSeeker.setId(skillSeekerEntity1.get().getId());
            seekerSave(skillSeekerEntity);
            logger.info("SkillSeekerServiceImpl || add Client Data by SA || client account is active and email is send");


        } else {
            if (null == skillSeeker.getPrimaryContactFullName()) {
                throw new ServiceException(INVALID_NAME.getErrorCode(), INVALID_NAME.getErrorDesc());
            }
            Registration registration = registrationService.insertDetails(seekerToRegistration(skillSeeker));
            skillSeekerEntity.setId(registration.getId());
            skillSeekerEntity.setTaxIdBusinessLicense(registration.getTaxIdBusinessLicense());
            Optional<SkillSeekerEntity> skillSeekerEntity1 = skillSeekerRepository.findById(registration.getId());
            skillSeekerEntity.setSubRoles(skillSeekerEntity1.get().getSubRoles());
            skillSeeker.setId(skillSeekerEntity1.get().getId());
            seekerSave(skillSeekerEntity);
            logger.info("SkillSeekerServiceImpl || add Client Data by SA || client account is inactive and email will not be send");

        }

        return skillSeeker;
    }

    public void seekerSave(SkillSeekerEntity skillSeekerEntity) {
        try {
            seekerRepo.save(skillSeekerEntity);
        } catch (ServiceException e) {
            throw new ServiceException(UNABLE_SAVE_SEEKER_DATA.getErrorCode(), UNABLE_SAVE_SEEKER_DATA.getErrorDesc());
        }
    }


    /**
     * @param skillSeeker id
     * @return dto
     */
    @Override
    public SkillSeeker updateClientDetails(SkillSeeker skillSeeker) {
        try {
            Optional<SkillSeekerEntity> skillSeekerData1 = seekerRepo.findById(skillSeeker.getId());
            Optional<RegistrationEntity> registration1 = registrationRepository.findById(skillSeeker.getId());
//            skillSeeker.setRegistrationAccess(registration1.get().isAccountStatus());
            if (registration1.get().isAccountStatus()) {
                skillSeeker.setEmail(registration1.get().getEmailId()); // cant change email
            }

            SkillSeekerEntity skillSeekerEntity = modelMapper.map(skillSeeker, SkillSeekerEntity.class);

            skillSeekerEntity.setOwnerSkillDomainEntity(skillSeekerEntity.getOwnerSkillDomainEntity());
            Optional<SkillSeekerEntity> skillSeekerData = getSkillSeekerEntity(skillSeekerEntity.getId());
            Optional<RegistrationEntity> registration = registrationRepository.findById(skillSeekerEntity.getId());
            if (skillSeekerData.isPresent() && registration.isPresent()) {
                BeanUtils.copyProperties(skillSeeker, skillSeekerData.get(), NullPropertyName.getNullPropertyNames(skillSeeker));
                BeanUtils.copyProperties(skillSeeker, registration.get(), NullPropertyName.getNullPropertyNames(skillSeeker));

                String primaryContactFullName = skillSeekerData.get().getPrimaryContactFullName().trim();
                String[] split = primaryContactFullName.split(" ");
                registration.get().setFirstName(split[0]);
                String lastName = "";
                for (int j = 1; j < split.length; j++) {
                    lastName += split[j];
                    if (j < split.length - 1) {
                        lastName += " ";
                    }
                }
                registration.get().setLastName(lastName);
                registration.get().setBusinessName(skillSeekerData.get().getSkillSeekerName());
                skillSeekerData.get().setAddedByAdmin(true);

                if (!registration1.get().isAccountStatus()) {
                    registration.get().setEmailId(skillSeekerData.get().getEmail());
                    registrationService.sendRegistrationConfirmationEmail(registration.get());
                }
                skillSeeker.setRegistrationAccess(registration1.get().isAccountStatus());

                saveOptionalSeeker(Optional.of(skillSeekerData.get()));
                registrationRepository.save(registration.get());

                logger.info("SkillSeekerServiceImpl || updateData || Updating the SkillSeeker basic details");
                SkillSeeker seeker = modelMapper.map(skillSeekerData, SkillSeeker.class);
                seeker.setRegistrationAccess(skillSeeker.isRegistrationAccess());
                return seeker;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_ROLE_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
        }
    }

    public void saveOptionalSeeker(Optional<SkillSeekerEntity> skillSeekerEntity) {
        skillSeekerEntity.ifPresentOrElse(skillSeekerRepository::save, () -> {
            throw new ServiceException(UNABLE_SAVE_SEEKER_DATA.getErrorCode(), UNABLE_SAVE_SEEKER_DATA.getErrorDesc());
        });
    }

    @Override
    @Transactional
    public List<SkillSeekerEntity> getAllSeeker() {
        try {
            return skillSeekerRepository.findAll();
        } catch (ServiceException e) {
            throw new ServiceException(UNABLE_TO_RETRIEVE_SEEKER.getErrorCode(), UNABLE_TO_RETRIEVE_SEEKER.getErrorDesc());
        }
    }

    @Override
    public Optional<SkillSeekerEntity> getById(int seekerId) {
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(seekerId);
        return skillSeeker;
    }

    public Optional<SkillSeekerEntity> getSkillSeekerEntity(int seekerId) {
        return Optional.ofNullable(skillSeekerRepository.findById(seekerId)
                .orElseThrow(() -> new ServiceException(SEEKER_NOT_FOUND.getErrorCode(), SEEKER_NOT_FOUND.getErrorDesc())));
    }

    /**
     * This method is to delete skillSeeker data based on id.
     *
     * @param id of skillSeeker
     */
    @Override
    public void deleteData(int id) {
        try {
            Optional<SkillSeekerEntity> skillSeeker = seekerRepo.findById(id);
            if (skillSeeker.isPresent()) {
                logger.info("SkillSeekerServiceImpl || deleteData || Deleting the SkillSeeker id: {}", id);
                seekerRepo.deleteById(id);
            } else {
                throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
        }
    }

    /**
     * @param id seekerid
     * @return dto
     */
    @Override
    public SkillSeeker getSeekerData(int id) {
        try {
            Optional<SkillSeekerEntity> skillSeeker = seekerRepo.findById(id);
            Optional<RegistrationEntity> registration = repositoryRepo.findById(skillSeeker.get().getId());

            if (skillSeeker.isPresent()) {
                SkillSeeker seekerDto = new SkillSeeker();
                logger.info("SkillSeekerServiceImpl || getSeekerData || Updating the SkillSeeker Info");
                BeanUtils.copyProperties(skillSeeker.get(), seekerDto);
                seekerDto.setModuleAccess(getAccessDetails(id));
                seekerDto.setRegistrationAccess(seekerDto.isRegistrationAccess());
                seekerDto.setRegistrationAccess(registration.get().isAccountStatus());
                skillSeeker.get().setRegistrationAccess(registration.get().isAccountStatus());
                seekerRepo.save(skillSeeker.get());
                SkillSeeker skillSeeker1 = modelMapper.map(skillSeeker.get(), SkillSeeker.class);
                skillSeeker1.setRegistrationAccess(registration.get().isAccountStatus());
                return skillSeeker1;

            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_ROLE_ID.getErrorCode(), INVALID_ROLE_ID.getErrorDesc());
        }
    }

    /**
     * This method is to update skillSeeker details.
     *
     * @param skillSeekerInfo object
     * @return It returns the updated data of skillSeeker.
     */
    @Override
    public SkillSeeker updateData(SkillSeeker skillSeekerInfo) {
        SkillSeekerEntity skillSeekerEntity = modelMapper.map(skillSeekerInfo, SkillSeekerEntity.class);
        try {
            Optional<SkillSeekerEntity> skillSeeker = getSkillSeekerEntity(skillSeekerEntity.getId());
            if (skillSeeker.isPresent()) {
                logger.info("SkillSeekerServiceImpl || updateData || Updating the SkillSeeker Info");
                BeanUtils.copyProperties(skillSeekerEntity, skillSeeker.get(), NullPropertyName.getNullPropertyNames(skillSeekerEntity));
                seekerRepo.save(skillSeeker.get());
                return modelMapper.map(skillSeeker, SkillSeeker.class);
            } else {
                throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorCode());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_SAVED.getErrorCode(), DATA_NOT_SAVED.getErrorDesc());
        }
    }

    @Override
    public void addEntryToSkillSeeker(RegistrationEntity registration) {

        SkillSeekerEntity skillSeeker = new SkillSeekerEntity();
        skillSeeker.setId(registration.getId());
        skillSeeker.setSkillSeekerName(registration.getBusinessName());
        skillSeeker.setPrimaryContactEmail(registration.getContactEmail());
        skillSeeker.setEmail(registration.getEmailId());
        skillSeeker.setCity(registration.getCity());
        skillSeeker.setState(registration.getState());
        skillSeeker.setPhone(registration.getContactPhone());
        skillSeeker.setTaxIdBusinessLicense(registration.getTaxIdBusinessLicense());
        skillSeeker.setOwnerSkillDomainEntity(ownerSkillDomainRepository.findById(registration.getDomainId()).get());

        Optional<List<SkillSeekerEntity>> byTaxIdBusinessLicense = seekerRepo.findByTaxIdBusinessLicense(registration.getTaxIdBusinessLicense());
        if (byTaxIdBusinessLicense.isPresent() && byTaxIdBusinessLicense.get().isEmpty()) {
            SubRoles subRoles = new SubRoles();
            subRoles.setId(1);
            skillSeeker.setSubRoles(subRoles);

            SubRole subRole = modelMapper.map(subRoles, SubRole.class);
            subRole.setRoleId(subRoles.getId());
            subRole.setTaxId(skillSeeker.getTaxIdBusinessLicense());
            subRole.setActive(true);

            List<Integer> integers = new ArrayList<>();

            List<SeekerModulesEntity> seekerModulesEntityList = seekerModuleRepository.findAll();
            seekerModulesEntityList.forEach(seekerModulesEntity -> integers.add(seekerModulesEntity.getId()));
            subRole.setModuleId(integers);
            addSubRole(subRole);

        }

        if (null != registration.getLastName()) {
            skillSeeker.setPrimaryContactFullName(registration.getFirstName() + " " + registration.getLastName());
        } else {
            skillSeeker.setPrimaryContactFullName(registration.getFirstName());
        }
        seekerSave(skillSeeker);
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate() throws FileNotFoundException {
        String filename = "src/main/resources/templates/SkillSeekerAgreement.docx";
        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = SkillSeekerAgreement.docx");
        logger.info("SkillSeekerServiceImpl || downloadTemplate || download Template For SkillSeeker");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM) // VERIFY
                .body(resource);

    }

    private RegistrationEntity seekerToRegistration(SkillSeeker skillSeeker) {


        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setEmailId(skillSeeker.getEmail());
        registrationEntity.setContactPhone(skillSeeker.getPhone());
        registrationEntity.setCity(skillSeeker.getCity());
        registrationEntity.setState(skillSeeker.getState());
        registrationEntity.setBusinessName(skillSeeker.getSkillSeekerName());
        registrationEntity.setFirstName(skillSeeker.getPrimaryContactFullName());
        registrationEntity.setTaxIdBusinessLicense(skillSeeker.getTaxIdBusinessLicense());
        registrationEntity.setDomainId(skillSeeker.getOwnerSkillDomainEntity().getDomainId());
        Roles roles = new Roles();
        roles.setRolesId(1L);
        registrationEntity.setRoles(roles);
        registrationEntity.setIsAccountActive(skillSeeker.isActive());
        registrationEntity.setDomainId(skillSeeker.getOwnerSkillDomainEntity().getDomainId());

        return registrationEntity;
    }

    public SkillSeeker addSeekerSubRoles(int skillSeeker, int role) {
        Optional<SkillSeekerEntity> skillSeekerEntity = seekerRepo.findById(skillSeeker);
        Optional<SubRoles> roles = subRolesRepository.findById(role);
        if (skillSeekerEntity.isPresent() && roles.isPresent()) {
            skillSeekerEntity.get().setSubRoles(roles.get());
            seekerRepo.save(skillSeekerEntity.get());
            logger.info("SkillSeekerServiceImpl || addSeekerSubRoles || adding The SubRoles");
            return modelMapper.map(skillSeekerEntity, SkillSeeker.class);
        } else {
            throw new ServiceException(INVALID_SEEKER_ID.getErrorCode(), INVALID_SEEKER_ID.getErrorDesc());
        }
    }

    public List<SkillSeeker> getSkillSeeker(String taxId) {
        try {
            Optional<List<SkillSeekerEntity>> skillSeeker = seekerRepo.findByTaxIdBusinessLicense(taxId);
            if (skillSeeker.isPresent() && !skillSeeker.get().isEmpty()) {
                List<SkillSeeker> seekerList = new ArrayList<>();
                for (SkillSeekerEntity skillSeekerEntity : skillSeeker.get()) {
                    SkillSeeker seeker = new SkillSeeker();
                    modelMapper.map(skillSeekerEntity, seeker);
                    seeker.setSkillSeekerName(skillSeekerEntity.getPrimaryContactFullName());
                    seeker.setModuleAccess(getAccessDetails(skillSeekerEntity.getId()));
                    seekerList.add(seeker);
                }
                logger.info("SkillSeekerServiceImpl || getSkillSeeker || Getting Skill Seeker By TaxId");
                return seekerList;
            } else {
                throw new ServiceException(INVALID_TAX_ID.getErrorCode(), INVALID_TAX_ID.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw new ServiceException(INVALID_TAX_ID.getErrorCode(), INVALID_TAX_ID.getErrorDesc());

        }
    }

    /**
     * @param subRole role
     * @return dto
     */
    @Override
    public List<SeekerAccess> addSubRole(SubRole subRole) {
        try {
            List<SeekerAccess> seekerAccessDtoList = new ArrayList<>();
            Optional<SubRoles> roles = subRolesRepository.findById(subRole.getRoleId());
            Optional<List<SeekerAccessEntity>> accessEntities = Optional.ofNullable(seekerAccessRepository.findByTaxIdAndSubRole(subRole.getTaxId(), subRole.getRoleId()));
            if (accessEntities.isPresent() && !accessEntities.get().isEmpty()) {
                for (SeekerAccessEntity access : accessEntities.get()) {
                    seekerAccessRepository.delete(access);
                }
            }
            for (Integer module : subRole.getModuleId()) {
                Optional<SeekerModulesEntity> seekerModules = seekerModuleRepository.findById(module);
                if (roles.isPresent() && seekerModules.isPresent()) {
                    SeekerAccessEntity seekerAccessEntity = new SeekerAccessEntity();
                    SeekerAccess seekerAccessDto = new SeekerAccess();
                    seekerAccessEntity.setSubRoles(roles.get());
                    seekerAccessEntity.setSeekerModulesEntity(seekerModules.get());
                    seekerAccessEntity.setTaxIdBusinessLicense(subRole.getTaxId());
                    seekerAccessEntity.setActive(subRole.isActive());
                    SeekerAccessEntity seekerAccess = seekerAccessRepository.save(seekerAccessEntity);
                    modelMapper.map(seekerAccess, seekerAccessDto);
                    seekerAccessDtoList.add(seekerAccessDto);
                } else {
                    throw new ServiceException();
                }
            }
            logger.info("SkillSeekerServiceImpl || addSubRole || adding The SubRoles");
            return seekerAccessDtoList;
        } catch (Exception e) {
            throw new ServiceException(INVALID_ROLE_ID.getErrorCode(), INVALID_ROLE_ID.getErrorDesc());
        }
    }

    /**
     * @ return list of module
     */
    @Override
    public List<SeekerModulesEntity> getModules() {
        Optional<List<SeekerModulesEntity>> modulesEntities = Optional.of(seekerModuleRepository.findAll());
        logger.info("SkillSeekerServiceImpl || getModules || getting The Modules");
        return modulesEntities.orElse(Collections.emptyList());
    }

    /**
     * @return the list of subroles user has
     */
    @Override
    public List<SubRoles> getRoles() {
        Optional<List<SubRoles>> roles = Optional.of(subRolesRepository.findAll());
        if (!roles.get().isEmpty()) {
            logger.info("SkillSeekerServiceImpl || getRoles || getting The Roles");
            return roles.get();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * @param seekerId id
     * @return dto
     */
    @Override
    public List<SeekerModulesEntity> getAccessDetails(int seekerId) {
        try {
            Optional<SkillSeekerEntity> skillSeeker = seekerRepo.findById(seekerId);
            if (skillSeeker.isPresent()) {
                List<SeekerModulesEntity> accessList = new ArrayList<>();
                if (null == skillSeeker.get().getSubRoles()) {
                    return Collections.emptyList();
                }
                if (skillSeeker.get().getSubRoles().getId() == 1) {
                    return seekerModuleRepository.findAll();
                }
                if (null != skillSeeker.get().getTaxIdBusinessLicense() || null != skillSeeker.get().getSubRoles()) {
                    Optional<List<SeekerAccessEntity>> seekerAccessEntity = Optional.ofNullable(seekerAccessRepository.findByTaxIdAndSubRole(skillSeeker.get().getTaxIdBusinessLicense(), skillSeeker.get().getSubRoles().getId()));
                    if (seekerAccessEntity.isPresent() && !seekerAccessEntity.get().isEmpty()) {
                        for (SeekerAccessEntity seekerAccess : seekerAccessEntity.get()) {
                            accessList.add(seekerAccess.getSeekerModulesEntity());
                        }
                        return accessList;
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return Collections.emptyList();
                }
            } else {
                throw new NullPointerException();
            }
        } catch (ServiceException e) {
            throw new ServiceException(ROLE_NOT_DEFINED.getErrorCode(), ROLE_NOT_DEFINED.getErrorDesc());
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_ROLE_ID.getErrorCode(), INVALID_ROLE_ID.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    /**
     * @param taxId tax id of business
     * @return list of access user has
     */
    @Override
    public List<SeekerRoleListing> getAccessByTaxId(String taxId) {

        Optional<List<SeekerAccessEntity>> seekerAccessEntity = Optional.ofNullable(seekerAccessRepository.findByTaxId(taxId));
        if (seekerAccessEntity.isPresent()) {
            List<SeekerRoleListing> accessList = new ArrayList<>();
            for (int i = 2; i <= getModules().size(); i++) {
                int finalI = i;
                List<SeekerAccessEntity> result = seekerAccessEntity.get().stream().filter(item -> item.getSubRoles().getId() == finalI).collect(Collectors.toList());
                if (!result.isEmpty()) {
                    SeekerRoleListing roleListing = new SeekerRoleListing();
                    roleListing.setRoleName(result.get(0).getSubRoles().getSubRoleDescription());
                    roleListing.setStatus(result.get(0).isActive());
                    roleListing.setRoleId("RID-0" + i);
                    List<SeekerModulesEntity> modulesEntities = new ArrayList<>();
                    for (SeekerAccessEntity seekerAccess : result) {
                        modulesEntities.add(seekerAccess.getSeekerModulesEntity());
                    }
                    roleListing.setAccessList(modulesEntities);
                    accessList.add(roleListing);
                }
            }
            logger.info("SkillSeekerServiceImpl || getAccessByTaxId || access By TaxId");
            return accessList;
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public List<Contracts> getContractDetails(int seekerId) {
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(seekerId);
        if (skillSeeker.isPresent()) {
            Optional<List<SkillSeekerEntity>> byTaxIdBusinessLicense = skillSeekerRepository.findByTaxIdBusinessLicense(skillSeeker.get().getTaxIdBusinessLicense());
            List<Contracts> contractsList = new ArrayList<>();
            if (byTaxIdBusinessLicense.isPresent() && !byTaxIdBusinessLicense.get().isEmpty()) {
                byTaxIdBusinessLicense.get().forEach(skillSeekerEntity -> {
                    Optional<List<SkillSeekerMSAEntity>> msaEntity = Optional.ofNullable(skillSeekerMsaRepository.findBySeekerId(skillSeekerEntity.getId()));
                    if (msaEntity.isPresent()) {
                        msaEntity.get().forEach(skillSeekerMSAEntity -> contractsList.add(skillOwnerService.ownerContractDetails(skillSeekerMSAEntity)));
                    } else {
                        throw new ServiceException(MSA_ID_NOT_FOUND.getErrorCode(), MSA_ID_NOT_FOUND.getErrorDesc());
                    }
                });
            } else {
                throw new ServiceException(TAX_ID_NOT_FOUND.getErrorCode(), TAX_ID_NOT_FOUND.getErrorDesc());
            }
            logger.info("SkillSeekerServiceImpl || getContractDetails || MSA | PO | SOW - In-writing or Released status called");
            return contractsList;
        } else {
            throw new ServiceException(INVALID_SEEKER_DATA.getErrorCode(), INVALID_SEEKER_DATA.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public List<ContractDetails> getListsOfContractDetails(int ownerId) {

        try {
            Optional<SelectionPhase> bySkillOwnerId = selectionPhaseRepository.findByOwnerId(ownerId);
            List<ContractDetails> contractDetails1 = new ArrayList<>();
            if (bySkillOwnerId.isPresent()) {
                Optional<PoEntity> poEntity = repository.findBySkillOwnerIdAndJobId(ownerId,bySkillOwnerId.get().getJob().getJobId());
                Optional<SkillSeekerMSAEntity> msaEntity = skillSeekerMsaRepository.findByJobIdAndSkillOwnerId(bySkillOwnerId.get().getJob().getJobId(), ownerId);
                Optional<StatementOfWorkEntity> sowEntity = statementOfWorkRepository.findByJobIdAndSkillOwnerId(bySkillOwnerId.get().getJob().getJobId(), ownerId);
                Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(ownerId);

                List<RequirementPhases> requirementPhases = new ArrayList<>();
                ContractDetails contractDetails = new ContractDetails();
                contractDetails.setCurrentStage(0);
                contractDetails.setJobId(msaEntity.get().getJobId().getJobId());

                bySkillOwnerId.get().getRequirementPhase().forEach(requirementPhase -> {

                    RequirementPhases requirementPhaseDto = new RequirementPhases();

                    requirementPhaseDto.setRequirementPhaseName(requirementPhase.getRequirementPhaseName());
                    requirementPhaseDto.setStage(requirementPhase.getStage());
                    requirementPhaseDto.setInterviewDate(requirementPhase.getDateOfInterview());

                    requirementPhases.add(requirementPhaseDto);
                    contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                    logger.info("SkillSeekerServiceImpl || getListsOfContractDetails || requirement phase list added ");
                });
                contractDetails.setPosition(bySkillOwnerId.get().getJob().getJobTitle());
                String years = bySkillOwnerId.get().getSkillOwnerEntity().getExpYears() == 1 ? bySkillOwnerId.get().getSkillOwnerEntity().getExpYears() + " Year, " : bySkillOwnerId.get().getSkillOwnerEntity().getExpYears() + " Years, ";
                String month = bySkillOwnerId.get().getSkillOwnerEntity().getExpMonths() == 1 ? bySkillOwnerId.get().getSkillOwnerEntity().getExpMonths() + " Month" : bySkillOwnerId.get().getSkillOwnerEntity().getExpMonths() + " Months";
                contractDetails.setExperience(years + month);
                contractDetails.setNameOfOwner(bySkillOwnerId.get().getSkillOwnerEntity().getFirstName() + " " + bySkillOwnerId.get().getSkillOwnerEntity().getLastName());

                if (msaEntity.isPresent()) {
                    contractDetails.setIsMsaCreated(true);
                    contractDetails.setMsaId(msaEntity.get().getId());
                    contractDetails.setMsaStatus("MSA " + msaEntity.get().getMsaStatus().getStatus());
                    RequirementPhases requirementPhaseDto = new RequirementPhases();
                    requirementPhaseDto.setRequirementPhaseName("MSA Contract Writing [" + msaEntity.get().getMsaStatus().getStatus() + "]");
                    requirementPhaseDto.setStage(requirementPhases.size() + 1);
                    if (null != msaEntity.get().getDateSigned()) {
                        requirementPhaseDto.setInterviewDate(msaEntity.get().getDateSigned());
                    }
                    contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                    requirementPhases.add(requirementPhaseDto);
                    logger.info("SkillSeekerServiceImpl || getListsOfContractDetails || MSA current stage is called ");
                }

                if (sowEntity.isPresent()) {
                    contractDetails.setIsSowCreated(true);
                    contractDetails.setSowId(sowEntity.get().getId());
                    contractDetails.setSowStatus("SOW " + sowEntity.get().getSowStatus().getStatus());
                    RequirementPhases requirementPhaseDto = new RequirementPhases();
                    requirementPhaseDto.setRequirementPhaseName("SOW Contract Writing [" + sowEntity.get().getSowStatus().getStatus() + "]");
                    requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                    if (null != sowEntity.get().getDateOfRelease()) {
                        requirementPhaseDto.setInterviewDate(sowEntity.get().getDateOfRelease());
                    }
                    contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                    requirementPhases.add(requirementPhaseDto);
                    logger.info("SkillSeekerServiceImpl || getListsOfContractDetails || SOW current stage is called ");
                }

                if (poEntity.isPresent()) {
                    contractDetails.setIsPoCreated(true);
                    contractDetails.setPoId(poEntity.get().getId());
                    contractDetails.setPoStatus("PO " + poEntity.get().getPoStatus().getStatus());
                    RequirementPhases requirementPhaseDto = new RequirementPhases();
                    requirementPhaseDto.setRequirementPhaseName("PO Contract Writing [" + poEntity.get().getPoStatus().getStatus() + "]");
                    requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                    if (null != poEntity.get().getDateOfRelease()) {
                        requirementPhaseDto.setInterviewDate(poEntity.get().getDateOfRelease().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                    requirementPhases.add(requirementPhaseDto);
                    logger.info("SkillSeekerServiceImpl || getListsOfContractDetails || PO current stage is called ");
                }
                if (null != skillOwnerEntity.get().getOnBoardingDate()) {
                    RequirementPhases requirementPhaseDto = new RequirementPhases();
                    requirementPhaseDto.setRequirementPhaseName("Final Release");
                    requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                    requirementPhaseDto.setInterviewDate(skillOwnerEntity.get().getOnBoardingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                    requirementPhases.add(requirementPhaseDto);
                }
                requirementPhases.sort(Comparator.comparingInt(RequirementPhases::getStage));
                contractDetails.setRequirementPhaseList(requirementPhases);
                contractDetails1.add(contractDetails);
            } else {
                throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
            }
            logger.info("SkillSeekerServiceImpl || getListsOfContractDetails || list of contracts-owner's details  Called");

            return contractDetails1;

        } catch (Exception e) {
            throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
        }
    }

    @Transactional
    @Override
    public OnBoarding onBoardingSkillOwner(OnboardingRequest onBoarding) {

        OnBoarding boarding = new OnBoarding();
        Optional<PoEntity> poEntity = poRepository.findByJobId(onBoarding.getJobId());
        Optional<StatementOfWorkEntity> statementOfWork = statementOfWorkRepository.findByJob(onBoarding.getJobId());
        Optional<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.findById(onBoarding.getSkillOwnerEntityId());
        Optional<SelectionPhase> skillAndJob = selectionPhaseRepository.findByJobIdAndSkillOwnerId(onBoarding.getJobId(), skillOwnerEntity.get().getSkillOwnerEntityId());

        if (skillAndJob.isPresent()) {
            Job job = skillAndJob.get().getJob();
            if (!poEntity.isPresent() && !statementOfWork.isPresent()) {
                throw new ServiceException(NO_VALID_PO_OR_SOW.getErrorCode(), NO_VALID_PO_OR_SOW.getErrorDesc());
            }else {

                if (poEntity.isPresent() && (job.getJobId() == poEntity.get().getJobId().getJobId()) && (poEntity.get().getPoStatus().getId() == 27)) {
                    // PO condition is true
                    LocalDate startDate = onBoarding.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate approvedDate = poEntity.get().getContractStartDate();

                    if (startDate.isBefore(approvedDate.plusDays(14))) {
                        // On-boarding is within 14 days from the approved date
                        skillOwnerEntity.get().setStartDate(onBoarding.getStartDate());
                        skillOwnerEntity.get().setEndDate(onBoarding.getEndDate());
                        skillOwnerEntity.get().setOnBoardingDate(Date.valueOf(LocalDate.now()));

                        Optional<OwnerSkillStatusEntity> hiring = ownerSkillStatusRepository.findById(4);
                        if (hiring.isPresent()) {
                            skillOwnerEntity.get().setOwnerSkillStatusEntity(hiring.get());
                        }

                        skillOwnerRepository.save(skillOwnerEntity.get());
                        poEntity.get().setOnBoarding(onBoarding.getStartDate());


                        if (poEntity.get().getSkillSeekerProject() != null) {
                            boarding.setProjectId(poEntity.get().getSkillSeekerProject().getId());
                            boarding.setProjectName(poEntity.get().getSkillSeekerProject().getTitle());
                            boarding.setStatus("On-boarded");
                        } else {
                            boarding.setProjectName("default");
                        }
                        boarding.setSeekerId(poEntity.get().getSkillSeekerId());
                        boarding.setSkillOwnerEntityId(onBoarding.getSkillOwnerEntityId());
                        boarding.setStartDate(onBoarding.getStartDate());
                        boarding.setEndDate(onBoarding.getEndDate());
                        boarding.setJobId(poEntity.get().getJobId().getJobId());
                        poRepository.save(poEntity.get());
                    } else {
                        // On-boarding is not within 14 days from the approved date
                        skillOwnerEntity.get().setStartDate(null);
                        skillOwnerEntity.get().setEndDate(null);
                        skillOwnerEntity.get().setOnBoardingDate(null);

                        Optional<OwnerSkillStatusEntity> available = ownerSkillStatusRepository.findById(1);
                        if (available.isPresent()) {
                            skillOwnerEntity.get().setOwnerSkillStatusEntity(available.get());
                        }

                        boarding.setSkillOwnerEntityId(skillOwnerEntity.get().getSkillOwnerEntityId());
                        boarding.setJobId(poEntity.get().getJobId().getJobId());
                        boarding.setProjectId(poEntity.get().getSkillSeekerProject().getId());
                        boarding.setSeekerId(poEntity.get().getSkillSeekerId());
                        boarding.setStartDate(null);
                        boarding.setEndDate(null);
                        boarding.setProjectName(null);
                        boarding.setStatus("On-boarding must be within 14 days from the date of PO creation");

                        skillOwnerRepository.save(skillOwnerEntity.get());
                    }
                } else if
                (statementOfWork.isPresent() && (job.getJobId() == statementOfWork.get().getJobId().getJobId()) && (statementOfWork.get().getSowStatus().getId() == 22)) {
                    // SOW condition is true
                    LocalDate startDate = onBoarding.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate approvedDate = statementOfWork.get().getSowStartDate();

                    if (startDate.isBefore(approvedDate.plusDays(14))) {
                        // On-boarding is within 14 days from the approved date
                        skillOwnerEntity.get().setStartDate(onBoarding.getStartDate());
                        skillOwnerEntity.get().setEndDate(onBoarding.getEndDate());
                        skillOwnerEntity.get().setOnBoardingDate(onBoarding.getStartDate());

                        Optional<OwnerSkillStatusEntity> hiring = ownerSkillStatusRepository.findById(4);
                        if (hiring.isPresent()) {
                            skillOwnerEntity.get().setOwnerSkillStatusEntity(hiring.get());
                        }

                        skillOwnerRepository.save(skillOwnerEntity.get());

                        if (statementOfWork.get().getSkillSeekerProject() != null) {
                            boarding.setProjectId(statementOfWork.get().getSkillSeekerProject().getId());
                            boarding.setProjectName(statementOfWork.get().getSkillSeekerProject().getTitle());
                            boarding.setStatus("On-boarded");
                        } else {
                            boarding.setProjectName("default");
                        }
                        boarding.setSeekerId(statementOfWork.get().getSkillSeekerId());
                        boarding.setSkillOwnerEntityId(onBoarding.getSkillOwnerEntityId());
                        boarding.setJobId(statementOfWork.get().getJobId().getJobId());
                        boarding.setStartDate(onBoarding.getStartDate());
                        boarding.setEndDate(onBoarding.getEndDate());

                        statementOfWorkRepository.save(statementOfWork.get());
                    } else {
                        // On-boarding is not within 14 days from the approved date
                        skillOwnerEntity.get().setStartDate(null);
                        skillOwnerEntity.get().setEndDate(null);
                        skillOwnerEntity.get().setOnBoardingDate(null);

                        Optional<OwnerSkillStatusEntity> available = ownerSkillStatusRepository.findById(1);
                        if (available.isPresent()) {
                            skillOwnerEntity.get().setOwnerSkillStatusEntity(available.get());
                        }

                        boarding.setSkillOwnerEntityId(skillOwnerEntity.get().getSkillOwnerEntityId());
                        boarding.setSeekerId(statementOfWork.get().getSkillSeekerId());
                        boarding.setProjectId(statementOfWork.get().getSkillSeekerProject().getId());
                        boarding.setJobId(statementOfWork.get().getJobId().getJobId());
                        boarding.setStartDate(null);
                        boarding.setEndDate(null);
                        boarding.setProjectName(null);
                        boarding.setStatus("On-boarding must be within 14 days from the date of SOW creation");

                        skillOwnerRepository.save(skillOwnerEntity.get());
                    }
                }
            }
        } else {
            throw new ServiceException(INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorCode(), INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID.getErrorDesc());
        }

        return boarding;
    }

    @Transactional
    @Scheduled(fixedRateString = "PT23H")
    public void removeFilesScheduler() {
        System.out.println("**************System time zone is : " + ZonedDateTime.now(ZoneId.systemDefault()));
        Optional<List<PoEntity>> poEntity = Optional.ofNullable(poRepository.findByDateOfRelease());
        if (poEntity.isPresent()) {
            poEntity.get().forEach(poEntity1 -> {
                Optional<SkillOwnerEntity> skillOwner = skillOwnerRepository.findById(poEntity1.getSkillOwnerEntity().getSkillOwnerEntityId());
                if (skillOwner.isPresent() && null == skillOwner.get().getOnBoardingDate()) {
                    Optional<SkillSeekerMSAEntity> byOwnerId = Optional.ofNullable(skillSeekerMsaRepository.findByOwnerId(poEntity1.getSkillOwnerEntity().getSkillOwnerEntityId()));
                    if (!byOwnerId.isEmpty()) {

                        skillSeekerMsaRepository.delete(byOwnerId.get());
                    }
                    Optional<StatementOfWorkEntity> byOwnerId1 = statementOfWorkRepository.findByOwnerId(poEntity1.getSkillOwnerEntity().getSkillOwnerEntityId());
                    if (!byOwnerId1.isEmpty()) {
                        statementOfWorkRepository.delete(byOwnerId1.get());
                    }
                    Optional<PoEntity> byOwnerId2 = poRepository.findByOwnerId(poEntity1.getSkillOwnerEntity().getSkillOwnerEntityId());
                    if (!byOwnerId2.isEmpty()) {
                        poRepository.delete(byOwnerId2.get());
                    }
                }
            });
        } else {
            logger.info("candidate are available in contracts");
        }
    }

    @Scheduled(fixedRateString = "PT23H")
    @Transactional
    public void poExpiry() {
        List<PoEntity> poEntities = poRepository.findAll();

        poEntities.forEach(poEntity -> {

//            Optional<PoEntity> deleteAtNull = poRepository.findByDeleteAtNull(poEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/DD");
            java.util.Date today = new java.util.Date();
            try {
                SkillOwnerEntity ownerEntity = skillOwnerRepository.findBySkillOwnerEntityId(poEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                java.util.Date expiry = formatter.parse(formatter.format(poEntity.getExpiryDate()));
                java.util.Date todayWithZeroTime = formatter.parse(formatter.format(today));
                if (expiry.equals(todayWithZeroTime) && ownerEntity.getOnBoardingDate() != null) {
                    poEntity.setDeletedAt(LocalDateTime.now());
                    poRepository.saveAndFlush(poEntity);
                }
            } catch (ParseException e) {
                throw new RuntimeException();
            }


        });


    }


    @Transactional
    @Override
    public ProjectTaskDetails getProjectTaskDetailsBySeeker(int id) {

        List<TaskList> taskInProjects = new ArrayList<>();
        ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
        Optional<List<SkillSeekerProjectEntity>> bySkillSeekerId = skillSeekerProjectRepository.findBySkillSeekerId(id);
        if (!bySkillSeekerId.get().isEmpty()) {
            for (SkillSeekerProjectEntity skillSeekerProject : bySkillSeekerId.get()) {
                TaskList taskList = new TaskList();
                List<Task> tasks = new ArrayList<>();
                taskList.setSkillSeekerProjectEntity(skillSeekerProject);
                Optional<List<SkillSeekerTaskEntity>> taskByProject = skillSeekerTaskRepository.findBySkillSeekerProjectId(skillSeekerProject.getId());
                if (taskByProject.isPresent() && !taskByProject.isEmpty()) {
                    for (SkillSeekerTaskEntity taskByProjectId : taskByProject.get()) {
                        Task task = new Task();
                        task.setTaskId(taskByProjectId.getId());
                        task.setTaskDescription(taskByProjectId.getTaskDescription());
                        task.setTaskTitle(taskByProjectId.getTaskTitle());
                        tasks.add(task);
                    }
                }
                taskList.setSkillSeekerTasks(tasks);
                taskInProjects.add(taskList);
            }
        }
        //Adding a static default project
        TaskList taskList = new TaskList();
        List<Task> tasks = new ArrayList<>();
        SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
        Optional<SkillSeekerEntity> skillSeeker = skillSeekerRepository.findById(id);
        skillSeekerProjectEntity.setTitle("Default");
        skillSeekerProjectEntity.setId(0);
        if (skillSeeker.isPresent()) {
            skillSeekerProjectEntity.setSkillSeeker(skillSeeker.get());
        }
        taskList.setSkillSeekerProjectEntity(skillSeekerProjectEntity);
        Optional<List<SkillSeekerTaskEntity>> taskByProject = skillSeekerTaskRepository.findBySkillSeekerProjectIdAndSkillSeekerId(id);
        if (taskByProject.isPresent() && !taskByProject.isEmpty()) {
            for (SkillSeekerTaskEntity taskByProjectId : taskByProject.get()) {
                Task task = new Task();
                task.setTaskId(taskByProjectId.getId());
                task.setTaskDescription(taskByProjectId.getTaskDescription());
                task.setTaskTitle(taskByProjectId.getTaskTitle());
                tasks.add(task);
            }
        }
        taskList.setSkillSeekerTasks(tasks);
        taskInProjects.add(taskList);

        projectTaskDetails.setSkillSeekerProjectAndTaskList(taskInProjects);
        return projectTaskDetails;
    }

    @Override
    @Transactional
    public List<Contracts> getAllContractDetails() {

        List<Contracts> contractsList = new ArrayList<>();
        List<SkillSeekerMSAEntity> skillSeekerMSAEntities = skillSeekerMsaRepository.findAll();

        for (SkillSeekerMSAEntity skillSeekerMSAEntity : skillSeekerMSAEntities) {
            contractsList.add(skillOwnerService.ownerContractDetails(skillSeekerMSAEntity));
        }

        logger.info("skillSeekerServiceImpl || getAllContractDetails || Getting all the contracts in Admin ");
        return contractsList;

    }


    @Override
    @Transactional
    public List<ContractDetails> getListsOfContractDetailsInPartner(int partnerId) {

        try {
            List<SkillOwnerEntity> skillOwnerEntities = skillOwnerRepository.findAll();

            ContractDetails contractDetails = new ContractDetails();

            List<ContractDetails> contractDetailsList = new ArrayList<>();

            skillOwnerEntities.forEach(skillOwnerEntity -> {
                Optional<List<SelectionPhase>> selectionPhases = selectionPhaseRepository.findBySkillOwnerId(skillOwnerEntity.getSkillOwnerEntityId());

                selectionPhases.get().forEach(selectionPhase -> {
                    if (selectionPhase.getSkillOwnerEntity().getSkillPartnerEntity().getSkillPartnerId() == partnerId) {
                        Optional<SkillSeekerMSAEntity> msaEntity = skillSeekerMsaRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                        Optional<StatementOfWorkEntity> statementOfWorkEntity = statementOfWorkRepository.findByJobIdAndSkillOwnerId(selectionPhase.getJob().getJobId(), selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                        Optional<PoEntity> poEntity = poRepository.findBySkillOwnerIdAndJobId( selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId(),selectionPhase.getJob().getJobId());


                        List<RequirementPhases> requirementPhases = new ArrayList<>();
                        contractDetails.setCurrentStage(0);

                        selectionPhase.getRequirementPhase().forEach(requirementPhase -> {

                            RequirementPhases requirementPhaseDto = new RequirementPhases();

                            requirementPhaseDto.setRequirementPhaseName(requirementPhase.getRequirementPhaseName());
                            requirementPhaseDto.setStage(requirementPhase.getStage());
                            requirementPhaseDto.setInterviewDate(requirementPhase.getDateOfInterview());

                            requirementPhases.add(requirementPhaseDto);
                            contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                            logger.info("SkillSeekerServiceImpl || getListsOfContractDetailsInPartner || requirement phase list added ");
                        });
                        contractDetails.setPosition(selectionPhase.getJob().getJobTitle());
                        contractDetails.setExperience(selectionPhase.getSkillOwnerEntity().getExpYears() + "Years," + selectionPhase.getSkillOwnerEntity().getExpMonths() + " Months");
                        contractDetails.setNameOfOwner(selectionPhase.getSkillOwnerEntity().getFirstName() + " " + selectionPhase.getSkillOwnerEntity().getLastName());
                        contractDetails.setJobId(selectionPhase.getJob().getJobId());
                        if (msaEntity.isPresent()) {
                            contractDetails.setIsMsaCreated(true);
                            contractDetails.setMsaId(msaEntity.get().getId());
                            contractDetails.setMsaStatus("MSA " + msaEntity.get().getMsaStatus().getStatus());
                            RequirementPhases requirementPhaseDto = new RequirementPhases();
                            requirementPhaseDto.setRequirementPhaseName("MSA Contract Writing [" + msaEntity.get().getMsaStatus().getStatus() + "]");
                            requirementPhaseDto.setStage(requirementPhases.size() + 1);
                            if (null != msaEntity.get().getDateSigned()) {
                                requirementPhaseDto.setInterviewDate(msaEntity.get().getDateSigned());
                            }
                            contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                            requirementPhases.add(requirementPhaseDto);
                            logger.info("SkillSeekerServiceImpl || getListsOfContractDetailsInPartner || MSA current stage is called ");
                        }
                        if (statementOfWorkEntity.isPresent()) {
                            contractDetails.setIsSowCreated(true);
                            contractDetails.setSowId(statementOfWorkEntity.get().getId());
                            contractDetails.setSowStatus("SOW " + statementOfWorkEntity.get().getSowStatus().getStatus());
                            RequirementPhases requirementPhaseDto = new RequirementPhases();
                            requirementPhaseDto.setRequirementPhaseName("SOW Contract Writing [" + statementOfWorkEntity.get().getSowStatus().getStatus() + "]");
                            requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                            if (null != statementOfWorkEntity.get().getDateOfRelease()) {
                                requirementPhaseDto.setInterviewDate(statementOfWorkEntity.get().getDateOfRelease());
                            }
                            contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                            requirementPhases.add(requirementPhaseDto);
                            logger.info("SkillSeekerServiceImpl || getListsOfContractDetailsInPartner || SOW current stage is called ");
                        }
                        if (poEntity.isPresent()) {
                            contractDetails.setIsPoCreated(true);
                            contractDetails.setPoId(poEntity.get().getId());
                            contractDetails.setPoStatus("PO " + poEntity.get().getPoStatus().getStatus());
                            RequirementPhases requirementPhaseDto = new RequirementPhases();
                            requirementPhaseDto.setRequirementPhaseName("PO Contract Writing [" + poEntity.get().getPoStatus().getStatus() + "]");
                            requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                            if (null != poEntity.get().getDateOfRelease()) {
                                requirementPhaseDto.setInterviewDate(poEntity.get().getDateOfRelease().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                            requirementPhases.add(requirementPhaseDto);
                            logger.info("SkillSeekerServiceImpl || getListsOfContractDetailsInPartner || PO current stage is called ");
                        }
                        if (null != skillOwnerEntity.getOnBoardingDate()) {
                            RequirementPhases requirementPhaseDto = new RequirementPhases();
                            requirementPhaseDto.setRequirementPhaseName("Final Release");
                            requirementPhaseDto.setStage(contractDetails.getCurrentStage() + 1);
                            requirementPhaseDto.setInterviewDate(skillOwnerEntity.getOnBoardingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            contractDetails.setCurrentStage(contractDetails.getCurrentStage() + 1);
                            requirementPhases.add(requirementPhaseDto);
                        }
                        requirementPhases.sort(Comparator.comparingInt(RequirementPhases::getStage));
                        contractDetails.setRequirementPhaseList(requirementPhases);
                        contractDetailsList.add(contractDetails);
                    } else {
                        throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());

                    }
                });
            });
            return contractDetailsList;
        } catch (Exception e) {
            throw new ServiceException(ID_NOT_FOUND.getErrorCode(), ID_NOT_FOUND.getErrorDesc());
        }
    }


}




