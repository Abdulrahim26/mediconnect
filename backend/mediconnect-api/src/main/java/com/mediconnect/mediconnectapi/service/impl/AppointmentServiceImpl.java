package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.*;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.AppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    @Override
    public AppointmentResponse bookAppointment(
            CreateAppointmentRequest request
    ) {

        // Logged-in patient
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));

        // Find doctor
        Doctor doctor = doctorRepository.findById(
                        request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        // Create appointment
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);

        appointment.setDoctor(doctor);

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setStatus(
                AppointmentStatus.PENDING
        );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return new AppointmentResponse(
                savedAppointment.getId(),
                patient.getFirstName() + " " + patient.getLastName(),
                doctor.getFirstName() + " " + doctor.getLastName(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getAppointmentTime(),
                savedAppointment.getStatus().name(),
                savedAppointment.getReason()
        );
    }
}