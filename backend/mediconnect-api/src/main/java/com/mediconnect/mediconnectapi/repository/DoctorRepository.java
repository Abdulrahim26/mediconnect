package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByUserId(UUID userId);

    boolean existsByEmail(String email);

    List<Doctor> findByDepartmentId(UUID departmentId);

}