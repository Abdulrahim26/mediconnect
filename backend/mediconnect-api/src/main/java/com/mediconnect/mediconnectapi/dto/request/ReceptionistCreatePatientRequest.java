package com.mediconnect.mediconnectapi.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;


@Getter
@Setter
public class ReceptionistCreatePatientRequest {


    @NotBlank
    private String firstName;


    @NotBlank
    private String lastName;


    @Email
    @NotBlank
    private String email;


    @NotBlank
    private String password;


    private String phone;


    private LocalDate dateOfBirth;


    private String gender;


    private String address;

}