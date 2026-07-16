package com.mediconnect.mediconnectapi.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class CreateHospitalAdminRequest {


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;


    @NotNull(message = "Hospital ID is required")
    private UUID hospitalId;

}