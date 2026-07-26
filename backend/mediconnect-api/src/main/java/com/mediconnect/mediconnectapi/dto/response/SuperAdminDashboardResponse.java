package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SuperAdminDashboardResponse {


    private long totalHospitals;


    private long totalDoctors;


    private long totalPatients;


    private long totalAppointments;


    private long totalDepartments;

}