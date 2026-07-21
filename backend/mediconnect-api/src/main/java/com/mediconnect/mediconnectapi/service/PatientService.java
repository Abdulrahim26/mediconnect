package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.UpdatePatientProfileRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;

public interface PatientService {

    PatientProfileResponse getProfile();

    PatientProfileResponse updateProfile(UpdatePatientProfileRequest request);

}