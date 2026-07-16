package com.mediconnect.mediconnectapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateDoctorRequest {


    @NotBlank(message = "First name is required")
    private String firstName;


    @NotBlank(message = "Last name is required")
    private String lastName;


    @NotBlank(message = "Specialty is required")
    private String specialty;


    private String qualification;


    private String phone;


    @Email(message = "Invalid email format")
    private String email;


    private BigDecimal consultationFee;


    private UUID departmentId;


    @NotBlank(message = "Password is required")
    private String password;

}