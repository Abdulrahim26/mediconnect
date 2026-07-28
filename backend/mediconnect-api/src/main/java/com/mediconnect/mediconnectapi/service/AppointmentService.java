package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentResponse bookAppointment(
            CreateAppointmentRequest request
    );

    List<AppointmentResponse> getDoctorAppointments();

    AppointmentResponse approveAppointment(UUID appointmentId);

    AppointmentResponse rejectAppointment(UUID appointmentId);

    AppointmentResponse completeAppointment(UUID appointmentId);

    List<AppointmentResponse> getMyAppointments();

    List<AppointmentResponse> getUpcomingAppointments();

    List<AppointmentResponse> getAppointmentHistory();

    AppointmentResponse cancelAppointment(UUID appointmentId);

    AppointmentResponse doctorCancelAppointment(UUID appointmentId);

    AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            CreateAppointmentRequest request
    );

    // ======================================================
    // RECEPTIONIST: VIEW HOSPITAL APPOINTMENTS
    // ======================================================
    List<AppointmentResponse> getHospitalAppointments();

    // ======================================================
    // RECEPTIONIST: FILTER HOSPITAL APPOINTMENTS BY STATUS
    // ======================================================
    List<AppointmentResponse> getHospitalAppointmentsByStatus(
            AppointmentStatus status
    );

    // ======================================================
    // RECEPTIONIST: CHECK-IN PATIENT
    // ======================================================
    AppointmentResponse checkInPatient(
            UUID appointmentId
    );

    // ======================================================
    // DOCTOR VIEW WAITING QUEUE
    // ======================================================
    List<AppointmentResponse> getWaitingQueue();
}