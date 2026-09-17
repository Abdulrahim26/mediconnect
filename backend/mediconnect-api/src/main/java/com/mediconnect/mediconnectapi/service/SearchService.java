package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.response.AppointmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DepartmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DoctorSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.HospitalSearchResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface SearchService {

    Page<DoctorSearchResponse> searchDoctors(
            String firstName,
            String lastName,
            String specialty,
            String department,
            String hospital,
            String location,
            int page,
            int size,
            String sortBy
    );

    Page<HospitalSearchResponse> searchHospitals(
            String name,
            String location,
            int page,
            int size,
            String sortBy
    );

    Page<DepartmentSearchResponse> searchDepartments(
            String name,
            String hospital,
            int page,
            int size,
            String sortBy
    );

    Page<AppointmentSearchResponse> searchAppointments(
            AppointmentStatus status,
            LocalDate date,
            String doctor,
            String patient,
            int page,
            int size,
            String sortBy
    );
}