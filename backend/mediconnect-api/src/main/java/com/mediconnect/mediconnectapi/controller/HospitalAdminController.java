package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.response.UserResponse;
import com.mediconnect.mediconnectapi.service.HospitalAdminService;


import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/hospital-admins")
@RequiredArgsConstructor
public class HospitalAdminController {


    private final HospitalAdminService hospitalAdminService;



    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createHospitalAdmin(
            @Valid @RequestBody CreateHospitalAdminRequest request
    ){

        return ResponseEntity.ok(
                hospitalAdminService.createHospitalAdmin(request)
        );

    }

}