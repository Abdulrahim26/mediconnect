package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.response.NotificationResponse;
import com.mediconnect.mediconnectapi.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // ======================================================
    // GET MY NOTIFICATIONS
    // ======================================================

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    // ======================================================
    // GET UNREAD COUNT
    // ======================================================

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {

        return ResponseEntity.ok(
                notificationService.getUnreadCount()
        );
    }

    // ======================================================
    // MARK ONE AS READ
    // ======================================================

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable UUID id
    ) {

        notificationService.markAsRead(id);

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }

    // ======================================================
    // MARK ALL AS READ
    // ======================================================

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead() {

        notificationService.markAllAsRead();

        return ResponseEntity.ok(
                "All notifications marked as read"
        );
    }
}