package com.flexcub.resourceplanning.skillseeker.service.impl;


import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.job.entity.Job;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.JobRepository;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.skillowner.entity.OwnerSkillSetEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import com.flexcub.resourceplanning.skillowner.repository.OwnerSkillSetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerResumeAndImageRepository;
import com.flexcub.resourceplanning.skillowner.service.OwnerSkillSetService;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillseeker.dto.RecommendedCandidates;
import com.flexcub.resourceplanning.skillseeker.entity.ContractStatus;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.StatementOfWorkEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.StatementOfWorkRepository;
import com.flexcub.resourceplanning.skillseeker.service.LocationMatchForJobService;
import com.flexcub.resourceplanning.skillseeker.service.TalentRecommendationService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.MSA_FILE_NOT_FOUND;

@Service

@Component
@Log4j2
public class TalentRecommendationServiceImpl implements TalentRecommendationService {

    @Autowired
    ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(TalentRecommendationServiceImpl.class);

    @Autowired
    ModelMapperConfiguration modelMapperConfiguration;

    @Autowired
    PoRepository poRepository;
    @Autowired
    StatementOfWorkRepository  statementOfWorkRepository;
    @Autowired
    OwnerSkillSetService ownerSkillSetService;
    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    @Autowired
    LocationMatchForJobService locationMatchForJobService;
    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    SkillOwnerResumeAndImageRepository skillOwnerResumeAndImageRepository;
    @Autowired
    OwnerSkillSetRepository ownerSkillSetRepository;

