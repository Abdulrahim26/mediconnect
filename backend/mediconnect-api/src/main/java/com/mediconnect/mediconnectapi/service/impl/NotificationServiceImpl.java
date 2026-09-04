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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ======================================================
    // CREATE NOTIFICATION
    // ======================================================

    @Override
    @Transactional
    public void createNotification(
            UUID userId,
            String message,
            NotificationType type
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus(false);

        notificationRepository.save(notification);
    }

    // ======================================================
    // GET MY NOTIFICATIONS
    // ======================================================

    @Override
    public List<NotificationResponse> getMyNotifications() {

        User user = getLoggedInUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    // ======================================================
    // GET UNREAD COUNT
    // ======================================================

    @Override
    public long getUnreadCount() {

        User user = getLoggedInUser();

        return notificationRepository
                .countByUserIdAndReadStatusFalse(
                        user.getId()
                );
    }

    // ======================================================
    // MARK ONE AS READ
    // ======================================================

    @Override
    @Transactional
    public void markAsRead(UUID notificationId) {

        User user = getLoggedInUser();

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        if (!notification.getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot update this notification"
            );
        }

        notification.setReadStatus(true);

        notificationRepository.save(notification);
    }

    // ======================================================
    // MARK ALL AS READ
    // ======================================================

    @Override
    @Transactional
    public void markAllAsRead() {

        User user = getLoggedInUser();

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadStatusFalseOrderByCreatedAtDesc(
                                user.getId()
                        );

        for (Notification notification : notifications) {
            notification.setReadStatus(true);
        }

        notificationRepository.saveAll(notifications);
    }

    // ======================================================
    // GET LOGGED-IN USER
    // ======================================================

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    // ======================================================
    // MAP ENTITY → RESPONSE
    // ======================================================

    private NotificationResponse map(
            Notification notification
    ) {

        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.isReadStatus(),
                notification.getCreatedAt()
        );
    }
}