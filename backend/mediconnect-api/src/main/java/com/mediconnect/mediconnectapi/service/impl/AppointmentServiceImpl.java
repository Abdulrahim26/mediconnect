package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.*;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.entity.enums.NotificationType;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.AppointmentService;
import com.mediconnect.mediconnectapi.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ReceptionistRepository receptionistRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(
            CreateAppointmentRequest request
    ) {

        Patient patient = getLoggedInPatient();

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                );

        if (exists) {
            throw new BadRequestException("Appointment slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);

        notificationService.createNotification(
                doctor.getUser().getId(),
                "New appointment request from "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_BOOKED
        );

        return map(saved);
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments() {
        Doctor doctor = getLoggedInDoctor();

        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(doctor.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public AppointmentResponse approveAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment saved = appointmentRepository.save(appointment);

        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been approved",
                NotificationType.APPOINTMENT_APPROVED
        );

        return map(saved);
    }

    @Override
    public AppointmentResponse rejectAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.REJECTED);
        Appointment saved = appointmentRepository.save(appointment);

        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been rejected",
                NotificationType.APPOINTMENT_REJECTED
        );

        return map(saved);
    }

    @Override
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new BadRequestException("Only approved appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);

        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been completed",
                NotificationType.APPOINTMENT_COMPLETED
        );

        return map(saved);
    }

    @Override
    public List<AppointmentResponse> getMyAppointments() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(patient.getId())
                .stream()
                .map(this::map)
                .toList();
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
                .map(this::map)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentHistory() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdAndStatus(patient.getId(), AppointmentStatus.COMPLETED)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public AppointmentResponse cancelAppointment(UUID appointmentId) {
        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(appointmentId, patient.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return map(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse doctorCancelAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.CANCELLED);

        return map(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            CreateAppointmentRequest request
    ) {
        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(appointmentId, patient.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        return map(appointmentRepository.save(appointment));
    }

    // ======================================================
    // RECEPTIONIST: VIEW ALL HOSPITAL APPOINTMENTS
    // ======================================================
    @Override
    public List<AppointmentResponse> getHospitalAppointments() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionistUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Receptionist receptionist = receptionistRepository
                .findByUserId(receptionistUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist profile not found")
                );

        return appointmentRepository
                .findByDoctorDepartmentHospitalId(receptionist.getHospital().getId())
                .stream()
                .map(this::map)
                .toList();
    }

    // ======================================================
    // RECEPTIONIST: FILTER APPOINTMENTS BY STATUS
    // ======================================================
    @Override
    public List<AppointmentResponse> getHospitalAppointmentsByStatus(
            AppointmentStatus status
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionistUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Receptionist receptionist = receptionistRepository
                .findByUserId(receptionistUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist profile not found")
                );

        return appointmentRepository
                .findByDoctorDepartmentHospitalIdAndStatus(
                        receptionist.getHospital().getId(),
                        status
                )
                .stream()
                .map(this::map)
                .toList();
    }

    // ======================================================
    // RECEPTIONIST: CHECK-IN PATIENT
    // ======================================================
    @Override
    public AppointmentResponse checkInPatient(UUID appointmentId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User receptionistUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Receptionist receptionist = receptionistRepository
                .findByUserId(receptionistUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist profile not found")
                );

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        if (!appointment.getDoctor()
                .getDepartment()
                .getHospital()
                .getId()
                .equals(receptionist.getHospital().getId())) {

            throw new BadRequestException(
                    "Appointment does not belong to your hospital."
            );
        }

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {

            throw new BadRequestException(
                    "Only approved appointments can be checked in."
            );
        }

        appointment.setStatus(AppointmentStatus.CHECKED_IN);

        Appointment saved = appointmentRepository.save(appointment);

        notificationService.createNotification(
                appointment.getDoctor().getUser().getId(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName()
                        + " has arrived.",
                NotificationType.APPOINTMENT_APPROVED
        );

        return map(saved);
    }

    // ======================================================
    // DOCTOR VIEW WAITING QUEUE
    // ======================================================
    @Override
    public List<AppointmentResponse> getWaitingQueue() {

        Doctor doctor = getLoggedInDoctor();

        return appointmentRepository
                .findByDoctorIdAndStatus(
                        doctor.getId(),
                        AppointmentStatus.CHECKED_IN
                )
                .stream()
                .map(this::map)
                .toList();
    }

    // ======================================================
    // HELPER METHODS
    // ======================================================

    private Patient getLoggedInPatient() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found")
                );
    }

    private Doctor getLoggedInDoctor() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found")
                );
    }

    private Appointment getDoctorAppointment(UUID id) {
        Doctor doctor = getLoggedInDoctor();

        return appointmentRepository
                .findByIdAndDoctorId(id, doctor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );
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