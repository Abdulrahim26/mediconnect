package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReceptionistResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String hospitalName;
}