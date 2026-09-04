package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.request.RescheduleAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdatePatientProfileRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;
import com.mediconnect.mediconnectapi.service.AppointmentService;
import com.mediconnect.mediconnectapi.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @GetMapping("/profile")
    public ResponseEntity<PatientProfileResponse> getProfile() {
        return ResponseEntity.ok(
                patientService.getProfile()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<PatientProfileResponse> updateProfile(
            @Valid @RequestBody UpdatePatientProfileRequest request
    ) {
        return ResponseEntity.ok(
                patientService.updateProfile(request)
        );
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(
                appointmentService.getMyAppointments()
        );
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<List<AppointmentResponse>> upcoming() {
        return ResponseEntity.ok(
                appointmentService.getUpcomingAppointments()
        );
    }

    @GetMapping("/appointments/history")
    public ResponseEntity<List<AppointmentResponse>> history() {
        return ResponseEntity.ok(
                appointmentService.getAppointmentHistory()
        );
    }

    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id)
        );
    }

    @PutMapping("/appointments/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(
            @PathVariable UUID id,
            @Valid @RequestBody RescheduleAppointmentRequest request
    ) {
        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(id, request)
        );
    }
}