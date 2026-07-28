package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateReceptionistProfileRequest;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    // ======================================================
    // 1. HOSPITAL ADMIN CREATES RECEPTIONIST
    // ======================================================
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @PostMapping
    public ResponseEntity<ReceptionistResponse> createReceptionist(
            @Valid @RequestBody CreateReceptionistRequest request
    ) {
        return ResponseEntity.ok(
                receptionistService.createReceptionist(request)
        );
    }

    // ======================================================
    // 2. HOSPITAL ADMIN VIEWS RECEPTIONISTS IN THEIR HOSPITAL
    // ======================================================
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReceptionistResponse>> getReceptionists() {
        return ResponseEntity.ok(
                receptionistService.getMyHospitalReceptionists()
        );
    }

    // ======================================================
    // 3. RECEPTIONIST VIEWS OWN PROFILE
    // ======================================================
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/profile")
    public ResponseEntity<ReceptionistResponse> getMyProfile() {
        return ResponseEntity.ok(
                receptionistService.getMyProfile()
        );
    }

    // ======================================================
    // 4. RECEPTIONIST UPDATES OWN PROFILE
    // ======================================================
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PutMapping("/profile")
    public ResponseEntity<ReceptionistResponse> updateMyProfile(
            @Valid @RequestBody UpdateReceptionistProfileRequest request
    ) {
        return ResponseEntity.ok(
                receptionistService.updateMyProfile(request)
        );
    }
}