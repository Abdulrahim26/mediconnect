package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;
import com.mediconnect.mediconnectapi.entity.*;
import com.mediconnect.mediconnectapi.repository.*;
import com.mediconnect.mediconnectapi.service.DoctorService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {


    private final DoctorRepository doctorRepository;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;



    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {


        // Get logged-in hospital admin
        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();



        User hospitalAdmin =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );



        if(hospitalAdmin.getHospital() == null){

            throw new RuntimeException(
                    "Hospital admin is not assigned to a hospital"
            );

        }



        // Check doctor email
        if(userRepository.existsByEmail(request.getEmail())){

            throw new RuntimeException(
                    "Email already exists"
            );

        }



        // Get DOCTOR role
        Role doctorRole =
                roleRepository.findByName("DOCTOR")
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "DOCTOR role not found"
                                )
                        );



        // Create login user
        User doctorUser = new User();

        doctorUser.setEmail(request.getEmail());

        doctorUser.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        doctorUser.setRole(doctorRole);

        doctorUser.setHospital(
                hospitalAdmin.getHospital()
        );


        User savedUser =
                userRepository.save(doctorUser);




        // Find department
        Department department =
                departmentRepository.findById(
                                request.getDepartmentId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Department not found"
                                )
                        );




        // Create doctor profile
        Doctor doctor = new Doctor();


        doctor.setFirstName(
                request.getFirstName()
        );


        doctor.setLastName(
                request.getLastName()
        );


        doctor.setSpecialty(
                request.getSpecialty()
        );


        doctor.setQualification(
                request.getQualification()
        );


        doctor.setPhone(
                request.getPhone()
        );


        doctor.setEmail(
                request.getEmail()
        );


        doctor.setConsultationFee(
                request.getConsultationFee()
        );


        doctor.setDepartment(
                department
        );


        doctor.setUser(
                savedUser
        );



        Doctor savedDoctor =
                doctorRepository.save(doctor);


        System.out.println(savedDoctor.getId());
        System.out.println(savedDoctor.getFirstName());
        System.out.println(savedDoctor.getLastName());
        System.out.println(savedDoctor.getSpecialty());
        System.out.println(savedDoctor.getEmail());
        System.out.println(savedDoctor.getDepartment().getName());

        return new DoctorResponse(
                savedDoctor.getId(),
                savedDoctor.getFirstName(),
                savedDoctor.getLastName(),
                savedDoctor.getSpecialty(),
                savedDoctor.getQualification(),
                savedDoctor.getPhone(),
                savedDoctor.getEmail(),
                savedDoctor.getConsultationFee(),
                savedDoctor.getDepartment().getName()
        );

    }

}