package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.NotificationResponse;
import com.mediconnect.mediconnectapi.entity.Notification;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.NotificationType;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.NotificationRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void createNotification(
            UUID userId,
            String message,
            NotificationType type
    ) {
        // 🔍 Debug: Confirm method is entered
        System.out.println("NotificationService.createNotification() called");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus(false);

        // 🔍 Debug: Before saving
        System.out.println("Saving notification for user: " + user.getEmail());

        notificationRepository.save(notification);

        // 🔍 Debug: After saving
        System.out.println("Notification saved successfully");
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return notificationRepository
                .findByUserId(user.getId())
                .stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getType(),
                                notification.isReadStatus(),
                                notification.getCreatedAt()
                        )
                )
                .toList();
    }

    @Override
    public void markAsRead(UUID notificationId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot update this notification");
        }

        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}