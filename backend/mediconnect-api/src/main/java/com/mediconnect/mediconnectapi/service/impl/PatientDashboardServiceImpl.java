package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.MedicalRecordRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.PatientDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl
        implements PatientDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public PatientDashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found")
                );

        long totalAppointments = appointmentRepository.countByPatientId(patient.getId());

        long pendingAppointments = appointmentRepository.countByPatientIdAndStatus(
                patient.getId(),
                AppointmentStatus.PENDING
        );

        long approvedAppointments = appointmentRepository.countByPatientIdAndStatus(
                patient.getId(),
                AppointmentStatus.APPROVED
        );

        long completedAppointments = appointmentRepository.countByPatientIdAndStatus(
                patient.getId(),
                AppointmentStatus.COMPLETED
        );

        long cancelledAppointments = appointmentRepository.countByPatientIdAndStatus(
                patient.getId(),
                AppointmentStatus.CANCELLED
        );

        List<AppointmentResponse> upcomingAppointments =
                appointmentRepository
                        .findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAsc(
                                patient.getId(),
                                LocalDate.now()
                        )
                        .stream()
                        .map(this::mapAppointment)
                        .toList();

        List<MedicalRecordResponse> recentMedicalRecords =
                medicalRecordRepository
                        .findByPatientIdOrderByCreatedAtDesc(
                                patient.getId()
                        )
                        .stream()
                        .map(record ->
                                new MedicalRecordResponse(
                                        record.getId(),
                                        record.getAppointment().getId(),
                                        record.getPatient().getFirstName()
                                                + " "
                                                + record.getPatient().getLastName(),
                                        record.getDoctor().getFirstName()
                                                + " "
                                                + record.getDoctor().getLastName(),
                                        record.getDiagnosis(),
                                        record.getSymptoms(),      // ✅ FIXED: symptoms
                                        record.getTreatment(),
                                        record.getPrescription(),   // ✅ FIXED: prescription (no longer null)
                                        record.getNotes(),
                                        record.getCreatedAt()
                                )
                        )
                        .toList();

        return new PatientDashboardResponse(
                patient.getFirstName() + " " + patient.getLastName(),
                totalAppointments,
                pendingAppointments,
                approvedAppointments,
                completedAppointments,
                cancelledAppointments,
                upcomingAppointments,
                recentMedicalRecords
        );
    }

    private AppointmentResponse mapAppointment(
            com.mediconnect.mediconnectapi.entity.Appointment appointment
    ) {
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