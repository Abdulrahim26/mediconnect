package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface HospitalRepository extends JpaRepository<Hospital, UUID> {


    Optional<Hospital> findByEmail(String email);


    boolean existsByEmail(String email);


    Page<Hospital> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );


    Page<Hospital> findByLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );


}