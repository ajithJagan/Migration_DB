package com.flexcub.resourceplanning.registration.service.impl;

import com.flexcub.resourceplanning.registration.entity.ForgotPasswordToken;
import com.flexcub.resourceplanning.registration.repository.ForgotPasswordRepository;
import com.flexcub.resourceplanning.registration.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;
    private static final int TOKEN_VALIDITY_MINUTES = 60 * 24 * 4;
    Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPasswordToken createForgotPasswordToken() {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setForgotToken(tokenValue);
        forgotPasswordToken.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
        forgotPasswordToken.setExpireAt(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES));
        logger.info("ForgotPasswordServiceImpl || createForgotPasswordToken || Token verification is successfully generated");
        return forgotPasswordToken;
    }

    @Override
    public void saveToken(ForgotPasswordToken forgotToken) {
        logger.info("ForgotPasswordServiceImpl || createForgotPasswordToken || Token has been securely saved");
        forgotPasswordRepository.save(forgotToken);
    }

    @Override
    public ForgotPasswordToken findByForgotToken(String forgotToken) {
        logger.info("ForgotPasswordServiceImpl || createForgotPasswordToken || User Details has been found by using token {}", forgotToken);
        return forgotPasswordRepository.findByForgotToken(forgotToken);
    }

    @Override
    public void removeForgotToken(ForgotPasswordToken forgotToken) {
        logger.info("ForgotPasswordServiceImpl || createForgotPasswordToken || Token has been deleted: {}", forgotToken.getForgotToken());
        forgotPasswordRepository.delete(forgotToken);
    }

}
