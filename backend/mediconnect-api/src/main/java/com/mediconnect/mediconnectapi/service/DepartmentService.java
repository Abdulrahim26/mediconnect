package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateDepartmentRequest;
import com.mediconnect.mediconnectapi.dto.response.DepartmentResponse;

import org.springframework.data.domain.Page;

public interface DepartmentService {


    DepartmentResponse createDepartment(
            CreateDepartmentRequest request
    );


    Page<DepartmentResponse> getDepartments(
            int page,
            int size,
            String search
    );
}