package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByHospitalIdAndRoleName(
            UUID hospitalId,
            String roleName
    );

    long countByHospitalId(UUID hospitalId);

    List<User> findByRoleName(String roleName);

    @Query("""
        SELECT u.id,
               u.email,
               r.name,
               h.id,
               h.name,
               u.active
        FROM User u
        JOIN u.role r
        LEFT JOIN u.hospital h
        WHERE r.name = :roleName
        """)
    List<Object[]> findHospitalAdminsWithHospital(
            @Param("roleName") String roleName
    );

    @Query("""
        SELECT u.id,
               u.email,
               r.name,
               u.createdAt,
               h.id,
               h.name,
               h.location,
               h.address,
               h.phone,
               h.email,
               u.active
        FROM User u
        JOIN u.role r
        LEFT JOIN u.hospital h
        WHERE u.id = :userId
          AND r.name = 'HOSPITAL_ADMIN'
        """)
    List<Object[]> findHospitalAdminDetails(
            @Param("userId") UUID userId
    );
}