package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface ReceptionistAppointmentService {

    List<AppointmentResponse> getHospitalAppointments();

    // ✅ NEW: Search appointments with filters
    List<AppointmentResponse> searchAppointments(
            String patient,
            String doctor,
            AppointmentStatus status,
            LocalDate date
    );

}