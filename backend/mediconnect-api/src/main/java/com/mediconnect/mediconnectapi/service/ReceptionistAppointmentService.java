package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;

import java.util.List;


public interface ReceptionistAppointmentService {


    List<AppointmentResponse> getHospitalAppointments();


}