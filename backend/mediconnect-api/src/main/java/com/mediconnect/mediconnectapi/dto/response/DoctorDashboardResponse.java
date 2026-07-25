package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class DoctorDashboardResponse {


    private String doctorName;


    private long totalAppointments;


    private long pendingAppointments;


    private long approvedAppointments;


    private long completedAppointments;


    private long cancelledAppointments;

    private long totalPatients;

    private List<DoctorAppointmentResponse> todaysAppointments;

}