package com.openapi.cloud.core.service;


import com.openapi.cloud.core.constants.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationFactory {

    private final Map<NotificationType, NotificationService> serviceMap;


    @Autowired
    public NotificationFactory(List<NotificationService> services) {
        this.serviceMap = services.stream().collect(Collectors.toMap(NotificationService::getType,
                service -> service));
    }

    public NotificationService getNotificationService(NotificationType type) {
        NotificationService service = serviceMap.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Notification Type not supported:" + type);
        }
        return service;

    }
}
