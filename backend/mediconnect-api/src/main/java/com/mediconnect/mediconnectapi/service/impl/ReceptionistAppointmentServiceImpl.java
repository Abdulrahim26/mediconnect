package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.AppointmentResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Receptionist;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistAppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReceptionistAppointmentServiceImpl
        implements ReceptionistAppointmentService {


    private final UserRepository userRepository;

    private final ReceptionistRepository receptionistRepository;

    private final AppointmentRepository appointmentRepository;



    @Override
    public List<AppointmentResponse> getHospitalAppointments() {


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
                receptionistRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist profile not found"
                                )
                        );



        return appointmentRepository
                .findByDoctorDepartmentHospitalId(
                        receptionist.getHospital().getId()
                )
                .stream()
                .map(this::mapAppointment)
                .toList();

    }




    private AppointmentResponse mapAppointment(
            Appointment appointment
    ) {


        return new AppointmentResponse(

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

                appointment.getStatus().name(),

                appointment.getReason()

        );

    }

}