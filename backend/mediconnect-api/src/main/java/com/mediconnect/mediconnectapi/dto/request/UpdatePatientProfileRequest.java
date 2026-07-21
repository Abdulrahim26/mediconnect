package com.mediconnect.mediconnectapi.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePatientProfileRequest {

    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    private String address;
}