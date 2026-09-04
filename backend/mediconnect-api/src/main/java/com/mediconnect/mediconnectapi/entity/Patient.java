package com.mediconnect.mediconnectapi.entity;

import com.mediconnect.mediconnectapi.entity.enums.InsuranceProvider;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {

    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    private String phone;


    private LocalDate dateOfBirth;


    private String gender;


    private String address;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // ==========================================================
    // GHANA CARD
    // ==========================================================

    @Pattern(
            regexp = "^GHA-\\d{9}-[A-Z0-9]$",
            message = "Ghana Card PIN format must follow GHA-XXXXXXXXX-X"
    )
    @Column(
            name = "ghana_card_pin",
            unique = true,
            length = 15
    )
    private String ghanaCardPin;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "ghana_card_verification_status",
            nullable = false
    )
    private VerificationStatus ghanaCardVerificationStatus =
            VerificationStatus.NOT_VERIFIED;


    // ==========================================================
    // NHIS
    // ==========================================================

    @Pattern(
            regexp = "^\\d{8}$",
            message = "NHIS number must be exactly 8 digits"
    )
    @Column(
            name = "nhis_number",
            unique = true,
            length = 8
    )
    private String nhisNumber;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "nhis_verification_status",
            nullable = false
    )
    private VerificationStatus nhisVerificationStatus =
            VerificationStatus.NOT_VERIFIED;


    // ==========================================================
    // INSURANCE
    // ==========================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "insurance_provider",
            nullable = false
    )
    private InsuranceProvider insuranceProvider =
            InsuranceProvider.CASH;


    @Column(
            name = "is_nhis_linked_to_ghana_card",
            nullable = false
    )
    private Boolean isNhisLinkedToGhanaCard = false;


    @Column(
            name = "insurance_status",
            nullable = false,
            length = 20
    )
    private String insuranceStatus = "INACTIVE";
}