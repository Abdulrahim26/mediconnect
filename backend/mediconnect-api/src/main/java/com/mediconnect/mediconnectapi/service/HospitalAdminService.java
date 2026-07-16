package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.response.UserResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class HospitalAdminService {


    private final UserRepository userRepository;

    private final HospitalRepository hospitalRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;



    public UserResponse createHospitalAdmin(
            CreateHospitalAdminRequest request
    ){


        if(userRepository.existsByEmail(request.getEmail())){

            throw new RuntimeException(
                    "Email already exists"
            );

        }



        Hospital hospital =
                hospitalRepository.findById(
                                request.getHospitalId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hospital not found"
                                )
                        );



        Role adminRole =
                roleRepository.findByName(
                                "HOSPITAL_ADMIN"
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "HOSPITAL_ADMIN role not found"
                                )
                        );



        User user = new User();


        user.setEmail(
                request.getEmail()
        );


        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setRole(
                adminRole
        );


        user.setHospital(
                hospital
        );



        User savedUser =
                userRepository.save(user);



        return new UserResponse(

                savedUser.getId(),

                savedUser.getEmail(),

                savedUser.getRole().getName()

        );

    }

}