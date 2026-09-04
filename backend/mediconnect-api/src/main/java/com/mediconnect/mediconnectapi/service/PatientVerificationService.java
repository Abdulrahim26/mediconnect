package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.VerifyGhanaCardRequest;
import com.mediconnect.mediconnectapi.dto.request.VerifyNhisRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;

public interface PatientVerificationService {

    PatientProfileResponse verifyGhanaCard(
            VerifyGhanaCardRequest request
    );

    PatientProfileResponse verifyNhis(
            VerifyNhisRequest request
    );
}
