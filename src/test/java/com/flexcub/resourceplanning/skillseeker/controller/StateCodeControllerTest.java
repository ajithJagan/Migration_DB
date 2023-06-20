//package com.flexcub.resourceplanning.skillseeker.controller;
//
//import com.flexcub.resourceplanning.skillseeker.entity.State;
//import com.flexcub.resourceplanning.skillseeker.service.StateCodeService;
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
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(classes = StateCodeController.class)
//class StateCodeControllerTest {
//    @Autowired
//    StateCodeController stateCodeController;
//    @MockBean
//    StateCodeService service;
//    State state = new State();
//    List<State> stateCodeTest = new ArrayList<>();
//
//    @BeforeEach
//    void beforeTest() {
//        state.setId(1);
//        state.setStateCode("AL");
//    }
//
//    @Test
//    void getSeekerProjectDetailsTest() {
//        stateCodeTest.add(state);
//        Mockito.when(service.getData()).thenReturn(stateCodeTest);
//        assertEquals(200, stateCodeController.getstateDetails().getStatusCodeValue());
//    }
//}
//
//
