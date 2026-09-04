package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.ReceptionistDashboardResponse;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceptionistDashboardServiceImpl
        implements ReceptionistDashboardService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public ReceptionistDashboardResponse getDashboard() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User receptionist =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if (receptionist.getHospital() == null) {

            throw new ResourceNotFoundException(
                    "Hospital not assigned"
            );
        }

        var hospitalId =
                receptionist.getHospital().getId();

        return new ReceptionistDashboardResponse(

                receptionist.getHospital().getName(),

                appointmentRepository
                        .countByDoctorDepartmentHospitalId(
                                hospitalId
                        ),

                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.PENDING
                        ),

                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.APPROVED
                        ),

                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.COMPLETED
                        ),

                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                hospitalId,
                                AppointmentStatus.CANCELLED
                        )

        );
    }
}