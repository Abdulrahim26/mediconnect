package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;
import com.mediconnect.mediconnectapi.service.DoctorService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {



    private final DoctorService doctorService;



    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request
    ){

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );

    }


}