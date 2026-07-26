package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.ReceptionistAppointmentResponse;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistDashboardResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Receptionist;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReceptionistDashboardServiceImpl
        implements ReceptionistDashboardService {


    private final UserRepository userRepository;

    private final ReceptionistRepository receptionistRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;



    @Override
    public ReceptionistDashboardResponse getDashboard() {


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



        Receptionist receptionist =
                receptionistRepository.findByUserId(
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist profile not found"
                                )
                        );



        long totalPatients =
                appointmentRepository
                        .countDistinctPatientsByHospitalId(
                                receptionist.getHospital().getId()
                        );


        long totalAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalId(
                                receptionist.getHospital().getId()
                        );



        long pendingAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                receptionist.getHospital().getId(),
                                AppointmentStatus.PENDING
                        );



        long approvedAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                receptionist.getHospital().getId(),
                                AppointmentStatus.APPROVED
                        );



        long completedAppointments =
                appointmentRepository
                        .countByDoctorDepartmentHospitalIdAndStatus(
                                receptionist.getHospital().getId(),
                                AppointmentStatus.COMPLETED
                        );



        List<ReceptionistAppointmentResponse> todayAppointments =
                appointmentRepository
                        .findByDoctorDepartmentHospitalIdAndAppointmentDate(
                                receptionist.getHospital().getId(),
                                LocalDate.now()
                        )
                        .stream()
                        .map(this::mapAppointment)
                        .toList();



        return new ReceptionistDashboardResponse(

                receptionist.getFirstName()
                        + " "
                        + receptionist.getLastName(),

                receptionist.getHospital().getName(),

                totalPatients,

                totalAppointments,

                pendingAppointments,

                approvedAppointments,

                completedAppointments,

                todayAppointments

        );

    }



    private ReceptionistAppointmentResponse mapAppointment(
            Appointment appointment
    ){

        return new ReceptionistAppointmentResponse(

                appointment.getId(),

                appointment.getPatient()
                        .getFirstName()
                        + " "
                        + appointment.getPatient()
                        .getLastName(),


                appointment.getDoctor()
                        .getFirstName()
                        + " "
                        + appointment.getDoctor()
                        .getLastName(),


                appointment.getAppointmentDate(),

                appointment.getAppointmentTime(),

                appointment.getStatus().name()

        );

    }

}