package com.flexcub.resourceplanning.skillseeker.controller;

import com.flexcub.resourceplanning.skillseeker.controller.TalentRecommendationController;
import com.flexcub.resourceplanning.skillseeker.dto.RecommendedCandidates;
import com.flexcub.resourceplanning.skillseeker.service.TalentRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = TalentRecommendationController.class)
class TalentRecommendationControllerTest {

    @Autowired
    TalentRecommendationController talentRecommendationController;

    @MockBean
    TalentRecommendationService talentRecommendationService;

    RecommendedCandidates recommendedCandidatesDto = new RecommendedCandidates();

    @BeforeEach
    void beforeTest() {
        recommendedCandidatesDto.setJobId("FJB-00001");
    }

    @Test
    void getTalentRecommendationControllerTest() {
        List<RecommendedCandidates> recommendedCandidatesDtoList = Collections.singletonList(recommendedCandidatesDto);
        Mockito.when(talentRecommendationService.getTalentRecommendation(String.valueOf(recommendedCandidatesDto))).thenReturn(recommendedCandidatesDtoList);
        assertEquals(200, talentRecommendationController.talentRecommendation(String.valueOf(recommendedCandidatesDto)).getStatusCodeValue());
    }
}