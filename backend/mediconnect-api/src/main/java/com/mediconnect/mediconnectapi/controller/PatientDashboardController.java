package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.PatientDashboardResponse;
import com.mediconnect.mediconnectapi.service.PatientDashboardService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/api/patient/dashboard")
@RequiredArgsConstructor
public class PatientDashboardController {


    private final PatientDashboardService patientDashboardService;



    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDashboardResponse> getDashboard(
            @PathVariable UUID patientId
    ) {


        return ResponseEntity.ok(
                patientDashboardService.getDashboard(patientId)
        );

    }

}