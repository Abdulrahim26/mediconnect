package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.DoctorAppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.DoctorDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Doctor;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.service.DoctorDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DoctorDashboardServiceImpl
        implements DoctorDashboardService {


    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;



    @Override
    public DoctorDashboardResponse getDashboard(UUID doctorId) {


        Doctor doctor =
                doctorRepository.findById(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"
                                )
                        );



        long totalAppointments =
                appointmentRepository.countByDoctorId(doctorId);



        long pendingAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.PENDING
                        );



        long approvedAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.APPROVED
                        );



        long completedAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.COMPLETED
                        );



        long cancelledAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.CANCELLED
                        );



        List<DoctorAppointmentResponse> todayAppointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDate(
                                doctorId,
                                LocalDate.now()
                        )
                        .stream()
                        .map(this::mapAppointment)
                        .toList();



        return new DoctorDashboardResponse(

                doctor.getFirstName()
                        + " "
                        + doctor.getLastName(),

                totalAppointments,

                pendingAppointments,

                approvedAppointments,

                completedAppointments,

                cancelledAppointments,

                todayAppointments

        );

    }




    private DoctorAppointmentResponse mapAppointment(
            Appointment appointment
    ) {


        return new DoctorAppointmentResponse(

                appointment.getId(),

                appointment.getPatient()
                        .getFirstName()
                        + " "
                        + appointment.getPatient()
                        .getLastName(),

                appointment.getAppointmentDate(),

                appointment.getAppointmentTime(),

                appointment.getReason(),

                appointment.getStatus().name()

        );

    }

}