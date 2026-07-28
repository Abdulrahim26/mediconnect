package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.ReceptionistDashboardResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/receptionist")
@RequiredArgsConstructor
public class ReceptionistDashboardController {


    private final ReceptionistDashboardService receptionistDashboardService;



    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/dashboard")
    public ResponseEntity<ReceptionistDashboardResponse> dashboard(){

        return ResponseEntity.ok(
                receptionistDashboardService.getDashboard()
        );

    }

}