package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreatePatientRequest;


public interface ReceptionistPatientService {


    String registerPatient(
            CreatePatientRequest request
    );

}