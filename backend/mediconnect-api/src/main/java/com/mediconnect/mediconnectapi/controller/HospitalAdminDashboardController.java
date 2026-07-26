package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.HospitalAdminDashboardResponse;
import com.mediconnect.mediconnectapi.service.HospitalAdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hospital-admin/dashboard")
@RequiredArgsConstructor
public class HospitalAdminDashboardController {


    private final HospitalAdminDashboardService dashboardService;



    @GetMapping
    public ResponseEntity<HospitalAdminDashboardResponse> getDashboard(){

        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );

    }

}