package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class HospitalDashboardResponse {


    private String hospitalName;


    private long totalDoctors;


    private long totalPatients;


    private long totalDepartments;


    private long totalAppointments;


    private long pendingAppointments;


    private long approvedAppointments;


    private long rejectedAppointments;


    private long completedAppointments;


    private long cancelledAppointments;

}