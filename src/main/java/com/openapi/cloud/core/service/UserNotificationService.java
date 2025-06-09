package com.openapi.cloud.core.service;

import com.openapi.cloud.core.constants.NotificationType;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService {

    private final NotificationFactory notificationFactory;

    public UserNotificationService(NotificationFactory notificationFactory) {
        this.notificationFactory = notificationFactory;
    }


    public void notifyUser(String to, String subject, String message, NotificationType type) {

        NotificationService notificationService = notificationFactory.getNotificationService(type);

        if (!notificationService.validateRecipient(to)) {
            throw new IllegalArgumentException("invalid recipient for notification type: " + type);
        }
        notificationService.sendNotification(to, subject, message);

    }


}
