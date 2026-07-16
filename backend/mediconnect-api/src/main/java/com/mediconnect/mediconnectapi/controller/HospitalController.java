package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateHospitalRequest;
import com.mediconnect.mediconnectapi.dto.response.HospitalResponse;
import com.mediconnect.mediconnectapi.service.HospitalService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {


    private final HospitalService hospitalService;



    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(
            @Valid @RequestBody CreateHospitalRequest request
    ){

        return ResponseEntity.ok(
                hospitalService.createHospital(request)
        );
    }



    @GetMapping
    public ResponseEntity<List<HospitalResponse>> getAllHospitals(){

        return ResponseEntity.ok(
                hospitalService.getAllHospitals()
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospitalById(
            @PathVariable UUID id
    ){

        return ResponseEntity.ok(
                hospitalService.getHospitalById(id)
        );
    }

}