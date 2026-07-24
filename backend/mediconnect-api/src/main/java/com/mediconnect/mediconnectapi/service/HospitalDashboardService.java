package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.response.HospitalDashboardResponse;

import java.util.UUID;

public interface HospitalDashboardService {

    HospitalDashboardResponse getDashboard(UUID hospitalId);

}