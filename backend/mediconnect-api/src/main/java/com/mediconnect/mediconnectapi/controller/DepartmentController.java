package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.CreateDepartmentRequest;
import com.mediconnect.mediconnectapi.dto.response.DepartmentResponse;
import com.mediconnect.mediconnectapi.service.DepartmentService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {


    private final DepartmentService departmentService;


    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request
    ) {

        return ResponseEntity.ok(
                departmentService.createDepartment(request)
        );
    }


    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getDepartments(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size,

            @RequestParam(
                    required = false
            )
            String search

    ) {

        return ResponseEntity.ok(
                departmentService.getDepartments(
                        page,
                        size,
                        search
                )
        );
    }
}