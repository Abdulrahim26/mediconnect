package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.response.PatientDashboardResponse;

import java.util.UUID;


public interface PatientDashboardService {


    PatientDashboardResponse getDashboard(UUID patientId);

}