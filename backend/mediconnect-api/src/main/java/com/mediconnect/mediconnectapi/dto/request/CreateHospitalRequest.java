package com.mediconnect.mediconnectapi.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateHospitalRequest {


    @NotBlank(message = "Hospital name is required")
    private String name;


    @NotBlank(message = "Location is required")
    private String location;


    private String address;


    private String phone;


    @Email(message = "Invalid email format")
    private String email;

}