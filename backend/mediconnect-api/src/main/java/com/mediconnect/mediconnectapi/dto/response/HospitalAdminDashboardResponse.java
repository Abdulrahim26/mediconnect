package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HospitalAdminDashboardResponse {

    private String hospitalName;

    private long totalDoctors;

    private long totalReceptionists;

    private long totalDepartments;

    private long totalPatients;

    private long totalAppointments;

    private long pendingAppointments;

    private long approvedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;
}