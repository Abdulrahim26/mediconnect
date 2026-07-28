package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.service.AppointmentService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ======================================================
    // PATIENT: BOOK APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        return ResponseEntity.ok(
                appointmentService.bookAppointment(request)
        );
    }

    // ======================================================
    // PATIENT: VIEW ALL APPOINTMENTS
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(
                appointmentService.getMyAppointments()
        );
    }

    // ======================================================
    // PATIENT: UPCOMING APPOINTMENTS
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments() {
        return ResponseEntity.ok(
                appointmentService.getUpcomingAppointments()
        );
    }

    // ======================================================
    // PATIENT: APPOINTMENT HISTORY
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/history")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentHistory() {
        return ResponseEntity.ok(
                appointmentService.getAppointmentHistory()
        );
    }

    // ======================================================
    // PATIENT: CANCEL APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id)
        );
    }

    // ======================================================
    // PATIENT: RESCHEDULE APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(id, request)
        );
    }

    // ======================================================
    // DOCTOR: VIEW ALL APPOINTMENTS
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments() {
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments()
        );
    }

    // ======================================================
    // DOCTOR: APPROVE APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AppointmentResponse> approveAppointment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.approveAppointment(id)
        );
    }

    // ======================================================
    // DOCTOR: REJECT APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.rejectAppointment(id)
        );
    }

    // ======================================================
    // DOCTOR: COMPLETE APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.completeAppointment(id)
        );
    }

    // ======================================================
    // DOCTOR: CANCEL APPOINTMENT
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/doctor-cancel")
    public ResponseEntity<AppointmentResponse> doctorCancelAppointment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.doctorCancelAppointment(id)
        );
    }

    // ======================================================
    // RECEPTIONIST: VIEW HOSPITAL APPOINTMENTS
    // ======================================================
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/hospital")
    public ResponseEntity<List<AppointmentResponse>> getHospitalAppointments() {
        return ResponseEntity.ok(
                appointmentService.getHospitalAppointments()
        );
    }

    // ======================================================
    // RECEPTIONIST: FILTER BY STATUS
    // ======================================================
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/hospital/status/{status}")
    public ResponseEntity<List<AppointmentResponse>> getHospitalAppointmentsByStatus(
            @PathVariable AppointmentStatus status
    ) {
        return ResponseEntity.ok(
                appointmentService.getHospitalAppointmentsByStatus(status)
        );
    }

    // ======================================================
    // RECEPTIONIST: CHECK-IN PATIENT
    // ======================================================
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PutMapping("/{id}/check-in")
    public ResponseEntity<AppointmentResponse> checkInPatient(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                appointmentService.checkInPatient(id)
        );
    }

    // ======================================================
    // DOCTOR VIEW WAITING QUEUE
    // ======================================================
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/waiting")
    public ResponseEntity<List<AppointmentResponse>> getWaitingQueue() {
        return ResponseEntity.ok(
                appointmentService.getWaitingQueue()
        );
    }
}