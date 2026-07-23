package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.service.AppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/doctor/appointments")
@RequiredArgsConstructor
public class DoctorAppointmentController {


    private final AppointmentService appointmentService;



    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments() {

        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments()
        );
    }



    @PutMapping("/{appointmentId}/approve")
    public ResponseEntity<AppointmentResponse> approveAppointment(
            @PathVariable UUID appointmentId
    ) {

        return ResponseEntity.ok(
                appointmentService.approveAppointment(
                        appointmentId
                )
        );
    }



    @PutMapping("/{appointmentId}/reject")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable UUID appointmentId
    ) {

        return ResponseEntity.ok(
                appointmentService.rejectAppointment(
                        appointmentId
                )
        );
    }



    @PutMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable UUID appointmentId
    ) {

        return ResponseEntity.ok(
                appointmentService.completeAppointment(
                        appointmentId
                )
        );
    }

}