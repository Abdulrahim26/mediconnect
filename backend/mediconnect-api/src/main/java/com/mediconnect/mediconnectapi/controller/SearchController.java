package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.response.AppointmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DepartmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DoctorSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.HospitalSearchResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorSearchResponse>> searchDoctors(

            @RequestParam(required = false)
            String firstName,

            @RequestParam(required = false)
            String lastName,

            @RequestParam(required = false)
            String specialty,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String hospital,

            @RequestParam(required = false)
            String location,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy

    ) {

        return ResponseEntity.ok(

                searchService.searchDoctors(

                        firstName,

                        lastName,

                        specialty,

                        department,

                        hospital,

                        location,

                        page,

                        size,

                        sortBy

                )

        );

    }

    @GetMapping("/hospitals")
    public ResponseEntity<Page<HospitalSearchResponse>> searchHospitals(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String location,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy

    ) {

        return ResponseEntity.ok(

                searchService.searchHospitals(

                        name,

                        location,

                        page,

                        size,

                        sortBy

                )

        );

    }

    @GetMapping("/departments")
    public ResponseEntity<Page<DepartmentSearchResponse>> searchDepartments(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String hospital,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy

    ) {

        return ResponseEntity.ok(

                searchService.searchDepartments(

                        name,

                        hospital,

                        page,

                        size,

                        sortBy

                )

        );

    }

    @GetMapping("/appointments")
    public ResponseEntity<Page<AppointmentSearchResponse>> searchAppointments(

            @RequestParam(required = false)
            AppointmentStatus status,

            @RequestParam(required = false)
            LocalDate date,

            @RequestParam(required = false)
            String doctor,

            @RequestParam(required = false)
            String patient,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "appointmentDate")
            String sortBy

    ) {

        return ResponseEntity.ok(

                searchService.searchAppointments(

                        status,

                        date,

                        doctor,

                        patient,

                        page,

                        size,

                        sortBy

                )

        );

    }
}