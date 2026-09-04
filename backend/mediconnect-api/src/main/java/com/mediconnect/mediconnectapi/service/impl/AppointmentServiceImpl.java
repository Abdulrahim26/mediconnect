package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.request.RescheduleAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;

import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Doctor;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.Receptionist;
import com.mediconnect.mediconnectapi.entity.User;

import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.entity.enums.NotificationType;
import com.mediconnect.mediconnectapi.entity.enums.VerificationStatus;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;

import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;

import com.mediconnect.mediconnectapi.service.AppointmentService;
import com.mediconnect.mediconnectapi.service.EmailService;
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
    private final EmailService emailService;

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

        // In-app notification to doctor
        notificationService.createNotification(
                doctor.getUser().getId(),
                "New appointment request from "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_CREATED
        );

        // In-app notification to hospital receptionists
        notifyHospitalReceptionists(
                appointment,
                "New appointment booked by "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_CREATED
        );

        // Email notification to doctor
        emailService.sendAppointmentCreatedEmail(
                doctor.getUser().getEmail(),
                patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                doctor.getFirstName()
                        + " "
                        + doctor.getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        return map(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorAppointments() {
        Doctor doctor = getLoggedInDoctor();

        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(doctor.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResponse approveAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to patient
        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been approved",
                NotificationType.APPOINTMENT_APPROVED
        );

        // Email notification to patient
        emailService.sendAppointmentApprovedEmail(
                appointment.getPatient().getUser().getEmail(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        return map(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse rejectAppointment(UUID appointmentId) {
        Appointment appointment = getDoctorAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.REJECTED);
        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to patient
        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been rejected",
                NotificationType.APPOINTMENT_REJECTED
        );

        // Email notification to patient
        emailService.sendAppointmentRejectedEmail(
                appointment.getPatient().getUser().getEmail(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        return map(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {

        Appointment appointment = getDoctorAppointment(appointmentId);

        /*
         * A patient must first be checked in by the receptionist
         * before the doctor can complete the consultation.
         */
        if (appointment.getStatus() != AppointmentStatus.CHECKED_IN) {
            throw new BadRequestException(
                    "Only checked-in appointments can be completed"
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to patient
        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been completed",
                NotificationType.APPOINTMENT_COMPLETED
        );

        // Email notification to patient
        emailService.sendAppointmentCompletedEmail(
                appointment.getPatient().getUser().getEmail(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        return map(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointments() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(patient.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentHistory() {
        Patient patient = getLoggedInPatient();

        return appointmentRepository
                .findByPatientIdAndStatus(patient.getId(), AppointmentStatus.COMPLETED)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId) {

        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(
                        appointmentId,
                        patient.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to doctor
        notificationService.createNotification(
                appointment.getDoctor().getUser().getId(),
                "Appointment cancelled by "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_CANCELLED
        );

        // In-app notification to hospital receptionists
        notifyHospitalReceptionists(
                appointment,
                "Appointment cancelled by "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_CANCELLED
        );

        // Email notification to doctor
        emailService.sendAppointmentCancelledEmail(
                appointment.getDoctor().getUser().getEmail(),
                patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                "Patient"
        );

        return map(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse doctorCancelAppointment(UUID appointmentId) {

        Appointment appointment = getDoctorAppointment(appointmentId);

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to patient
        notificationService.createNotification(
                appointment.getPatient().getUser().getId(),
                "Your appointment has been cancelled by the doctor",
                NotificationType.APPOINTMENT_CANCELLED
        );

        // In-app notification to hospital receptionists
        notifyHospitalReceptionists(
                appointment,
                "Appointment cancelled by Dr. "
                        + appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                NotificationType.APPOINTMENT_CANCELLED
        );

        // Email notification to patient
        emailService.sendAppointmentCancelledEmail(
                appointment.getPatient().getUser().getEmail(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                "Doctor"
        );

        return map(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            RescheduleAppointmentRequest request
    ) {
        Patient patient = getLoggedInPatient();

        Appointment appointment = appointmentRepository
                .findByIdAndPatientId(
                        appointmentId,
                        patient.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        )
                );

        boolean slotTaken =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                appointment.getDoctor().getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (slotTaken &&
                (!appointment.getAppointmentDate()
                        .equals(request.getAppointmentDate())
                        || !appointment.getAppointmentTime()
                        .equals(request.getAppointmentTime()))) {

            throw new BadRequestException(
                    "The selected appointment slot is already booked"
            );
        }

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setStatus(
                AppointmentStatus.PENDING
        );

        Appointment saved = appointmentRepository.save(appointment);

        // In-app notification to doctor
        notificationService.createNotification(
                appointment.getDoctor().getUser().getId(),
                "An appointment has been rescheduled by "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_RESCHEDULED
        );

        // In-app notification to hospital receptionists
        notifyHospitalReceptionists(
                appointment,
                "An appointment has been rescheduled by "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                NotificationType.APPOINTMENT_RESCHEDULED
        );

        // Email notification to doctor
        emailService.sendAppointmentRescheduledEmail(
                appointment.getDoctor().getUser().getEmail(),
                patient.getFirstName()
                        + " "
                        + patient.getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        return map(saved);
    }

    // ======================================================
    // RECEPTIONIST: VIEW ALL HOSPITAL APPOINTMENTS
    // ======================================================
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional
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

        // ======================================================
        // 1. APPOINTMENT MUST BELONG TO RECEPTIONIST'S HOSPITAL
        // ======================================================
        if (!appointment.getDoctor()
                .getDepartment()
                .getHospital()
                .getId()
                .equals(receptionist.getHospital().getId())) {

            throw new BadRequestException(
                    "Appointment does not belong to your hospital."
            );
        }

        // ======================================================
        // 2. APPOINTMENT MUST BE APPROVED
        // ======================================================
        if (appointment.getStatus()
                != AppointmentStatus.APPROVED) {

            throw new BadRequestException(
                    "Only approved appointments can be checked in."
            );
        }

        // ======================================================
        // 3. PATIENT IDENTITY MUST BE VERIFIED
        // ======================================================
        Patient patient = appointment.getPatient();

        boolean ghanaCardVerified =
                patient.getGhanaCardVerificationStatus()
                        == VerificationStatus.VERIFIED;

        boolean nhisVerified =
                patient.getNhisVerificationStatus()
                        == VerificationStatus.VERIFIED;

        boolean identityVerified =
                ghanaCardVerified || nhisVerified;

        if (!identityVerified) {

            throw new BadRequestException(
                    "Patient identity must be verified "
                            + "before check-in."
            );
        }

        // ======================================================
        // 4. CHECK IN PATIENT
        // ======================================================
        appointment.setStatus(
                AppointmentStatus.CHECKED_IN
        );

        Appointment saved =
                appointmentRepository.save(appointment);

        // ======================================================
        // 5. NOTIFY DOCTOR
        // ======================================================
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
    @Transactional(readOnly = true)
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

    /**
     * Notify all receptionists in the hospital where the appointment belongs.
     */
    private void notifyHospitalReceptionists(
            Appointment appointment,
            String message,
            NotificationType notificationType
    ) {

        UUID hospitalId = appointment.getDoctor()
                .getDepartment()
                .getHospital()
                .getId();

        List<Receptionist> receptionists =
                receptionistRepository.findByHospitalId(hospitalId);

        for (Receptionist receptionist : receptionists) {

            notificationService.createNotification(
                    receptionist.getUser().getId(),
                    message,
                    notificationType
            );
        }
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