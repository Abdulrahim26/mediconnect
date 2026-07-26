package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/receptionist/appointments")
@RequiredArgsConstructor
public class ReceptionistAppointmentController {


    private final ReceptionistAppointmentService receptionistAppointmentService;



    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateReceptionistAppointmentRequest request
    ){


        return ResponseEntity.ok(
                receptionistAppointmentService.createAppointment(request)
        );

    }


}