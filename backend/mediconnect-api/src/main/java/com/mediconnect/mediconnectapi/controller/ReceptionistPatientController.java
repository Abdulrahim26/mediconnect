package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreatePatientRequest;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistPatientResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistPatientService;
import com.mediconnect.mediconnectapi.service.ReceptionistPatientManagementService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionist/patients")
@RequiredArgsConstructor
public class ReceptionistPatientController {

    private final ReceptionistPatientService receptionistPatientService;
    private final ReceptionistPatientManagementService receptionistPatientManagementService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> registerPatient(
            @Valid @RequestBody CreatePatientRequest request
    ) {
        return ResponseEntity.ok(
                receptionistPatientService.registerPatient(request)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ReceptionistPatientResponse>> getPatients() {
        return ResponseEntity.ok(
                receptionistPatientManagementService.getPatients()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ReceptionistPatientResponse>> searchPatients(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(
                receptionistPatientManagementService.searchPatients(name)
        );
    }
}