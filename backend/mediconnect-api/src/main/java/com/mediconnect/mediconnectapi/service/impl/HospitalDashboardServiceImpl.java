package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.HospitalDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.*;
import com.mediconnect.mediconnectapi.service.HospitalDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HospitalDashboardServiceImpl
        implements HospitalDashboardService {


    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final DepartmentRepository departmentRepository;

    private final HospitalRepository hospitalRepository;

    private final UserRepository userRepository;



    @Override
    public HospitalDashboardResponse getDashboard(
            UUID hospitalId
    ) {


        // ================================
        // GET CURRENT LOGGED IN ADMIN
        // ================================

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();



        User admin =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );



        // ================================
        // SECURITY CHECK
        // ================================

        if(admin.getHospital() == null){

            throw new BadRequestException(
                    "Admin is not assigned to a hospital."
            );

        }


        if(!admin.getHospital()
                .getId()
                .equals(hospitalId)){

            throw new BadRequestException(
                    "You cannot access another hospital dashboard."
            );

        }



        // ================================
        // LOAD HOSPITAL
        // ================================

        Hospital hospital =
                hospitalRepository.findById(hospitalId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Hospital not found"
                                )
                        );



        long totalDoctors =
                doctorRepository.countByDepartmentHospitalId(
                        hospitalId
                );


        long totalDepartments =
                departmentRepository.countByHospitalId(
                        hospitalId
                );


        long totalAppointments =
                appointmentRepository.countByDoctorDepartmentHospitalId(
                        hospitalId
                );


        long totalPatients =
                appointmentRepository
                        .countDistinctPatientsByHospitalId(
                                hospitalId
                        );


        long pendingAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.PENDING
                        );


        long approvedAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.APPROVED
                        );


        long rejectedAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.REJECTED
                        );


        long completedAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.COMPLETED
                        );


        long cancelledAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.CANCELLED
                        );



        return new HospitalDashboardResponse(

                hospital.getName(),

                totalDoctors,

                totalPatients,

                totalDepartments,

                totalAppointments,

                pendingAppointments,

                approvedAppointments,

                rejectedAppointments,

                completedAppointments,

                cancelledAppointments

        );

    }

}