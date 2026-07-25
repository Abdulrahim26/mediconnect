package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.DoctorAppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.DoctorDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Doctor;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.DoctorDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DoctorDashboardServiceImpl
        implements DoctorDashboardService {


    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;



    @Override
    public DoctorDashboardResponse getDashboard() {


        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();



        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );



        Doctor doctor =
                doctorRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor profile not found"
                                )
                        );



        long totalAppointments =
                appointmentRepository.countByDoctorId(
                        doctor.getId()
                );



        long pendingAppointments =
                appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(),
                        AppointmentStatus.PENDING
                );



        long approvedAppointments =
                appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(),
                        AppointmentStatus.APPROVED
                );



        long completedAppointments =
                appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(),
                        AppointmentStatus.COMPLETED
                );



        long cancelledAppointments =
                appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(),
                        AppointmentStatus.CANCELLED
                );



        long totalPatients =
                appointmentRepository.countDistinctPatientsByDoctorId(
                        doctor.getId()
                );



        List<DoctorAppointmentResponse> todayAppointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDate(
                                doctor.getId(),
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

                totalPatients,

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