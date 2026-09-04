package com.mediconnect.mediconnectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyNhisRequest {

    @NotBlank(message = "NHIS number is required")
    @Pattern(
            regexp = "^\\d{8}$",
            message = "NHIS number must contain exactly 8 digits"
    )
    private String nhisNumber;
}