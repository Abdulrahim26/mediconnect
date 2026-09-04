package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.PatientVerificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientVerificationServiceImpl
        implements PatientVerificationService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PatientProfileResponse verifyGhanaCard(
            VerifyGhanaCardRequest request
    ) {

        Patient patient = getCurrentPatient();

        patientRepository
                .findByGhanaCardPin(request.getGhanaCardPin())
                .ifPresent(existingPatient -> {
                    if (!existingPatient.getId().equals(patient.getId())) {
                        throw new IllegalArgumentException(
                                "This Ghana Card PIN is already associated with another patient."
                        );
                    }
                });

        patient.setGhanaCardPin(request.getGhanaCardPin());
        patient.setGhanaCardVerificationStatus(
                VerificationStatus.PENDING
        );

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    @Override
    @Transactional
    public PatientProfileResponse verifyNhis(
            VerifyNhisRequest request
    ) {

        Patient patient = getCurrentPatient();

        patientRepository
                .findByNhisNumber(request.getNhisNumber())
                .ifPresent(existingPatient -> {
                    if (!existingPatient.getId().equals(patient.getId())) {
                        throw new IllegalArgumentException(
                                "This NHIS number is already associated with another patient."
                        );
                    }
                });

        patient.setNhisNumber(request.getNhisNumber());
        patient.setNhisVerificationStatus(
                VerificationStatus.PENDING
        );

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    private Patient getCurrentPatient() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return patientRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        )
                );
    }

    private PatientProfileResponse mapToResponse(
            Patient patient
    ) {

        return new PatientProfileResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getUser().getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getAddress(),

                // Ghana Card
                patient.getGhanaCardPin(),
                patient.getGhanaCardVerificationStatus(),

                // NHIS
                patient.getNhisNumber(),
                patient.getNhisVerificationStatus(),

                // Insurance
                patient.getInsuranceProvider(),
                patient.getIsNhisLinkedToGhanaCard(),
                patient.getInsuranceStatus()
        );
    }


}
