package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReceptionistDashboardResponse {


    private String receptionistName;


    private String hospitalName;


    private long totalPatients;


    private long totalAppointments;


    private long pendingAppointments;


    private long approvedAppointments;


    private long completedAppointments;


    private List<ReceptionistAppointmentResponse> todaysAppointments;

}