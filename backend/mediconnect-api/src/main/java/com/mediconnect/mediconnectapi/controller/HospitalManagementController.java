package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.response.HospitalAdminDashboardResponse;
import com.mediconnect.mediconnectapi.service.HospitalAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospital-management")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HOSPITAL_ADMIN')")
public class HospitalManagementController {

    private final HospitalAdminDashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<HospitalAdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );
    }
}