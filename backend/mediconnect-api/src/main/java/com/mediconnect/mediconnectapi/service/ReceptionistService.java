package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistRequest;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistResponse;

import java.util.List;

public interface ReceptionistService {

    ReceptionistResponse createReceptionist(
            CreateReceptionistRequest request
    );

    List<ReceptionistResponse> getMyHospitalReceptionists();

}