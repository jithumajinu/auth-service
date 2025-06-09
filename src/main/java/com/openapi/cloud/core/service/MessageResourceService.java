package com.openapi.cloud.core.service;

import java.util.Locale;

public interface MessageResourceService {

    String getLabel(String key);
    String getLabel(String key, Locale locale);

}