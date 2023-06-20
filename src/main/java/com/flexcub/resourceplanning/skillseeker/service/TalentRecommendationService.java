package com.flexcub.resourceplanning.skillseeker.service;

import com.flexcub.resourceplanning.skillseeker.dto.RecommendedCandidates;

import java.util.List;

public interface TalentRecommendationService {
    List<RecommendedCandidates> getTalentRecommendation(String jobId);

}
