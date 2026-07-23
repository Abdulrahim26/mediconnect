package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.UpdatePatientProfileRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;
import com.mediconnect.mediconnectapi.service.PatientService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {


    private final PatientService patientService;



    @GetMapping("/profile")
    public ResponseEntity<PatientProfileResponse> getProfile() {

        return ResponseEntity.ok(
                patientService.getProfile()
        );
    }



    @PutMapping("/profile")
    public ResponseEntity<PatientProfileResponse> updateProfile(
            @RequestBody UpdatePatientProfileRequest request
    ) {

        return ResponseEntity.ok(
                patientService.updateProfile(request)
        );
    }

}