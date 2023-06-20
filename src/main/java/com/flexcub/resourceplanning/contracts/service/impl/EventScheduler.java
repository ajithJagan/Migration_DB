package com.flexcub.resourceplanning.contracts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {
    @Autowired
    private NotificationEventListener listener;

    @Scheduled(fixedRateString = "PT23H")
    public void notificationScheduler() {
        NotificationEvent notificationEvent = new NotificationEvent(this);
        listener.onApplicationEvent(notificationEvent);
    }
}