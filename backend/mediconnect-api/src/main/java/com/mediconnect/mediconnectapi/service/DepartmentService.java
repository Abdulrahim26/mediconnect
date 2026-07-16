package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateDepartmentRequest;
import com.mediconnect.mediconnectapi.dto.response.DepartmentResponse;


public interface DepartmentService {


    DepartmentResponse createDepartment(
            CreateDepartmentRequest request
    );

}