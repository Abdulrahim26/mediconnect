package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.DoctorDashboardResponse;
import com.mediconnect.mediconnectapi.service.DoctorDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/api/doctor/dashboard")
@RequiredArgsConstructor
public class DoctorDashboardController {


    private final DoctorDashboardService doctorDashboardService;



    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDashboardResponse> getDashboard(
            @PathVariable UUID doctorId
    ) {


        return ResponseEntity.ok(
                doctorDashboardService.getDashboard(doctorId)
        );

    }

}