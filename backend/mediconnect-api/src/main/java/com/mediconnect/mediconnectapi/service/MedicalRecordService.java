package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateMedicalRecordRequest;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;

import java.util.List;
import java.util.UUID;


public interface MedicalRecordService {


    MedicalRecordResponse createRecord(
            CreateMedicalRecordRequest request
    );


    List<MedicalRecordResponse> getPatientRecords();

}