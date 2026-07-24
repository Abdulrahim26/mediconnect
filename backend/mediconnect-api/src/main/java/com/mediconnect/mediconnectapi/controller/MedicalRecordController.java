package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateMedicalRecordRequest;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;
import com.mediconnect.mediconnectapi.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {


    private final MedicalRecordService medicalRecordService;



    // Doctor creates a medical record
    @PostMapping
    public ResponseEntity<MedicalRecordResponse> createRecord(
            @RequestBody CreateMedicalRecordRequest request
    ){

        return ResponseEntity.ok(
                medicalRecordService.createRecord(request)
        );

    }



    // Patient views own medical records
    @GetMapping("/patient")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientRecords(){

        return ResponseEntity.ok(
                medicalRecordService.getPatientRecords()
        );

    }

}