package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface DoctorRepository extends JpaRepository<Doctor, UUID> {


    List<Doctor> findByDepartmentId(UUID departmentId);


    List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);

}