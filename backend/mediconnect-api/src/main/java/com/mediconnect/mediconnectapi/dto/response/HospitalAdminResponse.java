package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class HospitalAdminResponse {

    private UUID id;
    private String email;
    private String role;
    private UUID hospitalId;
    private String hospitalName;
    private boolean active;
}