package com.openapi.cloud.core.service.impl;

import com.openapi.cloud.core.constants.NotificationType;
import com.openapi.cloud.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("emailNotification")
public class EmailServiceImpl implements NotificationService {

    @Override
    public void sendNotification(String to, String subject, String message) {

        System.out.println("Send Email");
    }

    @Override
    public boolean validateRecipient(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }
}
