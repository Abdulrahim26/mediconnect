package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.HospitalAdminDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.HospitalAdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalAdminDashboardServiceImpl
        implements HospitalAdminDashboardService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public HospitalAdminDashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        // ✅ FIXED: Check if user has a hospital assigned
        if (user.getHospital() == null) {
            throw new ResourceNotFoundException(
                    "Hospital not assigned to this admin"
            );
        }

        // ✅ FIXED: Get hospital ID from the hospital entity
        UUID hospitalId = user.getHospital().getId();

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital not found")
                );

        long totalDoctors = userRepository.countByHospitalIdAndRoleName(
                hospitalId,
                "DOCTOR"
        );

        long totalAppointments = appointmentRepository
                .countByDoctorDepartmentHospitalId(hospitalId);

        long pendingAppointments = appointmentRepository
                .countByDoctorDepartmentHospitalIdAndStatus(
                        hospitalId,
                        AppointmentStatus.PENDING
                );

        long approvedAppointments = appointmentRepository
                .countByDoctorDepartmentHospitalIdAndStatus(
                        hospitalId,
                        AppointmentStatus.APPROVED
                );

        long completedAppointments = appointmentRepository
                .countByDoctorDepartmentHospitalIdAndStatus(
                        hospitalId,
                        AppointmentStatus.COMPLETED
                );

        long cancelledAppointments = appointmentRepository
                .countByDoctorDepartmentHospitalIdAndStatus(
                        hospitalId,
                        AppointmentStatus.CANCELLED
                );

        long totalPatients = appointmentRepository
                .countDistinctPatientsByHospitalId(hospitalId);

        return new HospitalAdminDashboardResponse(
                hospital.getName(),
                totalDoctors,
                totalPatients,
                totalAppointments,
                pendingAppointments,
                approvedAppointments,
                completedAppointments,
                cancelledAppointments
        );
    }
}