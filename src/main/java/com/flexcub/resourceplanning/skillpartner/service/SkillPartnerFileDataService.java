package com.flexcub.resourceplanning.skillpartner.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public interface SkillPartnerFileDataService {

    String readSkillPartnerExcelFile(int fileID);

    ResponseEntity<String> importCsvFile(InputStream excel, String fileName) throws IOException;

    String importExcelFile(InputStream excelInputStream, String fileName) throws IOException;


}
