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



    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {


        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );

    }



    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable UUID id
    ) {


        notificationService.markAsRead(id);


        return ResponseEntity.ok(
                "Notification marked as read"
        );

    }

}