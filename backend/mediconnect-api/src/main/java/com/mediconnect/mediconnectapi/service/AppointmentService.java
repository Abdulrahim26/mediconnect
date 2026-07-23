package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;

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

}