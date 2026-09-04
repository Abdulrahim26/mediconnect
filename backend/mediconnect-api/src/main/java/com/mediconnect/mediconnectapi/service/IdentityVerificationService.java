package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.IdentityVerificationResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientIdentityStatusResponse;
import com.mediconnect.mediconnectapi.security.IdentityAuditContext;

import java.util.UUID;

public interface IdentityVerificationService {

    IdentityVerificationResponse verifyGhanaCard(
            VerifyGhanaCardRequest request,
            IdentityAuditContext auditContext
    );

    IdentityVerificationResponse verifyNhis(
            VerifyNhisRequest request,
            IdentityAuditContext auditContext
    );

    PatientIdentityStatusResponse getPatientIdentityStatus(
            UUID patientId
    );
}