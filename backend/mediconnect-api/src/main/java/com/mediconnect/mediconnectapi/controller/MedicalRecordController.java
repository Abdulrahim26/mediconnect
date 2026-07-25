package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateMedicalRecordRequest;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;
import com.mediconnect.mediconnectapi.service.MedicalRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {


    private final MedicalRecordService medicalRecordService;



    // Doctor creates medical record
    @PostMapping
    public ResponseEntity<MedicalRecordResponse> createRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request
    ) {

        return ResponseEntity.ok(
                medicalRecordService.createRecord(request)
        );

    }



    // Patient views own medical records
    @GetMapping("/my-records")
    public ResponseEntity<List<MedicalRecordResponse>> getMyRecords() {

        return ResponseEntity.ok(
                medicalRecordService.getPatientRecords()
        );

    }

}