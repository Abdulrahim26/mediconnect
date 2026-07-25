package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.response.NotificationResponse;
import com.mediconnect.mediconnectapi.entity.enums.NotificationType;

import java.util.List;
import java.util.UUID;


public interface NotificationService {


    void createNotification(
            UUID userId,
            String message,
            NotificationType type
    );


    List<NotificationResponse> getMyNotifications();


    void markAsRead(UUID notificationId);

}