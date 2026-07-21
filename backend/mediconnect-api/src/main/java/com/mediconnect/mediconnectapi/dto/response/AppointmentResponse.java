package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AppointmentResponse {

    private UUID id;

    private String patientName;

    private String doctorName;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String status;

    private String reason;

}