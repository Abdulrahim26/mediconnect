package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistAppointmentRequest;
import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;


public interface ReceptionistAppointmentService {


    AppointmentResponse createAppointment(
            CreateReceptionistAppointmentRequest request
    );

}