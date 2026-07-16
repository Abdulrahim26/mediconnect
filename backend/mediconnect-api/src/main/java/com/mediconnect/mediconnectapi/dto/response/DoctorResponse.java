package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DoctorResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String specialty;

    private String qualification;

    private String phone;

    private String email;

    private BigDecimal consultationFee;

    private String departmentName;
}