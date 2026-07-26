package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/receptionist/appointments")
@RequiredArgsConstructor
public class ReceptionistAppointmentController {


    private final ReceptionistAppointmentService receptionistAppointmentService;



    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getHospitalAppointments() {


        return ResponseEntity.ok(
                receptionistAppointmentService.getHospitalAppointments()
        );

    }

}