package com.flexcub.resourceplanning.skillseeker.service;

import java.util.List;

public interface USALocationService {

    boolean loadDataForUSLocation();

    List<String> getLocationFromDatabase(String keyword);
}
