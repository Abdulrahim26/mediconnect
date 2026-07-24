package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.PatientAppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.PatientDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.service.PatientDashboardService;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl
        implements PatientDashboardService {


    private final PatientRepository patientRepository;


    private final AppointmentRepository appointmentRepository;



    @Override
    public PatientDashboardResponse getDashboard(UUID patientId) {


        Patient patient =
                patientRepository.findById(patientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found"
                                )
                        );



        long totalAppointments =
                appointmentRepository.countByPatientId(patientId);



        long completedAppointments =
                appointmentRepository
                        .countByPatientIdAndStatus(
                                patientId,
                                AppointmentStatus.COMPLETED
                        );



        long cancelledAppointments =
                appointmentRepository
                        .countByPatientIdAndStatus(
                                patientId,
                                AppointmentStatus.CANCELLED
                        );



        List<PatientAppointmentResponse> appointments =
                appointmentRepository
                        .findByPatientIdOrderByAppointmentDateDesc(
                                patientId
                        )
                        .stream()
                        .map(this::mapAppointment)
                        .toList();



        long upcomingAppointments =
                appointments.stream()
                        .filter(
                                appointment ->
                                        appointment.getAppointmentDate()
                                                .isAfter(LocalDate.now())
                        )
                        .count();



        return new PatientDashboardResponse(

                patient.getFirstName()
                        + " "
                        + patient.getLastName(),

                patient.getUser().getEmail(),

                totalAppointments,

                upcomingAppointments,

                completedAppointments,

                cancelledAppointments,

                appointments

        );

    }




    private PatientAppointmentResponse mapAppointment(
            Appointment appointment
    ) {


        return new PatientAppointmentResponse(

                appointment.getId(),

                appointment.getDoctor()
                        .getFirstName()
                        + " "
                        + appointment.getDoctor()
                        .getLastName(),

                appointment.getDoctor()
                        .getDepartment()
                        .getHospital()
                        .getName(),

                appointment.getAppointmentDate(),

                appointment.getAppointmentTime(),

                appointment.getReason(),

                appointment.getStatus().name()

        );

    }

}