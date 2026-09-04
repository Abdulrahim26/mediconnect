package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class HospitalAdminDetailsResponse {

    private UUID id;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private UUID hospitalId;
    private String hospitalName;
    private String hospitalLocation;
    private String hospitalAddress;
    private String hospitalPhone;
    private String hospitalEmail;
    private boolean active;
}