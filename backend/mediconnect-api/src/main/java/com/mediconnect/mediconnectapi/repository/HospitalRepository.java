package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface HospitalRepository extends JpaRepository<Hospital, UUID> {


    Optional<Hospital> findByEmail(String email);


    boolean existsByEmail(String email);

}