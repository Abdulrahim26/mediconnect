package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, UUID> {


    boolean existsByAppointmentId(UUID appointmentId);


    List<MedicalRecord> findByPatientId(UUID patientId);


    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(
            UUID patientId
    );


    Optional<MedicalRecord> findByIdAndPatientId(
            UUID id,
            UUID patientId
    );

}