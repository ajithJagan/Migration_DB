package com.flexcub.resourceplanning.location.service;

import java.util.HashMap;
import java.util.List;

public interface LocationService {
    String generateToken();

    List<HashMap<String, String>> getStates();

    List<HashMap<String, String>> getCities(String state);
}
