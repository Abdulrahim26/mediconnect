package com.mediconnect.mediconnectapi.dto.response;

import com.mediconnect.mediconnectapi.entity.enums.IdentityType;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class IdentityVerificationResponse {

    private boolean verified;

    private IdentityType identityType;

    private VerificationStatus verificationStatus;

    private String maskedIdentifier;

    private UUID patientId;

    private String firstName;

    private String lastName;

    private String message;
}