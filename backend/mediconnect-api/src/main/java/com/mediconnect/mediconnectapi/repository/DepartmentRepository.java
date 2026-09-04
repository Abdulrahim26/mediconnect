package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository
        extends JpaRepository<Department, UUID> {

    long countByHospitalId(UUID hospitalId);

    boolean existsByNameAndHospitalId(
            String name,
            UUID hospitalId
    );

    Page<Department> findByHospitalId(
            UUID hospitalId,
            Pageable pageable
    );

    Page<Department> findByHospitalIdAndNameContainingIgnoreCase(
            UUID hospitalId,
            String name,
            Pageable pageable
    );

    Page<Department> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Department> findByHospitalNameContainingIgnoreCase(
            String hospital,
            Pageable pageable
    );

    Optional<Department> findByIdAndHospitalId(
            UUID departmentId,
            UUID hospitalId
    );
}