package com.flexcub.resourceplanning.registration.service;

import com.flexcub.resourceplanning.registration.entity.AbstractEmailContext;

import javax.mail.MessagingException;

public interface FlexcubEmailService {
    void sendMail(AbstractEmailContext email, String token) throws MessagingException;

    void forgotMail(AbstractEmailContext email, String forgotToken) throws MessagingException;

    void sendFailedMail(AbstractEmailContext email1) throws MessagingException;

    void hiringProcesss(AbstractEmailContext email) throws MessagingException;

    void rejectionMail(AbstractEmailContext email) throws MessagingException;

    void scheduleMail(AbstractEmailContext email) throws MessagingException;

    void selectedForRoundMail(AbstractEmailContext email) throws MessagingException;

    void newSlotRequest(AbstractEmailContext email) throws MessagingException;

    void trialAccountExpiry(AbstractEmailContext email) throws MessagingException;

    //    void dateOfInterview(AccountVerificationEmailContext email) throws MessagingException;
    void skillOwnerTimeSheetApproval(AbstractEmailContext emailContext) throws MessagingException;
    void reScheduleMail(AbstractEmailContext email) throws MessagingException;

}

