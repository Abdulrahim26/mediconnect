package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DoctorSearchResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String specialty;

    private String qualification;

    private BigDecimal consultationFee;

    private String department;

    private String hospital;
}