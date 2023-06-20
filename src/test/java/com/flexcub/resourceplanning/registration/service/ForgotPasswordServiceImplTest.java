package com.flexcub.resourceplanning.registration.service;

import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import com.flexcub.resourceplanning.registration.repository.ForgotPasswordRepository;
import com.flexcub.resourceplanning.registration.service.impl.ForgotPasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ForgotPasswordServiceImpl.class)
public class ForgotPasswordServiceImplTest {

    ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    private ForgotPasswordServiceImpl forgotPasswordService;

    @BeforeEach
    void setUpTest() {
        forgotPasswordToken.setForgotToken("Xz1U3jQL6aZGFeQmjuL3");
    }

    @Test
    void createForgotPasswordTokenTest() {
        assertThat(forgotPasswordService.createForgotPasswordToken()).isNotNull();
    }

    @Test
    void saveTokenTest() {

        forgotPasswordService.saveToken(forgotPasswordToken);
        Mockito.verify(forgotPasswordRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void findByForgotTokenTestTest() {
        Mockito.when(forgotPasswordRepository.findByForgotToken(forgotPasswordToken.getForgotToken())).thenReturn(forgotPasswordToken);
        assertEquals(forgotPasswordToken, forgotPasswordService.findByForgotToken("Xz1U3jQL6aZGFeQmjuL3"));
    }

    @Test
    void removeForgotTokenTest() {
        forgotPasswordService.removeForgotToken(forgotPasswordToken);
        Mockito.verify(forgotPasswordRepository, Mockito.times(1)).delete(Mockito.any());
    }
}
