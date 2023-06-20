package com.flexcub.resourceplanning.contracts.service.impl;


import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {
    public NotificationEvent(Object source) {
        super(source);
    }

}

