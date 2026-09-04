package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorProfileRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    // ======================================================
    // 1. CREATE DOCTOR
    // ====================================================== // id="bppj65"
    DoctorResponse createDoctor(
            CreateDoctorRequest request
    );

    // ======================================================
    // 2. GET ALL DOCTORS IN HOSPITAL
    // ====================================================== // id="q66ari"
    List<DoctorResponse> getHospitalDoctors();

    // ======================================================
    // 3. GET SINGLE DOCTOR
    // ====================================================== // id="q0q4k8"
    DoctorResponse getDoctor(
            UUID doctorId
    );

    // ======================================================
    // 4. UPDATE DOCTOR (HOSPITAL ADMIN)
    // ====================================================== // id="vgrr1o"
    DoctorResponse updateDoctor(
            UUID doctorId,
            UpdateDoctorRequest request
    );

    // ======================================================
    // 5. DEACTIVATE DOCTOR
    // ====================================================== // id="9qk372"
    String deactivateDoctor(
            UUID doctorId
    );

    // ======================================================
    // 6. DOCTOR VIEW OWN PROFILE
    // ====================================================== // id="2wjgkx"
    DoctorResponse getMyProfile();

    // ======================================================
    // 7. DOCTOR UPDATE OWN PROFILE
    // ====================================================== // id="10lfvc"
    DoctorResponse updateMyProfile(
            UpdateDoctorProfileRequest request
    );

}