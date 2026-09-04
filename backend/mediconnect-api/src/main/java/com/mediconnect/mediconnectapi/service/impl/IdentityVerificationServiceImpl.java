package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.IdentityVerificationResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientIdentityStatusResponse;
import com.mediconnect.mediconnectapi.entity.IdentityLookupAuditLog;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.Receptionist;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.IdentityType;
import com.mediconnect.mediconnectapi.entity.enums.VerificationAction;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.IdentityLookupAuditLogRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.security.IdentityAuditContext;
import com.mediconnect.mediconnectapi.service.IdentityVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentityVerificationServiceImpl
        implements IdentityVerificationService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ReceptionistRepository receptionistRepository;
    private final IdentityLookupAuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public IdentityVerificationResponse verifyGhanaCard(
            VerifyGhanaCardRequest request,
            IdentityAuditContext auditContext
    ) {

        User user = getAuthenticatedUser();

        Receptionist receptionist = getAuthenticatedReceptionist(user);

        String normalizedPin = normalizeGhanaCard(request.getGhanaCardPin());

        Patient patient = patientRepository
                .findByGhanaCardPin(normalizedPin)
                .orElse(null);

        if (patient == null) {

            saveAuditLog(
                    user,
                    IdentityType.GHANA_CARD,
                    VerificationAction.GHANA_CARD_LOOKUP.name(),
                    maskGhanaCard(normalizedPin),
                    false,
                    "Ghana Card was not found",
                    receptionist,
                    auditContext
            );

            return new IdentityVerificationResponse(
                    false,
                    IdentityType.GHANA_CARD,
                    VerificationStatus.FAILED,
                    maskGhanaCard(normalizedPin),
                    null,
                    null,
                    null,
                    "Ghana Card was not found"
            );
        }

        verifyHospitalAccess(patient, receptionist);

        patient.setGhanaCardVerificationStatus(
                VerificationStatus.VERIFIED
        );

        patientRepository.save(patient);

        saveAuditLog(
                user,
                IdentityType.GHANA_CARD,
                VerificationAction.GHANA_CARD_VERIFICATION.name(),
                maskGhanaCard(normalizedPin),
                true,
                null,
                receptionist,
                auditContext
        );

        return new IdentityVerificationResponse(
                true,
                IdentityType.GHANA_CARD,
                VerificationStatus.VERIFIED,
                maskGhanaCard(normalizedPin),
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                "Ghana Card identity verified successfully"
        );
    }

    @Override
    @Transactional
    public IdentityVerificationResponse verifyNhis(
            VerifyNhisRequest request,
            IdentityAuditContext auditContext
    ) {

        User user = getAuthenticatedUser();

        Receptionist receptionist = getAuthenticatedReceptionist(user);

        String normalizedNumber =
                normalizeNhis(request.getNhisNumber());

        Patient patient = patientRepository
                .findByNhisNumber(normalizedNumber)
                .orElse(null);

        if (patient == null) {

            saveAuditLog(
                    user,
                    IdentityType.NHIS,
                    VerificationAction.NHIS_LOOKUP.name(),
                    maskNhis(normalizedNumber),
                    false,
                    "NHIS number was not found",
                    receptionist,
                    auditContext
            );

            return new IdentityVerificationResponse(
                    false,
                    IdentityType.NHIS,
                    VerificationStatus.FAILED,
                    maskNhis(normalizedNumber),
                    null,
                    null,
                    null,
                    "NHIS number was not found"
            );
        }

        verifyHospitalAccess(patient, receptionist);

        patient.setNhisVerificationStatus(
                VerificationStatus.VERIFIED
        );

        patientRepository.save(patient);

        saveAuditLog(
                user,
                IdentityType.NHIS,
                VerificationAction.NHIS_VERIFICATION.name(),
                maskNhis(normalizedNumber),
                true,
                null,
                receptionist,
                auditContext
        );

        return new IdentityVerificationResponse(
                true,
                IdentityType.NHIS,
                VerificationStatus.VERIFIED,
                maskNhis(normalizedNumber),
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                "NHIS identity verified successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PatientIdentityStatusResponse getPatientIdentityStatus(
            UUID patientId
    ) {

        User user = getAuthenticatedUser();

        Receptionist receptionist =
                getAuthenticatedReceptionist(user);

        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found"
                        )
                );

        verifyHospitalAccess(
                patient,
                receptionist
        );

        VerificationStatus ghanaCardStatus =
                patient.getGhanaCardVerificationStatus();

        VerificationStatus nhisStatus =
                patient.getNhisVerificationStatus();

        boolean identityVerified =
                ghanaCardStatus == VerificationStatus.VERIFIED
                        || nhisStatus == VerificationStatus.VERIFIED;

        return new PatientIdentityStatusResponse(
                patient.getId(),
                ghanaCardStatus,
                nhisStatus,
                identityVerified
        );
    }

    private User getAuthenticatedUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found"
                        )
                );
    }

    private Receptionist getAuthenticatedReceptionist(
            User user
    ) {

        return receptionistRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receptionist profile not found"
                        )
                );
    }

    private void verifyHospitalAccess(
            Patient patient,
            Receptionist receptionist
    ) {

        if (patient.getUser().getHospital() == null) {
            throw new AccessDeniedException(
                    "Patient is not associated with a hospital"
            );
        }

        if (receptionist.getHospital() == null) {
            throw new AccessDeniedException(
                    "Receptionist is not associated with a hospital"
            );
        }

        if (!patient.getUser()
                .getHospital()
                .getId()
                .equals(receptionist.getHospital().getId())) {

            throw new AccessDeniedException(
                    "Access denied: patient belongs to another hospital"
            );
        }
    }

    private String normalizeGhanaCard(String value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Ghana Card PIN is required"
            );
        }

        return value
                .trim()
                .toUpperCase();
    }

    private String normalizeNhis(String value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "NHIS number is required"
            );
        }

        return value.trim();
    }

    private String maskGhanaCard(String pin) {

        if (pin == null || pin.length() < 6) {
            return "GHA-*********-*";
        }

        return pin.substring(0, 4)
                + "*********"
                + pin.substring(pin.length() - 2);
    }

    private String maskNhis(String number) {

        if (number == null || number.length() < 4) {
            return "****";
        }

        return "****"
                + number.substring(number.length() - 4);
    }

    private void saveAuditLog(
            User user,
            IdentityType identityType,
            String actionType,
            String maskedIdentifier,
            boolean successful,
            String failureReason,
            Receptionist receptionist,
            IdentityAuditContext auditContext
    ) {

        IdentityLookupAuditLog auditLog =
                new IdentityLookupAuditLog();

        auditLog.setPerformedByUser(user);

        auditLog.setActionType(actionType);

        auditLog.setIdentifierType(
                identityType.name()
        );

        auditLog.setMaskedIdentifier(
                maskedIdentifier
        );

        auditLog.setSuccessful(
                successful
        );

        auditLog.setFailureReason(
                failureReason
        );

        auditLog.setIpAddress(
                auditContext.getIpAddress()
        );

        auditLog.setUserAgent(
                auditContext.getUserAgent()
        );

        auditLog.setProvider(
                "INTERNAL"
        );

        auditLog.setQueriedAt(
                LocalDateTime.now()
        );

        auditLogRepository.save(auditLog);
    }
}