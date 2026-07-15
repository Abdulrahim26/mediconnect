package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.LoginRequest;
import com.mediconnect.mediconnectapi.dto.request.RegisterRequest;
import com.mediconnect.mediconnectapi.dto.response.LoginResponse;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;



    public String register(RegisterRequest request) {


        Role patientRole =
                roleRepository.findByName("PATIENT")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "PATIENT role not found"
                                )
                        );


        User user = new User();


        user.setEmail(request.getEmail());


        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setRole(patientRole);


        userRepository.save(user);


        return "User registered successfully";
    }




    public LoginResponse login(LoginRequest request) {


        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );


        User user =
                userRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow();


        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().getName()
                );


        return new LoginResponse(
                token,
                user.getRole().getName()
        );
    }

}