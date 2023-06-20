package com.flexcub.resourceplanning.verificationmail.service;//package com.flexcub.resourceplanning.verificationmail.service;

import com.flexcub.resourceplanning.verificationmail.entity.VerificationToken;
import com.flexcub.resourceplanning.verificationmail.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private static final int TOKEN_VALIDITY_MINUTES = 60 * 24 * 7;

    private final VerificationTokenRepository verificationTokenRepository;

    Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Override
    public VerificationToken createVerificationToken() {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(tokenValue);
        verificationToken.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
        verificationToken.setExpireAt(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES));
        verificationTokenRepository.save(verificationToken);
        logger.info("VerificationServiceImpl ||createVerificationToken|| Token verification is successfully generated");
        return verificationToken;
    }

    @Override
    public void saveSecureToken(VerificationToken token) {
        logger.info("VerificationServiceImpl || saveSecureToken || Token has been saved and secured");
        verificationTokenRepository.save(token);
    }

    @Override
    public VerificationToken findByToken(String token) {
        logger.info("VerificationServiceImpl || findByToken || Userdetails has been found by using token");
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void removeToken(VerificationToken token) {
        logger.info("VerificationServiceImpl || removeToken || Token has been deleted");
        verificationTokenRepository.delete(token);
    }

    @Override
    public void removeTokenByToken(String token) {
        verificationTokenRepository.removeByToken(token);
    }


}

