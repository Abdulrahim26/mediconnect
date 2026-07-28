package com.mediconnect.mediconnectapi.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateDoctorProfileRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private String qualification;

    private BigDecimal consultationFee;

    private String specialty;

}