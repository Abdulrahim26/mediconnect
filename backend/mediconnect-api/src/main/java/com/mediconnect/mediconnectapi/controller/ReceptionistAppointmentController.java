package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/receptionist/appointments")
@RequiredArgsConstructor
public class ReceptionistAppointmentController {

    private final ReceptionistAppointmentService receptionistAppointmentService;

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAppointments() {
        return ResponseEntity.ok(
                receptionistAppointmentService.getHospitalAppointments()
        );
    }

    // ✅ NEW: Search appointments with filters
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentResponse>> searchAppointments(

            @RequestParam(required = false) String patient,

            @RequestParam(required = false) String doctor,

            @RequestParam(required = false) AppointmentStatus status,

            @RequestParam(required = false) LocalDate date

    ) {
        return ResponseEntity.ok(
                receptionistAppointmentService.searchAppointments(
                        patient,
                        doctor,
                        status,
                        date
                )
        );
    }
}