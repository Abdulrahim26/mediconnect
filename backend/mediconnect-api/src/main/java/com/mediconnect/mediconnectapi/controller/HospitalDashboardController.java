package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.HospitalDashboardResponse;
import com.mediconnect.mediconnectapi.service.HospitalDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/hospital/dashboard")
@RequiredArgsConstructor
public class HospitalDashboardController {


    private final HospitalDashboardService hospitalDashboardService;



    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalDashboardResponse> getDashboard(
            @PathVariable UUID hospitalId
    ) {


        return ResponseEntity.ok(
                hospitalDashboardService.getDashboard(hospitalId)
        );

    }

}