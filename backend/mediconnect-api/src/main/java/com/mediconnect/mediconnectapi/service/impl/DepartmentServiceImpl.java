package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateDepartmentRequest;
import com.mediconnect.mediconnectapi.dto.response.DepartmentResponse;
import com.mediconnect.mediconnectapi.entity.Department;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.DepartmentRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.DepartmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(
            CreateDepartmentRequest request
    ) {

        User user = getAuthenticatedUser();

        if (user.getHospital() == null) {

            throw new RuntimeException(
                    "User is not assigned to a hospital"
            );
        }

        boolean exists =
                departmentRepository.existsByNameAndHospitalId(
                        request.getName(),
                        user.getHospital().getId()
                );

        if (exists) {

            throw new RuntimeException(
                    "Department already exists in this hospital"
            );
        }

        Department department = new Department();

        department.setName(request.getName());

        department.setDescription(
                request.getDescription()
        );

        department.setHospital(
                user.getHospital()
        );

        Department saved =
                departmentRepository.save(department);

        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getDepartments(
            int page,
            int size,
            String search
    ) {

        User user = getAuthenticatedUser();

        if (user.getHospital() == null) {

            throw new RuntimeException(
                    "User is not assigned to a hospital"
            );
        }

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Department> departments;

        if (search != null && !search.trim().isEmpty()) {

            departments =
                    departmentRepository
                            .findByHospitalIdAndNameContainingIgnoreCase(
                                    user.getHospital().getId(),
                                    search.trim(),
                                    pageable
                            );

        } else {

            departments =
                    departmentRepository
                            .findByHospitalId(
                                    user.getHospital().getId(),
                                    pageable
                            );
        }

        return departments.map(
                this::convertToResponse
        );
    }

    private User getAuthenticatedUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

    private DepartmentResponse convertToResponse(
            Department department
    ) {

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getHospital().getName()
        );
    }
}