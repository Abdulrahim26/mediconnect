package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.response.HospitalAdminDetailsResponse;
import com.mediconnect.mediconnectapi.dto.response.HospitalAdminResponse;
import com.mediconnect.mediconnectapi.dto.response.UserResponse;
import com.mediconnect.mediconnectapi.service.HospitalAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hospital-admins")
@RequiredArgsConstructor
public class HospitalAdminController {

    private final HospitalAdminService hospitalAdminService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createHospitalAdmin(
            @Valid @RequestBody CreateHospitalAdminRequest request
    ) {
        return ResponseEntity.ok(
                hospitalAdminService.createHospitalAdmin(request)
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<HospitalAdminResponse>> getAllHospitalAdmins() {
        return ResponseEntity.ok(
                hospitalAdminService.getAllHospitalAdmins()
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<HospitalAdminDetailsResponse> getHospitalAdminDetails(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                hospitalAdminService.getHospitalAdminDetails(id)
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HospitalAdminDetailsResponse> updateHospitalAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateHospitalAdminRequest request
    ) {
        return ResponseEntity.ok(
                hospitalAdminService.updateHospitalAdmin(id, request)
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateHospitalAdmin(
            @PathVariable UUID id
    ) {
        hospitalAdminService.activateHospitalAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateHospitalAdmin(
            @PathVariable UUID id
    ) {
        hospitalAdminService.deactivateHospitalAdmin(id);
        return ResponseEntity.noContent().build();
    }
}