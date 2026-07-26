package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.SuperAdminDashboardResponse;
import com.mediconnect.mediconnectapi.repository.*;
import com.mediconnect.mediconnectapi.service.SuperAdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class SuperAdminDashboardServiceImpl
        implements SuperAdminDashboardService {


    private final HospitalRepository hospitalRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final DepartmentRepository departmentRepository;



    @Override
    public SuperAdminDashboardResponse getDashboard() {


        return new SuperAdminDashboardResponse(

                hospitalRepository.count(),

                doctorRepository.count(),

                patientRepository.count(),

                appointmentRepository.count(),

                departmentRepository.count()

        );

    }

}