package com.flexcub.resourceplanning.skillseeker.service;

public interface LocationMatchForJobService {
    int getLocationMatchPercentage(String jobLocation, String ownerLocation);
}
