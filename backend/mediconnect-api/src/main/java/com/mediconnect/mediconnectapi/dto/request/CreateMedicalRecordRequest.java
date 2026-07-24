package com.mediconnect.mediconnectapi.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class CreateMedicalRecordRequest {


    private UUID appointmentId;


    private String diagnosis;


    private String treatment;


    private String prescription;


    private String notes;

}