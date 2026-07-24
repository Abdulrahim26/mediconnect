package com.mediconnect.mediconnectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class PatientDashboardResponse {


    private String patientName;


    private String email;


    private long totalAppointments;


    private long upcomingAppointments;


    private long completedAppointments;


    private long cancelledAppointments;


    private List<PatientAppointmentResponse> appointments;

}