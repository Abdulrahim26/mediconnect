package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.UUID;


@Getter
@AllArgsConstructor
public class DepartmentResponse {


    private UUID id;

    private String name;

    private String description;

    private String hospitalName;

}