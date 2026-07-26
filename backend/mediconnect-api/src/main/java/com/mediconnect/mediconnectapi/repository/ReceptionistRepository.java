package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceptionistRepository
        extends JpaRepository<Receptionist, UUID> {

    Optional<Receptionist> findByUserId(UUID userId);

    List<Receptionist> findByHospitalId(UUID hospitalId);

    boolean existsByUserId(UUID userId);
}