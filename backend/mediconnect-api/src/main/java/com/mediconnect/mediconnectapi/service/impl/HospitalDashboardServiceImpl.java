package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.HospitalDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DepartmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.service.HospitalDashboardService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public HospitalDashboardResponse getDashboard(UUID hospitalId) {

        // ✅ Verify hospital exists
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hospital not found"
                        )
                );

        long totalDoctors =
                doctorRepository.countByDepartmentHospitalId(hospitalId);

        long totalDepartments =
                departmentRepository.countByHospitalId(hospitalId);

        long totalAppointments =
                appointmentRepository.countByDoctorDepartmentHospitalId(hospitalId);

        long totalPatients =
                appointmentRepository.countDistinctPatientsByHospitalId(hospitalId);

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