package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Doctor;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.entity.enums.NotificationType;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.NotificationService;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceptionistAppointmentServiceImpl
        implements ReceptionistAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public AppointmentResponse createAppointment(
            CreateReceptionistAppointmentRequest request
    ) {

        // Get logged in receptionist
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionist = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist not found")
                );

        if (receptionist.getHospital() == null) {
            throw new ResourceNotFoundException(
                    "Receptionist hospital not assigned"
            );
        }

        // Find patient
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found")
                );

        // Find doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        // Hospital security check
        if (!doctor.getDepartment()
                .getHospital()
                .getId()
                .equals(receptionist.getHospital().getId())) {

            throw new BadRequestException(
                    "Doctor does not belong to receptionist hospital"
            );
        }

        // Check appointment slot
        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                );

        if (exists) {
            throw new BadRequestException(
                    "The selected appointment slot is already booked."
            );
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);

        // 🔍 Debug: Confirm notification is being called
        System.out.println("Creating notification for doctor: " + doctor.getUser().getEmail());

        // ✅ Notify the doctor
        notificationService.createNotification(
                doctor.getUser().getId(),
                "New appointment request from "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_BOOKED
        );

        return new AppointmentResponse(
                appointment.getId(),
                patient.getFirstName() + " " + patient.getLastName(),
                doctor.getFirstName() + " " + doctor.getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus().name(),
                appointment.getReason()
        );
    }
}