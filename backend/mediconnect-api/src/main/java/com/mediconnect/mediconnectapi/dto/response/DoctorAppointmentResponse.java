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
public class DoctorAppointmentResponse {


    private UUID appointmentId;


    private String patientName;


    private LocalDate appointmentDate;


    private LocalTime appointmentTime;


    private String reason;


    private String status;

}