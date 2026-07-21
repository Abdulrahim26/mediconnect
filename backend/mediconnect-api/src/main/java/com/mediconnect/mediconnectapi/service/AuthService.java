package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.LoginRequest;
import com.mediconnect.mediconnectapi.dto.request.RegisterRequest;
import com.mediconnect.mediconnectapi.dto.response.LoginResponse;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PatientRepository patientRepository;

    @Transactional
    public String register(RegisterRequest request) {

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("PATIENT role not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(patientRole);

        // Save User first
        userRepository.save(user);

        // ✅ Create Patient linked to User
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setUser(user);

        // Save Patient
        patientRepository.save(patient);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        return new LoginResponse(
                token,
                user.getRole().getName()
        );
    }
}