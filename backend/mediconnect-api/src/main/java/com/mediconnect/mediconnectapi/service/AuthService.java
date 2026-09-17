package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.ForgotPasswordRequest;
import com.mediconnect.mediconnectapi.dto.request.LoginRequest;
import com.mediconnect.mediconnectapi.dto.request.RegisterRequest;
import com.mediconnect.mediconnectapi.dto.request.ResetPasswordRequest;
import com.mediconnect.mediconnectapi.dto.response.LoginResponse;
import com.mediconnect.mediconnectapi.entity.PasswordResetToken;
import com.mediconnect.mediconnectapi.entity.Patient;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.entity.enums.InsuranceProvider;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.PasswordResetTokenRepository;
import com.mediconnect.mediconnectapi.repository.PatientRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PatientRepository patientRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public String register(RegisterRequest request) {

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new ResourceNotFoundException("PATIENT role not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(patientRole);

        // Save User first
        userRepository.save(user);

        // Create Patient linked to User
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setUser(user);

        // Set additional patient fields
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());

        patient.setGhanaCardPin(request.getGhanaCardPin());
        patient.setNhisNumber(request.getNhisNumber());

        if (request.getInsuranceProvider() != null
                && !request.getInsuranceProvider().isBlank()) {

            patient.setInsuranceProvider(
                    InsuranceProvider.valueOf(
                            request.getInsuranceProvider()
                                    .trim()
                                    .toUpperCase()
                    )
            );
        }

        // Save Patient
        patientRepository.save(patient);

        // Send welcome email
        emailService.sendWelcomeEmail(
                user.getEmail(),
                patient.getFirstName()
        );

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        return new LoginResponse(
                token,
                user.getRole().getName()
        );
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        /*
         * Do not reveal whether the email exists.
         */
        if (user == null) {
            return;
        }

        /*
         * Generate a new secure token.
         */
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByUser(user)
                        .orElseGet(PasswordResetToken::new);

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(30)
        );
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        /*
         * Frontend reset page.
         */
        String resetLink =
                "http://localhost:5173/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetLink
        );
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(request.getToken())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired password reset token"
                                )
                        );

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException(
                    "This password reset link has already been used"
            );
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "This password reset link has expired"
            );
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        /*
         * Prevent the same reset link from being used again.
         */
        resetToken.setUsed(true);

        passwordResetTokenRepository.save(resetToken);
    }
}