package com.mediconnect.mediconnectapi.dto.response;

import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PatientIdentityStatusResponse {

    private UUID patientId;

    private VerificationStatus ghanaCardStatus;

    private VerificationStatus nhisStatus;

    private boolean identityVerified;
}