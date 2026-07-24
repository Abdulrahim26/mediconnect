package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.response.DoctorDashboardResponse;


import java.util.UUID;


public interface DoctorDashboardService {


    DoctorDashboardResponse getDashboard(UUID doctorId);

}