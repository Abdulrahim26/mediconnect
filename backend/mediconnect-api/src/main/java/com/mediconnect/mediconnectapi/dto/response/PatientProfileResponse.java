
        package com.mediconnect.mediconnectapi.dto.response;

import com.mediconnect.mediconnectapi.entity.enums.InsuranceProvider;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PatientProfileResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    private String address;

    // ==========================================================
    // GHANA CARD
    // ==========================================================

    private String ghanaCardPin;

    private VerificationStatus ghanaCardVerificationStatus;

    // ==========================================================
    // NHIS
    // ==========================================================

    private String nhisNumber;

    private VerificationStatus nhisVerificationStatus;

    // ==========================================================
    // INSURANCE
    // ==========================================================

    private InsuranceProvider insuranceProvider;

    private Boolean isNhisLinkedToGhanaCard;

    private String insuranceStatus;
}

