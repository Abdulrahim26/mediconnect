package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {


    List<Appointment> findByPatientId(UUID patientId);


    List<Appointment> findByDoctorId(UUID doctorId);


    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

}