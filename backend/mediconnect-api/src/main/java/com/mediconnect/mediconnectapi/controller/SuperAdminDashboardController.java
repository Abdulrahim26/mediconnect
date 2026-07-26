package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.SuperAdminDashboardResponse;
import com.mediconnect.mediconnectapi.service.SuperAdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/super-admin/dashboard")
@RequiredArgsConstructor
public class SuperAdminDashboardController {


    private final SuperAdminDashboardService dashboardService;


    @GetMapping
    public ResponseEntity<SuperAdminDashboardResponse> getDashboard(){


        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );

    }

}