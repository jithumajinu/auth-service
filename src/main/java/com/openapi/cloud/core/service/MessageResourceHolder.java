package com.openapi.cloud.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageResourceHolder {

    private static MessageResourceService messageResourceService;

    @Autowired
    public MessageResourceHolder(MessageResourceService messageResourceService) {
        MessageResourceHolder.messageResourceService = messageResourceService;
    }

    public static MessageResourceService get() {
        return messageResourceService;
    }
}
