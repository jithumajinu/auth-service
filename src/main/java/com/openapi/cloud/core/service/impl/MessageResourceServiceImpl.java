package com.openapi.cloud.core.service.impl;

import com.openapi.cloud.core.service.MessageResourceService;
import com.openapi.cloud.core.util.LocaleUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;


import java.util.Locale;

@Service
public class MessageResourceServiceImpl implements MessageResourceService {
    @Autowired
    private MessageSource messageSource;

    @Override
    public String getLabel(String key) {
        try {
            return messageSource.getMessage(key, new Object[] {}, "An error has occurred", LocaleUtils.getLocale());
        } catch (NoSuchMessageException e) {
            return null;
        }
    }

    @Override
    public String getLabel(String key, Locale locale) {
        return messageSource.getMessage(key, new Object[] {}, "An error has occurred", locale);
    }
}
