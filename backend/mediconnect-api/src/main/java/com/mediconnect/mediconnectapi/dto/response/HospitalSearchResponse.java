package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class HospitalSearchResponse {


    private UUID id;


    private String name;


    private String location;


    private String address;


    private String phone;


    private String email;


}