//package com.flexcub.resourceplanning.skillseeker.service.impl;
//
//import com.flexcub.resourceplanning.exceptions.ServiceException;
//import com.flexcub.resourceplanning.skillseeker.dto.CityUS;
//import com.flexcub.resourceplanning.skillseeker.entity.CityEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.CityRepository;
//import com.flexcub.resourceplanning.skillseeker.service.CityService;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Optional;
//
//import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_FOUND;
//
//@Service
//
//@CacheConfig(cacheNames = "city")
//public class CityServiceImpl implements CityService {
//
//    @Autowired
//    ModelMapper modelMapper;
//    @Autowired
//    CityRepository repository;
//    Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);
//
//    /**
//     * This method is to get city details
//     *
//     * @return It returns the list of cities
//     */
//    @Override
//    public List<CityUS> getData() {
//
//        Optional<List<CityEntity>> cityList = Optional.ofNullable(repository.findAll());
//        List<CityUS> cityUSList = new ArrayList<>();
//
//        try {
//            if (cityList.isPresent()) {
//                for (CityEntity city : cityList.get()) {
//                    CityUS cityUS = modelMapper.map(city, CityUS.class);
//                    cityUSList.add(cityUS);
//                }
//                logger.info("CityServiceImpl || getData || Get the City Details...");
//                return cityUSList;
//            } else {
//                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//            }
//        } catch (ServiceException e) {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//        }
//    }
//
//    /**
//     * This method loads City and State data
//     */
//    @Override
//    @Cacheable
//    public void loadData() {
//        try {
//            try (FileInputStream file = new FileInputStream(new File("src/main/resources/City_State.xlsx"))) {
//                XSSFWorkbook workbook = new XSSFWorkbook(file);
//                XSSFSheet sheet = workbook.getSheetAt(0);
//                Iterator<Row> rowIterator = sheet.iterator();
//                while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    int c = 1;
//                    String str = "";
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//                        CityEntity city = new CityEntity();
//                        if ((c % 2) == 1)
//                            str = cell.getStringCellValue();
//                        else {
//                            city.setCityNames(str);
//                            Optional<CityEntity> saveCity = repository.findByCityNamesAndStateCode(str, cell.getStringCellValue());
//                            if (!saveCity.isPresent()) {
//                                city.setStateCode(cell.getStringCellValue());
//                                repository.save(city);
//                            }
//                        }
//                        c++;
//                        logger.info("CityServiceImpl || loadData || Loading the CellValue...");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "City Not Found");
//        }
//    }
//}
