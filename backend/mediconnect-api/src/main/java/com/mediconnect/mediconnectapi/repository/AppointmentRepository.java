package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

}