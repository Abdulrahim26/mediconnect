package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface PatientRepository extends JpaRepository<Patient, UUID> {


    Optional<Patient> findByUserId(UUID userId);


    Optional<Patient> findByPhone(String phone);

}