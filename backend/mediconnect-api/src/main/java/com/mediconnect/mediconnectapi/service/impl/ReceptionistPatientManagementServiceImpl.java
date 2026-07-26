package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.response.ReceptionistPatientResponse;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistPatientManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;



@Service
@RequiredArgsConstructor
public class ReceptionistPatientManagementServiceImpl
        implements ReceptionistPatientManagementService {


    private final PatientRepository patientRepository;



    @Override
    public List<ReceptionistPatientResponse> getPatients() {


        return patientRepository.findAll()
                .stream()
                .map(this::mapPatient)
                .toList();

    }



    @Override
    public List<ReceptionistPatientResponse> searchPatients(
            String name
    ) {


        return patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        name,
                        name
                )
                .stream()
                .map(this::mapPatient)
                .toList();

    }




    private ReceptionistPatientResponse mapPatient(
            Patient patient
    ){

        return new ReceptionistPatientResponse(

                patient.getId(),

                patient.getFirstName(),

                patient.getLastName(),

                patient.getUser().getEmail(),

                patient.getPhone()

        );

    }

}