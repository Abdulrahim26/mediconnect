package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.response.ReceptionistPatientResponse;

import java.util.List;


public interface ReceptionistPatientManagementService {


    List<ReceptionistPatientResponse> getPatients();



    List<ReceptionistPatientResponse> searchPatients(
            String name
    );

}