package com.flexcub.resourceplanning.verificationmail.service;//package com.flexcub.resourceplanning.verificationmail.service;


import com.flexcub.resourceplanning.verificationmail.entity.VerificationToken;

public interface VerificationService {

    VerificationToken createVerificationToken();

    void saveSecureToken(VerificationToken token);

    VerificationToken findByToken(String token);

    void removeToken(VerificationToken token);

    void removeTokenByToken(String token);
}

