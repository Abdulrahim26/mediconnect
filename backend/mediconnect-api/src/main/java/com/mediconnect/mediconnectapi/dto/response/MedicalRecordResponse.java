package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class MedicalRecordResponse {


    private UUID id;


    private UUID appointmentId;


    private String patientName;


    private String doctorName;


    private String diagnosis;


    private String treatment;


    private String prescription;


    private String notes;


    private LocalDateTime createdAt;

}