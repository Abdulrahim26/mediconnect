package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.PatientDashboardResponse;
import com.mediconnect.mediconnectapi.service.PatientDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/patient/dashboard")
@RequiredArgsConstructor
public class PatientDashboardController {


    private final PatientDashboardService patientDashboardService;



    @GetMapping
    public ResponseEntity<PatientDashboardResponse> getDashboard() {


        return ResponseEntity.ok(
                patientDashboardService.getDashboard()
        );

    }

}