    /**
     * Get the matching skill owners for the job
     *
     * @param jobId
     * @return the list of recommended candidates
     */
    @Transactional
    @Override
    public List<RecommendedCandidates> getTalentRecommendation(String jobId) {
        List<SkillOwnerEntity> skillOwnerEntity = skillOwnerRepository.getAvailableTalents();
       List<SkillOwnerEntity> skillOwnerEntities = partnerRestrict(skillOwnerEntity, jobId);
        List<RecommendedCandidates> recommendedCandidatesList = new ArrayList<>();
        Optional<Job> job = jobRepository.findByJobId(jobId);
        if (job.isPresent()) {
            skillOwnerEntities.forEach(availableSkillOwnerEntity -> {
                if (skillOwnerRestriction(job, availableSkillOwnerEntity)) {
                    double jobMaxRate = job.get().getMaxRate();
                    Integer ownerRateCard = availableSkillOwnerEntity.getRateCard();
                    double ratePercentage = (0.1 * jobMaxRate);
                    double max_limit=jobMaxRate+ratePercentage;
                    if (max_limit>=ownerRateCard) {

                        try {
                            logger.info("TalentRecommendationServiceImpl || getTalentRecommendation ||Setting Values to recommended list");
                            RecommendedCandidates recommendedCandidatesDto = new RecommendedCandidates();
                            recommendedCandidatesDto.setJobId(jobId);
                            recommendedCandidatesDto.setJobTitle(job.get().getJobTitle());
                            recommendedCandidatesDto.setPreScreen(job.get().getScreeningQuestions());
                            recommendedCandidatesDto.setVerified(true);
                            int ownerTotalExp = availableSkillOwnerEntity.getExpMonths() + (availableSkillOwnerEntity.getExpYears() * 12); //30
                            String jobExp = job.get().getOwnerSkillYearOfExperience().getExperience();
                            int jobExpMonth;
                            if (jobExp.equalsIgnoreCase("0")) {
                                jobExpMonth = 0;
                            } else {
                                jobExpMonth = (Integer.parseInt(jobExp.substring(0, jobExp.length() - 1))) * 12;
                            }
                            if (jobExpMonth <= ownerTotalExp) {
                                logger.info("TalentRecommendationServiceImpl || getTalentRecommendation ||Setting Values to recommended list");
                                recommendedCandidatesDto.setSkillOwnerContact(availableSkillOwnerEntity.getPhoneNumber());
                                recommendedCandidatesDto.setSkillOwnerEmailAddress(availableSkillOwnerEntity.getPrimaryEmail());
                                String years= availableSkillOwnerEntity.getExpYears() == 1 || availableSkillOwnerEntity.getExpYears() == 0 ? availableSkillOwnerEntity.getExpYears() + " Year, ": availableSkillOwnerEntity.getExpYears() + " Years, ";
                                String months= availableSkillOwnerEntity.getExpMonths() == 1 || availableSkillOwnerEntity.getExpMonths() == 0 ? availableSkillOwnerEntity.getExpMonths() + " Month" : availableSkillOwnerEntity.getExpMonths()+ " Months";
                                recommendedCandidatesDto.setSkillOwnerExperience(years + months);
                                recommendedCandidatesDto.setSkillOwnerId(availableSkillOwnerEntity.getSkillOwnerEntityId());
                                recommendedCandidatesDto.setSkillOwnerName(availableSkillOwnerEntity.getFirstName() + " " + availableSkillOwnerEntity.getLastName());
                                //TODO : resume must be inserted into skillOwner
                                recommendedCandidatesDto.setSkillOwnerResume(" ");
                                Optional<List<OwnerSkillSetEntity>> ownerSkillSetEntity = Optional.ofNullable(ownerSkillSetRepository.findBySkillOwnerEntityId(availableSkillOwnerEntity.getSkillOwnerEntityId()));
                                if (ownerSkillSetEntity.isPresent()) {
                                    logger.info("TalentRecommendationServiceImpl || getTalentRecommendation ||Setting Values for skill set percentage");
                                    int skillSetPercentage = ownerSkillSetService.skillSetPercentage(jobId, ownerSkillSetEntity.get());
                                    recommendedCandidatesDto.setSkillSetMatchPercentage(skillSetPercentage);
                                    String jobLocation = job.get().getJobLocation().replace(" ", "+");
                                    String ownerLocation = (availableSkillOwnerEntity.getCity() + "," + availableSkillOwnerEntity.getState()).replace(" ", "+");
                                    int locationMatchPercentage = locationMatchForJobService.getLocationMatchPercentage(jobLocation, ownerLocation);
                                    recommendedCandidatesDto.setLocationMatchPercentage(locationMatchPercentage);
                                    logger.info("TalentRecommendationServiceImpl || getTalentRecommendation ||Setting Values for Rate percentage");
                                    int rateMatchPercentage = 0;
                                    double ownerRate = availableSkillOwnerEntity.getRateCard();
                                    double jobRate = job.get().getMaxRate();
                                    if (ownerRate <= jobRate) {
                                        rateMatchPercentage = 100;
                                    } else if (ownerRate <= (0.1*jobRate+jobRate)) {
                                        rateMatchPercentage = (int) (100.0 - (((ownerRate-jobRate) / (0.1*jobRate)) * 100));
                                    }
                                    recommendedCandidatesDto.setRateMatchPercentage(rateMatchPercentage);
                                    double avgMatchPercentage = (double) (rateMatchPercentage + locationMatchPercentage + skillSetPercentage) / 3;
                                    recommendedCandidatesDto.setOverallMatchPercentage((int) avgMatchPercentage);
                                    Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(jobId, availableSkillOwnerEntity.getSkillOwnerEntityId());
                                    if (selectionPhase.isPresent()) {
                                        recommendedCandidatesDto.setShortlist(true);
                                        recommendedCandidatesDto.setAccepted(selectionPhase.get().getAccepted());
                                    }
                                    recommendedCandidatesDto.setResumeAvailable(availableSkillOwnerEntity.isResumeAvailable());
                                    recommendedCandidatesDto.setImageAvailable(availableSkillOwnerEntity.isImageAvailable());
                                    if (job.get().getFederalSecurityClearance() && !availableSkillOwnerEntity.isFederalSecurityClearance()) {
                                        recommendedCandidatesList.remove(recommendedCandidatesDto);

                                    } else {
                                        recommendedCandidatesDto.setFederalSecurityClearance(availableSkillOwnerEntity.isFederalSecurityClearance());
                                        recommendedCandidatesList.add(recommendedCandidatesDto);
                                        recommendedCandidatesList.sort(Comparator.comparing(RecommendedCandidates::getOverallMatchPercentage).reversed());

                                    }

                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error setting SkillOwnerID {} to recommended Talent List, with exception {}",
                                    availableSkillOwnerEntity.getSkillOwnerEntityId(), e.getMessage());
                        }
                    }
                }
            });

        } else {
            throw new ServiceException(INVALID_JOB_ID.getErrorCode(), INVALID_JOB_ID.getErrorDesc());
        }
        logger.info("TalentRecommendationServiceImpl || AddTalentRecommendation ||RecommendedCandidateListAdded");
        return recommendedCandidatesList;
    }

    private boolean skillOwnerRestriction(Optional<Job> job, SkillOwnerEntity skillOwnerEntity) {
        Optional<List<SelectionPhase>> bySkillOwnerId = selectionPhaseRepository.findBySkillOwnerIdAndJobId(skillOwnerEntity.getSkillOwnerEntityId(),job.get().getJobId());
        Optional<PoEntity> poByJobId = poRepository.findByJobId(job.get().getJobId());
        Optional<StatementOfWorkEntity> sowByJobId = statementOfWorkRepository.findByJob(job.get().getJobId());
        if (bySkillOwnerId.isPresent() && (poByJobId.isPresent()||sowByJobId.isPresent()) ) {
            int numberOfJobsShortlisted = 0;
            boolean hiring = false;

            for (SelectionPhase selectionPhase : bySkillOwnerId.get()) {
                if (job.get().getSkillSeeker().getId() == selectionPhase.getJob().getSkillSeeker().getId()) {
                    if (selectionPhase.getAccepted()) {
                        LocalDate today = LocalDate.now();
                        if(poByJobId.isPresent()){
                            LocalDate poCreatedDay =  poByJobId.get().getContractStartDate();
                            long daysBetweenPo = ChronoUnit.DAYS.between(poCreatedDay, today);

                            if(bySkillOwnerId.isPresent() && job.get().getJobId().equals(selectionPhase.getJob().getJobId()) && (daysBetweenPo>=14)){
                                hiring = false;
                            } else if (sowByJobId.isPresent()) {
                                LocalDate sowCreatedDay = sowByJobId.get().getSowStartDate();
                                long daysBetweenSow = ChronoUnit.DAYS.between(sowCreatedDay,today);

                                if(bySkillOwnerId.isPresent() && job.get().getJobId().equals(selectionPhase.getJob().getJobId()) &&(daysBetweenSow>=14)){
                                    hiring = false;
                                }
                            }
                        }
                        else if (selectionPhase.getSkillOwnerEntity().getOnBoardingDate()==null && selectionPhase.getRejectedOn()==null) {
                            hiring = true;
                        } else if (selectionPhase.getRejectedOn() != null ) {
                            LocalDate rejectedOn = selectionPhase.getRejectedOn();
                            if (LocalDate.now().isBefore(rejectedOn.plusDays(60))) {
                                return false;
                            }
                        }
                    }
                    else
                        return true;
                }

                if (selectionPhase.getAccepted() && selectionPhase.getSkillOwnerEntity().getOnBoardingDate() == null) {
                    numberOfJobsShortlisted++;
                }
                if (selectionPhase.getSkillOwnerEntity().getOnBoardingDate() == null && selectionPhase.getRejectedOn() == null) {
                    //false
                    return false;
                }
            }
            if (hiring) {
                return false;
            }
            return numberOfJobsShortlisted < 3 && skillOwnerEntity.getOnBoardingDate() == null;
        }

        return true;
    }

    private List<SkillOwnerEntity> partnerRestrict(List<SkillOwnerEntity> skillOwnerEntities, String jobId) {
        List<SkillOwnerEntity> skillOwnerEntities1 = new ArrayList<>();
        Optional<Job> job = jobRepository.findByJobId(jobId);
        for (SkillOwnerEntity ownerEntity : skillOwnerEntities) {
            SkillPartnerEntity skillPartnerEntity = ownerEntity.getSkillPartnerEntity();
            if (skillPartnerEntity != null) {
                ContractStatus msaStatusId = skillPartnerEntity.getMsaStatusId();
                if (msaStatusId != null && (msaStatusId.getId() == 17 || msaStatusId.getId() == 11)) {
                    skillOwnerEntities1.add(ownerEntity);
                }
            }
        }
        return skillOwnerEntities1;
    }
}