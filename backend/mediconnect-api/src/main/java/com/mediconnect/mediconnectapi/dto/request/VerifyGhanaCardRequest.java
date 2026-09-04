package com.mediconnect.mediconnectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyGhanaCardRequest {

    @NotBlank(message = "Ghana Card PIN is required")
    @Pattern(
            regexp = "^GHA-\\d{9}-[A-Z0-9]$",
            message = "Ghana Card PIN must follow the format GHA-XXXXXXXXX-X"
    )
    private String ghanaCardPin;
}