package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.response.DoctorDashboardResponse;
import com.mediconnect.mediconnectapi.service.DoctorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor/dashboard")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                doctorDashboardService.getDashboard()
        );
    }
}