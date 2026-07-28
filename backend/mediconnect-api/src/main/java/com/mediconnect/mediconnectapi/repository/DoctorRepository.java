package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByUserId(UUID userId);

    boolean existsByEmail(String email);

    List<Doctor> findByDepartmentId(UUID departmentId);

    long countByDepartmentHospitalId(UUID hospitalId);

    // Search by doctor's first name
    Page<Doctor> findByFirstNameContainingIgnoreCase(
            String firstName,
            Pageable pageable
    );

    // Search by doctor's last name
    Page<Doctor> findByLastNameContainingIgnoreCase(
            String lastName,
            Pageable pageable
    );

    // Search by specialty
    Page<Doctor> findBySpecialtyContainingIgnoreCase(
            String specialty,
            Pageable pageable
    );

    // Search by department
    Page<Doctor> findByDepartmentNameContainingIgnoreCase(
            String department,
            Pageable pageable
    );

    // Search by hospital
    Page<Doctor> findByDepartmentHospitalNameContainingIgnoreCase(
            String hospital,
            Pageable pageable
    );
    List<Doctor> findByDepartmentHospitalId(
            UUID hospitalId
    );

    Optional<Doctor> findByIdAndDepartmentHospitalId(
            UUID doctorId,
            UUID hospitalId
    );
}