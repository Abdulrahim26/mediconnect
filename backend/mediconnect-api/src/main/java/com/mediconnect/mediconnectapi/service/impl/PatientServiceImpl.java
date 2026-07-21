package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.request.UpdatePatientProfileRequest;
import com.mediconnect.mediconnectapi.dto.response.PatientProfileResponse;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.PatientService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    private final UserRepository userRepository;



    @Override
    public PatientProfileResponse getProfile() {

        Patient patient = getCurrentPatient();

        return mapToResponse(patient);
    }



    @Override
    @Transactional
    public PatientProfileResponse updateProfile(
            UpdatePatientProfileRequest request
    ) {


        Patient patient = getCurrentPatient();


        patient.setPhone(request.getPhone());

        patient.setDateOfBirth(
                request.getDateOfBirth()
        );

        patient.setGender(
                request.getGender()
        );

        patient.setAddress(
                request.getAddress()
        );


        Patient updatedPatient =
                patientRepository.save(patient);


        return mapToResponse(updatedPatient);
    }



    private Patient getCurrentPatient() {


        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();


        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient profile not found"
                        )
                );
    }



    private PatientProfileResponse mapToResponse(
            Patient patient
    ) {


        return new PatientProfileResponse(

                patient.getId(),

                patient.getFirstName(),

                patient.getLastName(),

                patient.getUser().getEmail(),

                patient.getPhone(),

                patient.getDateOfBirth(),

                patient.getGender(),

                patient.getAddress()
        );
    }

}