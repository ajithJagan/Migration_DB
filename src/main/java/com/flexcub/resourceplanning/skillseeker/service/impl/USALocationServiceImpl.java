package com.flexcub.resourceplanning.skillseeker.service.impl;

import com.flexcub.resourceplanning.config.ModelMapperConfiguration;
import com.flexcub.resourceplanning.skillseeker.entity.USALocationEntity;
import com.flexcub.resourceplanning.skillseeker.repository.USALocationRepository;
import com.flexcub.resourceplanning.skillseeker.service.USALocationService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class USALocationServiceImpl implements USALocationService {

    @Autowired
    ModelMapperConfiguration modelMapperConfiguration;
    Logger logger = LoggerFactory.getLogger(USALocationServiceImpl.class);
    @Autowired
    private USALocationRepository usaLocationRepository;

    @Override
    public boolean loadDataForUSLocation() {
        try {
            try (FileInputStream file = new FileInputStream(new File("src/main/resources/US Location.xlsx"))) {
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();
                List<USALocationEntity> locationList = new ArrayList<>();
                while (rowIterator.hasNext()) {
                    USALocationEntity usaLocation = new USALocationEntity();
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    int c = 1;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (c == 1) {
                            usaLocation.setId((int) cell.getNumericCellValue());

                        } else {
                            usaLocation.setCityAndState(cell.getStringCellValue());
                        }
                        c++;
                    }
                    locationList.add(usaLocation);
                }
                usaLocationRepository.saveAll(locationList);
                logger.info("USALocationServiceImpl || loadDataForUSLocation || getting Location ForUSLocation");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getLocationFromDatabase(String keyword) {
        if (keyword.length() < 3) {
            return new ArrayList<>();
        } else {
            logger.info("USALocationServiceImpl || getLocationFromDatabase || locationFromDB");
            return usaLocationRepository.findByCityAndStateLike("%" + keyword + "%");
        }
    }
}
