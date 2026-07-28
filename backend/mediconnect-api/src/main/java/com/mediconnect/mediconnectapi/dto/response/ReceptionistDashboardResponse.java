package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceptionistDashboardResponse {

    private String hospitalName;

    private long totalAppointments;

    private long pendingAppointments;

    private long approvedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

}