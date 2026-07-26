package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.request.CreatePatientRequest;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistPatientService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReceptionistPatientServiceImpl
        implements ReceptionistPatientService {


    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public String registerPatient(
            CreatePatientRequest request
    ) {


        if(userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );

        }



        Role patientRole =
                roleRepository.findByName("PATIENT")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "PATIENT role not found"
                                )
                        );



        User user = new User();


        user.setEmail(
                request.getEmail()
        );


        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setRole(patientRole);



        userRepository.save(user);



        Patient patient = new Patient();


        patient.setFirstName(
                request.getFirstName()
        );


        patient.setLastName(
                request.getLastName()
        );


        patient.setPhone(
                request.getPhone()
        );


        patient.setDateOfBirth(
                request.getDateOfBirth()
        );


        patient.setGender(
                request.getGender()
        );


        patient.setAddress(
                request.getAddress()
        );


        patient.setUser(user);



        patientRepository.save(patient);



        return "Patient registered successfully";

    }

}