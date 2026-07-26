package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.ReceptionistDashboardResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/receptionist/dashboard")
@RequiredArgsConstructor
public class ReceptionistDashboardController {


    private final ReceptionistDashboardService receptionistDashboardService;



    @GetMapping
    public ResponseEntity<ReceptionistDashboardResponse> getDashboard(){


        return ResponseEntity.ok(
                receptionistDashboardService.getDashboard()
        );

    }

}