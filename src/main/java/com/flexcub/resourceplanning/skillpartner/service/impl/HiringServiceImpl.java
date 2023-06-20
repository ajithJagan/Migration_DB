package com.flexcub.resourceplanning.skillpartner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;
import com.flexcub.resourceplanning.skillpartner.repository.HiringRepository;
import com.flexcub.resourceplanning.skillpartner.service.HiringService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
@Log4j2
public class HiringServiceImpl implements HiringService {

    @Autowired
    HiringRepository hiringRepository;
    Logger logger = LoggerFactory.getLogger(HiringServiceImpl.class);

    /**
     * Method to get a  List all HiringEntity details
     *
     * @return the list of Hiring details
     */

    public List<HiringEntity> getData() {
        logger.info("HiringServiceImpl || getData || HiringEntity data has been successfully displayed");
        return hiringRepository.findAll();
    }

    /**
     * Method to insert new data into HiringEntity
     *
     * @param hiringEntity
     * @return newly inserted data
     */


    public HiringEntity insertData(HiringEntity hiringEntity) {
        try {
            logger.info("HiringServiceImpl || insertData || HiringEntity data has been successfully inserted into skillpartner_hiring table {} //-> ", hiringEntity);
            return hiringRepository.save(hiringEntity);
        } catch (Exception e) {
            throw new ServiceException(INVALID_DATA.getErrorCode(), INVALID_DATA.getErrorDesc());
        }

    }

    /**
     * Method to update the existing data in HiringEntity
     *
     * @param update
     * @return updated data
     */

    public HiringEntity updateData(HiringEntity update) {
        try {
            logger.info("HiringServiceImpl || updateData || HiringEntity data has been successfully updated into skillpartner_hiring table {} // ->", update);
            return hiringRepository.saveAndFlush(update);
        } catch (Exception e) {
            throw new ServiceException(UPDATE_FAILED.getErrorCode(), UPDATE_FAILED.getErrorDesc());
        }

    }

    /**
     * Method to delete data from the HiringEntity based on id
     *
     * @param id
     */
    public void deleteData(int id) {

        try {
            logger.info("HiringServiceImpl || deleteData || HiringEntity data has been successfully deleted from skillPartner_hiring table for id {}", id);
            hiringRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(HIRING_ID_NOT_FOUND.getErrorCode(), HIRING_ID_NOT_FOUND.getErrorDesc());
        }

    }
}
