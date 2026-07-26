package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class ReceptionistAppointmentResponse {


    private UUID id;


    private String patientName;


    private String doctorName;


    private LocalDate appointmentDate;


    private LocalTime appointmentTime;


    private String status;

}