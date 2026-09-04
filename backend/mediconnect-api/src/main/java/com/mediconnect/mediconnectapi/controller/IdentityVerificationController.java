package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.IdentityVerificationResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientIdentityStatusResponse;
import com.mediconnect.mediconnectapi.security.IdentityAuditContext;
import com.mediconnect.mediconnectapi.security.IdentityRateLimitExceededException;
import com.mediconnect.mediconnectapi.security.IdentityRateLimitService;
import com.mediconnect.mediconnectapi.security.IdentityVerificationType;
import com.mediconnect.mediconnectapi.service.IdentityVerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class IdentityVerificationController {

    private final IdentityVerificationService identityVerificationService;

    private final IdentityRateLimitService identityRateLimitService;

    @PostMapping("/ghana-card")
    @org.springframework.security.access.prepost.PreAuthorize(
            "hasRole('RECEPTIONIST')"
    )
    public ResponseEntity<IdentityVerificationResponse> verifyGhanaCard(
            @Valid @RequestBody VerifyGhanaCardRequest request,
            HttpServletRequest httpRequest
    ) {

        IdentityAuditContext auditContext =
                createAuditContext(httpRequest);

        checkRateLimit(
                auditContext,
                IdentityVerificationType.GHANA_CARD
        );

        return ResponseEntity.ok(
                identityVerificationService.verifyGhanaCard(
                        request,
                        auditContext
                )
        );
    }

    @PostMapping("/nhis")
    @org.springframework.security.access.prepost.PreAuthorize(
            "hasRole('RECEPTIONIST')"
    )
    public ResponseEntity<IdentityVerificationResponse> verifyNhis(
            @Valid @RequestBody VerifyNhisRequest request,
            HttpServletRequest httpRequest
    ) {

        IdentityAuditContext auditContext =
                createAuditContext(httpRequest);

        checkRateLimit(
                auditContext,
                IdentityVerificationType.NHIS
        );

        return ResponseEntity.ok(
                identityVerificationService.verifyNhis(
                        request,
                        auditContext
                )
        );
    }

    @GetMapping("/patient/{patientId}/status")
    @org.springframework.security.access.prepost.PreAuthorize(
            "hasRole('RECEPTIONIST')"
    )
    public ResponseEntity<PatientIdentityStatusResponse> getPatientIdentityStatus(
            @PathVariable UUID patientId
    ) {

        return ResponseEntity.ok(
                identityVerificationService.getPatientIdentityStatus(
                        patientId
                )
        );
    }

    /**
     * Applies the identity verification rate limit.
     *
     * Maximum:
     * 5 requests per authenticated user/IP combination
     * within a 60-second window.
     */
    private void checkRateLimit(
            IdentityAuditContext auditContext,
            IdentityVerificationType verificationType
    ) {

        String authenticatedUser =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        String ipAddress =
                auditContext.getIpAddress();

        boolean allowed =
                identityRateLimitService.isAllowed(
                        authenticatedUser,
                        ipAddress,
                        verificationType
                );

        if (!allowed) {

            long retryAfter =
                    identityRateLimitService
                            .getRetryAfterSeconds(
                                    authenticatedUser,
                                    ipAddress,
                                    verificationType
                            );

            throw new IdentityRateLimitExceededException(
                    "Too many identity verification requests. "
                            + "Please try again in "
                            + retryAfter
                            + " seconds."
            );
        }
    }

    private IdentityAuditContext createAuditContext(
            HttpServletRequest request
    ) {

        String ipAddress =
                getClientIpAddress(request);

        String userAgent =
                request.getHeader("User-Agent");

        if (userAgent == null || userAgent.isBlank()) {

            userAgent = "UNKNOWN";
        }

        return new IdentityAuditContext(
                ipAddress,
                userAgent
        );
    }

    private String getClientIpAddress(
            HttpServletRequest request
    ) {

        String forwardedFor =
                request.getHeader("X-Forwarded-For");

        if (forwardedFor != null
                && !forwardedFor.isBlank()) {

            return forwardedFor
                    .split(",")[0]
                    .trim();
        }

        String realIp =
                request.getHeader("X-Real-IP");

        if (realIp != null
                && !realIp.isBlank()) {

            return realIp;
        }

        return request.getRemoteAddr();
    }
}