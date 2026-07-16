package com.mediconnect.mediconnectapi.service.impl;


import com.mediconnect.mediconnectapi.dto.request.CreateDepartmentRequest;
import com.mediconnect.mediconnectapi.dto.response.DepartmentResponse;
import com.mediconnect.mediconnectapi.entity.Department;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.DepartmentRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.DepartmentService;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;



    @Override
    public DepartmentResponse createDepartment(
            CreateDepartmentRequest request
    ) {


        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();



        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );



        if(user.getHospital() == null){

            throw new RuntimeException(
                    "User is not assigned to a hospital"
            );

        }



        boolean exists =
                departmentRepository.existsByNameAndHospitalId(
                        request.getName(),
                        user.getHospital().getId()
                );


        if(exists){

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



        return new DepartmentResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getHospital().getName()
        );

    }

}