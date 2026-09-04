package com.mediconnect.mediconnectapi.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {

    void sendWelcomeEmail(
            String recipientEmail,
            String firstName
    );

    void sendPasswordResetEmail(
            String recipientEmail,
            String resetLink
    );

    void sendAppointmentCreatedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    void sendAppointmentApprovedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    void sendAppointmentRejectedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    void sendAppointmentCancelledEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String cancelledBy
    );

    void sendAppointmentRescheduledEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    void sendAppointmentCompletedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );
}