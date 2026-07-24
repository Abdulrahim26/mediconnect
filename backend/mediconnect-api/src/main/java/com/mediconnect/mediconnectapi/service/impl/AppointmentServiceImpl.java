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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentResponse bookAppointment(CreateAppointmentRequest request) {

        // Logged-in patient
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        // Find doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // ✅ Check if the slot is already booked
        boolean slotBooked = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                );

        if (slotBooked) {
            throw new RuntimeException(
                    "The selected appointment slot is already booked."
            );
        }

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments() {
        Doctor doctor = getCurrentDoctor();

        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        doctor.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AppointmentResponse approveAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending appointments can be approved."
            );
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    @Override
    public AppointmentResponse rejectAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending appointments can be rejected."
            );
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    @Override
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new RuntimeException(
                    "Only approved appointments can be completed."
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    @Override
    public List<AppointmentResponse> getMyAppointments() {
        Patient patient = getLoggedInPatient();

        List<Appointment> appointments = appointmentRepository
                .findByPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        patient.getId()
                );

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointments() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAsc(
                        patient.getId(),
                        LocalDate.now()
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentHistory() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdAndStatus(
                        patient.getId(),
                        AppointmentStatus.COMPLETED
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse cancelAppointment(UUID appointmentId) {
        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(appointmentId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Completed appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    public AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            CreateAppointmentRequest request
    ) {
        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(appointmentId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Completed appointment cannot be rescheduled");
        }

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    // ✅ HELPER METHOD: Get logged-in patient
    private Patient getLoggedInPatient() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));
    }

    // ✅ HELPER METHOD: Get current doctor
    private Doctor getCurrentDoctor() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }

    // ✅ HELPER METHOD: Get doctor's appointment by ID
    private Appointment getDoctorAppointment(UUID appointmentId) {
        Doctor doctor = getCurrentDoctor();

        return appointmentRepository
                .findByIdAndDoctorId(
                        appointmentId,
                        doctor.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found or you don't have permission."
                        )
                );
    }

    // ✅ HELPER METHOD: Map Appointment to AppointmentResponse
    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getFirstName() + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName() + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus().name(),
                appointment.getReason()
        );
    }
}