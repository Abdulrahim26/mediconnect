package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    List<Appointment> findByDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(
            UUID doctorId
    );

    Optional<Appointment> findByIdAndDoctorId(
            UUID appointmentId,
            UUID doctorId
    );

    List<Appointment> findByPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(
            UUID patientId
    );

    Optional<Appointment> findByIdAndPatientId(
            UUID appointmentId,
            UUID patientId
    );

    List<Appointment> findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAsc(
            UUID patientId,
            LocalDate date
    );

    List<Appointment> findByPatientIdAndStatus(
            UUID patientId,
            AppointmentStatus status
    );

    long countByDoctorId(UUID doctorId);


    long countByDoctorIdAndStatus(
            UUID doctorId,
            AppointmentStatus status
    );


    List<Appointment> findByDoctorIdAndAppointmentDate(
            UUID doctorId,
            LocalDate appointmentDate
    );


    List<Appointment> findByDoctorIdOrderByAppointmentDateAsc(
            UUID doctorId
    );

    // ✅ Hospital Dashboard Statistics Methods
    long countByDoctorDepartmentHospitalId(UUID hospitalId);

    long countByDoctorDepartmentHospitalIdAndStatus(
            UUID hospitalId,
            AppointmentStatus status
    );

    @Query("""
           SELECT COUNT(DISTINCT a.patient.id)
           FROM Appointment a
           WHERE a.doctor.department.hospital.id = :hospitalId
           """)
    long countDistinctPatientsByHospitalId(UUID hospitalId);
}