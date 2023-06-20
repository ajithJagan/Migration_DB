package com.flexcub.resourceplanning.skillseeker.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public interface SkillSeekerFileDataService {

    String readSkillSeekerFile(int fileId) throws IOException;


    String importExcelFile(InputStream excel, String fileName) throws IOException;
}
