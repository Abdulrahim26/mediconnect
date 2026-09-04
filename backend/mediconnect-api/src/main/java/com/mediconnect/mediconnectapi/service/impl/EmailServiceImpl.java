package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.service.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendWelcomeEmail(
            String recipientEmail,
            String firstName
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("Welcome to MediConnect");

        message.setText(
                "Hello " + firstName + ",\n\n"
                        + "Welcome to MediConnect!\n\n"
                        + "Your patient account has been successfully created.\n\n"
                        + "You can now log in to MediConnect and access your healthcare services, "
                        + "including finding doctors, booking appointments, and managing your medical records.\n\n"
                        + "Thank you for choosing MediConnect.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentCreatedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - New Appointment Request");

        message.setText(
                "Hello,\n\n"
                        + "A new appointment has been requested on MediConnect.\n\n"
                        + "Patient: " + patientName + "\n"
                        + "Doctor: " + doctorName + "\n"
                        + "Date: " + appointmentDate + "\n"
                        + "Time: " + appointmentTime + "\n\n"
                        + "The appointment is currently pending approval.\n\n"
                        + "Please log in to MediConnect to review the appointment.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentApprovedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Appointment Approved");

        message.setText(
                "Hello " + patientName + ",\n\n"
                        + "Good news! Your appointment has been approved.\n\n"
                        + "Doctor: " + doctorName + "\n"
                        + "Date: " + appointmentDate + "\n"
                        + "Time: " + appointmentTime + "\n\n"
                        + "Please make sure you arrive on time for your appointment.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentRejectedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Appointment Rejected");

        message.setText(
                "Hello " + patientName + ",\n\n"
                        + "Unfortunately, your appointment request has been rejected.\n\n"
                        + "Doctor: " + doctorName + "\n"
                        + "Date: " + appointmentDate + "\n"
                        + "Time: " + appointmentTime + "\n\n"
                        + "You can log in to MediConnect to choose another available appointment.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentCancelledEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String cancelledBy
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Appointment Cancelled");

        message.setText(
                "Hello,\n\n"
                        + "An appointment on MediConnect has been cancelled.\n\n"
                        + "Patient: " + patientName + "\n"
                        + "Doctor: " + doctorName + "\n"
                        + "Date: " + appointmentDate + "\n"
                        + "Time: " + appointmentTime + "\n"
                        + "Cancelled by: " + cancelledBy + "\n\n"
                        + "Please log in to MediConnect for more information.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentRescheduledEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Appointment Rescheduled");

        message.setText(
                "Hello,\n\n"
                        + "An appointment on MediConnect has been rescheduled.\n\n"
                        + "Patient: " + patientName + "\n"
                        + "Doctor: " + doctorName + "\n"
                        + "New date: " + appointmentDate + "\n"
                        + "New time: " + appointmentTime + "\n\n"
                        + "Please log in to MediConnect to review the updated appointment.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }


    @Override
    public void sendAppointmentCompletedEmail(
            String recipientEmail,
            String patientName,
            String doctorName,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Appointment Completed");

        message.setText(
                "Hello " + patientName + ",\n\n"
                        + "Your appointment has been completed.\n\n"
                        + "Doctor: " + doctorName + "\n"
                        + "Date: " + appointmentDate + "\n"
                        + "Time: " + appointmentTime + "\n\n"
                        + "Thank you for using MediConnect.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }
    @Override
    public void sendPasswordResetEmail(
            String recipientEmail,
            String resetLink
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject("MediConnect - Password Reset");

        message.setText(
                "Hello,\n\n"
                        + "We received a request to reset your MediConnect password.\n\n"
                        + "Please use the link below to reset your password:\n\n"
                        + resetLink
                        + "\n\n"
                        + "This link will expire according to the password reset policy.\n\n"
                        + "If you did not request a password reset, you can safely ignore this email.\n\n"
                        + "Best regards,\n"
                        + "MediConnect Team"
        );

        mailSender.send(message);
    }
}