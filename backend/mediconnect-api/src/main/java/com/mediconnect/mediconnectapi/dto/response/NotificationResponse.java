package com.mediconnect.mediconnectapi.dto.response;


import com.mediconnect.mediconnectapi.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {


    private UUID id;


    private String message;


    private NotificationType type;


    private boolean readStatus;


    private LocalDateTime createdAt;

}