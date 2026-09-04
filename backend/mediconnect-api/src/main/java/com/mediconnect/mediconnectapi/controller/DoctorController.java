package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorProfileRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;
import com.mediconnect.mediconnectapi.service.DoctorService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // ======================================================
    // 1. CREATE DOCTOR
    // ====================================================== // id="bppj65"
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );

    }

    // ======================================================
    // 2. GET ALL DOCTORS IN HOSPITAL
    // ====================================================== // id="q66ari"
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getHospitalDoctors() {

        return ResponseEntity.ok(
                doctorService.getHospitalDoctors()
        );

    }

    // ======================================================
    // 3. GET SINGLE DOCTOR
    // ====================================================== // id="q0q4k8"
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctor(id)
        );

    }

    // ======================================================
    // 4. UPDATE DOCTOR (HOSPITAL ADMIN)
    // ======================================================
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDoctorRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request)
        );
    }

    // ======================================================
    // 5. DOCTOR VIEWS OWN PROFILE (/profile)
    // ====================================================== // id="new1"
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/profile")
    public ResponseEntity<DoctorResponse> getMyProfile() {

        return ResponseEntity.ok(
                doctorService.getMyProfile()
        );

    }

    // ======================================================
    // 6. DOCTOR UPDATES OWN PROFILE (/profile)
    // ====================================================== // id="new2"
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/profile")
    public ResponseEntity<DoctorResponse> updateMyProfile(
            @Valid @RequestBody UpdateDoctorProfileRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.updateMyProfile(request)
        );

    }

    // ======================================================
    // 7. DOCTOR VIEWS OWN PROFILE (/me)
    // ====================================================== // id="me1"
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/me")
    public ResponseEntity<DoctorResponse> getMyProfileMe() {

        return ResponseEntity.ok(
                doctorService.getMyProfile()
        );

    }

    // ======================================================
    // 8. DOCTOR UPDATES OWN PROFILE (/me)
    // ====================================================== // id="me2"
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/me")
    public ResponseEntity<DoctorResponse> updateMyProfileMe(
            @Valid @RequestBody UpdateDoctorProfileRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.updateMyProfile(request)
        );

    }

    // ======================================================
    // 9. DEACTIVATE DOCTOR
    // ====================================================== // id="9qk372"
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateDoctor(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                doctorService.deactivateDoctor(id)
        );

    }

}