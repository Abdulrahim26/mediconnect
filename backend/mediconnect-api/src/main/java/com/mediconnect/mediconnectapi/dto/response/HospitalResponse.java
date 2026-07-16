package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.UUID;


@Getter
@AllArgsConstructor
public class HospitalResponse {


    private UUID id;


    private String name;


    private String location;


    private String address;


    private String phone;


    private String email;

}