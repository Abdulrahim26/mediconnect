package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // ✅ Patient Statistics Methods
    long countByPatientId(UUID patientId);

    long countByPatientIdAndStatus(
            UUID patientId,
            AppointmentStatus status
    );

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(
            UUID patientId
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

    List<Appointment> findByDoctorDepartmentHospitalIdAndAppointmentDate(
            UUID hospitalId,
            LocalDate appointmentDate
    );

    List<Appointment> findByDoctorDepartmentHospitalId(
            UUID hospitalId
    );

    // ✅ Receptionist Appointment Management Methods
    List<Appointment> findByDoctorDepartmentHospitalIdAndStatus(
            UUID hospitalId,
            AppointmentStatus status
    );

    // ✅ Paginated search methods for receptionist
    Page<Appointment> findByDoctorDepartmentHospitalIdAndStatus(
            UUID hospitalId,
            AppointmentStatus status,
            Pageable pageable
    );

    Page<Appointment> findByDoctorDepartmentHospitalIdAndPatientFirstNameContainingIgnoreCase(
            UUID hospitalId,
            String name,
            Pageable pageable
    );

    Page<Appointment> findByDoctorDepartmentHospitalIdAndAppointmentDate(
            UUID hospitalId,
            LocalDate date,
            Pageable pageable
    );

    List<Appointment> findByDoctorDepartmentHospitalIdAndPatientFirstNameContainingIgnoreCaseOrDoctorDepartmentHospitalIdAndPatientLastNameContainingIgnoreCase(
            UUID hospitalId,
            String firstName,
            UUID hospitalId2,
            String lastName
    );

    // ✅ Doctor Dashboard Statistics Methods
    @Query("""
           SELECT COUNT(DISTINCT a.patient.id)
           FROM Appointment a
           WHERE a.doctor.id = :doctorId
           """)
    long countDistinctPatientsByDoctorId(UUID doctorId);

    // ✅ Paginated Search Methods
    Page<Appointment> findByStatus(
            AppointmentStatus status,
            Pageable pageable
    );

    Page<Appointment> findByAppointmentDate(
            LocalDate appointmentDate,
            Pageable pageable
    );

    Page<Appointment> findByDoctorFirstNameContainingIgnoreCaseOrDoctorLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    Page<Appointment> findByPatientFirstNameContainingIgnoreCaseOrPatientLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    // ✅ NEW: Doctor waiting queue
    List<Appointment> findByDoctorIdAndStatus(
            UUID doctorId,
            AppointmentStatus status
    );
}