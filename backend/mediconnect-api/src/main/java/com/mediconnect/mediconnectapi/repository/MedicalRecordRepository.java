package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;



public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, UUID> {


    List<MedicalRecord> findByPatientId(UUID patientId);


    boolean existsByAppointmentId(UUID appointmentId);

}