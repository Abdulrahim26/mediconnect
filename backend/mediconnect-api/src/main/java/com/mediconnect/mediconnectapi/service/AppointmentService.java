package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse bookAppointment(
            CreateAppointmentRequest request
    );

}