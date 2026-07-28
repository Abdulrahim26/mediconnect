package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceptionistAppointmentServiceImpl
        implements ReceptionistAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<AppointmentResponse> getHospitalAppointments() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionist = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        if (receptionist.getHospital() == null) {
            throw new ResourceNotFoundException("Hospital not assigned");
        }

        return appointmentRepository
                .findByDoctorDepartmentHospitalId(
                        receptionist.getHospital().getId()
                )
                .stream()
                .map(this::map)
                .toList();
    }

    // ✅ NEW: Search appointments with filters
    @Override
    public List<AppointmentResponse> searchAppointments(
            String patient,
            String doctor,
            AppointmentStatus status,
            LocalDate date
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionist = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        UUID hospitalId = receptionist.getHospital().getId();

        // Start with all hospital appointments
        List<Appointment> appointments = appointmentRepository
                .findByDoctorDepartmentHospitalId(hospitalId);

        // Apply filters using streams
        return appointments.stream()

                // Filter by patient name (case-insensitive)
                .filter(a ->
                        patient == null ||
                                a.getPatient().getFirstName().toLowerCase()
                                        .contains(patient.toLowerCase()) ||
                                a.getPatient().getLastName().toLowerCase()
                                        .contains(patient.toLowerCase())
                )

                // Filter by doctor name (case-insensitive)
                .filter(a ->
                        doctor == null ||
                                a.getDoctor().getFirstName().toLowerCase()
                                        .contains(doctor.toLowerCase()) ||
                                a.getDoctor().getLastName().toLowerCase()
                                        .contains(doctor.toLowerCase())
                )

                // Filter by status
                .filter(a ->
                        status == null ||
                                a.getStatus() == status
                )

                // Filter by date
                .filter(a ->
                        date == null ||
                                a.getAppointmentDate().equals(date)
                )

                .map(this::map)
                .toList();
    }

    private AppointmentResponse map(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus().name(),
                appointment.getReason()
        );
    }
}