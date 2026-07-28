package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateMedicalRecordRequest;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;
import com.mediconnect.mediconnectapi.service.MedicalRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {


    private final MedicalRecordService medicalRecordService;



    // ==============================
    // DOCTOR CREATES MEDICAL RECORD
    // ==============================

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ResponseEntity<MedicalRecordResponse> createRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request
    ) {

        return ResponseEntity.ok(
                medicalRecordService.createRecord(request)
        );

    }



    // ==============================
    // PATIENT VIEW ALL RECORDS
    // ==============================

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my-records")
    public ResponseEntity<List<MedicalRecordResponse>> getMyRecords() {

        return ResponseEntity.ok(
                medicalRecordService.getPatientRecords()
        );

    }



    // ==============================
    // PATIENT VIEW SINGLE RECORD
    // ==============================

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my-records/{id}")
    public ResponseEntity<MedicalRecordResponse> getMyRecord(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                medicalRecordService.getPatientRecord(id)
        );

    }



    // ==============================
    // DOCTOR VIEW ALL CREATED RECORDS
    // ==============================

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<MedicalRecordResponse>> getDoctorRecords() {

        return ResponseEntity.ok(
                medicalRecordService.getDoctorRecords()
        );

    }



    // ==============================
    // DOCTOR VIEW SINGLE RECORD
    // ==============================

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor/{id}")
    public ResponseEntity<MedicalRecordResponse> getDoctorRecord(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                medicalRecordService.getDoctorRecord(id)
        );

    }



    // ==============================
    // DOCTOR UPDATE RECORD
    // ==============================

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponse> updateRecord(
            @PathVariable UUID id,
            @Valid @RequestBody CreateMedicalRecordRequest request
    ) {

        return ResponseEntity.ok(
                medicalRecordService.updateRecord(
                        id,
                        request
                )
        );

    }

}