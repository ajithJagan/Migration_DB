//package com.flexcub.resourceplanning.skillseeker.service.impl;
//
//import com.flexcub.resourceplanning.exceptions.ServiceException;
//import com.flexcub.resourceplanning.skillseeker.dto.StateUS;
//import com.flexcub.resourceplanning.skillseeker.entity.StateEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.StateCodeRepository;
//import com.flexcub.resourceplanning.skillseeker.service.StateCodeService;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.DATA_NOT_FOUND;
//
//@Service
//public class StateCodeServiceImpl implements StateCodeService {
//
//    @Autowired
//    ModelMapper modelMapper;
//    @Autowired
//    StateCodeRepository stateCodeRepository;
//    Logger logger = LoggerFactory.getLogger(StateCodeServiceImpl.class);
//
//
//    /**
//     * This method is to get data of state code.
//     *
//     * @return It returns list of state code.
//     */
//    @Override
//    public List<StateUS> getData() {
//
//        Optional<List<StateEntity>> stateEntityList = Optional.ofNullable(stateCodeRepository.findAll());
//        List<StateUS> stateDtoList = new ArrayList<>();
//
//        try {
//            if (stateEntityList.isPresent()) {
//                for (StateEntity stateEntity : stateEntityList.get()) {
//                    StateUS baseAndMaxRateDto = modelMapper.map(stateEntity, StateUS.class);
//                    stateDtoList.add(baseAndMaxRateDto);
//                }
//                logger.info("StateCodeServiceImpl || getData || Getting the all StateCode Details....");
//                return stateDtoList;
//            } else {
//                throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//            }
//        } catch (ServiceException e) {
//            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Data Not Found");
//        }
//
//
////        logger.info("StateCodeServiceImpl || getData || Getting the all StateCode Details....");
////        return stateCodeRepository.findAll();
//    }
//}
