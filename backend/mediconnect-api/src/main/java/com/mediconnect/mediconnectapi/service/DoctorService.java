package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;


public interface DoctorService {


    DoctorResponse createDoctor(
            CreateDoctorRequest request
    );


}