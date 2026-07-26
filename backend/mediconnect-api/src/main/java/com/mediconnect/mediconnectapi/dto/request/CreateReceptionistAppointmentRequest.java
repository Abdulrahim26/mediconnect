package com.mediconnect.mediconnectapi.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;



@Getter
@Setter
public class CreateReceptionistAppointmentRequest {


    @NotNull
    private UUID patientId;


    @NotNull
    private UUID doctorId;


    @NotNull
    private LocalDate appointmentDate;


    @NotNull
    private LocalTime appointmentTime;


    private String reason;

}