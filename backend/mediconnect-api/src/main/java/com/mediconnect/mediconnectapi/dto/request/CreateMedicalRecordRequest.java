package com.mediconnect.mediconnectapi.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class CreateMedicalRecordRequest {


    @NotNull
    private UUID appointmentId;


    @NotBlank
    private String diagnosis;


    private String symptoms;


    private String treatment;


    private String prescription;


    private String notes;

}