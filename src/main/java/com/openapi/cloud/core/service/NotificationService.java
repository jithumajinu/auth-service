package com.openapi.cloud.core.service;

import com.openapi.cloud.core.constants.NotificationType;

public interface NotificationService {

    void sendNotification(String to, String subject, String message);
    boolean validateRecipient(String to);
    NotificationType getType();
}
