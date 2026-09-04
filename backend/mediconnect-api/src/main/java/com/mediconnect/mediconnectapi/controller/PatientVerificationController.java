package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;
import com.mediconnect.mediconnectapi.service.PatientVerificationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients/verification")
@RequiredArgsConstructor
public class PatientVerificationController {

    private final PatientVerificationService patientVerificationService;

    @PostMapping("/ghana-card")
    public ResponseEntity<PatientProfileResponse> verifyGhanaCard(
            @Valid @RequestBody VerifyGhanaCardRequest request
    ) {

        return ResponseEntity.ok(
                patientVerificationService.verifyGhanaCard(request)
        );
    }

    @PostMapping("/nhis")
    public ResponseEntity<PatientProfileResponse> verifyNhis(
            @Valid @RequestBody VerifyNhisRequest request
    ) {

        return ResponseEntity.ok(
                patientVerificationService.verifyNhis(request)
        );
    }
}

