package com.openapi.cloud.core.service.impl;

import com.openapi.cloud.core.constants.NotificationType;
import com.openapi.cloud.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.openapi.cloud.core.constants.AppConstants.PHONE_REGEX;

@Component
@Qualifier("smsNotification")
public class SMSServiceImpl implements NotificationService {

    @Override
    public void sendNotification(String to, String subject, String message) {
        System.out.println("Send SMS");
    }

    @Override
    public boolean validateRecipient(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches(PHONE_REGEX);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.SMS;
    }
}
