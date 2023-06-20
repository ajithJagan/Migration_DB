//package com.flexcub.resourceplanning.skillseeker.service;
//
//import com.flexcub.resourceplanning.skillseeker.entity.StateEntity;
//import com.flexcub.resourceplanning.skillseeker.repository.StateCodeRepository;
//import com.flexcub.resourceplanning.skillseeker.service.impl.StateCodeServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(classes = StateCodeServiceImpl.class)
//class StateCodeServiceTest {
//
//    @Autowired
//    StateCodeServiceImpl stateCodeService;
//
//    @MockBean
//    StateCodeRepository stateCodeRepository;
//
//    StateEntity state = new StateEntity();
//    List<StateEntity> stateCodeTest = new ArrayList<StateEntity>();
//
//    @BeforeEach
//    void beforeTest() {
//        state.setId(1);
//        state.setStateCode("AL");
//    }
//
//    @Test
//    void getStateCodeServiceTest() {
//
//        stateCodeTest.add(state);
//        Mockito.when(stateCodeRepository.findAll()).thenReturn(stateCodeTest);
//
//        assertThat(stateCodeService.getData()).isEqualTo(stateCodeTest);
//
//    }
//}
